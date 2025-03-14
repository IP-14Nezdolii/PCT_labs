package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.example.test.Test;

public class App 
{
    public static void main( String[] args )
    {
        List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3 , 10, 0));
        Comparator<Integer> cmp = Integer::compare;

        sort(new ArrayList<>(list), cmp);
        System.out.println("---------------------------------------");
        sortModified(new ArrayList<>(list), cmp);
        System.out.println();


        Test.run(0, 10);
    }

    public static <T> void sort(
        List<T> list, 
        Comparator<T> cmp
    ) {
        for (int gap = list.size()/2; gap > 0; gap /= 2) {
            for (int i = gap; i < list.size(); i++) {
                T temp = list.get(i);
                    int j = i;
                    while (j >= gap && 
                            cmp.compare(list.get(j - gap), temp) > 0
                    ){
                        list.set(j, list.get(j - gap));
                        j -= gap;
                    }
                    list.set(j, temp);
            }
            System.out.println(list);
        }
    }

    public static <T> void sortModified(
    List<T> list, 
        Comparator<T> cmp
    ) {
        for (int gap = list.size()/2; gap > 0; gap /= 2) {
            for (int g = gap; g < gap + gap && g < list.size(); g++) {
                for (int i = g; i < list.size(); i+=gap) {
                    T temp = list.get(i);
                    int j = i;
                    while (j >= gap && 
                            cmp.compare(list.get(j - gap), temp) > 0
                    ){
                        list.set(j, list.get(j - gap));
                        j -= gap;
                    }
                    list.set(j, temp);
                }
            }
            System.out.println(list);
        }
    }
}
