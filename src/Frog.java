import java.awt.*;
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

    public Frog(FroggerPanel froggerPanel, int moveRight, int moveUp, int moveLeft, int moveDown) {
        fp = froggerPanel;
        movementKeys = new int[]{moveRight, moveUp, moveLeft, moveDown};
        reset();
    }

    public void reset() {
        x = 400;
        y = 750;
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

    private void move() {
        if (jumpStack.isEmpty() && x == jumpX && y == jumpY) {
            inAir = false;
            return;
        }

        if (x == jumpX && y == jumpY && !jumpStack.isEmpty()) {
            facing = jumpStack.remove(0);
            jumpProgress = 0.0;
            
            // If the frog is at the edge of the screen and it is trying to move off the screen, don't jump
            if ((facing == LEFT && x <= 0) || (facing == RIGHT && x >= 800 - JUMP_DIST) || (facing == UP && y <= 0) || (facing == DOWN && y >= 800 - JUMP_DIST)) {
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

        // Ease out
        double progress = 1.0 - Math.pow(1.0 - jumpProgress, 2); // Quadratic ease out
        x = (int) (x + (jumpX - x) * progress);
        y = (int) (y + (jumpY - y) * progress);

        jumpProgress += 0.25;
        
        System.out.println(x - jumpX);

        if (jumpProgress >= 1.0 || (x == jumpX && y == jumpY)) {
            x = jumpX;
            y = jumpY;
            inAir = false;
        }
    }
    
    public boolean isColliding(Terrain t) {
        return t.isColliding(getRect());
    }

    public boolean isRiding(Log l) {
        return l.collides(getRect()) && !inAir;
    }

    public boolean isRiding(LilyPad lp) {
        return lp.collides(getRect()) && !inAir && !lp.isUnderwater();
    }

    public void slide(Log l) {
        x += l.getVelocity();
        jumpX += l.getVelocity();
    }

    public void slide(LilyPad lp) {
        x += lp.getVelocity();
        jumpX += lp.getVelocity();
    }

    public boolean isColliding(Car c) {
        return c.collides(getRect());
    }

    public boolean inAir() {
        return inAir;
    }

    public boolean offScreen() {
        return x < -50 || x > 850 || y < -50 || y > 850; // MAGIC NUMBERS
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, 50, 50);
    }

}
