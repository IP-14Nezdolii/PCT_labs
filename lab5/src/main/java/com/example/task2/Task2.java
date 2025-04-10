package com.example.task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;

import com.example.task1.Queueing;
import com.example.utils.Result;

public class Task2 {
    private static final int RUNS = 5;

    public static void main(String[] args) {
        List<Callable<Result>> tasks = new ArrayList<>(RUNS);
        tasks.add(() -> {return new Queueing(1, 1, true).simulate();});
        tasks.add(() -> {return new Queueing(1, 1, false).simulate();});
        tasks.add(() -> {return new Queueing(1, 2, false).simulate();});
        tasks.add(() -> {return new Queueing(1, 3, false).simulate();});
        tasks.add(() -> {return new Queueing(1, 4, false).simulate();});

        try (ExecutorService executor = Executors.newFixedThreadPool(RUNS)) {
            var results = executor.invokeAll(tasks);

            double finalAvgQueueLength = 0.0;
            double finalRejectionProb = 0.0;
            for (var result : results) {
                var res = result.get();

                finalAvgQueueLength += res.avgQueueLengths();
                finalRejectionProb += res.rejectionProb();
            }

            finalAvgQueueLength /= RUNS;
            finalRejectionProb /= RUNS;

            System.out.println("Avg queue length: " + finalAvgQueueLength + ", Rejection prob: " + finalRejectionProb);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
