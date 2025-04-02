package com.example.task1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.utils.Document;
import com.example.utils.Folder;

public class WordCounter {

    private static final int THRESHOLD = 100000;
    private static final int N_THEADS = 8;
    private static final int INITIAL_CAPACITY = 20;

    private static final Pattern WORD_PATTERN = 
        Pattern.compile("\\p{L}+('\\p{L}+)*"); //ATAGATGCATAGCGCATAGCTAGATGTGCTAGC

    private Map<Integer, Integer> wordsLengthsInLines(List<String> lines) {
        Map<Integer, Integer> lengths = new HashMap<>(INITIAL_CAPACITY);

        for (String line : lines) {

            if (line == null || line.isEmpty()) {
                continue;
            }
            
            Matcher matcher = WORD_PATTERN.matcher(line);       
            while (matcher.find()) {
                
                lengths.merge(
                    matcher.group().length(), 
                    1, 
                    Integer::sum
                );
            };
        }
        return lengths;
    }

    public Map<Integer, Integer> getWordsLengthsInParallel(Folder folder) {   
        Map<Integer, Integer> result = new HashMap<>(INITIAL_CAPACITY);
        
        try (ForkJoinPool forkJoinPool = new ForkJoinPool(N_THEADS)) {
            result = forkJoinPool.invoke(new FolderSearchTask(folder));
        }

        return result;
    }

    public Map<Integer, Integer> getWordsLengthsOnSingleThread(Folder folder) {
        Map<Integer, Integer> result = new HashMap<>(INITIAL_CAPACITY);
        for (Folder subFolder : folder.getSubFolders()) {
            mergeMaps(result, getWordsLengthsOnSingleThread(subFolder));
        }
        for (Document document : folder.getDocuments()) {
            mergeMaps(result, wordsLengthsInLines(document.getLines()));
        }
        return result;
    }

    private class DocumentSearchTask extends RecursiveTask<Map<Integer, Integer>> {
        private final List<String> lines;
    
        DocumentSearchTask(List<String> documentLines) {
            super();
            this.lines = documentLines;
        }

        @Override
        protected Map<Integer, Integer> compute() {
            Map<Integer, Integer> result = null;

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
                mergeMaps(result, task2.join());
            } else {
                result = wordsLengthsInLines(lines);
            }

            return result;
        }
    }

    private class FolderSearchTask extends RecursiveTask<Map<Integer, Integer>> {
        private final Folder folder;
      
        public FolderSearchTask(Folder folder) {
          super();
          this.folder = folder;
        }
      
        @Override
        protected Map<Integer, Integer> compute() {
        Map<Integer, Integer> result = new HashMap<>(INITIAL_CAPACITY);

          List<RecursiveTask<Map<Integer, Integer>>> forks = new LinkedList<>();
          for (Folder subFolder : folder.getSubFolders()) {
            FolderSearchTask task = new FolderSearchTask(subFolder);
            forks.add(task);
            task.fork();
          }
          for (Document document : folder.getDocuments()) {
            DocumentSearchTask task = new DocumentSearchTask(document.getLines());
            forks.add(task);
            task.fork();
          }

          for (var task : forks) {
            mergeMaps(result, task.join());
          }
          return result;
        }
    }

    private void mergeMaps(
        Map<Integer, Integer> m1, 
        Map<Integer, Integer> m2
    ) {
        m2.forEach(
            (key, value) -> m1.merge(key, value, (v1, v2) -> v1 + v2)
        );
    }
}




