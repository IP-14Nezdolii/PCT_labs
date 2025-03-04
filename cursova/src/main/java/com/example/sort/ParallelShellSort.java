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
            (new Sorter<>(list, comp, maxThreads)).sort();
        else
            sort(list, comp);
    }

    private static class Sorter<T> {
        private final TaskService service;

        private final List<T> list;
        private final Comparator<T> comp;

        private int gap;

        public Sorter(
            List<T> list, 
            Comparator<T> comp,
            int maxThreads
        ) {
            service = new TaskService(maxThreads, maxThreads/2);

            this.list = list;
            this.comp = comp;

            gap = getStartGap(list.size());
        }

        public void sort() {
            for (gap = getStartGap(list.size()); gap > 0; gap /= 2) {

                if (list.size() / gap < 1000 || gap == 1) {
                    for (int g = gap; g < 2*gap && g < list.size(); g++)
                        gapSort(g);
                } else {
                    for (int g = gap; g < 2*gap && g < list.size(); g++) 
                        service.addTask(createTask(g));
                    service.waitTasks();
                }
            }
            service.stop();
        }

        private void gapSort(int g) {
            for (int i = g; i < list.size(); i+=gap) {
                T temp = list.get(i);
                int j = i;
                while (j >= gap && 
                    comp.compare(list.get(j - gap), temp) > 0
                ){
                    list.set(j, list.get(j - gap));
                    j -= gap;
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
