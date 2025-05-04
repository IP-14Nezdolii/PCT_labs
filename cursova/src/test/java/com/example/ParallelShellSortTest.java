package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.example.sort.ParallelShell;

public class ParallelShellSortTest {
    private final long seed = 10;
    private final long maxThreads = 10;

    @Test
    public void testSortedList() {
        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {

            List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
            Comparator<Integer> comp = Integer::compare;
            
            ParallelShell.sort(list, comp, 4);

            List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
            assertEquals(
                "Відсортований список повинен залишитися незмінним", expected, list
            );
            
        }
    }

    @Test
    public void testReverseSortedList() {
        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {

            List<Integer> list = Arrays.asList(5, 4, 3, 2, 1);
            Comparator<Integer> comp = Integer::compare;
            
            ParallelShell.sort(list, comp, 4);
            
            List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
            assertEquals(
                "Зворотно відсортований список повинен бути перевернутий", expected, list
            );
            
        }
    }

    @Test
    public void testEmptyList() {
        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {

            List<Integer> list = new ArrayList<>();
            Comparator<Integer> comp = Integer::compare;
            
            ParallelShell.sort(list, comp, 4);
            
            assertTrue(
                "Порожній список повинен залишитися порожнім", list.isEmpty()
            );
            
        }
    }

    @Test
    public void testSingleElementList() {
        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {

            List<Integer> list = Arrays.asList(42);
            Comparator<Integer> comp = Integer::compare;
            
            ParallelShell.sort(list, comp, 4);
            
            List<Integer> expected = Arrays.asList(42);
            assertEquals(
                "Список з одним елементом повинен залишитися незмінним", expected, list
            );
            
        }
    }

    @Test
    public void testTwoElementList() {
        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {

            List<Integer> list1 = Arrays.asList(42, 36);
            List<Integer> list2 = Arrays.asList(36, 42);
            
            Comparator<Integer> comp = Integer::compare;
            
            ParallelShell.sort(list1, comp, 4);
            ParallelShell.sort(list2, comp, 4);
            
            List<Integer> expected = Arrays.asList(36, 42);
            assertEquals(
                "Список з одним елементом повинен залишитися незмінним", expected, list1
            );
            assertEquals(
                "Список з одним елементом повинен залишитися незмінним", expected, list2
            );
        }
    }

    @Test
    public void testListWithDuplicates() {
        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {

            List<Integer> list = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
            Comparator<Integer> comp = Integer::compare;
            
            ParallelShell.sort(list, comp, 4);
            
            List<Integer> expected = Arrays.asList(1, 1, 2, 3, 3, 4, 5, 5, 6, 9);
            assertEquals(
                "Список з дублікатами повинен бути відсортований", expected, list
            );
        }
    }

    @Test
    public void testLargeRandomList() {

        int size = 10000;
        int bound = 10000;

        Random random = new Random(seed);
        Comparator<Integer> cmp = Integer::compare;

        List<Integer> old = new ArrayList<>(bound);
        for (int i = 0; i < size; i++) {
            old.add(random.nextInt(bound));
        }
        List<Integer> expected = old.stream().sorted(cmp).toList();

        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {
            List<Integer> list = new ArrayList<>(old);
            ParallelShell.sort(list, cmp, 4);

            assertEquals(
                "Список не відсортовано: seeed="+seed+" size="+size+" bound="+bound, expected, list
            );
        }
    }

    @Test
    public void testRandomArrays() {
        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {

            Random random = new Random(seed);
            Comparator<Integer> cmp = Integer::compare;

            for (int i = 0; i < 10000; i++) {

                ArrayList<Integer> list = new ArrayList<>(random.nextInt(300) + 1);

                for (int j = 0; j < list.size(); j++) 
                    list.add(random.nextInt(100));

                ArrayList<Integer> old = new ArrayList<>(list);
                List<Integer> expected = list.stream().sorted(cmp).toList()
;
                ParallelShell.sort(list, cmp, 4);

                assertEquals(old.toString(), expected, list);
            }
            
        }
    }
}