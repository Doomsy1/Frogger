/*
 * Alligator.java
 * Ario Barin Ostovary
 * This class controls the alligator's drawing, movement, and logic
 */

import java.awt.*;

public class Alligator extends MovingObject {
    private double mouth = 0.0; // opening / closing mouth (0 - 0.5 is closed, 0.5 - 1 is open)
    private static final int MOUTH_CYCLE = 100; // number of frames in a mouth cycle

    public Alligator(int x, int y, int width, int speed, boolean left) {
        super(x, y, width, speed, left);
    }

    public boolean mouthIsOpen() {
        return mouth >= 0.5;
    }

    public Rectangle getMouthRect() {
        if (left) {
            return new Rectangle(x, y, 50, HEIGHT);
        } else {
            return new Rectangle(x + width - 50, y, 50, HEIGHT);
        }
    }

    @Override
    public void move() {
        super.move();
        mouth = (mouth + 1.0 / MOUTH_CYCLE) % 1.0;
    }

    public void draw(Graphics g) {
        // draw the body
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, HEIGHT);

        // draw the mouth if it is open
        if (mouthIsOpen()) {
            g.setColor(Color.RED);
            Rectangle mouthRect = getMouthRect();
            g.fillRect(mouthRect.x, mouthRect.y, mouthRect.width, mouthRect.height);
        }
    }
}
