package com.example.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import com.example.test.Tester.TestResult;

public class TestScenario<T> {
    private final List<TestResult> results = new ArrayList<>();

    private final HashMap<Integer, List<T>> lists = new HashMap<>();

    private final List<Params> lengthParams = new ArrayList<>();
    private final List<Params> threadNumbParams = new ArrayList<>();
    private final List<Params> sublistParamParams = new ArrayList<>();

    private final Function<Integer, List<T>> listGenerator;
    private final Comparator<T> cmp;
    private final int retestNumb;

    private record Params(
        int from, 
        int to, 
        int val
    ) {}

    private TestScenario(
        Function<Integer, List<T>> listGenerator, 
        Comparator<T> cmp,
        int retestNumb
    ) {
        this.listGenerator = listGenerator;
        this.cmp = cmp;
        this.retestNumb = retestNumb;
    }

    public static <T> TestScenario<T> makeScenario(
        Function<Integer, List<T>> listGenerator, 
        Comparator<T> cmp,
        int retestNumb
    ) {
        return new TestScenario<T>(listGenerator, cmp, retestNumb);
    }

    public TestScenario<T> addLengthParams(int from, int to, int val) {
        lengthParams.add(new Params(from, to, val));
        return this;
    }

    public TestScenario<T> addThreadNumbParams(int from, int to, int val) {
        threadNumbParams.add(new Params(from, to, val));
        return this;
    }

    public TestScenario<T> addSublistParamParams(int from, int to, int val) {
        sublistParamParams.add(new Params(from, to, val));
        return this;
    }

    public List<TestResult> start() {
        for (int i = 0; i < retestNumb; i++) 
            startScenatio();

        return results;
    }

    public void outputResults() {
        for (Params length : lengthParams) {
            for (Params threadNumb : threadNumbParams) {
                for (Params sublistParamParam : sublistParamParams) {
                    outputParamResults(results, length, threadNumb, sublistParamParam);
                }
            }
        }
    }

    private void startScenatio() {
        for (Params length : lengthParams) {
            for (Params threadNumb : threadNumbParams) {
                for (Params sublistParamParam : sublistParamParams) {

                    for (int listSize = length.from(); 
                        listSize <= length.to(); 
                        listSize *= length.val()
                    ) {
                        for (int threadNum = threadNumb.from(); 
                            threadNum <= threadNumb.to(); 
                            threadNum += threadNumb.val()
                        ) {
                            for (int sublistParam = sublistParamParam.from(); 
                                sublistParam <= sublistParamParam.to(); 
                                sublistParam += sublistParamParam.val()
                            ) {

                                if (!lists.containsKey(listSize))
                                    lists.put(listSize, listGenerator.apply(listSize));

                                results.add(Tester.run(
                                        new ArrayList<>(lists.get(listSize)), cmp, threadNum, sublistParam
                                ));
                            }
                        }
                    }
                }
            }
        }
    }

    private static void outputParamResults(
        List<TestResult> results,
        Params length,
        Params threadNumb,
        Params sublistParamParam
    ) {
        System.out.println(
            "-------------| "+ results.get(0).sortedClassName() +" |-------------");
        System.out.println(
            "-----------------------------------------------");
        System.out.printf(
            "%-10s | %-10s | %-10s | %-5s%n","ListSize", "ThreadNum", "SublistParam", "AvgTime");

        for (int listSize = length.from(); 
            listSize <= length.to(); 
            listSize *= length.val()
        ) {
            for (int threadNum = threadNumb.from(); 
                threadNum <= threadNumb.to(); 
                threadNum += threadNumb.val()
            ) {
                for (int sublistParam = sublistParamParam.from(); 
                    sublistParam <= sublistParamParam.to(); 
                    sublistParam += sublistParamParam.val()
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
     
                    System.out.printf("%-10s | %-10s | %-12s | %-5.3f%n", listSize, threadNum, sublistParam, avgTime);
                }
            }
        }
        System.out.println(
            "-----------------------------------------------");
        System.out.println();
    }
}
