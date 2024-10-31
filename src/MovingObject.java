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
        if (left) {
            x -= speed;
        } else {
            x += speed;
        }

        if (notVisible()) {
            x = left ? 800 : -width;
        }
    }

    public int getX() {
        return x;
    }

    public int getVelocity() {
        return speed * (left ? -1 : 1);
    }

    public boolean notVisible() {
        return x < -width || x > 800 + width;
    }
}
