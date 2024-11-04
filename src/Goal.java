/*
 * Goal.java
 * Ario Barin Ostovary
 * This class controls the goal's drawing
 */

import java.awt.*;
import java.util.Random;

public class Goal {
    private final int x, y;
    private boolean filled;
    private boolean hasFly;
    private static final Random rand = new Random();

    private static final int WIDTH = 50, HEIGHT = 50;

    public Goal(int x, int y) {
        filled = false;
        this.x = x;
        this.y = y;
        hasFly = rand.nextDouble() < 0.25;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean fill() {
        if (filled) {
            return false;
        }
        filled = true;
        return true;
    }

    public boolean isFilled() {
        return filled;
    }

    public boolean hasFly() {
        return hasFly;
    }

    public void draw(Graphics g) {
        if (isFilled()) {
            g.setColor(Color.YELLOW);
        } else {
            if (hasFly()) {
                g.setColor(Color.GRAY);
            } else {
                g.setColor(Color.WHITE);
            }
        }
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
}
