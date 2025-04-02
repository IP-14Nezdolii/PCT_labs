package com.example.task1;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.example.utils.Folder;
import com.example.utils.PdfToTxt;
import com.example.utils.StatisticsCalculator;

public class Task1 {
    private static final WordCounter wordCounter = new WordCounter();

    static public void doTask(int treshold, int retest) {

        PdfToTxt.convert(
            "lab4\\src\\main\\java\\com\\example\\task1\\Algorithms 4th Ed.pdf", 
            "lab4\\src\\main\\java\\com\\example\\task1\\testFolder2\\Algorithms 4th Ed.txt", 
            false
        );

        try {
            Folder folder = Folder.fromDirectory(
                new File(
                    "lab4\\src\\main\\java\\com\\example\\task1\\testFolder2"
            ));

            for (int i = 0; i < treshold; i++) {
                testMulti(folder);
                testSingle(folder);
            }
    
            if (retest > 0) {
                
                double singleT = 0;
                double multiT = 0;
                for (int i = 0; i < retest; i++) {
                    multiT += testMulti(folder);
                    singleT += testSingle(folder);
                }
                singleT/=retest;
                multiT/=retest;

                System.out.println("Time single thread: "+ singleT);
                System.out.println(
                    "Time 8 threads: "+ multiT + 
                    ", speedUp: " + singleT/multiT +
                    ", efficiency: " + singleT/multiT / 8
                );
                System.out.println();
            }
            
            Map<Integer, Integer> result = wordCounter.getWordsLengthsInParallel(folder);
            StatisticsCalculator.calcAndOutputAll(result);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static double testSingle(Folder folder) {
        double start;
        double end;

        start = System.currentTimeMillis();
        wordCounter.getWordsLengthsOnSingleThread(folder);
        end = System.currentTimeMillis();

        return (end - start)/1000.0;
    }

    static double testMulti(Folder folder) {
        double start;
        double end;

        start = System.currentTimeMillis();
        wordCounter.getWordsLengthsInParallel(folder);
        end = System.currentTimeMillis();

        return (end - start)/1000.0;
    }
}
