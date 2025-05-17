package com.example.utils.testing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import com.example.sort.Shell;
import com.example.utils.testing.Test.SortMethod;
import com.example.utils.testing.Tester.TestResult;

import lombok.Getter;

@Getter
public class TestScenario<T> {
    private final List<TestResult> results = new ArrayList<>();

    private final HashMap<Integer, List<T>> lists = new HashMap<>();
    private final HashMap<Integer, List<T>> expectedLists = new HashMap<>();

    private final List<Params> lengthParams = new ArrayList<>();
    private final List<Params> threadNumbParams = new ArrayList<>();
    private final List<Params> sublistParamParams = new ArrayList<>();

    private final Function<Integer, List<T>> listGenerator;
    private final Comparator<T> cmp;
    private final int retestNumb;

    private final SortMethod<T> sorter;

    private TestScenario(
        Function<Integer, List<T>> listGenerator, 
        Comparator<T> cmp,
        int retestNumb,
        SortMethod<T> sorter
    ) {
        this.listGenerator = listGenerator;
        this.cmp = cmp;
        this.retestNumb = retestNumb;
        this.sorter = sorter;
    }

    public static <T> TestScenario<T> makeScenario(
        Function<Integer, List<T>> listGenerator, 
        Comparator<T> cmp,
        int retestNumb,
        SortMethod<T> sorter
    ) {
        return new TestScenario<T>(listGenerator, cmp, retestNumb, sorter);
    }

    public TestScenario<T> addLengthParams(int from, int to, int sum, int mul) {
        lengthParams.add(new Params(from, to, mul, sum));
        return this;
    }

    public TestScenario<T> addThreadNumbParams(int from, int to, int sum, int mul) {
        threadNumbParams.add(new Params(from, to, mul, sum));
        return this;
    }

    public TestScenario<T> addSublistParamParams(int from, int to, int sum, int mul) {
        sublistParamParams.add(new Params(from, to, mul, sum));
        return this;
    }

    public List<TestResult> start() {
        for (int i = 0; i < retestNumb; i++) {
            System.out.println("Starting iteration " + (i + 1) + " of " + retestNumb);

            testSingle();
            startScenatio();
        }
        return results;
    }

    private void startScenatio() {
        for (Params length : lengthParams) {
            for (Params threadNumb : threadNumbParams) {
                for (Params sublistParamParam : sublistParamParams) {

                    for (int listSize = length.from; 
                        listSize <= length.to; 
                        listSize += length.sum
                    ) {
                        for (int threadNum = threadNumb.from; 
                            threadNum <= threadNumb.to; 
                            threadNum += threadNumb.sum
                        ) {
                            for (int sublistParam = sublistParamParam.from; 
                                sublistParam <= sublistParamParam.to; 
                                sublistParam += sublistParamParam.sum
                            ) {     
                                results.add(Tester.run(
                                    new ArrayList<>(lists.get(listSize)),
                                    expectedLists.get(listSize), 
                                    cmp,
                                    threadNum, 
                                    sublistParam,
                                    sorter
                                ));

                                sublistParam *= sublistParamParam.mul;
                            }
                            threadNum *= threadNumb.mul;
                        }
                        listSize *= length.mul;
                    }
                }
            }
        }
    }

    private void testSingle() {
        for (Params length : lengthParams) {
            for (int listSize = length.from; listSize <= length.to; listSize += length.sum) {

                if (!lists.containsKey(listSize)) {
                    lists.put(listSize, listGenerator.apply(listSize));
                }
                List<T> expected = new ArrayList<>(lists.get(listSize));

                double start = System.nanoTime();
                Shell.sort(expected, cmp);
                double end = System.nanoTime();

                results.add(new TestResult(
                    expected.size(), 
                    (end - start)/1_000_000, 
                    1, 
                    0,
                    expected.get(0).getClass().getName()
                ));

                expectedLists.putIfAbsent(listSize, expected);

                listSize *= length.mul;
            }
        }
    }
}
