/*
 * Terrain.java
 * Ario Barin Ostovary
 * This class controls the terrain's drawing
 */

import java.awt.*;

public class Terrain {
    private final int x, y;
    private final int width, height;
    private final int type;

    public static final int GRASS = 0, WATER = 1, ROAD = 2, BARRIER = 3;

    public Terrain(int x, int y, int width, int height, int type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public void draw(Graphics g) {
        switch (type) {
            case GRASS -> g.setColor(Color.GREEN);
            case WATER -> g.setColor(Color.BLUE);
            case ROAD -> g.setColor(Color.GRAY);
            case BARRIER -> g.setColor(Color.BLACK);
        }
        g.fillRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
