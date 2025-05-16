package com.example.sort;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueingShellSort {

    // class Queueing {
    //     private static final int CONSUMERS = 4;
    //     private static final int QUEUE_CAPACITY = 20;

    //     private static final long MIN_ARRIVAL_INTERVAL = 3;
    //     private static final long MAX_ARRIVAL_INTERVAL = 12;
        
    //     private static final long MIN_SERVICE_TIME = 15;
    //     private static final long MAX_SERVICE_TIME = 60;

    //     private static final long SIMULATION_TIME = 10_000;
    //     private static final long REPORT_INTERVAL = 500;

    //     private volatile boolean isRunning = true;

    //     private int runs = 5;
    //     private boolean enableLogging = true;

    //     private int seed = 0;
    //     private final Random random = new Random(seed);

    //     private final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    //     private int totalArrivals = 0;
    //     private int totalRejected = 0;
    //     private AtomicInteger totalServed = new AtomicInteger(0);

    //     private long queueLengthSum = 0;
    //     private int queueLengthMeasurements = 0;

    //     private long startTime = 0;

    //     public Queueing() {}

    //     public Queueing(int runs, int seed, boolean enableLogging) {
    //         this.runs = runs;
    //         this.random.setSeed(seed);
    //         this.enableLogging = enableLogging;
    //     }

    //     private long getRandomTime(long min, long max) {
    //         return min + random.nextLong(max - min);
    //     }

    //     private Runnable createProducer() {
    //         return () -> {
    //             try {
    //                 while ((System.currentTimeMillis() - startTime) < SIMULATION_TIME) {
        
    //                     totalArrivals++;
    //                     if (!queue.offer(new TaskObj(
    //                             getRandomTime(MIN_SERVICE_TIME, MAX_SERVICE_TIME)))
    //                     ) {
    //                         totalRejected++;
    //                     }
    //                     Thread.sleep(getRandomTime(MIN_ARRIVAL_INTERVAL, MAX_ARRIVAL_INTERVAL));
    //                 }
        
    //                 isRunning = false;
        
    //             } catch (InterruptedException e) {
    //                 Thread.currentThread().interrupt();
    //             }
    //         };
    //     }

    //     private Runnable createConsumer() {
    //         return () -> {
    //             while (isRunning) {
    //                 try {
    //                     var task = queue.take();
    //                     task.doTask();
    //                     totalServed.incrementAndGet();
    //                 } catch (InterruptedException e) {
    //                     Thread.currentThread().interrupt();
    //                     break;
    //                 }
    //             }
    //         };
    //     }


    //     private void runSimulation() {
    //         totalServed.set(0);
    //         totalArrivals = 0;
    //         totalRejected = 0;
    //         queueLengthSum = 0;
    //         queueLengthMeasurements = 0;
    //         queue.clear();
    //         isRunning = true;
    //         startTime = System.currentTimeMillis();

    //         try(ExecutorService executor = Executors.newFixedThreadPool(CONSUMERS + 2)) {
    //             // List<Callable<Result>> tasks = new ArrayList<>(CONSUMERS + 2);
    //             // for (int i = 0; i < CONSUMERS; i++) {
    //             //     tasks.add(()-> {createConsumer().run();return null;});
    //             // }
    //             // tasks.add(()-> {createReporter().run();return null;});
    //             // tasks.add(()-> {createProducer().run();return null;});
    //             // executor.invokeAll(tasks, SIMULATION_TIME, TimeUnit.MILLISECONDS);
    //             for (int i = 0; i < CONSUMERS; i++) {
    //                 executor.execute(createConsumer());
    //             }
    //             executor.execute(createReporter());
    //             executor.execute(createProducer());

    //             if (executor.awaitTermination(SIMULATION_TIME + MAX_SERVICE_TIME, TimeUnit.MILLISECONDS)) {
    //                 executor.shutdownNow();
    //             }
    //         } catch (InterruptedException e) {
    //             Thread.currentThread().interrupt();
    //         } finally {
    //             isRunning = false;
    //         }
    //     }
    // }
}
