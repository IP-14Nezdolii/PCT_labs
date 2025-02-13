package com.example.elems;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public abstract class CanvasElem {
    private Component canvas;

    private int XSIZE;
    private int YSIZE;

    private int x;
    private int y;

    private Color color;

    CanvasElem(
        Component canvas, 
        Color color, 
        int x, 
        int y, 
        int xSize, 
        int ySize
    ) {
        this.canvas = canvas;
        this.color = color;
        this.x = x;
        this.y = y;
        this.XSIZE = xSize;
        this.YSIZE = ySize;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getXSize() {
        return XSIZE;
    }

    public int getYSize() {
        return YSIZE;
    }

    public Component getCanvas() {
        return canvas;
    }
}
