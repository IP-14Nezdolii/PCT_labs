package com.example.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import com.example.test.Tester.Params;
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

    public TestScenario<T> addLengthParams(Params params) {
        lengthParams.add(params);
        return this;
    }

    public TestScenario<T> addThreadNumbParams(Params params) {
        threadNumbParams.add(params);
        return this;
    }

    public TestScenario<T> addSublistParamParams(Params params) {
        sublistParamParams.add(params);
        return this;
    }

    public List<TestResult> start() {
        for (int i = 0; i < retestNumb; i++) 
            startScenatio();

        return results;
    }

    private void startScenatio() {
        for (Params length : lengthParams) {
            for (Params threadNumb : threadNumbParams) {
                for (Params sublistParamParam : sublistParamParams) {

                    for (int listSize = length.from(); 
                        listSize <= length.to(); 
                        listSize *= length.num()
                    ) {
                        for (int threadNum = threadNumb.from(); 
                            threadNum <= threadNumb.to(); 
                            threadNum += threadNumb.num()
                        ) {
                            for (int sublistParam = sublistParamParam.from(); 
                                sublistParam <= sublistParamParam.to(); 
                                sublistParam += sublistParamParam.num()
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
}
