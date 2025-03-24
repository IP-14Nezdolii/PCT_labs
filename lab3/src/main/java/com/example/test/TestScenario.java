package com.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.test.Tester.TestResult;
import com.example.utils.Matrix;

public class TestScenario<T> {
    private final List<TestResult> results = new ArrayList<>();
    private final List<TestResult> stripeResults = new ArrayList<>();
    private final List<TestResult> foxResults = new ArrayList<>();

    private final HashMap<Integer, Matrix> matricesA = new HashMap<>();
    private final HashMap<Integer, Matrix> matricesB = new HashMap<>();

    private final HashMap<Integer, Matrix> expectedMatrices = new HashMap<>();

    private final List<Params> lengthParams = new ArrayList<>();
    private final List<Params> threadNumbParams = new ArrayList<>();

    private final int nRetest;
    private final long seed;

    private static class Params {
        int from;
        int to; 
        int mul;
        int sum;

        public Params(int from, int to, int mul, int sum) {
            this.from = from;
            this.to = to;
            this.mul = mul;
            this.sum = sum;
        }
    }

    private TestScenario(int retestNumb, long seed) {
        this.nRetest = retestNumb;
        this.seed = seed;
    }

    public static <T> TestScenario<T> makeScenario(int nRetest, long seed) {
        return new TestScenario<T>(nRetest, seed);
    }

    public TestScenario<T> addLengthParams(int from, int to, int sum, int mul) {
        lengthParams.add(new Params(from, to, mul, sum));
        return this;
    }

    public TestScenario<T> addThreadNumbParams(int from, int to, int sum, int mul) {
        threadNumbParams.add(new Params(from, to, mul, sum));
        return this;
    }

    public List<TestResult> start() {
        for (int i = 0; i < nRetest; i++) 
            startScenatio();

        return results;
    }

    public void outputResults() {
        System.out.println(
            "-------------| "+ "Default multiply" +" |-------------");
        System.out.println(
            "-----------------------------------------------");

        for (Params length : lengthParams) {
            outputParamResults(results, length, new Params(0, 0, 1, 1));
        }

        System.out.println(
            "-------------| "+ "Stripe multiply" +" |-------------");
        System.out.println(
            "-----------------------------------------------");

        for (Params length : lengthParams) {
            for (Params threadNumb : threadNumbParams) {
                outputParamResults(stripeResults, length, threadNumb);
            }
        }

        System.out.println(
            "-------------| "+ "Fox multiply" +" |-------------");
        System.out.println(
            "-----------------------------------------------");

        for (Params length : lengthParams) {
            for (Params threadNumb : threadNumbParams) {
                outputParamResults(foxResults, length, threadNumb);
            }
        }
    }

    private void startScenatio() {
        for (Params length : lengthParams) {
            for (Params threadNumb : threadNumbParams) {

                for (int rowSize = length.from; 
                    rowSize <= length.to; 
                    rowSize += length.sum,
                    rowSize *= length.mul
                ) {
                    addMatrices(rowSize);
                    results.add(Tester.run(
                        matricesA.get(rowSize),
                        matricesB.get(rowSize),
                        expectedMatrices
                    ));

                    for (int threadNum = threadNumb.from; 
                        threadNum <= threadNumb.to; 
                        threadNum += threadNumb.sum,
                        threadNum *= threadNumb.mul
                    ) {
                        stripeResults.add(Tester.runStripe(
                            matricesA.get(rowSize),
                            matricesB.get(rowSize),
                            expectedMatrices.get(rowSize),
                            threadNum
                        ));
                        foxResults.add(Tester.runFox(
                            matricesA.get(rowSize),
                            matricesB.get(rowSize),
                            expectedMatrices.get(rowSize),
                            threadNum
                        ));

                        
                    }
                }    
            }
        }
    }

    private void addMatrices(int rowSize) {
        if (!matricesA.containsKey(rowSize)) {

            Matrix matrixA = Matrix.genRandomMatrix(rowSize, rowSize, seed);
            Matrix matrixB = Matrix.genRandomMatrix(rowSize, rowSize, seed + 111);

            matricesA.put(rowSize, matrixA);
            matricesB.put(rowSize, matrixB);
        }
    }

    private static void outputParamResults(
        List<TestResult> results,
        Params length,
        Params threadNumb
    ) {
        System.out.printf(
            "%-10s | %-10s | %-5s%n","MatrixRowSize", "ThreadNum", "AvgTime");

        for (int rowSize = length.from; 
            rowSize <= length.to; 
            rowSize += length.sum,
            rowSize *= length.mul
        ) {
            for (int threadNum = threadNumb.from; 
                threadNum <= threadNumb.to; 
                threadNum += threadNumb.sum,
                threadNum *= threadNumb.mul
            ) {
  
                final int finalListSize = rowSize;
                final int finalThreadNum = threadNum;

                double avgTime = results.stream()
                    .filter(result -> result.size() == finalListSize)
                    .filter(result -> result.threadNumb() == finalThreadNum)
                    .mapToDouble(result -> result.time())
                    .average()
                    .getAsDouble();
    
                System.out.printf("%-10s | %-10s | %-5.3f%n", rowSize, threadNum, avgTime);
            }
        }
        System.out.println(
            "-----------------------------------------------");
        System.out.println();
    }
}
