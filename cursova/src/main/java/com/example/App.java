package com.example;

import java.util.Collections;
import java.util.List;

import com.example.bench.Bencher;

public class App 
{
    public static void main( String[] args )
    {
        Bencher.bench(0);
        // System.out.printf("%-10s %-15s %-5s%n", "ID", "Имя", "Возраст");
        // System.out.println("--------------------------------");
        // System.out.printf("%-10d %-15s %-5d%n", 1, "Алексей", 25);
        // System.out.printf("%-10d %-15s %-5d%n", 2, "Мария", 30);
        // System.out.printf("%-10d %-15s %-5d%n", 3, "Иван", 22);

        // var list = List.of(1,2,3,4,5,6,7,8,9,10);

        // var elemGap = 3;

        // for (int g = elemGap; g < elemGap*2 && g < list.size(); g++) 
        //     System.out.println(g);
        // System.out.println();

        // var step = 10;

        // int from = elemGap;
        // int to = elemGap*2;

        // for (int g = from; g < to && g < list.size(); g+=step) 
        //     for (int k = g; k < g + step && k < elemGap*2; k++) 
        //         System.out.println(k);
        // System.out.println();
    }

    // static void shellSort(List<Integer> array) {
    //     for (int s = array.size() / 2; s > 0; s /= 2) {
    //         for (int i = s; i < array.size(); ++i) {
    //             for (int j = i - s; j >= 0 && array.get(j) > array.get(j + s); j -= s)  {
    //                 Collections.swap(array, j, j + s);
    //             }
    //         }
    //     }        
    // }
}
