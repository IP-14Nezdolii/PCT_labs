package com.example.task1;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.example.utils.Folder;
import com.example.utils.PdfToTxt;

public class Task1 {
    static public void doTask() {

        PdfToTxt.convert(
            "lab4\\src\\main\\java\\com\\example\\task1\\Algorithms 4th Ed.pdf", 
            "lab4\\src\\main\\java\\com\\example\\task1\\testFolder2\\Algorithms 4th Ed.txt", 
            false
        );

        try {
            WordCounter wordCounter = new WordCounter();
            Folder folder = Folder.fromDirectory(
                new File(
                    "lab4\\src\\main\\java\\com\\example\\task1\\testFolder2"
            ));


            double start;
            double end;

            start = System.currentTimeMillis();
            List<Integer> result1 = wordCounter.getWordsLengthsOnSingleThread(folder);
            end = System.currentTimeMillis();
            System.out.println((end - start)/1000.0);

            start = System.currentTimeMillis();
            List<Integer> result2 = wordCounter.getWordsLengthsInParallel(folder);
            end = System.currentTimeMillis();
            System.out.println((end - start)/1000.0);


            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
