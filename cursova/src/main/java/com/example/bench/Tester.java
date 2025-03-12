package com.example.bench;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.sort.ParallelShellSort;

public class Tester {
    
    public record TestResult (
        int size,
        float time, 
        int threadNumb,
        String sortedClassName
    ) {}

    public static <T> TestResult run(List<T> list, Comparator<T> cmp, int threadNumb) {
        List<T> expected = 
            new ArrayList<>(list)
                .stream().sorted(cmp).toList();

        var timer = new Timer();
        timer.start();
        ParallelShellSort.sort(list, cmp, threadNumb);
        timer.end();

        if (!expected.equals(list)) 
            throw new IllegalStateException("List is not sorted");

        return new TestResult(
            list.size(), 
            timer.resultTime(), 
            threadNumb, 
            list.get(0).getClass().getName()
        );
    }

    public static <T> TestResult run(List<T> list, Comparator<T> cmp, int threadNumb, int threadGapLen) {
        List<T> expected = 
            new ArrayList<>(list)
                .stream().sorted(cmp).toList();

        var timer = new Timer();
        timer.start();
        ParallelShellSort.sort(list, cmp, threadNumb);
        timer.end();

        if (!expected.equals(list)) 
            throw new IllegalStateException("List is not sorted");

        return new TestResult(
            list.size(), 
            timer.resultTime(), 
            threadNumb, 
            list.get(0).getClass().getName()
        );
    }

    //public static List<>//TODO
}