package com.example.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Document {
    private final List<String> lines;
    private final String filename;

    Document(List<String> lines, String filename) {
        this.lines = lines;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public List<String> getLines() {
        return this.lines;
    }

    public static Document fromFile(File file) throws IOException {
        List<String> lines = new LinkedList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        }
        return new Document(lines, file.getAbsolutePath());
    }
}
