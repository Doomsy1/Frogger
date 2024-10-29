import java.awt.*;
import java.util.ArrayList;

public class Frog {
    private int x, y;
    private int jumpX, jumpY; // Where the frog is jumping to
    private int facing; // Direction the frog is facing
    private int[] movementKeys;
    private FroggerPanel fp;

    private static final int JUMP_DIST = 50;
    private static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
    private ArrayList<Integer> jumpStack = new ArrayList<>(); // Move will stay in stack until the frog reaches the destination

    public Frog(FroggerPanel froggerPanel, int moveRight, int moveUp, int moveLeft, int moveDown) {
        fp = froggerPanel;
        reset(moveRight, moveUp, moveLeft, moveDown);
    }

    private void reset(int moveRight, int moveUp, int moveLeft, int moveDown) {
        x = 400;
        y = 750;
        facing = UP;
        movementKeys = new int[]{moveRight, moveUp, moveLeft, moveDown};
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
    }

    private void queueJump(int direction) {
        jumpStack.add(direction);
    }

    private void removeJump() {
        jumpStack.remove(0);
    }
    
    private void move() {
        if (jumpStack.isEmpty()) {
            return;
        }
        if (jumpX == x && jumpY == y) {
            removeJump();
        }
        if (jumpStack.isEmpty()) {
            return;
        }

        int direction = jumpStack.get(0);
    }

    private void move(int direction) {
        switch (direction) {
            case RIGHT -> {
                jumpX = x + JUMP_DIST;
                jumpY = y;
            }
            case UP -> {
                jumpX = x;
                jumpY = y - JUMP_DIST;
            }
            case LEFT -> {
                jumpX = x - JUMP_DIST;
                jumpY = y;
            }
            case DOWN -> {
                jumpX = x;
                jumpY = y + JUMP_DIST;
            }
            default -> {
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, 50, 50);
    }

}
