package com.example.bench;

import java.util.ArrayList;
import java.util.List;

import com.example.sort.ParallelShellSort;

public class Tester {
    
    public record TestResult (
        int size,
        float time, 
        int threadNumb
    ) {}

    public static TestResult run(List<Integer> list, int threadNumb) {
        List<Integer> expected = new ArrayList<>(list);
        expected.sort(Integer::compare);

        var timer = new Timer();
        timer.start();
        ParallelShellSort.sort(list, Integer::compare, threadNumb);
        timer.end();

        if (!expected.equals(list)) 
            throw new IllegalStateException("List is not sorted");

        return new TestResult(list.size(), timer.resultTime(), threadNumb);
    }

    public static TestResult run(List<Integer> list, int threadNumb, int threadGapLen) {
        List<Integer> expected = new ArrayList<>(list);
        expected.sort(Integer::compare);

        var timer = new Timer();
        timer.start();
        ParallelShellSort.sort(list, Integer::compare, threadNumb);
        timer.end();

        if (!expected.equals(list)) 
            throw new IllegalStateException("List is not sorted");

        return new TestResult(list.size(), timer.resultTime(), threadNumb);
    }

    //public static List<>//TODO
}