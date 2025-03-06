package com.example.sort;

import java.util.Comparator;
import java.util.List;

import com.example.TaskService;

public class ParallelShellSort extends ShellSort {

    public static <T> void sort(
        List<T> list, 
        Comparator<T> comp,
        int maxThreads
    ) {
        if (maxThreads > 1) 
            (new Sorter<>(list, comp, maxThreads, 20)).sort();
        else
            sort(list, comp);
    }

    public static <T> void sort(
        List<T> list, 
        Comparator<T> comp,
        int maxThreads,
        int minThreadSublistLen 
    ) {
        if (maxThreads > 1) 
            (new Sorter<>(list, comp, maxThreads, minThreadSublistLen)).sort();
        else
            sort(list, comp);
    }

    private static class Sorter<T> {
        private final TaskService service;

        private final List<T> list;
        private final Comparator<T> comp;

        private int elemGap;
        private int minThreadSublistLen;

        public Sorter(
            List<T> list, 
            Comparator<T> comp,
            int maxThreads,
            int minThreadGapLen 
        ) {
            service = new TaskService(maxThreads, maxThreads/2);
            this.minThreadSublistLen = minThreadGapLen;

            this.list = list;
            this.comp = comp;

            elemGap = getStartGap(list.size());
        }

        public void sort() {
            for (elemGap = getStartGap(list.size()); elemGap > 0; elemGap /= 2) {

                if (list.size() / elemGap <= minThreadSublistLen || elemGap == 1) {
                    for (int g = elemGap; g < elemGap*2 && g < list.size(); g++)
                        gapSort(g);
                } else {
                    for (int g = elemGap; g < elemGap*2 && g < list.size(); g++) 
                        service.addTask(createTask(g));
                    service.waitTasks();
                }
            }
            service.stop();
        }

        private void gapSort(int g) {
            for (int i = g; i < list.size(); i+=elemGap) {
                T temp = list.get(i);
                int j = i;
                while (j >= elemGap && 
                    comp.compare(list.get(j - elemGap), temp) > 0
                ){
                    list.set(j, list.get(j - elemGap));
                    j -= elemGap;
                }
                list.set(j, temp);
            }
        }

        private Runnable createTask(int g) {
            return () -> {
                gapSort(g);
            };
        }
    }
}
