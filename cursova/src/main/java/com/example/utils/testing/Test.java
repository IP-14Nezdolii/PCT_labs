package com.example.utils.testing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.example.utils.testing.output.TerminalOutputer;

import lombok.Value;

import com.example.sort.ParallelShell;
import com.example.sort.QueueingShell;
import com.example.utils.testing.output.ScenarioResultsOutputer;

public class Test {
    private static final ScenarioResultsOutputer outputer = new TerminalOutputer();
    private static int SEED = 0;

    static final Function<Integer, List<Double>> doubleListGenerator = (size) -> {
        return genDoubleList(size, 1_000_000);
    };

    static final Function<Integer, List<TestData>> testDataListGenerator = (size) -> {
        return genTestDataList(size);
    };

    public static void runThrasholdP(int retestNumb) {
        var thrasholdScenario1 = TestScenario.makeScenario(
            doubleListGenerator, Comparator.naturalOrder(), retestNumb, ParallelShell::sort
        )
            .addLengthParams(100_000, 10_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(40, 40, 1, 1);

        thrasholdScenario1.start();

        var thrasholdScenario2 = TestScenario.makeScenario(
            testDataListGenerator, TestData.getComparator(), retestNumb, ParallelShell::sort
        )
            .addLengthParams(100_000, 10_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(40, 40, 1, 1);

        thrasholdScenario2.start();
    }

    public static void runThrasholdQ(int retestNumb) {
        var thrasholdScenario1 = TestScenario.makeScenario(
            doubleListGenerator, Comparator.naturalOrder(), retestNumb, QueueingShell::sort
        )
            .addLengthParams(100_000, 10_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(20, 20, 1, 1);

        thrasholdScenario1.start();

        var thrasholdScenario2 = TestScenario.makeScenario(
            testDataListGenerator, TestData.getComparator(), retestNumb, QueueingShell::sort
        )
            .addLengthParams(100_000, 10_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(20, 20, 1, 1);

        thrasholdScenario2.start();
    }

    public static void runSingle(int retestNumb) {
        var mainScenario1 = TestScenario.makeScenario(
            doubleListGenerator, Comparator.naturalOrder(), retestNumb, ParallelShell::sort
        )
            .addLengthParams(10_000, 10_000_000, 0, 10)
            .addThreadNumbParams(1, 1, 1, 1)
            .addSublistParamParams(1, 1, 1, 1);

        mainScenario1.start();
        outputer.output(mainScenario1);

        var mainScenario2 = TestScenario.makeScenario(
            testDataListGenerator, TestData.getComparator(), retestNumb, ParallelShell::sort
        )
            .addLengthParams(10_000, 10_000_000, 0, 10)
            .addThreadNumbParams(1, 1, 1, 1)
            .addSublistParamParams(1, 1, 1, 1);

        mainScenario2.start();
        outputer.output(mainScenario2);
    }

        public static void runSublistParamP(int retestNumb) {
        var sublistParamScenario1 = TestScenario.makeScenario(
            doubleListGenerator, Comparator.naturalOrder(), retestNumb, ParallelShell::sort
        )
            .addLengthParams(2_000_000, 20_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(5, 700, 0, 2);

        sublistParamScenario1.start();
        outputer.output(sublistParamScenario1);

        var sublistParamScenario2 = TestScenario.makeScenario(
            testDataListGenerator, TestData.getComparator(), retestNumb, ParallelShell::sort
        )
            .addLengthParams(2_000_000, 20_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(5, 700, 0, 2);

        sublistParamScenario2.start();
        outputer.output(sublistParamScenario2);
    }

    public static void runSublistParamQ(int retestNumb) {
        var sublistParamScenario = TestScenario.makeScenario(
            doubleListGenerator, Comparator.naturalOrder(), retestNumb, QueueingShell::sort
        )
            .addLengthParams(2_000_000, 20_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(5, 700, 0, 2);

        sublistParamScenario.start();
        outputer.output(sublistParamScenario);

        var sublistParamScenario2 = TestScenario.makeScenario(
            testDataListGenerator, TestData.getComparator(), retestNumb, QueueingShell::sort
        )
            .addLengthParams(2_000_000, 20_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(5, 700, 0, 2);

        sublistParamScenario2.start();
        outputer.output(sublistParamScenario2);
    }

    public static void runThreadsP(int retestNumb) {
        var mainScenario1 = TestScenario.makeScenario(
            doubleListGenerator, Comparator.naturalOrder(), retestNumb, ParallelShell::sort
        )
            .addLengthParams(100_000, 10_000_000, 0, 10)
            .addThreadNumbParams(2, 8, 0, 2)
            .addSublistParamParams(30, 30, 1, 1);

        mainScenario1.start();
        outputer.output(mainScenario1);

        var mainScenario2 = TestScenario.makeScenario(
            testDataListGenerator, TestData.getComparator(), retestNumb, ParallelShell::sort
        )
            .addLengthParams(100_000, 10_000_000, 0, 10)
            .addThreadNumbParams(2, 8, 0, 2)
            .addSublistParamParams(30, 30, 1, 1);

        mainScenario2.start();
        outputer.output(mainScenario2);
    }

    public static void runThreadsQ(int retestNumb) {
        var mainScenario1 = TestScenario.makeScenario(
            doubleListGenerator, Comparator.naturalOrder(), retestNumb, QueueingShell::sort
        )
            .addLengthParams(100_000, 10_000_000, 0, 10)
            .addThreadNumbParams(2, 8, 0, 2)
            .addSublistParamParams(20, 20, 1, 1);

        mainScenario1.start();
        outputer.output(mainScenario1);

        var mainScenario2 = TestScenario.makeScenario(
            testDataListGenerator, TestData.getComparator(), retestNumb, QueueingShell::sort
        )
            .addLengthParams(100_000, 10_000_000, 0, 10)
            .addThreadNumbParams(2, 8, 0, 2)
            .addSublistParamParams(20, 20, 1, 1);

        mainScenario2.start();
        outputer.output(mainScenario2);
    }

    private static List<Double> genDoubleList(int size, int bound) {
        Random random = new Random(SEED);
        List<Double> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) 
            list.add(random.nextDouble(bound));
        return list;
    }

    private static List<TestData> genTestDataList(int size) {
        Random random = new Random(SEED);
        List<TestData> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) 
            list.add(TestData.nextRendomTestData(random));
        return list;
    }

    public static void setSeed(int seed) {
        SEED = seed;
    }

    @FunctionalInterface
    public interface SortMethod<T> {
        void sort(List<T> list, Comparator<T> cmp, int maxThreads, int minSublistLen);
    } 
}

@Value
class TestData implements Serializable {
    int paramA;
    double paramB;
    BigInteger paramC;
    BigDecimal paramD;

    public static TestData nextRendomTestData(Random random) {
        return new TestData(
            random.nextInt(Integer.MAX_VALUE),
            random.nextDouble(Double.MAX_VALUE),
            BigInteger.valueOf(random.nextInt(Integer.MAX_VALUE)).multiply(BigInteger.valueOf(2)),
            BigDecimal.valueOf(random.nextDouble(Double.MAX_VALUE)).multiply(BigDecimal.valueOf(2))
        );
    }

    public static Comparator<TestData> getComparator() {
        return (a, b) -> {
            Integer[] cmps = {
                Integer.compare(a.paramA, b.paramA),
                Double.compare(a.paramB, b.paramB),
                a.paramC.compareTo(b.paramC),
                a.paramD.compareTo(b.paramD),
            };

            int zeroCount = 0;

            for (Integer cmp : cmps) {
                if (cmp == 0) {
                    zeroCount++;
                } 
            }

            if (zeroCount >= cmps.length / 2) {
                return 0;
            }
            else {
                return cmps[0] != 0 
                    ? cmps[0] 
                    : cmps[1] != 0 
                        ? cmps[1] 
                        : cmps[2] != 0 
                            ? cmps[2] 
                            : cmps[3];
            }
        };
    }
}


