package com.example.sort;

import java.util.Comparator;
import java.util.List;

import com.example.TaskService;

public class ParallelShell {

    public static <T> void sort(
        List<T> list, 
        Comparator<T> cmp,
        int maxThreads
    ) {
        if (maxThreads > 1) 
            (new Sorter<>(list, cmp, maxThreads, 1092)).sort();
        else
            Shell.sort(list, cmp);
    }

    public static <T> void sort(
        List<T> list, 
        Comparator<T> cmp,
        int maxThreads,
        int sublistParam 
    ) {
        if (maxThreads > 1) 
            (new Sorter<>(list, cmp, maxThreads, sublistParam)).sort();
        else
            Shell.sort(list, cmp);
    }

    private static class Sorter<T> {
        private final TaskService service;

        private final List<T> list;
        private final Comparator<T> cmp;

        private int h;
        private int sublistParam;

        public Sorter(
            List<T> list, 
            Comparator<T> cmp,
            int maxThreads,
            int sublistParam 
        ) {
            service = new TaskService(maxThreads, maxThreads/2);
            this.sublistParam = sublistParam;

            this.list = list;
            this.cmp = cmp;
        }

        public void sort() {
            for (h = knuthGap(); h > 0; h /= 3) {

                if (list.size() / h <= sublistParam || h == 1) {
                    for (int g = h; g < h*2; g++)
                        gapInsertionSort(g);
                } else {
                    for (int g = h; g < h*2; g++) 
                        service.addTask(createTask(g));
                    service.waitTasks();
                }
            }
            service.stop();
        }

        private void gapInsertionSort(int g) {
            for (int i = g; i < list.size(); i+=h) {  
                T temp = list.get(i);
                int j = i;
                for (;j >= h && cmp.compare(list.get(j - h), temp) > 0; j -= h)
                    list.set(j, list.get(j - h));
                list.set(j, temp);
            }
        }

        private Runnable createTask(int g) {
            return () -> {
                gapInsertionSort(g);
            };
        }

        private int knuthGap() {
            int h = 1;

            double sz = Math.ceil(list.size()/9.0);

            while (h < sz) 
                h = 3*h + 1;
            
            return h;
        }
    }
}
