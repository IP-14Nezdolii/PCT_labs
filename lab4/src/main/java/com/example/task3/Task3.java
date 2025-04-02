package com.example.task3;

import java.io.File;
import java.util.Set;

public class Task3 {
    static private WordCounter wordCounter = new WordCounter();

    static public void doTask() {
        Set<String> words = wordCounter.getCommonWords(
            new File("lab4\\src\\main\\java\\com\\example\\task3\\testFolder\\file1.txt"), 
            new File("lab4\\src\\main\\java\\com\\example\\task3\\testFolder\\file2.txt")
        );

        System.out.println("Common words: " + words);
    }
}
