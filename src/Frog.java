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
    private static final int STACK_SIZE = 2;

    private double jumpProgress = 0.0;
    private boolean inAir = false;
    private int momentum = 0;

    // Images [direction][movement type] - movement type is either in air (1) or not (0)
    private static final BufferedImage[][] frogImages;

    static {
        frogImages = new BufferedImage[4][2];
        for (int i = 0; i < 4; i++) {
            frogImages[i][0] = Util.loadImage("src/assets/Frog/" + i + "0.png");
            frogImages[i][1] = Util.loadImage("src/assets/Frog/" + i + "1.png");
        }
    }

    public Frog(FroggerPanel froggerPanel, int moveRight, int moveUp, int moveLeft, int moveDown) {
        fp = froggerPanel;
        movementKeys = new int[]{moveRight, moveUp, moveLeft, moveDown};
        reset();
    }

    public void reset() {
        x = 400;
        y = 650;
        jumpX = x;
        jumpY = y;
        facing = UP;
        inAir = false;
        clearJumpStack();
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, 50, 50);
    }

    public void move(boolean[] keys) {
        for (int i = 0; i < 4; i++) {
            if (keys[movementKeys[i]]) {
                queueJump(i);
            }
        }
        move();
    }

    private void queueJump(int direction) {
        if (jumpStack.size() >= STACK_SIZE) {
            return;
        }
        jumpStack.add(direction);
    }

    private void clearJumpStack() {
        jumpStack.clear();
    }

    public void resetMomentum() {
        momentum = 0;
    }

    private void move() {

        // If the frog is not in the air and the jump stack is empty, the frog is not moving
        if (jumpStack.isEmpty() && x == jumpX && y == jumpY) {
            inAir = false;

            // Move the frog by the momentum 
            x += momentum;
            jumpX += momentum;
            return;
        }

        if (x == jumpX && y == jumpY && !jumpStack.isEmpty()) {
            facing = jumpStack.remove(0);
            jumpProgress = 0.0;

            // If the frog is at the edge of the screen and it is trying to move off the screen, don't jump
            if ((facing == LEFT && x <= 0) || (facing == RIGHT && x >= 800 - JUMP_DIST) || (facing == UP && y <= 0)
                    || (facing == DOWN && y >= 700 - JUMP_DIST)) {

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

        // Ease
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
    
    public boolean isColliding(Terrain t) {
        return getRect().intersects(t.getRect());
    }
    
    public boolean isColliding(Goal g) {
        return getRect().intersects(g.getRect());
    }
    
    public boolean isColliding(Car c) {
        return getRect().intersects(c.getRect());
    }

    public boolean isBeingEaten(Alligator a) {
        return getRect().intersects(a.getMouthRect()) && a.mouthIsOpen();
    }
    
    public boolean isRiding(Turtle lp) {
        Point center = new Point(x + 25, y + 25);
        return lp.getRect().contains(center) && !inAir && !lp.isUnderwater();
    }

    public boolean isRiding(Log l) {
        Point center = new Point(x + 25, y + 25);
        return l.getRect().contains(center) && !inAir;
    }

    public boolean isRiding(Alligator a) {
        Point center = new Point(x + 25, y + 25);
        return a.getRect().contains(center) && !inAir;
    }
    
    public void slide(Log l) {
        momentum = l.getVelocity();
    }

    public void slide(Turtle lp) {
        momentum = lp.getVelocity();
    }

    public void slide(Alligator a) {
        momentum = a.getVelocity();
    }

    public boolean inAir() {
        return inAir;
    }

    public boolean offScreen() {
        return x < -50 || x > 850 || y < -50 || y > 850; // MAGIC NUMBERS
    }

    public void draw(Graphics g) {
        Util.drawImage(g, frogImages[facing][inAir ? 1 : 0], x, y, 50, 50);
    }

}
