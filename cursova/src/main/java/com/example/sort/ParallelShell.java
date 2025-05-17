package com.example.sort;

import java.util.Comparator;
import java.util.List;

import com.example.utils.TaskService;

public class ParallelShell{

    public static <T> void sort(
        List<T> list, 
        Comparator<T> cmp, 
        int maxThreads
    ) {
        if (maxThreads > 1) {
            (new Sorter<>(list, cmp)).sort(maxThreads, 80);
        } else {
            Shell.sort(list, cmp);
        }  
    }

    public static <T> void sort(
        List<T> list, 
        Comparator<T> cmp,
        int maxThreads,
        int minSublistLen 
    ) {
        if (maxThreads > 1) 
            (new Sorter<>(list, cmp)).sort(maxThreads, minSublistLen);
        else
            Shell.sort(list, cmp);
    }

    private static class Sorter<T> {
        private final List<T> list;
        private final Comparator<T> cmp;
        private int h;

        public Sorter(List<T> list, Comparator<T> cmp) {
            this.list = list;
            this.cmp = cmp;
        }

        public void sort(int maxThreads, int minSublistLen) {
            try (TaskService service = new TaskService(maxThreads)) {
                for (h = knuthGap(); h > 0; h /= 3) {

                    if (list.size() / h >= minSublistLen) {
                        for (int g = h; g < h*2; g++) {
                            service.addAndExecute(createTask(g));
                        }
                        service.waitTasks();
                    } else {
                        for (int g = h; g < h*2; g++) {
                            gapInsertionSort(g);
                        }
                    }
                }
            }
        }

        private void gapInsertionSort(int g) {
            for (int i = g; i < list.size(); i+=h) {  
                T temp = list.get(i);
                int j = i;
                for (;j >= h && cmp.compare(list.get(j - h), temp) > 0; j -= h) {
                    list.set(j, list.get(j - h));
                }
                list.set(j, temp);
            }
        }

        private int knuthGap() {
            int h = 1;
            double sz = Math.ceil(list.size()/9.0);
            while (h <= sz) {
                h = 3*h + 1;  
            }
            return h;
        }

        private Runnable createTask(int g) {
            return () -> {
                gapInsertionSort(g);
            };
        }
    }
}
