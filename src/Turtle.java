/*
 * LilyPad.java
 * Ario Barin Ostovary
 * This class controls the turtle's drawing, movement, and underwater logic
 */

import java.awt.*;

public class Turtle extends MovingObject {
    private final boolean breathing;
    private double breath = 0;

    private static final int BREATH_SPEED = 120; // Breathing cycle duration in frames

    public Turtle(int x, int y, int width, int speed, boolean left, boolean breathing) {
        super(x, y, width, speed, left);
        this.breathing = breathing;
        breath = Util.randomDouble(0, 1);
    }

    public void draw(Graphics g) {
        if (isUnderwater()) {
            return;
        }
        g.setColor(new Color(144, 238, 144)); // Light green color
        g.fillRect(x, y, width, HEIGHT);
    }

    public boolean isUnderwater() {
        return breathing && breath < 0.5;
    }

    @Override
    public void move() {
        breath = (breath + 1.0 / BREATH_SPEED) % 1;
        if (left) {
            x -= speed;
        } else {
            x += speed;
        }

        if (notVisible()) {
            x = left ? 800 : -width;
        }
    }
}
