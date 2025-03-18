package com.example.sort;

import java.util.Comparator;
import java.util.List;

public class Shell {

    public static <T> void sort(
        List<T> list, 
        Comparator<T> cmp
    ) {
        for (int h = knuthGap(list.size()); h > 0; h /= 3) {

            for (int i = h; i < list.size(); i++) {
                T temp = list.get(i);
                int j = i;   
                for (;j >= h && cmp.compare(list.get(j - h), temp) > 0; j -= h)
                    list.set(j, list.get(j - h));
                list.set(j, temp);
            }
        }
    }

    private static int knuthGap(int size) {
        int h = 1;

        while (h <= size/9) 
            h = 3*h + 1;
        
        return h;
    }

}
