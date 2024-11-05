/*
 * Goal.java
 * Ario Barin Ostovary
 * This class controls the goal's drawing
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

    private static final BufferedImage[] DANGER_IMAGES = new BufferedImage[2];
    static {
        DANGER_IMAGES[0] = Util.loadImage("src/assets/Goal/Danger0.png");
        DANGER_IMAGES[1] = Util.loadImage("src/assets/Goal/Danger1.png");
    }

    private static final BufferedImage FLY_IMAGE = Util.loadImage("src/assets/Goal/Fly.png");

    private static final BufferedImage FILLED_IMAGE = Util.loadImage("src/assets/Goal/Filled.png");

    private static final BufferedImage GOAL_IMAGE = Util.loadImage("src/assets/Goal/Goal.png");

    public Goal(int x, int y) {
        filled = false;
        this.x = x;
        this.y = y;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void update() {
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

    private int getDangerFrame() {
        int frame = 1 - (int) Math.abs(dangerTimer - (DANGER_SPEED / 2)) / (DANGER_SPEED / 4);
        System.out.println(frame);
        return frame;
    }

    public boolean isDanger() {
        return getDangerFrame() == 1;
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
        if (danger) {
            Util.drawImage(g, DANGER_IMAGES[getDangerFrame()], x, y, WIDTH, HEIGHT);
        } else if (hasFly) {
            Util.drawImage(g, FLY_IMAGE, x, y, WIDTH, HEIGHT);
        } else if (filled) {
            Util.drawImage(g, FILLED_IMAGE, x, y, WIDTH, HEIGHT);
        }
        Util.drawImage(g, GOAL_IMAGE, x - 50, y - 50, WIDTH + 100, HEIGHT + 50);
    }
}
