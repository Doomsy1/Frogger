/*
 * Goal.java
 * Ario Barin Ostovary
 * This class controls the goal's drawing and logic.
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class Goal {
    private final int x, y;
    private boolean filled;
    private boolean hasFly;
    private boolean danger;
    private static final int DANGER_SPEED = 120; // time in frames to spawn then disappear
    private int dangerTimer;

    private static final int WIDTH = 50, HEIGHT = 50;

    // Danger images - alligator
    private static final BufferedImage[] DANGER_IMAGES = new BufferedImage[2];
    static {
        DANGER_IMAGES[0] = Util.loadImage("src/assets/Goal/Danger0.png");
        DANGER_IMAGES[1] = Util.loadImage("src/assets/Goal/Danger1.png");
    }

    // Images
    private static final BufferedImage FLY_IMAGE = Util.loadImage("src/assets/Goal/Fly.png");
    private static final BufferedImage FILLED_IMAGE = Util.loadImage("src/assets/Goal/Filled.png");
    private static final BufferedImage GOAL_IMAGE = Util.loadImage("src/assets/Goal/Goal.png");

    public Goal(int x, int y) {
        filled = false;
        this.x = x;
        this.y = y;
    }

    public Rectangle getRect() {
        // Get the rectangle of the goal
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void update() {
        // If the goal is filled, don't update
        if (isFilled()) {
            return;
        }

        // 1% chance to spawn a fly
        if (Util.randomDouble(0, 1) < 0.001) {
            hasFly = !hasFly;
        }

        // 1% chance to spawn a danger
        if (Util.randomDouble(0, 1) < 0.001 && !danger) {
            danger = true;
            dangerTimer = 0;
        }

        // Update danger timer
        if (danger) {
            dangerTimer++;
            if (dangerTimer >= DANGER_SPEED) {
                danger = false;
            }
        }
    }

    // If the goal has a fly, remove it
    public boolean fly() {
        if (!hasFly) {
            return false;
        }
        hasFly = false;
        return true;
    }

    private int getDangerFrame() {
        int frame = 1 - (int) Math.abs(dangerTimer - (DANGER_SPEED / 2)) / (DANGER_SPEED / 4);
        return frame;
    }

    public boolean isDanger() {
        // If the danger frame is 1, the danger is active
        return getDangerFrame() == 1;
    }

    public boolean fill() {
        // If the goal is already filled, don't fill it
        if (filled) {
            return false;
        }
        filled = true;
        return true;
    }

    public boolean isFilled() {
        // Return if the goal is filled
        return filled;
    }

    public boolean hasFly() {
        // Return if the goal has a fly
        return hasFly;
    }

    public void draw(Graphics g) {
        // Draw the danger image if the goal is dangerous
        if (danger) {
            Util.drawImage(g, DANGER_IMAGES[getDangerFrame()], x, y, WIDTH, HEIGHT);
        } else if (hasFly) {
            // Draw the fly image if the goal has a fly
            Util.drawImage(g, FLY_IMAGE, x, y, WIDTH, HEIGHT);
        } else if (filled) {
            Util.drawImage(g, FILLED_IMAGE, x, y, WIDTH, HEIGHT);
        }
        
        // Draw the goal image
        Util.drawImage(g, GOAL_IMAGE, x - 50, y - 50, WIDTH + 100, HEIGHT + 50);
    }
}
