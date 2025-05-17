package com.example.sort;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class QueueingShell {

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
        private volatile int h;

        public Sorter(List<T> list, Comparator<T> cmp) {
            this.list = list;
            this.cmp = cmp;
        }

        public void sort(int maxThreads, int minSublistLen) {
            try (QueueingService service = new QueueingService(maxThreads)) {
                for (h = knuthGap(); h > 0; h /= 3) {

                    if (list.size() / h >= minSublistLen) {
                        for (int g = h; g < h*2; g++) {
                            service.execute(g);
                        }
                        service.waitTasks();
                    } else {
                        for (int g = h; g < h*2; g++) {
                            gapInsertionSort(g);
                        }
                    }
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
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

        private class QueueingService implements AutoCloseable {
            private final int END = -2;
            private final int NEW_ITER = -1;

            private final BlockingQueue<Integer> queue;
            private final Thread[] threads;

            private final CyclicBarrier barrier;

            public QueueingService(int maxThreads) {
                maxThreads = maxThreads - 1;
                int queueLen = maxThreads * 2;

                this.barrier = new CyclicBarrier(maxThreads + 1);

                queue = new ArrayBlockingQueue<>(queueLen);
                threads = new Thread[maxThreads];

                for (int i = 0; i < threads.length; i++) {
                    threads[i] = new Thread(createConsumer());
                    threads[i].start();
                }
            }

            public void execute(int g) throws InterruptedException {
                if (!queue.offer(g)) {
                    gapInsertionSort(g);
                }
            }

            private Runnable createConsumer() {
                return () -> {
                    try {
                        int g;

                        while (true) {
                            g = queue.take();
                            if (g < 0) {
                                if (g == NEW_ITER) {
                                    barrier.await();
                                    continue;
                                } else {
                                    break;
                                }
                            }
                            gapInsertionSort(g);
                        }
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                };
            }

            public void waitTasks() throws InterruptedException, BrokenBarrierException {
                Integer elem = queue.poll();
                while (elem != null) {  
                    gapInsertionSort(elem);
                    elem = queue.poll();
                }

                for (int i = 0; i < threads.length; i++) {
                    queue.add(NEW_ITER);
                }

                barrier.await();
            }

            @Override
            public void close() throws InterruptedException, BrokenBarrierException {
                waitTasks();

                for (int i = 0; i < threads.length; i++) {
                    queue.add(END);
                }
                for (Thread t : threads) {
                    t.join();
                }
            }
        }
    }    
}