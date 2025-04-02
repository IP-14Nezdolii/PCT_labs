package com.example.task4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.utils.Document;
import com.example.utils.Folder;

public class FileSearcher {

    private static final int THRESHOLD = 100000;
    private static final int N_THEADS = 8;

    private static final Pattern WORD_PATTERN = Pattern.compile("\\p{L}+('\\p{L}+)*"); // ATAGATGCATAGCGCATAGCTAGATGTGCTAGC

    private Set<String> keyWords;

    private Set<String> wordsInLines(List<String> lines) {
        Set<String> words = new HashSet<>();

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

    public List<String> getFileNames(
        Folder folder, 
        List<String> keyWords
    ) {
        this.keyWords = new HashSet<>(keyWords);
        
        List<String> result = null;

        try (ForkJoinPool forkJoinPool = new ForkJoinPool(N_THEADS)) {
            result = forkJoinPool.invoke(new FolderSearchTask(folder));
        }

        return result;
    }

    private class DocumentSearchTask extends RecursiveTask<Set<String>> {
        private final List<String> lines;
        private Document document = null;
    
        DocumentSearchTask(List<String> documentLines) {
            super();
            this.lines = documentLines;
        }
        
        public void setDocument(Document document) {
            this.document = document;
        }

        public Document getDocument() {
            return document;
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

    private class FolderSearchTask extends RecursiveTask<List<String>> {
        private final Folder folder;

        public FolderSearchTask(Folder folder) {
            super();
            this.folder = folder;
        }

        @Override
        protected List<String> compute() {
            List<String> filenames = new ArrayList<>();
            
            List<RecursiveTask<List<String>>> folderTasks = new LinkedList<>();
            List<DocumentSearchTask> documentTasks = new LinkedList<>();
            
            for (Folder subFolder : folder.getSubFolders()) {
                FolderSearchTask task = new FolderSearchTask(subFolder);
                task.fork();
                folderTasks.add(task);
            }
            for (Document document : folder.getDocuments()) {
                DocumentSearchTask task = new DocumentSearchTask(document.getLines());
                task.setDocument(document);
                task.fork();

                documentTasks.add(task);
            }

            for (var task : folderTasks) {
                filenames.addAll(task.join());
            }

            for (var task : documentTasks) {
                var words = task.join();
                if (words.containsAll(keyWords)) {
                    filenames.add(task.getDocument().getFilename());
                }
                
            }

            return filenames;
        }
    }
}
