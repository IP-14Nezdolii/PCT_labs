package com.example;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.example.elems.Ball;
import com.example.elems.Pocket;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;

@SuppressWarnings("unused")
public class BounceFrame extends JFrame {
    private Controller controller;

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce programm");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var canvas = new Canvas(new Model(), new BallLabel());
        controller = new Controller(canvas);

        Container content = this.getContentPane();
        content.add(canvas, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBackground(Color.lightGray);
        panel.setPreferredSize(new Dimension(WIDTH, 100));

        JButton buttonAddBall = new JButton("Add ball");
        JButton buttonAddRedBall = new JButton("Add red ball (high priority)");
        JButton buttonAddBalls = new JButton("Add balls");
        JButton buttonAddPocket = new JButton("Add pocket");
        JButton buttonClearBalls = new JButton("Clear balls");
        JButton buttonClearPocket = new JButton("Clear pocket");
        
        JButton buttonDoTask32 = new JButton("Do Task 3.2.1");
        JButton buttonDoTask322 = new JButton("Do Task 3.2.2");
        JButton buttonDoTask323 = new JButton("Do Task 3.2.3(low priority)");

        JButton buttonDoTask4 = new JButton("Do Task 4");

        buttonAddBall.addActionListener((e) -> {
            controller.addBall(new Ball(canvas));    
        });
        buttonAddBalls.addActionListener((e) -> {
            for (int i = 0; i < 50; i++) 
                controller.addBall(new Ball(canvas));
        });
        buttonAddPocket.addActionListener((e) -> {
            controller.addPocket(new Pocket(canvas));
        });
        buttonClearBalls.addActionListener((e) -> {
            controller.clearBalls();
        });
        buttonClearPocket.addActionListener((e) -> {
            controller.clearPockets();
        });
        buttonAddRedBall.addActionListener((e) -> {
            controller.addBall(new Ball(canvas, Color.RED, Thread.MAX_PRIORITY));    
        });
        buttonDoTask32.addActionListener((e) -> {
            for (int i = 0; i < 50; i++)
                controller.addBall(new Ball(canvas, Color.BLUE, Thread.MIN_PRIORITY, 100, 100));
            controller.addBall(new Ball(canvas, Color.RED, Thread.MAX_PRIORITY, 100, 100));
        });
        buttonDoTask322.addActionListener((e) -> {
            for (int i = 0; i < 150; i++)
                controller.addBall(new Ball(canvas, Color.BLUE, Thread.MIN_PRIORITY, 100, 100));
            controller.addBall(new Ball(canvas, Color.RED, Thread.MAX_PRIORITY, 100, 100));
        });
        buttonDoTask323.addActionListener((e) -> {
            for (int i = 0; i < 150; i++)
                controller.addBall(new Ball(canvas, Color.BLUE, Thread.MAX_PRIORITY, 100, 100));
            controller.addBall(new Ball(canvas, Color.RED, Thread.MIN_PRIORITY, 100, 100));
        });
        buttonDoTask4.addActionListener((e) -> {
            controller.doTask4(
                new Ball(canvas, Color.BLUE, 5), 
                new Ball(canvas, Color.GREEN, 5)
            );
        });

        panel.add(buttonAddBall);
        panel.add(buttonAddRedBall);
        panel.add(buttonAddBalls);
        panel.add(buttonAddPocket);
        panel.add(buttonClearBalls);
        panel.add(buttonClearPocket);
        panel.add(buttonDoTask32);
        panel.add(buttonDoTask322);
        panel.add(buttonDoTask323);
        panel.add(buttonDoTask4);
        panel.add(canvas.getBallLabel());

        content.add(panel, BorderLayout.SOUTH);

        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());
    }
}
