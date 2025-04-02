package com.example.task3;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.utils.Document;

public class WordCounter {

    private static final int THRESHOLD = 100000;
    private static final int N_THEADS = 8;
    private static final int INITIAL_CAPACITY = 20;

    private static final Pattern WORD_PATTERN = 
        Pattern.compile("\\p{L}+('\\p{L}+)*");

    private Set<String> wordsInLines(List<String> lines) {
        Set<String> words = new HashSet<>(INITIAL_CAPACITY);

        for (String line : lines) {

            if (line == null || line.isEmpty()) {
                continue;
            }
            
            Matcher matcher = WORD_PATTERN.matcher(line);       
            while (matcher.find()) {     
                words.add(matcher.group());
            }
        }
        return words;
    }

    public Set<String> getCommonWords(File file1, File file2) {   
        Set<String> result = null;
        
        try (ForkJoinPool pool = new ForkJoinPool(N_THEADS)) {
            Document a = Document.fromFile(file1);
            Document b = Document.fromFile(file2);

            DocumentSearchTask taskA = new DocumentSearchTask(a.getLines());
            DocumentSearchTask taskB = new DocumentSearchTask(b.getLines());

            pool.submit(taskA);
            pool.submit(taskB);

            result = taskA.join();
            result.retainAll(taskB.join());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private class DocumentSearchTask extends RecursiveTask<Set<String>> {
        private final List<String> lines;
    
        DocumentSearchTask(List<String> documentLines) {
            super();
            this.lines = documentLines;
        }

        @Override
        protected Set<String> compute() {
            Set<String> result = null;

            if (lines.size() > THRESHOLD) {
                int mid = lines.size() / 2;

                var task1 = new DocumentSearchTask(
                    lines.subList(0, mid)
                );
                var task2 = new DocumentSearchTask(
                    lines.subList(mid, lines.size())
                );

                task2.fork();
                result = task1.compute();  
                result.addAll(task2.join());
            } else {
                result = wordsInLines(lines);
            }

            return result;
        }
    }
}




