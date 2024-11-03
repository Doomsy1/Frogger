import java.awt.*;

public class Log extends MovingObject {

    public Log(int x, int y, int width, int speed, boolean left) {
        super(x, y, width, speed, left);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(139, 69, 19));
        g.fillRect(x, y, width, HEIGHT);
    }
}
