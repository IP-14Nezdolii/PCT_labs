package com.example.bench;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bencher {
    public static void bench(long seed) {
        int bound = 1_000;
        
        // System.out.println(
        //     Tester.run(
        //         genList(400_000, bound, seed), 8)
        // );

        // System.out.println(
        //     Tester.run(
        //         genList(4_000_000, bound, seed), 8)
        // );

        System.out.println(Tester.run(
                genList(10_000_000, bound, seed), 
                Integer::compareTo, 
                8
        ));
    }

    static List<Integer> genList(int size, int bound, long seed) {
        Random random = new Random(seed);

        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add(random.nextInt(bound));
        return list;
    }
}


