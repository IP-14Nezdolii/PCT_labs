package com.example;

import javax.swing.JLabel;

public class BallLabel extends JLabel {

    public BallLabel() {
        draw(0);
    }

    public void draw(int count) {
        setText("Count balls: " + count);
    }
}
