package com.example.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.example.test.Tester.Params;
import com.example.test.Tester.TestResult;

public class Test {
    public static void run(long seed, int retestNumb) {

        final Function<Integer, List<Integer>> integerListGenerator = (Integer size) -> {
            return genList(size, 100_000, seed);
        };

        final Params lengthParams = new Params(1_000_000, 100_000_000, 10);

        final Params threadNumbParams1 = new Params(4, 8, 4);
        final Params threadNumbParams2 = new Params(1, 1, 1);

        final Params sublistParamParams1 = new Params(5, 30, 2);
        final Params sublistParamParams2 = new Params(20, 20, 1);

        List<TestResult> results1 = 
            TestScenario.makeScenario(integerListGenerator, Comparator.naturalOrder(), retestNumb)
                .addLengthParams(lengthParams)
                .addThreadNumbParams(threadNumbParams1)
                .addSublistParamParams(sublistParamParams1)
                .start();
        
        // List<TestResult> results2 = 
        //     TestScenario.makeScenario(integerListGenerator, Comparator.naturalOrder(), retestNumb)
        //         .addLengthParams(lengthParams)
        //         .addThreadNumbParams(threadNumbParams2)
        //         .addSublistParamParams(sublistParamParams2)
        //         .start();

        outputParamResults(results1, lengthParams, threadNumbParams1, sublistParamParams1);
    }

    private static void outputParamResults(
        List<TestResult> results,
        Params length,
        Params threadNumb,
        Params sublistParamParam
    ) {
        System.out.printf(
            "%-10s %-10s %-10s %-5s%n","ListSize", "ThreadNum", "SublistParam", "AvgTime");

        for (int listSize = length.from(); 
            listSize <= length.to(); 
            listSize *= length.num()
        ) {
            System.out.println("--------------------------------------------");

            for (int threadNum = threadNumb.from(); 
                threadNum <= threadNumb.to(); 
                threadNum += threadNumb.num()
            ) {
                for (int sublistParam = sublistParamParam.from(); 
                    sublistParam <= sublistParamParam.to(); 
                    sublistParam += sublistParamParam.num()
                ) {
                    final int finalListSize = listSize;
                    final int finalThreadNum = threadNum;
                    final int finalSublistParam = sublistParam;

                    double avgTime = results.stream()
                        .filter(result -> result.size() == finalListSize)
                        .filter(result -> result.threadNumb() == finalThreadNum)
                        .filter(result -> result.sublistParam() == finalSublistParam)
                        .mapToDouble(result -> result.time())
                        .average()
                        .getAsDouble();

                    System.out.printf("%-10s %-10s %-10s %-5.3f%n", listSize, threadNum, sublistParam, avgTime);
                }
            }
        }
        System.out.println("--------------------------------------------");
        System.out.println();
    }

    private static List<Integer> genList(int size, int bound, long seed) {
        Random random = new Random(seed);

        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add(random.nextInt(bound));
        return list;
    }
}


