package com.example;

import javax.swing.JPanel;

import com.example.elems.Ball;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Canvas extends JPanel {
    private Model model;
    private BallLabel ballLabel;

    Canvas(Model model, BallLabel counter) {
        this.model = model;
        this.ballLabel = counter;
    }

    public Model getModel() {
        return model;
    }

    public BallLabel getBallLabel() {
        return ballLabel;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (Ball ball : model.getBalls()) {
            if (!ball.isFallen())
                ball.draw(g2);
        }
        
        if (model.getPocket() != null)
            model.getPocket().draw(g2);

        ballLabel.draw(
            (int)model
                .getBalls()
                .stream()
                .filter(Ball::isFallen)
                .count()
        );
    }
}
