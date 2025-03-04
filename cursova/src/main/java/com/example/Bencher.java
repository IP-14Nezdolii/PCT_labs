package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.example.sort.ParallelShellSort;
import com.example.sort.ShellSort;

public class Bencher {
    public static void bench(long seed) {
        Random random = new Random(seed);

        int size = 20_000_000;
        //int size = 10;
        List<Integer> list1 = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list1.add(random.nextInt(10000));
        }
        Comparator<Integer> comp = Integer::compare;
        List<Integer> list2 = new ArrayList<>(list1);
        
        long startTime = System.currentTimeMillis();
        //ParallelShellSort.sort(list1, comp, 8);
        ShellSort.sort(list1, comp);
        //ParallelShellSort.sort(list1, comp);
        long endTime = System.currentTimeMillis();

        long timeElapsed = endTime - startTime;
        System.out.println("Час виконання в секундах: " + timeElapsed/1000);

        startTime = System.currentTimeMillis(); 
        //Tester.shellSort(list2);
        ParallelShellSort.sort(list2, comp,  8);
        endTime = System.currentTimeMillis();

        timeElapsed = endTime - startTime;
        System.out.println("Час виконання в секундах: " + timeElapsed/1000);

        if (!Tester.isSorted(list1, comp)) 
            throw new IllegalStateException("List1 is not sorted");
        if (!Tester.isSorted(list2, comp)) 
            throw new IllegalStateException("List2 is not sorted");

        if (!list1.equals(list2)) {
            throw new IllegalStateException("Список з дублікатами повинен бути відсортований");
        }
    }

    static RunResult run(Runnable sortRun, int threadNumb) {
        long startTime = System.currentTimeMillis();
        sortRun.run();
        long endTime = System.currentTimeMillis();

        return new RunResult(endTime - startTime, threadNumb);
    }

    static class RunResult {
        public final long time;
        public final int threadNumb;

        public RunResult(
            long time,
            int threadNumb
        ) {
            this.time = time;
            this.threadNumb = threadNumb;
        }
    }
}

class Tester 
{
    static List<Integer> genList(int size, int seed, int bound) {
        Random random = new Random(seed);

        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add(random.nextInt(bound));
        return list;
    }

    static void shellSort(List<Integer> array) {
        for (int s = array.size() / 2; s > 0; s /= 2) {
            for (int i = s; i < array.size(); ++i) {
                for (int j = i - s; j >= 0 && array.get(j) > array.get(j + s); j -= s)  {
                    Collections.swap(array, j, j + s);
                }
            }
        }        
    }

    static void parallelShellSort(List<Integer> list, int threadNumb) {
        ParallelShellSort.sort(list, Integer::compare, threadNumb);
    }

    static <T> boolean isSorted(List<T> list, Comparator<T> comp) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (comp.compare(list.get(i), list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }
}
