import java.awt.*;

public class Car extends MovingObject {
    public static final int RED = 0, GREEN = 1, BLUE = 2;

    private final int color;

    public Car(int color, int x, int y, int width, int speed, boolean direction) {
        super(x, y, width, speed, direction);
        this.color = color;
    }

    public void draw(Graphics g) {
        switch (color) {
            case RED -> g.setColor(Color.RED);
            case GREEN -> g.setColor(Color.GREEN);
            case BLUE -> g.setColor(Color.BLUE);
        }
        g.fillRect(x, y, width, HEIGHT);
    }

    public boolean collides(Frog frog) {
        return getRect().intersects(frog.getRect());
    }
}
