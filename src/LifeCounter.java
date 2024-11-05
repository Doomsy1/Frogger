/*
 * LifeCounter.java
 * Ario Barin Ostovary
 * This class controls the life counter's drawing and logic
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class LifeCounter {
    private int lives = 3;
    private final int x, y;
    private final int lifeWidth = 40, lifeHeight = 40;

    // Load image
    private static final BufferedImage lifeImage;

    static {
        lifeImage = Util.loadImage("src/assets/Life/0.png");
    }

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
        for (int i = 0; i < lives; i++) {
            Util.drawImage(g, lifeImage, x + i * lifeWidth, y, lifeWidth, lifeHeight);
        }
    }
}
