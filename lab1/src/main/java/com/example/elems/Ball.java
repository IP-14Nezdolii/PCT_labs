package com.example.elems;

import java.awt.Component;
import java.awt.Color;
import java.util.Random;

public class Ball extends CanvasElem {

    private int dx = 2;
    private int dy = 2;

    private boolean isFallen = false;
    private int priority = Thread.NORM_PRIORITY;

    public Ball(Component canvas) {
        super(canvas, Color.BLUE, 0, 0, 20, 20);

        if (Math.random() < 0.5) 
            setX(new Random().nextInt(getCanvas().getWidth()));
        else 
            setY(new Random().nextInt(getCanvas().getHeight()));
    }

    public Ball(Component canvas, Color color, int priority) {
        super(canvas, color, 0, 0, 20, 20);
        this.priority = priority;

        if (Math.random() < 0.5) 
            setX(new Random().nextInt(getCanvas().getWidth()));
        else 
            setY(new Random().nextInt(getCanvas().getHeight()));
    }

    public Ball(Component canvas, Color color, int priority, int x, int y) {
        super(canvas, color, 0, 0, 20, 20);
        this.priority = priority;

        setX(x);
        setY(y);            
    }

    public void move() {

        int x = getX();
        int y = getY();

        x += dx;
        y += dy;
        if (x < 0) {
            x = 0;
            dx = -dx;
        }
        if (x + getXSize() >= getCanvas().getWidth()) {
            x = getCanvas().getWidth() - getXSize();
            dx = -dx;
        }
        if (y < 0) {
            y = 0;
            dy = -dy;
        }
        if (y + getYSize() >= getCanvas().getHeight()) {
            y = getCanvas().getHeight() - getYSize();

            dy = -dy;
        }

        setX(x);
        setY(y);
        getCanvas().repaint();
    }

    public synchronized boolean isFallen() {
        return isFallen;
    }

    public synchronized void setFallen(boolean onDelete) {
        this.isFallen = onDelete;
    }

    public int getPriority() {
        return priority;
    }
}
