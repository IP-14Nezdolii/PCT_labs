package com.example.task1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.utils.Document;
import com.example.utils.Folder;

public class WordCounter {
    private ForkJoinPool forkJoinPool;

    private static final int THRESHOLD = 100000;
    private static final int N_THEADS = 8;

    private static final Pattern WORD_PATTERN = 
        Pattern.compile("\\p{L}+('\\p{L}+)*", Pattern.CASE_INSENSITIVE);
    
    // String[] wordsIn(String line) {
    //     return line.trim().split("(\\s|\\p{Punct})+");
    // }

    private List<Integer> wordsLengthsInLines(List<String> lines) {
        List<Integer> lengths = new ArrayList<>(lines.size() * 15);
        for (String line : lines) {

            if (line == null || line.isEmpty()) {
                continue;
            }
            
            Matcher matcher = WORD_PATTERN.matcher(line);       
            while (matcher.find()) {
                lengths.add(matcher.group().length());
            };
        }
        return lengths;
    }

    public List<Integer> getWordsLengthsInParallel(Folder folder) {   
        List<Integer> result = new ArrayList<>();
        
        try {
            forkJoinPool = new ForkJoinPool(N_THEADS);
            result = forkJoinPool.invoke(new FolderSearchTask(folder));  
            
            forkJoinPool.shutdown();
            if (!forkJoinPool.awaitTermination(5, TimeUnit.MILLISECONDS)) {
                forkJoinPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 

        return result;
    }

    public List<Integer> getWordsLengthsOnSingleThread(Folder folder) {
        List<Integer> result = new ArrayList<>();
        for (Folder subFolder : folder.getSubFolders()) {
            result.addAll(getWordsLengthsOnSingleThread(subFolder));
        }
        for (Document document : folder.getDocuments()) {
            result.addAll(wordsLengthsInLines(document.getLines()));
        }
        return result;
    }

    private class DocumentSearchTask extends RecursiveTask<List<Integer>> {
        private final List<String> lines;
    
        DocumentSearchTask(List<String> documentLines) {
            super();
            this.lines = documentLines;
        }

        @Override
        protected List<Integer> compute() {
            List<Integer> result;

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
                result = wordsLengthsInLines(lines);
            }

            return result;
        }
    }

    private class FolderSearchTask extends RecursiveTask<List<Integer>> {
        private final Folder folder;
      
        public FolderSearchTask(Folder folder) {
          super();
          this.folder = folder;
        }
      
        @Override
        protected List<Integer> compute() {
        List<Integer> result = new ArrayList<>();

          List<RecursiveTask<List<Integer>>> forks = new LinkedList<>();
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

          for (RecursiveTask<List<Integer>> task : forks) {
            result.addAll(task.join());
          }
          return result;
        }
    }
}




