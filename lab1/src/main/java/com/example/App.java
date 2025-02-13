package com.example;

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        Model model = new Model();
        model.doTask5();


        BounceFrame frame = new BounceFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        System.out.println("Thread name = " + Thread.currentThread().getName());
    }
}
