package com.example.elems;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

public class Pocket extends CanvasElem {

    public Pocket(Component canvas) {
        super(canvas, Color.BLACK, 0, 0, 100, 100);
        
        setX(new Random().nextInt(getCanvas().getWidth()));
        setY(new Random().nextInt(getCanvas().getHeight()));

        getCanvas().repaint();
    }

    public boolean contains(Ball ball) {
        return ball.getX() >= getX() && ball.getX() <= getX() + getXSize() &&
               ball.getY() >= getY() && ball.getY() <= getY() + getYSize();
    }
}
