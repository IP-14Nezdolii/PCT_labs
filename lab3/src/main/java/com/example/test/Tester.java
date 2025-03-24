package com.example.test;

import java.util.Map;

import com.example.algorithms.FoxAlgorithm;
import com.example.algorithms.StripeAlgorithm;
import com.example.utils.Matrix;

public class Tester {
    
    public record TestResult (
        int size,
        double time, 
        int threadNumb
    ) {}

    public static TestResult runStripe(
        Matrix a, 
        Matrix b, 
        Matrix expected,
        int nThreads
    ) {
        var timer = new Timer();
        timer.start();
        Matrix result = StripeAlgorithm.multiply(a, b, nThreads);
        timer.end();

        if (!expected.equals(result)) 
            throw new IllegalStateException("Incorrect multiply");

        return new TestResult(
            a.rows(), 
            timer.resultMicroTime(), 
            nThreads
        );
    }

    public static TestResult runFox(
        Matrix a, 
        Matrix b, 
        Matrix expected,
        int nThreads
    ) {
        var timer = new Timer();
        timer.start();
        Matrix result = FoxAlgorithm.multiply(a, b, nThreads);
        timer.end();

        if (!expected.equals(result)) 
            throw new IllegalStateException("Incorrect multiply");

        return new TestResult(
            a.rows(), 
            timer.resultMicroTime(), 
            nThreads
        );
    }

    public static TestResult run(
        Matrix a, 
        Matrix b,
        Map<Integer, Matrix> results
    ) {
        var timer = new Timer();
        timer.start();
        var result = Matrix.multiply(a, b);
        timer.end();

        results.putIfAbsent(result.rows(), result);

        return new TestResult(
            a.rows(), 
            timer.resultMicroTime(), 
            0
        );
    }
}