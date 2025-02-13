package com.example;

import com.example.elems.Ball;
import com.example.elems.Pocket;

public class Controller {
    private final Model model;
    private final Canvas canvas;

    public Controller(Canvas canvas) {
        this.model = canvas.getModel();
        this.canvas = canvas;
    }

    public void clearBalls() {
        model.clearBalls();
        canvas.repaint();
    }

    public void clearPockets() {
        model.clearPocket();
        canvas.repaint();
    }

    public void addBall(Ball ball) {
        model.add(ball);
    }

    public void addBall(Ball ball, long sleepTime) {
        model.add(ball, sleepTime);
    }

    public void addPocket(Pocket pocket) {
        model.add(pocket);
    }

    public void doTask4(Ball ball1, Ball ball2) {
        model.doTask4(ball1, ball2);
    }
}
