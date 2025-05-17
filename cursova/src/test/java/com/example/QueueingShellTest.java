package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.example.sort.QueueingShell;
import com.example.sort.Shell;

public class QueueingShellTest {
    private final long seed = 10;
    private final long maxThreads = 10;

    @Test
    public void testSortedList() {
        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {

            List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
            Comparator<Integer> comp = Integer::compare;
            
            QueueingShell.sort(list, comp, threadNumb, 0);

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
            
            QueueingShell.sort(list, comp, threadNumb, 0);
            
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
            
            QueueingShell.sort(list, comp, threadNumb, 0);
            
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
            
            QueueingShell.sort(list, comp, threadNumb, 0);
            
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
            
            QueueingShell.sort(list1, comp, threadNumb, 0);
            QueueingShell.sort(list2, comp, threadNumb, 0);
            
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
            
            QueueingShell.sort(list, comp, threadNumb, 0);
            
            List<Integer> expected = Arrays.asList(1, 1, 2, 3, 3, 4, 5, 5, 6, 9);
            assertEquals(
                "Список з дублікатами повинен бути відсортований", expected, list
            );
        }
    }

    @Test
    public void testLargeRandomList() {
        int size = 1_000_000;

        Random random = new Random(seed);
        Comparator<Integer> cmp = Integer::compare;

        List<Integer> old = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            old.add(random.nextInt(10000));
        }
        List<Integer> expected = new ArrayList<>(old);
        Shell.sort(expected, cmp);

        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {
            List<Integer> list = new ArrayList<>(old);
            QueueingShell.sort(list, cmp, threadNumb, 0);

            assertEquals(
                "Список не відсортовано: seeed="+seed+" size="+size, expected, list
            );
        }
    }

    @Test
    public void testRandomArrays() {
        for (int threadNumb = 2; threadNumb <= maxThreads; threadNumb++) {

            Random random = new Random(seed);
            Comparator<Integer> cmp = Integer::compare;

            for (int i = 0; i < 1_000; i++) {

                ArrayList<Integer> list = new ArrayList<>(i);

                for (int j = 0; j < i; j++) 
                    list.add(random.nextInt(1000));

                ArrayList<Integer> old = new ArrayList<>(list);
                List<Integer> expected = list.stream().sorted(cmp).toList();
                QueueingShell.sort(list, cmp, threadNumb);

                assertEquals(old.toString(), expected, list);
            }
        }
    }
}