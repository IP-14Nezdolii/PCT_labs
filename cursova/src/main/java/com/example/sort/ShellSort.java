package com.example.sort;

import java.util.Comparator;
import java.util.List;

public class ShellSort {

    public static <T> void sort(
        List<T> list, 
        Comparator<T> comp
    ) {
        for (int gap = getStartGap(list.size()); gap > 0; gap /= 2) {
            for (int g = gap; g < gap + gap && g < list.size(); g++) {
                for (int i = g; i < list.size(); i+=gap) {
                    T temp = list.get(i);
                    int j = i;
                    while (j >= gap && 
                        comp.compare(list.get(j - gap), temp) > 0
                    ){
                        list.set(j, list.get(j - gap));
                        j -= gap;
                    }
                    list.set(j, temp);
                }
            }
        }
    }

    protected static int getStartGap(int len) {
        int gap = 0;

        for (int i = 0; gap <= len; i++) 
            gap = (1 << i) - 1;

        return gap / 2;
    }
}
