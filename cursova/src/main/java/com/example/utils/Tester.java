package com.example.utils;

import java.util.Comparator;
import java.util.List;

import com.example.sort.ParallelShell;

public class Tester {
    
    public record TestResult (
        int size,
        double time, 
        int threadNumb,
        int sublistParam,
        String sortedClassName
    ) {}

    public static <T> TestResult run(
        List<T> list, 
        List<T> expected,
        Comparator<T> cmp, 
        int threadNumb, 
        int sublistParam
    ) {
        var timer = new Timer();
        timer.start();
        ParallelShell.sort(list, cmp, threadNumb, sublistParam);
        timer.end();

        if (!expected.equals(list)) 
            throw new IllegalStateException("List is not sorted");

        return new TestResult(
            list.size(), 
            timer.resultMicroTime(), 
            threadNumb, 
            sublistParam,
            list.get(0).getClass().getName()
        );
    }
}

class Timer {
    private double start = 0;
    private long end = 0;

    public double start() {
        this.start = System.nanoTime();
        return this.start;
    }

    public double end() {
        this.end = System.nanoTime();
        return this.end;
    }

    public double resultNanoTime() {
        return end - start;
    }

    public double resultMicroTime() {
        return (end - start)/1000.0;
    }
}