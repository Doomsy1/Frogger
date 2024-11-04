/*
 * TimerBar.java
 * Ario Barin Ostovary
 * This class controls the timer bar's drawing and logic
 */

import java.awt.*;

public class TimerBar {
    private final int x, y, width, height;
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
        g.setColor(Color.RED);
        g.fillRect(x, y, (int)((timeLimit - timeSpent) / timeLimit * width), height);
    }
}
