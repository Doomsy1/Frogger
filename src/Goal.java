/*
 * Goal.java
 * Ario Barin Ostovary
 * This class controls the goal's drawing
 */

import java.awt.*;

public class Goal {
    private final int x, y;
    private boolean filled;

    private static final int WIDTH = 50, HEIGHT = 50;

    public Goal(int x, int y) {
        filled = false;
        this.x = x;
        this.y = y;
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


    public void draw(Graphics g) {
        g.setColor(filled ? Color.YELLOW : Color.GRAY);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
}
