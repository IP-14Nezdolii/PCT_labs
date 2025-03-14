package com.example.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Test {
    public static void run(long seed, int retestNumb) {

        final Function<Integer, List<Integer>> integerListGenerator = (Integer size) -> {
            return genList(size, 100_000, seed);
        };

        var scenario1 = TestScenario.makeScenario(
            integerListGenerator, Comparator.naturalOrder(), retestNumb
        )
            .addLengthParams(1_000_000, 10_000_000, 10)
            .addThreadNumbParams(1, 1, 1)
            .addSublistParamParams(1, 1, 1);

        scenario1.start();
        scenario1.outputResults();

        var scenario2 = TestScenario.makeScenario(
            integerListGenerator, Comparator.naturalOrder(), retestNumb
        )
            .addLengthParams(1_000_000, 10_000_000, 10)
            .addThreadNumbParams(2, 8, 2)
            .addSublistParamParams(10, 30, 5);

        scenario2.start();
        scenario2.outputResults();
    }

    private static List<Integer> genList(int size, int bound, long seed) {
        Random random = new Random(seed);

        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add(random.nextInt(bound));
        return list;
    }
}


