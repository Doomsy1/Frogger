import java.awt.*;

public class Goal {
    private final int x, y;

    private static final int WIDTH = 50, HEIGHT = 50;

    public Goal(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
}
