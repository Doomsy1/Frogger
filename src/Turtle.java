/*
 * Turtle.java
 * Ario Barin Ostovary
 * This class controls the turtle's drawing and logic.
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class Turtle extends MovingObject {
    private final boolean breathing;
    private double stroke = 0;
    private static final int NUM_FRAMES = 5;

    private static final int STROKE_SPEED = 25; // the number of game frames per stroke cycle (not breathing)
    private static final int BREATH_SPEED = 75; // Breathing cycle duration in frames

    private static BufferedImage[] turtleImages;

    // Load the turtle images
    static {
        turtleImages = new BufferedImage[NUM_FRAMES];
        for (int i = 0; i < NUM_FRAMES; i++) {
            turtleImages[i] = Util.loadImage("src/assets/Turtle/Turtle" + i + ".png");
        }
    }

    public Turtle(int x, int y, int width, int speed, boolean left, boolean breathing) {
        super(x, y, width, speed, left);
        this.breathing = breathing;

        // Start the strokes at a random point so they aren't all in sync
        stroke = Util.randomDouble(0, 1);
    }

    private void drawLineOfTurtles(Graphics g, int frame, int x, int y, int width) {
        BufferedImage img = turtleImages[frame];
        for (int i = 0; i < width / 50; i++) {
            Util.drawImage(g, img, x, y, 50, 50);
            x += 50;
        }
    }

    private int getFrame() {
        if (!breathing) {
            return (int) (stroke * (NUM_FRAMES - 2)); // Desmos
        }
        return (int) (stroke * (NUM_FRAMES + 3)) % NUM_FRAMES; // Desmos
    }

    public void draw(Graphics g) {
        int frame = getFrame();
        drawLineOfTurtles(g, frame, x, y, width);
    }

    public boolean isUnderwater() {
        // Turtle is underwater if the frame is 3 or 4
        return getFrame() >= 3;
    }

    @Override
    public void move() {
        // Update the stroke based on whether the turtle is breathing or not
        if (!breathing) {
            stroke = (stroke + 1.0 / STROKE_SPEED) % 1;
        } else {
            stroke = (stroke + 1.0 / BREATH_SPEED) % 1;
        }

        // Move
        super.move();
    }
}
