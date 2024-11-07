/*
 * TimerBar.java
 * Ario Barin Ostovary
 * This class controls the timer bar's drawing and logic
 */

import java.awt.*;

public class TimerBar {
    private final int x, y, width, height;
    private static final int OUTLINE_WIDTH = 2;
    private final double timeLimit;
    private double timeSpent;
    private final int FPS;

    public TimerBar(int x, int y, int width, int height, double timeLimit, int FPS) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.timeLimit = timeLimit;
        this.FPS = FPS;
    }

    public void update() {
        timeSpent += 1.0 / FPS;
        timeSpent = Math.min(timeSpent, timeLimit);
    }

    public boolean isOverTimeLimit() {
        return timeSpent >= timeLimit;
    }

    public double getTimeLimit() {
        return timeLimit;
    }

    public double getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(double timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void reset() {
        timeSpent = 0;
    }

    public void draw(Graphics g) {
        // White outline
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        // Black inner rectangle
        g.setColor(Color.BLACK);
        g.fillRect(x + OUTLINE_WIDTH, y + OUTLINE_WIDTH, width - 2 * OUTLINE_WIDTH, height - 2 * OUTLINE_WIDTH);

        double ratio = Math.min(1, (timeSpent + 0.3 * timeLimit) / timeLimit);

        int red = (int) (255 * ratio);
        int green = (int) (255 * (1 - ratio));
        g.setColor(new Color(red, green, 0));

        // Draw the bar
        g.fillRect(x + 2 * OUTLINE_WIDTH, y + 2 * OUTLINE_WIDTH, (int) ((timeLimit - timeSpent) / timeLimit * (width - 4 * OUTLINE_WIDTH)), height - 4 * OUTLINE_WIDTH);
    }
}
