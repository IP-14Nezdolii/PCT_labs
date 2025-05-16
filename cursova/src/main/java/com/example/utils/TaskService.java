package com.example.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class TaskService implements AutoCloseable {
    private final ExecutorService executorService;
    private final Semaphore semaphore;
    private final int waitingN;

    public TaskService(int maxThreads, int waitingQueueSize) {
        this.waitingN = waitingQueueSize + maxThreads;
        this.executorService = Executors.newFixedThreadPool(maxThreads);
        this.semaphore = new Semaphore(waitingN);
    }

    public void addAndExecute(Runnable task) {
        // if (semaphore.tryAcquire()) {

        // }

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
            semaphore.acquire(waitingN);
            semaphore.release(waitingN);
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