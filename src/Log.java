import java.awt.*;

public class Log extends MovingObject {

    public Log(int length, int x, int y, int speed, boolean left) {
        super(x, y, length * 50, speed, left);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(139, 69, 19));
        g.fillRect(x, y, width, HEIGHT);
    }

    public boolean collides(Rectangle r) {
        return getRect().intersects(r);
    }
}
