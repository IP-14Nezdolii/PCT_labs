package com.example;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.example.sort.ShellSort;

public class ShellSortTest {
    private long seed = 0;

    @Test
    public void testSortedList() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Comparator<Integer> cmp = Integer::compare;
        
        ShellSort.sort(list, cmp);
        
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals(
            "Відсортований список повинен залишитися незмінним", expected, list
        );
    }

    @Test
    public void testReverseSortedList() {
        List<Integer> list = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        Comparator<Integer> cmp = Integer::compare;
        
        ShellSort.sort(list, cmp);
        
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals(
            "Зворотно відсортований список повинен бути перевернутий", expected, list
        );
    }

    @Test
    public void testEmptyList() {
        List<Integer> list = new ArrayList<>();
        Comparator<Integer> cmp = Integer::compare;
        
        ShellSort.sort(list, cmp);
        
        assertTrue(
            "Порожній список повинен залишитися порожнім", list.isEmpty()
        );
    }

    @Test
    public void testSingleElementList() {
        List<Integer> list = new ArrayList<>(Arrays.asList(42));
        Comparator<Integer> cmp = Integer::compare;
        
        ShellSort.sort(list, cmp);
        
        List<Integer> expected = Arrays.asList(42);
        assertEquals(
            "Список з одним елементом повинен залишитися незмінним", expected, list
        );
    }

    @Test
    public void testTwoElementList() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(42, 36));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(36, 42));
        
        Comparator<Integer> cmp = Integer::compare;
        
        ShellSort.sort(list1, cmp);
        ShellSort.sort(list2, cmp);
        
        List<Integer> expected = Arrays.asList(36, 42);

        assertEquals(
            "Список з одним елементом повинен залишитися незмінним", expected, list1
        );
        assertEquals(
            "Список з одним елементом повинен залишитися незмінним", expected, list2
        );
    }

    @Test
    public void testListWithDuplicates() {
        List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3));
        Comparator<Integer> cmp = Integer::compare;
        
        ShellSort.sort(list, cmp);
        
        List<Integer> expected = Arrays.asList(1, 1, 2, 3, 3, 4, 5, 5, 6, 9);
        assertEquals(
            "Список з дублікатами повинен бути відсортований", expected, list
        );
    }

    @Test
    public void testLargeRandomList() {
        Random random = new Random(seed);
        List<Integer> list = new ArrayList<>();
        int size = 10000;
        
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt(10000));
        }
        Comparator<Integer> cmp = Integer::compare;

        List<Integer> expected = list.stream().sorted(cmp).toList();
        ShellSort.sort(list, cmp);
        
        assertEquals(
            "Список з дублікатами повинен бути відсортований", expected, list
        );
    }

    @Test
    public void testRandomArrays() {
        Random random = new Random(seed);
        
        Comparator<Integer> cmp = Integer::compare;

        for (int i = 0; i < 10000; i++) {

            ArrayList<Integer> list = new ArrayList<>(random.nextInt(300) + 1);

            for (int j = 0; j < list.size(); j++) 
                list.add(random.nextInt(100));

            List<Integer> expected = list.stream().sorted(cmp).toList();
            ShellSort.sort(list, cmp);

            assertEquals(
                "Список з дублікатами повинен бути відсортований", expected, list
            );
        }
    }
}
