package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TaskService {
    private final ExecutorService executorService;
    private final Semaphore semaphore;

    private final int maxThreads;
    private final int waitingQueueSize;

    public TaskService(int maxThreads, int waitingQueueSize) {
        this.maxThreads = maxThreads;
        this.waitingQueueSize = waitingQueueSize;

        executorService = Executors.newFixedThreadPool(maxThreads);
        semaphore = new Semaphore(maxThreads + waitingQueueSize);
    }

    public void addTask(Runnable task) {
        try {
            semaphore.acquire();
            executorService.execute(() -> {
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
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        executorService.shutdown();
        waitTasks();

        if (executorService.isTerminated())
            return;

        try {
            if (!executorService.awaitTermination(10, TimeUnit.MILLISECONDS))
                System.err.println("Pool did not terminate"); 
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            e.printStackTrace();
        }
    }
}