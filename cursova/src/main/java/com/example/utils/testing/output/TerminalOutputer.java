package com.example.utils.testing.output;

import java.io.Serializable;
import java.util.List;

import com.example.utils.testing.Params;
import com.example.utils.testing.TestScenario;
import com.example.utils.testing.Tester.TestResult;

public class TerminalOutputer implements ScenarioResultsOutputer {
    private static final String BORDER = "-----------------------------------------------------------------------";
    private static final String HALF_BORDER = " |------------------------| ";
    private static final String FORMAT = "%-12s | %-12s | %-12s | %-15s | %-15s%n";

    public void output(TestScenario<? extends Serializable> scenario) {
        for (Params length : scenario.getLengthParams()) { 
            for (Params threadNumb : scenario.getThreadNumbParams()) {
                for (Params sublistParamParam : scenario.getSublistParamParams()) {
                    outputParamResults(scenario.getResults(), length, threadNumb, sublistParamParam);
                }
            }
        }
    };

    private static void outputParamResults(
        List<TestResult> results,
        Params length,
        Params threadNumb,
        Params sublistParamParam
    ) {
        System.out.println(HALF_BORDER + results.get(0).sortedClassName() + HALF_BORDER);
        System.out.println(BORDER);
        System.out.printf(FORMAT,"ListSize", "ThreadNum", "SublistParam", "AvgTime, ms", "SpeedUp");

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
                    final int finalListSize = listSize;
                    final int finalThreadNum = threadNum;
                    final int finalSublistParam = sublistParam;

                    double singleTime = results.stream()
                        .filter(result -> result.size() == finalListSize)
                        .filter(result -> result.threadNumb() == 1)
                        .mapToDouble(result -> result.time())
                        .average()
                        .getAsDouble();

                    double avgTime = results.stream()
                        .filter(result -> result.size() == finalListSize)
                        .filter(result -> result.threadNumb() == finalThreadNum)
                        .filter(result -> result.sublistParam() == finalSublistParam)
                        .mapToDouble(result -> result.time())
                        .average()
                        .getAsDouble();

                    sublistParam *= sublistParamParam.mul;
     
                    System.out.printf(FORMAT, listSize, threadNum, sublistParam, String.format("%.3f", avgTime), String.format("%.3f", singleTime / avgTime));
                }
                threadNum *= threadNumb.mul;
            }
            listSize *= length.mul;
        }
        System.out.println(BORDER);
        System.out.println();
    }
}
