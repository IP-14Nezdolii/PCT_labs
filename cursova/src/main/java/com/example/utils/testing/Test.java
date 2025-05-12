package com.example.utils.testing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.example.utils.testing.output.TerminalOutputer;
import com.example.utils.testing.output.ScenarioResultsOutputer;

public class Test {
    private static final ScenarioResultsOutputer outputer = new TerminalOutputer();
    private static int SEED = 0;

    static final Function<Integer, List<Double>> listGenerator = (size) -> {
        return genDoubleList(size, 1_000_000);
    };

    public static void runThashold(int retestNumb) {
        var threcholdScenario = TestScenario.makeScenario(
            listGenerator, Comparator.naturalOrder(), retestNumb
        )
            .addLengthParams(10_000, 10_000_000, 0, 10)
            .addThreadNumbParams(1, 8, 0, 8)
            .addSublistParamParams(100, 100, 1, 1);

        threcholdScenario.start();
    }

    public static void runThreads(int retestNumb) {
        var mainScenario = TestScenario.makeScenario(
            listGenerator, Comparator.naturalOrder(), retestNumb
        )
            .addLengthParams(1_000, 10_000, 0, 10)
            .addThreadNumbParams(2, 8, 0, 4)
            .addSublistParamParams(1092, 1092, 1, 1);

        mainScenario.start();
        outputer.output(mainScenario);
    }

    public static void runSublistParam(int retestNumb) {
        var sublistParamScenario = TestScenario.makeScenario(
            listGenerator, Comparator.naturalOrder(), retestNumb
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
}


