package com.example.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskService implements AutoCloseable {
    private final ExecutorService executorService;

    private final AtomicInteger activeTaskCount;
    private final int maxTasks;
    private final Object lock;
    
    public TaskService(int maxThreads) {
        if (maxThreads < 2) {
            throw new IllegalArgumentException("Number of threads must be at least 2");
        }

        maxThreads -= 1;

        this.maxTasks = maxThreads * 2 + 1;
        this.executorService = Executors.newFixedThreadPool(maxThreads);

        this.activeTaskCount = new AtomicInteger(0);
        this.lock = new Object();
    }

    public void addAndExecute(Runnable task) {
        if (activeTaskCount.get() < maxTasks) {
            activeTaskCount.incrementAndGet();

            executorService.execute(() -> {
                task.run();

                if (activeTaskCount.decrementAndGet() == 0) {
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            });
        } else {
            task.run();
        }
    }

    public void waitTasks() {
        try {
            synchronized (lock) {
                while (activeTaskCount.get() != 0) {
                    lock.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        executorService.close();
    }
}