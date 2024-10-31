import java.awt.*;

public class LilyPad extends MovingObject {
    private final boolean breathing;
    private double breath = 0;

    private static final int BREATH_SPEED = 120; // Breathing cycle duration in frames

    public LilyPad(int x, int y, int speed, boolean left, boolean breathing) {
        super(x, y, 50, speed, left);
        this.breathing = breathing;
    }

    public void draw(Graphics g) {
        if (isUnderwater()) {
            return;
        }
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, HEIGHT);
    }
    
    public boolean collides(Rectangle r) {
        return getRect().intersects(r);
    }
    
    public boolean isUnderwater() {
        return breathing && breath < 0.5;
    }
    
    @Override
    public void move() {
        breath = (breath + 1.0 / BREATH_SPEED) % 1;
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
