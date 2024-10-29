import java.awt.*;

public class Log extends MovingObject {

    public Log(int length, int x, int y, int speed, boolean left) {
        super(x, y, length * 50, speed, left);
    }

    public int getX() {
        return x;
    }

    public int getVelocity() {
        return speed * (left ? -1 : 1);
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, width, HEIGHT);
    }

    public boolean collides(Rectangle r) {
        return getRect().intersects(r);
    }
}
