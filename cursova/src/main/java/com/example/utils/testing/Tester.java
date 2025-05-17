package com.example.utils.testing;

import java.util.Comparator;
import java.util.List;

import com.example.utils.testing.Test.SortMethod;

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
        int sublistParam,
        SortMethod<T> sorter
    ) {
        double start = System.nanoTime();
        sorter.sort(list, cmp, threadNumb, sublistParam);
        double end = System.nanoTime();

        if (!expected.equals(list)) {
            throw new IllegalStateException("List is not sorted");
        }

        return new TestResult(
            list.size(), 
            (end - start)/1_000_000, 
            threadNumb, 
            sublistParam,
            list.get(0).getClass().getName()
        );
    }
}