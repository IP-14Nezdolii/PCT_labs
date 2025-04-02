package com.example.task2;

import com.example.utils.Matrix;

@SuppressWarnings("unused")
public class Task2 {

    static public void doTask(int treshold, int retest) {
        Matrix a = Matrix.genRandomMatrix(1000, 1000, 123);
        Matrix b = Matrix.genRandomMatrix(1000, 1000, 456);

        for (int i = 0; i < treshold; i++) {         
            var result1 = testSingle(a, b);
            var result2 = testMulti(a, b);
            var result3 = testFork(a, b);
        }

        double singleT = 0;
        double multiT = 0;
        double multiF = 0;
        for (int i = 0; i < retest; i++) {
            singleT += testSingle(a, b);
            multiT += testMulti(a, b);
            multiF += testFork(a, b);
        }
        singleT/=retest;
        multiT/=retest;
        multiF/=retest;

        System.out.println("Time single thread: "+ singleT);
        System.out.println(
            "Time thread array 8 threads: "+ multiT + 
            ", speedUp: " + singleT/multiT +
            ", efficiency: " + singleT/multiT / 8
        );
        System.out.println(
            "Time forkjoin 8 threads: "+ multiF + 
            ", speedUp: " + singleT/multiF +
            ", efficiency: " + singleT/multiF / 8
        );
        System.out.println();

    }


    static double testSingle(Matrix a, Matrix b) {
        double start;
        double end;

        start = System.currentTimeMillis();
        var result = Matrix.multiply(a, b);
        end = System.currentTimeMillis();

        return (end - start)/1000.0;
    }

    static double testMulti(Matrix a, Matrix b) {
        double start;
        double end;

        start = System.currentTimeMillis();
        var result = StripeAlgorithm.multiply(a, b, 8);
        end = System.currentTimeMillis();

        return (end - start)/1000.0;
    }

    static double testFork(Matrix a, Matrix b) {
        double start;
        double end;

        start = System.currentTimeMillis();
        var result = StripeForkAlgorithm.multiply(a, b, 8);
        end = System.currentTimeMillis();

        return (end - start)/1000.0;
    }
}
