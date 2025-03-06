package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TaskService {
    private final ExecutorService executor;
    private final Semaphore semaphore;

    private final int maxThreads;
    private final int waitingQueueSize;

    public TaskService(int maxThreads, int waitingQueueSize) {
        this.maxThreads = maxThreads;
        this.waitingQueueSize = waitingQueueSize;

        executor = Executors.newFixedThreadPool(maxThreads);
        semaphore = new Semaphore(maxThreads + waitingQueueSize);
    }

    public void addTask(Runnable task) {
        try {
            semaphore.acquire();
            executor.execute(() -> {
                task.run();
                semaphore.release();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void waitTasks() {
        try {
            semaphore.acquire(maxThreads + waitingQueueSize);
            semaphore.release(maxThreads + waitingQueueSize);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        waitTasks();
        executor.shutdown();

        if (executor.isTerminated())
            return;

        try {
            for (;;) {
                if (executor.awaitTermination(1, TimeUnit.MILLISECONDS)) 
                    break;  
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int maxThreads() {
        return maxThreads;
    }
}
