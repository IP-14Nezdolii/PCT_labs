package com.example.task1;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.utils.Result;
import com.example.utils.TaskObj;

public class Queueing {
    private static final int CONSUMERS = 4;
    private static final int QUEUE_CAPACITY = 20;

    private static final long MIN_ARRIVAL_INTERVAL = 3;
    private static final long MAX_ARRIVAL_INTERVAL = 12;
    
    private static final long MIN_SERVICE_TIME = 15;
    private static final long MAX_SERVICE_TIME = 60;

    private static final long SIMULATION_TIME = 10_000;
    private static final long REPORT_INTERVAL = 500;

    private volatile boolean isRunning = true;

    private int runs = 5;
    private boolean enableLogging = true;

    private int seed = 0;
    private final Random random = new Random(seed);

    private final BlockingQueue<TaskObj> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    private int totalArrivals = 0;
    private int totalRejected = 0;
    private AtomicInteger totalServed = new AtomicInteger(0);

    private long queueLengthSum = 0;
    private int queueLengthMeasurements = 0;

    private long startTime = 0;

    public Queueing() {}

    public Queueing(int runs, int seed, boolean enableLogging) {
        this.runs = runs;
        this.random.setSeed(seed);
        this.enableLogging = enableLogging;
    }

    private long getRandomTime(long min, long max) {
        return min + random.nextLong(max - min);
    }

    private void runProducer() {
        try {
            while ((System.currentTimeMillis() - startTime) < SIMULATION_TIME) {

                totalArrivals++;
                if (!queue.offer(new TaskObj(
                        getRandomTime(MIN_SERVICE_TIME, MAX_SERVICE_TIME)))
                ) {
                    totalRejected++;
                }
                Thread.sleep(getRandomTime(MIN_ARRIVAL_INTERVAL, MAX_ARRIVAL_INTERVAL));
            }

            isRunning = false;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Runnable createConsumer() {
        return () -> {
            while (isRunning) {
                try {
                    var task = queue.take();
                    task.doTask();
                    totalServed.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };
    }

    private Runnable createReporter() {
        return () -> {
            while (isRunning) {
                try {
                    Thread.sleep(REPORT_INTERVAL);

                    int currentQueueLength = queue.size();
                    queueLengthSum+=currentQueueLength;
                    queueLengthMeasurements++;

                    if (enableLogging) {
                        System.out.printf(
                            "Time: %.1f с, Queue length: %d, totalServed: %d, totalRejected: %d%n",
                                (System.currentTimeMillis() - startTime) / 1000.0,
                                currentQueueLength, totalServed.get(), totalRejected);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };
    }

    private void runSimulation() {
        totalServed.set(0);
        totalArrivals = 0;
        totalRejected = 0;
        queueLengthSum = 0;
        queueLengthMeasurements = 0;
        queue.clear();
        isRunning = true;
        startTime = System.currentTimeMillis();

        try(ExecutorService executor = Executors.newFixedThreadPool(CONSUMERS + 1)) {
            for (int i = 0; i < CONSUMERS; i++) {
                executor.execute(createConsumer());
            }
            executor.execute(createReporter());
            runProducer();
        }
    }

    private Result calculateStatistics(int run) {
        double avgQueueLength = queueLengthMeasurements > 0
                ? (double) queueLengthSum / queueLengthMeasurements
                : 0.0;
        double rejectionProb = totalArrivals > 0
                ? (double) totalRejected / totalArrivals
                : 0.0;

        if (enableLogging) {
            System.out.printf("Test %d: Mean queue length = %.2f, failure probability  = %.4f%n",
            run + 1, avgQueueLength, rejectionProb);
        }
        
        return new Result(avgQueueLength, rejectionProb);
    }

    public Result simulate() {
        Result[] results = new Result[runs];

        for (int run = 0; run < runs; run++) {

            if (enableLogging) {
                System.out.println("Start test " + (run + 1));
            }
            
            runSimulation();
            results[run] = calculateStatistics(run);
        }

        double finalAvgQueueLength = 0.0;
        double finalRejectionProb = 0.0;
        for (int i = 0; i < runs; i++) {
            finalAvgQueueLength += results[i].avgQueueLengths();
            finalRejectionProb += results[i].rejectionProb();
        }
        finalAvgQueueLength /= runs;
        finalRejectionProb /= runs;

        if (enableLogging) {
            System.out.printf("Final resuls (%d runs):%n", runs);
            System.out.printf("Mean queue length = %.2f%n", finalAvgQueueLength);
            System.out.printf("Mean failure probability = %.4f%n", finalRejectionProb);
        }

        return new Result(finalAvgQueueLength, finalRejectionProb);
    }

    public static void main(String[] args) {
        new Queueing().simulate();
    }
}