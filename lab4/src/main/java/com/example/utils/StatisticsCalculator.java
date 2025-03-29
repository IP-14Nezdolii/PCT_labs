package com.example.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatisticsCalculator {

    public static double calculateMean(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (double num : numbers) {
            sum += num;
        }
        return sum / numbers.size();
    }
    
    public static double calculateSTD(List<Integer> numbers, double mean) {
        if (numbers == null || numbers.isEmpty()) return 0.0;
        
        double sumSquaredDiff = 0.0;
        for (double num : numbers) {
            double diff = num - mean;
            sumSquaredDiff += diff * diff;
        }
        return Math.sqrt(sumSquaredDiff / numbers.size());
    }
    
    public static void printFrequencyHistogram(List<Integer> numbers, int nBeans) {
        if (numbers == null || numbers.isEmpty()){
            return;
        } 
        
        int min = Collections.min(numbers);
        int max = Collections.max(numbers);
        
        int binWidth = (max - min) / nBeans;
        
        Map<Integer, Integer> frequencyMap = new TreeMap<>();
        
        for (double num : numbers) {
            int binIndex = Math.min((int)((num - min) / binWidth), nBeans - 1);
            frequencyMap.put(binIndex, frequencyMap.getOrDefault(binIndex, 0) + 1);
        }
        
        System.out.println("\nHist:");
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            int binIndex = entry.getKey();
            int frequency = entry.getValue();
            int lowerBound = min + (binIndex * binWidth);
            int upperBound = lowerBound + binWidth;
            
            System.out.printf("[%.2f - %.2f): %d %s%n", 
                lowerBound, upperBound, frequency, "*".repeat(frequency));
        }
    }
}
