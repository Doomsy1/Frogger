import java.awt.*;

public class Terrain {
    private final int x, y;
    private final int width, height;
    private final int type; // 0 = Grass, 1 = Water

    public static final int GRASS = 0, WATER = 1;

    public Terrain(int x, int y, int width, int height, int type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public void draw(Graphics g) {
        g.setColor(type == GRASS ? Color.GREEN : Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isColliding(Rectangle r) {
        return r.intersects(getRect());
    }
}
