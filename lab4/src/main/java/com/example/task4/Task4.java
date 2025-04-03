package com.example.task4;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.example.utils.Folder;

public class Task4 {
    static private FileSearcher searcher = new FileSearcher();

    static public void doTask() {
        Folder folder;
        try {
            folder = Folder.fromDirectory(
                new File(
                    "lab4\\src\\main\\java\\com\\example"
            ));

            List<String> filenames = searcher.getFileNames(folder, List.of(
                "FileSearcher", "searcher"
            ));
            System.out.println("Key words in: " + filenames);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
