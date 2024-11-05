/*
 * LifeCounter.java
 * Ario Barin Ostovary
 * This class controls the life counter's drawing and logic
 */

import java.awt.*;

public class LifeCounter {
    private int lives = 3;
    private final int x, y;
    private final int lifeWidth = 60, lifeHeight = 60;

    public LifeCounter(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void reset() {
        lives = 3;
    }

    public void loseLife() {
        lives--;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i < lives; i++) {
            g.fillRect(x + i * (lifeWidth + 10), y, lifeWidth, lifeHeight);
        }
    }
}
