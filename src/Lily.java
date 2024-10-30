import java.awt.*;

public class Lily extends MovingObject {
    private final boolean breathing;
    private double breath = 0;

    public Lily(int x, int y, int speed, boolean left, boolean breathing) {
        super(x, y, 50, speed, left);
        this.breathing = breathing;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        if (breathing) {
            g.setColor(Color.CYAN);
        }
        g.fillRect(x, y, width, HEIGHT);
    }
    
    public boolean collides(Rectangle r) {
        return getRect().intersects(r);
    }
    
    public boolean isUnderwater() {
        return breathing && breath % 1 < 0.5;
    }
    
    @Override
    public void move() {
        breath += 0.1;
        if (left) {
            x -= speed;
        } else {
            x += speed;
        }

        if (notVisible()) {
            x = left ? 800 : -width;
        }
    }
}
