package com.example.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Test {
    public static void run(long seed, int retestNumb) {

        final Function<Integer, List<Integer>> integerListGenerator = (Integer size) -> {
            return genIntegerList(size, 1_000_000, seed);
        };

        final Function<Integer, List<TestData>> testDataListGenerator = (Integer size) -> {
            return genTestDataList(size, 1_000_000, seed);
        };

        final Comparator<TestData> testDataCmp = (TestData a, TestData b) -> {
            return Double.compare(a.getComparableData(), b.getComparableData());
        };

        var integerScenario = TestScenario.makeScenario(
            integerListGenerator, Comparator.naturalOrder(), retestNumb
        )
            .addLengthParams(10000, 10_000_000, 0, 10)
            .addThreadNumbParams(1, 1, 1, 1)
            .addSublistParamParams(1, 1, 1, 1);

        integerScenario.start();
        integerScenario.outputResults();

        var teatDataScenario = TestScenario.makeScenario(
            testDataListGenerator, testDataCmp, retestNumb
        )
            .addLengthParams(10000, 10_000_000, 0, 10)
            .addThreadNumbParams(1, 1, 1, 1)
            .addSublistParamParams(1, 1, 1, 1);

        teatDataScenario.start();
        teatDataScenario.outputResults();


        ///////////////////////////////////////////////////////

        var scenario1 = TestScenario.makeScenario(
            integerListGenerator, Comparator.naturalOrder(), retestNumb
        )
            .addLengthParams(20_000_000, 20_000_000,0, 10)
            .addThreadNumbParams(1, 1, 1, 1)
            .addSublistParamParams(1, 1, 1, 1);

        scenario1.start();
        scenario1.outputResults();

        var sublistParamScenario1 = TestScenario.makeScenario(
            integerListGenerator, Comparator.naturalOrder(), retestNumb
        )
            .addLengthParams(100_000, 10_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(1092, 30000, 1, 3);

        sublistParamScenario1.start();
        sublistParamScenario1.outputResults();

        var sublistParamScenario2 = TestScenario.makeScenario(
            testDataListGenerator, testDataCmp, retestNumb
        )
            .addLengthParams(100_000, 10_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(1092, 30000, 1, 3);

        sublistParamScenario2.start();
        sublistParamScenario2.outputResults();

        var mainScenario1 = TestScenario.makeScenario(
            integerListGenerator, Comparator.naturalOrder(), retestNumb
        )
            .addLengthParams(100_000_000, 100_000_000, 0, 10)
            .addThreadNumbParams(1, 8, 7, 1)
            .addSublistParamParams(1092, 1092, 1, 1);

        mainScenario1.start();
        mainScenario1.outputResults();

        var mainScenario2 = TestScenario.makeScenario(
            testDataListGenerator, testDataCmp, retestNumb
        )
            .addLengthParams(100_000_000, 100_000_000, 0, 10)
            .addThreadNumbParams(1, 8, 7, 1)
            .addSublistParamParams(1092, 1092, 1, 1);

        mainScenario2.start();
        mainScenario2.outputResults();
    }

    private static List<Integer> genIntegerList(int size, int bound, long seed) {
        Random random = new Random(seed);

        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add(random.nextInt(bound));
        return list;
    }

    private static List<TestData> genTestDataList(int size, int bound, long seed) {
        Random random = new Random(seed);

        List<TestData> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add(
                new TestData(random.nextDouble(bound), random.nextDouble(), random.nextDouble())
            );
        return list;
    }

    record TestData(
        double a,
        double b,
        double c
    ) {
        public double getComparableData() {
            return Math.pow(a, b) * Math.exp(c);
        }
    }
}


