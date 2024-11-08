/*
 * Alligator.java
 * Ario Barin Ostovary
 * This class controls the alligator's drawing, movement, and logic
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class Alligator extends MovingObject {
    private double mouth = 0.0; // opening / closing mouth (0 - 0.5 is closed, 0.5 - 1 is open)
    private static final int MOUTH_CYCLE = 100; // number of frames in a mouth cycle

    // Images
    private static final BufferedImage[] alligatorImages;

    static {
        alligatorImages = new BufferedImage[2]; // open (1) or closed (0)
        for (int i = 0; i < 2; i++) {
            alligatorImages[i] = Util.loadImage("src/assets/Alligator/" + i + ".png");
        }
    }

    public Alligator(int x, int y, int width, int speed, boolean left) {
        super(x, y, width, speed, left);
    }

    public boolean mouthIsOpen() {
        return mouth >= 0.5; // Mouth open for 50% of the cycle
    }

    public Rectangle getMouthRect() {
        if (left) {
            return new Rectangle(x, y, 50, HEIGHT);
        } else {
            return new Rectangle(x + width - 50, y, 50, HEIGHT);
        }
    }

    @Override
    public void move() {
        super.move();
        mouth = (mouth + 1.0 / MOUTH_CYCLE) % 1.0; // Update mouth position
    }

    public void draw(Graphics g) {
        // Draw the body
        Util.drawImage(g, alligatorImages[mouthIsOpen() ? 1 : 0], x, y, width, HEIGHT);
    }
}
