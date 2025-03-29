package com.example.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfToTxt {

    static public void convert(
        String pdfFilePath, 
        String txtFilePath, 
        boolean replace
    ) {
        try {
            File txtfFile = new File(txtFilePath);
            if (txtfFile.exists() && replace == false) {
                return;
            }

            File pdfFile = new File(pdfFilePath);
            PDDocument document = Loader.loadPDF(pdfFile);

            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);

            document.close();

            try (FileWriter writer = new FileWriter(txtfFile)) {
                writer.write(text);
                System.out.println("Текст успішно збережено у " + txtFilePath);
            }

        } catch (IOException e) {
            System.err.println("Помилка при обробці PDF-файлу: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
