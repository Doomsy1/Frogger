/*
 * MovingObject.java
 * Ario Barin Ostovary
 * This is a superclass for most moving objects in the game. It controls the
 * objects' movement and collision detection.
 */

import java.awt.*;

public class MovingObject {
    protected int x;
    protected final int y;
    protected final int speed;
    protected static final int HEIGHT = 50;
    protected final int width;
    protected final boolean left; // Is the object moving left?

    public MovingObject(int x, int y, int width, int speed, boolean left) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.speed = speed;
        this.left = left;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, HEIGHT);
    }
    
    public void move() {
        // Move the object left or right
        if (left) {
            x -= speed;
        } else {
            x += speed;
        }

        // If the object is not visible, move it to the other side
        if (notVisible()) {
            x = left ? 800 : -width;
        }
    }

    public int getX() {
        return x;
    }

    public int getVelocity() {
        // Get the velocity of the object
        return speed * (left ? -1 : 1);
    }

    public boolean notVisible() {
        // Return if the object is not visible
        return x < -width || x > 800;
    }
}
