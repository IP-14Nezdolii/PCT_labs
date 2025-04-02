package com.example.utils;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class StatisticsCalculator {

    public static void calcAndOutputAll(Map<Integer, Integer> numbers) {
        double mean = calculateMean(numbers);
        double std = calculateSTD(numbers, mean);

        System.out.println("mean: " + mean);
        System.out.println("std: " + std);
        printFrequencyHist(numbers, 10);
        System.out.println();
    }


    public static double calculateMean(Map<Integer, Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0.0;
        }
        
        int del = 0;
        double sum = 0.0;
        for (var entry : numbers.entrySet()) {
            sum += entry.getKey() * entry.getValue();
            del += entry.getValue();
        }
        return sum / del;
    }
    
    public static double calculateSTD(
        Map<Integer, Integer> numbers, 
        double mean
    ) {
        if (numbers == null || numbers.isEmpty()){
            return 0.0;
        } 

        int n = 0;
        double sumSquaredDiff = 0.0;
        for (var entry : numbers.entrySet()) {

            double x = entry.getKey();
            double diff = x - mean;
            double squaredDiff = diff * diff;
            sumSquaredDiff += squaredDiff * entry.getValue();

            n += entry.getValue();
        }

        return Math.sqrt(sumSquaredDiff / (n-1));
    }
    
    public static void printFrequencyHist(Map<Integer, Integer> numbers, int nBins) {
        if (numbers == null || numbers.isEmpty()) {
            return;
        }
    
        int min = Collections.min(numbers.keySet());
        int max = Collections.max(numbers.keySet());
    
        int binWidth = (max - min) / nBins;
        if (binWidth == 0) binWidth = 1;
    
        Map<Integer, Integer> frequencyMap = new TreeMap<>();
    
        int maxIndex = Integer.MIN_VALUE;
        for (Map.Entry<Integer, Integer> entry : numbers.entrySet()) {
            int num = entry.getKey();
            int count = entry.getValue();
            int binIndex = Math.min((int) ((num - min) / binWidth), nBins - 1);
            frequencyMap.put(binIndex, frequencyMap.getOrDefault(binIndex, 0) + count);

            maxIndex = Math.max(maxIndex, binIndex);
        }
    
        System.out.println("\nHist:");
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            int binIndex = entry.getKey();
            int frequency = entry.getValue();
            int lowerBound = min + (binIndex * binWidth);
            int upperBound = lowerBound + binWidth;

            if(binIndex == maxIndex) {
                upperBound = max;
            }
    
            System.out.printf("[%.2f - %.2f): %d%n",
                (float) lowerBound, (float) upperBound, frequency);
        }
    }
}
