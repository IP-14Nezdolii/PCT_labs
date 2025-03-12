package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.example.sort.ParallelShellSort;

public class ParallelShellSortTest {
    private long seed = 0;

    @Test
    public void testCustomList() {
        List<Integer> list = new ArrayList<>(Arrays.asList(5, 2, 9, 1, 5, 6));
        Comparator<Integer> comp = Integer::compare;
        
        ParallelShellSort.sort(list, comp, 4);
        
        List<Integer> expected = Arrays.asList(1, 2, 5, 5, 6, 9);
        assertEquals("Список повинен бути відсортований за зростанням", expected, list);
    }

    // Тест для вже відсортованого списку
    @Test
    public void testSortedList() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Comparator<Integer> comp = Integer::compare;
        
        ParallelShellSort.sort(list, comp, 4);
        
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals("Відсортований список повинен залишитися незмінним", expected, list);
    }

    // Тест для списку, відсортованого у зворотному порядку
    @Test
    public void testReverseSortedList() {
        List<Integer> list = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        Comparator<Integer> comp = Integer::compare;
        
        ParallelShellSort.sort(list, comp, 4);
        
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals("Зворотно відсортований список повинен бути перевернутий", expected, list);
    }

    // Тест для порожнього списку
    @Test
    public void testEmptyList() {
        List<Integer> list = new ArrayList<>();
        Comparator<Integer> comp = Integer::compare;
        
        ParallelShellSort.sort(list, comp, 4);
        
        assertTrue("Порожній список повинен залишитися порожнім", list.isEmpty());
    }

    // Тест для списку з одним елементом
    @Test
    public void testSingleElementList() {
        List<Integer> list = new ArrayList<>(Arrays.asList(42));
        Comparator<Integer> comp = Integer::compare;
        
        ParallelShellSort.sort(list, comp, 4);
        
        List<Integer> expected = Arrays.asList(42);
        assertEquals("Список з одним елементом повинен залишитися незмінним", expected, list);
    }

    @Test
    public void testTwoElementList() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(42, 36));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(36, 42));
        List<Integer> expected = Arrays.asList(36, 42);

        Comparator<Integer> comp = Integer::compare;
        
        ParallelShellSort.sort(list1, comp, 4);
        ParallelShellSort.sort(list2, comp, 4);
        
        assertEquals("Список з одним елементом повинен залишитися незмінним", expected, list1);
        assertEquals("Список з одним елементом повинен залишитися незмінним", expected, list2);
    }

    // Тест для списку з дублікатами
    @Test
    public void testListWithDuplicates() {
        List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3));
        Comparator<Integer> comp = Integer::compare;
        
        ParallelShellSort.sort(list, comp, 4);
        
        List<Integer> expected = Arrays.asList(1, 1, 2, 3, 3, 4, 5, 5, 6, 9);
        assertEquals("Список з дублікатами повинен бути відсортований", expected, list);
    }

    // Тест для великого випадкового списку
    @Test
    public void testLargeRandomList() {
        Random random = new Random(seed);
        List<Integer> list = new ArrayList<>();
        int size = 10000;
        
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt(10000));
        }
        
        Comparator<Integer> comp = Integer::compare;
        ParallelShellSort.sort(list, comp, 4);
        
        assertTrue("Великий випадковий список повинен бути відсортований", isSorted(list, comp));
    }

    @Test
    public void testRandomArrays() {
        Random random = new Random(seed);
        
        Comparator<Integer> comp = Integer::compare;

        int arrayNumb = 1000;
        for (int i = 0; i < arrayNumb; i++) {
            System.out.println(i);

            int length = random.nextInt(300) + 1;
            ArrayList<Integer> list = new ArrayList<>(length);

            for (int j = 0; j < length; j++) 
                list.add(random.nextInt(100));

            ArrayList<Integer> old = new ArrayList<>(list);
            ArrayList<Integer> expected = new ArrayList<>(list);
            expected.sort(comp);

            ParallelShellSort.sort(list, comp, 4);

            assertEquals(old.toString(), expected, list);
        }
    }

    // Допоміжний метод для перевірки сортування
    public <T> boolean isSorted(List<T> list, Comparator<T> comp) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (comp.compare(list.get(i), list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }
}