package com.example;

import com.example.test.Test;

public class App 
{
    public static void main( String[] args )
    {
        Test.run(0, 10);
    }

}

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Comparator;
// import java.util.List;

// List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 0, 33, 5, 22));
//         Comparator<Integer> cmp = Integer::compare;

//         sort(new ArrayList<>(list), cmp);
//         System.out.println("---------------------------------------");
//         sortModified(new ArrayList<>(list), cmp);
//         System.out.println();



// public static <T> void sort(
//     List<T> list, 
//     Comparator<T> cmp
// ) {
//     for (int h = knuthGap(list.size()); h > 0; h /= 3) { 

//         // сортування вставками кожного підмасиву
//         for (int i = h; i < list.size(); i++) {
//             T temp = list.get(i);
//             int j = i;   
//             for (;j >= h && cmp.compare(list.get(j - h), temp) > 0; j -= h)
//                 list.set(j, list.get(j - h));
//             list.set(j, temp);
//         }
//         System.out.println(list); // результат ітерації
//     }
// }

// public static <T> void sortModified(
//     List<T> list, 
//     Comparator<T> cmp
// ) {
//     for (int h = knuthGap(list.size()); h > 0; h /= 3) {
//         for (int g = h; g < h + h && g < list.size(); g++) {

//             // окреме сортування вставками для кожного підмасиву
//             for (int i = g; i < list.size(); i+=h) {
//                 T temp = list.get(i);
//                 int j = i;
//                 for (;j >= h && cmp.compare(list.get(j - h), temp) > 0; j -= h)
//                     list.set(j, list.get(j - h));
//                 list.set(j, temp);
//             }
//         }
//         System.out.println(list); // результат ітерації
//     }
// }

// private static int knuthGap(int size) {
//     int h = 1;

//     double sz = Math.ceil(size/9.0);

//     while (h < sz) 
//         h = 3*h + 1;
    
//     return h;
// }