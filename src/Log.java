/*
 * Log.java
 * Ario Barin Ostovary
 * This class controls the log's drawing
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class Log extends MovingObject {
    private static final BufferedImage leftImage, middleImage, rightImage;

    // Load the log images
    static {
        leftImage = Util.loadImage("src/assets/Log/Left.png");
        middleImage = Util.loadImage("src/assets/Log/Middle.png");
        rightImage = Util.loadImage("src/assets/Log/Right.png");
    }

    public Log(int x, int y, int width, int speed, boolean left) {
        super(x, y, width, speed, left);
    }

    public void draw(Graphics g) {
        // Draw the left image
        Util.drawImage(g, leftImage, x, y, 50, HEIGHT);

        // Draw the middle images - extend the log
        for (int i = 0; i < (width - 100) / 50; i++) {
            Util.drawImage(g, middleImage, x + 50 * i + 50, y, 50, HEIGHT);
        }

        // Draw the right image
        Util.drawImage(g, rightImage, x + width - 50, y, 50, HEIGHT);
    }
}
