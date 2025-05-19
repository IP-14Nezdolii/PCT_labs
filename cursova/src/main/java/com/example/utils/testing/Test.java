package com.example.utils.testing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.example.utils.testing.output.TerminalOutputer;
import com.example.sort.ParallelShell;
import com.example.sort.QueueingShell;
import com.example.utils.testing.output.ScenarioResultsOutputer;

public class Test {
    private static final ScenarioResultsOutputer outputer = new TerminalOutputer();
    private static int SEED = 0;

    static final Function<Integer, List<Double>> listGenerator = (size) -> {
        return genDoubleList(size, 1_000_000);
    };

    public static void runThrasholdP(int retestNumb) {
        SortMethod<Double> m = ParallelShell::sort;

        var thrasholdScenario = TestScenario.makeScenario(
            listGenerator, Comparator.naturalOrder(), retestNumb, m
        )
            .addLengthParams(10_000, 10_000_000, 0, 10)
            .addThreadNumbParams(1, 8, 0, 8)
            .addSublistParamParams(100, 100, 1, 1);

        thrasholdScenario.start();
    }

    public static void runThrasholdQ(int retestNumb) {
        SortMethod<Double> m = QueueingShell::sort;

        var thrasholdScenario = TestScenario.makeScenario(
            listGenerator, Comparator.naturalOrder(), retestNumb, m
        )
            .addLengthParams(10_000, 10_000_000, 0, 10)
            .addThreadNumbParams(1, 8, 0, 8)
            .addSublistParamParams(100, 100, 1, 1);

        thrasholdScenario.start();
    }

    public static void runSingle(int retestNumb) {
        SortMethod<Double> m = ParallelShell::sort;

        var mainScenario = TestScenario.makeScenario(
            listGenerator, Comparator.naturalOrder(), retestNumb, m
        )
            .addLengthParams(10_000, 10_000_000, 0, 10)
            .addThreadNumbParams(1, 1, 1, 1)
            .addSublistParamParams(1, 1, 1, 1);

        mainScenario.start();
        outputer.output(mainScenario);
    }

    public static void runThreadsP(int retestNumb) {
        SortMethod<Double> m = ParallelShell::sort;

        var mainScenario = TestScenario.makeScenario(
            listGenerator, Comparator.naturalOrder(), retestNumb, m
        )
            .addLengthParams(20_000, 20_000_000, 0, 10)
            .addThreadNumbParams(2, 8, 0, 4)
            .addSublistParamParams(40, 40, 1, 1);

        mainScenario.start();
        outputer.output(mainScenario);
    }

    public static void runThreadsQ(int retestNumb) {
        SortMethod<Double> m = QueueingShell::sort;

        var mainScenario = TestScenario.makeScenario(
            listGenerator, Comparator.naturalOrder(), retestNumb, m
        )
            .addLengthParams(20_000, 20_000_000, 0, 10)
            .addThreadNumbParams(2, 8, 0, 4)
            .addSublistParamParams(20, 20, 1, 1);

        mainScenario.start();
        outputer.output(mainScenario);
    }

    public static void runSublistParamP(int retestNumb) {
        SortMethod<Double> m = ParallelShell::sort;

        var sublistParamScenario = TestScenario.makeScenario(
            listGenerator, Comparator.naturalOrder(), retestNumb, m
        )
            .addLengthParams(2_000_000, 20_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(5, 1000, 0, 2);

        sublistParamScenario.start();
        outputer.output(sublistParamScenario);
    }

    public static void runSublistParamQ(int retestNumb) {
        SortMethod<Double> m = QueueingShell::sort;

        var sublistParamScenario = TestScenario.makeScenario(
            listGenerator, Comparator.naturalOrder(), retestNumb, m
        )
            .addLengthParams(2_000_000, 20_000_000, 0, 10)
            .addThreadNumbParams(8, 8, 1, 1)
            .addSublistParamParams(5, 1000, 0, 2);

        sublistParamScenario.start();
        outputer.output(sublistParamScenario);
    }

    private static List<Double> genDoubleList(int size, int bound) {
        Random random = new Random(SEED);
        List<Double> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) 
            list.add(random.nextDouble(bound));
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


