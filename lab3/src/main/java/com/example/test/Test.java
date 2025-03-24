package com.example.test;

public class Test {
    public static void run(long seed, int nRetest, int trashold) {
        var trasholdScenario = TestScenario.makeScenario(trashold, seed)
            .addLengthParams(504, 504, 1, 1)
            .addThreadNumbParams(4, 4, 1, 1);
        trasholdScenario.start();

        var scenario1 = TestScenario.makeScenario(nRetest, seed)
            .addLengthParams(504, 1512, 504, 1)
            .addThreadNumbParams(2, 8, 2, 1);
            scenario1.start();
        scenario1.outputResults();

        var scenario2 = TestScenario.makeScenario(nRetest, seed)
            .addLengthParams(10, 20, 10, 1)
            .addThreadNumbParams(4, 9, 5, 1);
            scenario2.start();
        scenario2.outputResults();
    }
}


