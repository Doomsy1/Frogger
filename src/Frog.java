/*
 * Frog.java
 * Ario Barin Ostovary
 * This class controls the frog's movement, jumping, drawing, and collision detection
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Frog {
    private int x, y;
    private int jumpX, jumpY; // Where the frog is jumping to
    private int facing; // Direction the frog is facing
    private final int[] movementKeys;
    private final FroggerPanel fp;

    private static final int JUMP_DIST = 50;
    private static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
    private final ArrayList<Integer> jumpStack = new ArrayList<>();
    private static final int STACK_SIZE = 2; // Maximum number of jumps in the stack

    private double jumpProgress = 0.0; // Progress of the jump
    private boolean inAir = false; // Whether the frog is in the air
    private int momentum = 0; // Momentum of the frog

    private double deathTimer; // 1.0 -> 0.0
    private static final int DEATH_TIME = 30; // Number of game frames the frog's death animation lasts
    private static final int DEATH_FRAMES = 7; // Number of frames in the frog's death animation
    private static final BufferedImage[] DEATH_IMAGES = new BufferedImage[DEATH_FRAMES];

    // Load the death images
    static {
        for (int i = 0; i < DEATH_FRAMES; i++) {
            DEATH_IMAGES[i] = Util.loadImage("src/assets/Death/" + i + ".png");
        }
    }

    // Images [direction][movement type] - movement type is either in air (1) or not (0)
    private static final BufferedImage[][] frogImages;

    // Load the frog images
    static {
        frogImages = new BufferedImage[4][2];
        for (int i = 0; i < 4; i++) {
            frogImages[i][0] = Util.loadImage("src/assets/Frog/" + i + "0.png");
            frogImages[i][1] = Util.loadImage("src/assets/Frog/" + i + "1.png");
        }
    }

    public Frog(FroggerPanel froggerPanel, int moveRight, int moveUp, int moveLeft, int moveDown) {
        fp = froggerPanel;
        movementKeys = new int[] { moveRight, moveUp, moveLeft, moveDown };
        reset();
    }

    public void reset() {
        x = 400;
        y = 700;
        jumpX = x;
        jumpY = y;
        facing = UP;
        inAir = false;
        deathTimer = 0.0;
        clearJumpStack();
    }

    public void die() {
        // Clear the jump stack to prevent the frog from jumping while dead and after death
        clearJumpStack();

        // If the frog is already dead, do nothing - this is to prevent the frog from dying multiple times
        if (deathTimer > 0.0) {
            return;
        }

        // Start the death animation
        deathTimer = 1.0;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, 50, 50);
    }

    public void move(boolean[] keys) {
        // Check if the movement keys are pressed and queue the jump
        for (int i = 0; i < 4; i++) {
            if (keys[movementKeys[i]]) {
                queueJump(i);
            }
        }

        // Move the frog
        move();
    }

    private void queueJump(int direction) {
        // If the frog is dead, do nothing
        if (deathTimer > 0.0) {
            return;
        }

        // If the jump stack is full, do nothing
        if (jumpStack.size() >= STACK_SIZE) {
            return;
        }

        // Add the direction to the jump stack
        jumpStack.add(direction);
    }

    private void clearJumpStack() {
        // Clear the jump stack
        jumpStack.clear();
    }

    public void resetMomentum() {
        // Reset the momentum
        momentum = 0;
    }

    private void move() {
        // If the frog is dead, update the death timer
        if (deathTimer > 0.0) {
            deathTimer -= 1.0 / DEATH_TIME;
            if (deathTimer < 0.0) {
                deathTimer = 0.0;
                reset();
            }
            return;
        }

        // If the frog is not in the air and the jump stack is empty, the frog is not moving
        if (jumpStack.isEmpty() && x == jumpX && y == jumpY) {
            inAir = false;

            // Move the frog by the momentum
            x += momentum;
            jumpX += momentum;
            return;
        }

        // If the frog is at the destination and the jump stack is not empty, the frog is starting a jump
        if (x == jumpX && y == jumpY && !jumpStack.isEmpty()) {
            facing = jumpStack.remove(0);
            jumpProgress = 0.0;

            // If the frog is at the edge of the screen and it is trying to move off the screen, don't jump
            if ((facing == LEFT && x <= 0) || (facing == RIGHT && x >= 800 - JUMP_DIST) || (facing == UP && y <= 0)
                    || (facing == DOWN && y >= 750 - JUMP_DIST)) {

                // Move the frog by the momentum
                x += momentum;
                jumpX += momentum;
                return;
            }

            inAir = true;
            switch (facing) {
                case RIGHT -> jumpX += JUMP_DIST;
                case UP -> jumpY -= JUMP_DIST;
                case LEFT -> jumpX -= JUMP_DIST;
                case DOWN -> jumpY += JUMP_DIST;
            }
        }

        // Ease the frog into the jump
        double progress = 1.0 - Math.pow(1.0 - jumpProgress, 2);
        x = (int) (x + (jumpX - x) * progress + momentum);
        y = (int) (y + (jumpY - y) * progress);

        jumpX += momentum;

        jumpProgress += 0.2;

        if (jumpProgress >= 1.0 || (x == jumpX && y == jumpY)) {
            x = jumpX;
            y = jumpY;
            inAir = false;
        }
    }

    public boolean isColliding(Rectangle r) {
        // Calculate the distance between the frog and the rectangle - circle collision detection (more forgiving)
        int frogCenterX = x + 25;
        int frogCenterY = y + 25;
        int frogRadius = 25;

        int rectCenterX = r.x + r.width / 2;
        int rectCenterY = r.y + r.height / 2;

        // Calculate the distance between the frog and the rectangle
        int deltaX = frogCenterX - rectCenterX;
        int deltaY = frogCenterY - rectCenterY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // If the distance is less than the sum of the frog's radius and half the rectangle's width or height, the frog is colliding
        return distance < (frogRadius + Math.min(r.width, r.height) / 2);
    }
    
    public boolean isColliding(Terrain t) {
        // Check if the frog is colliding with the terrain
        return isColliding(t.getRect());
    }
    
    public boolean isColliding(Goal g) {
        // Check if the frog is colliding with the goal
        return isColliding(g.getRect());
    }
    
    public boolean isColliding(Car c) {
        // Check if the frog is colliding with the car
        return isColliding(c.getRect());
    }

    public boolean isBeingEaten(Alligator a) {
        // Check if the frog is colliding with the alligator's mouth and the alligator's mouth is open
        return isColliding(a.getMouthRect()) && a.mouthIsOpen();
    }
    
    public boolean isRiding(Turtle lp) {
        // Check if the frog is colliding with the turtle and the turtle is not underwater
        Point center = new Point(x + 25, y + 25);
        return lp.getRect().contains(center) && !inAir && !lp.isUnderwater();
    }

    public boolean isRiding(Log l) {
        // Check if the frog is colliding with the log and the frog is not in the air
        Point center = new Point(x + 25, y + 25);
        return l.getRect().contains(center) && !inAir;
    }

    public boolean isRiding(Alligator a) {
        // Check if the frog is colliding with the alligator and the frog is not in the air
        Point center = new Point(x + 25, y + 25);
        return a.getRect().contains(center) && !inAir;
    }
    
    public void slide(Log l) {
        // Set the momentum to the log's velocity
        momentum = l.getVelocity();
    }

    public void slide(Turtle lp) {
        // Set the momentum to the turtle's velocity
        momentum = lp.getVelocity();
    }

    public void slide(Alligator a) {
        // Set the momentum to the alligator's velocity
        momentum = a.getVelocity();
    }

    public boolean inAir() {
        // Check if the frog is in the air
        return inAir;
    }

    public boolean offScreen() {
        // Check if the frog is off the screen - 25 pixels off the edge
        return x <= -25 || x >= 775 || y <= -25 || y >= 775;
    }

    public boolean isDead() {
        // Check if the frog is dead
        return deathTimer > 0.0;
    }

    private int getDeathFrame() {
        // Get the current frame of the death animation
        return 6 - (int) (deathTimer * DEATH_FRAMES);
    }

    public void draw(Graphics g) {
        // Draw the frog - death animation or not
        if (deathTimer > 0.0) {
            Util.drawImage(g, DEATH_IMAGES[getDeathFrame()], x, y, 50, 50);
        } else {
            Util.drawImage(g, frogImages[facing][inAir ? 1 : 0], x, y, 50, 50);
        }
    }

}
