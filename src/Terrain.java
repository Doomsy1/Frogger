/*
 * Terrain.java
 * Ario Barin Ostovary
 * This class controls the terrain's drawing.
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class Terrain {
    private final int x, y;
    private final int width, height;
    private final int type;

    public static final int GRASS = 0, WATER = 1, ROAD = 2;

    private static final BufferedImage grassImage;

    // Load the grass image
    static {
        grassImage = Util.loadImage("src/assets/Terrain/Grass.png");
    }

    public Terrain(int x, int y, int width, int height, int type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public void draw(Graphics g) {
        switch (type) {
            // Draw the grass in 50x50 tiles
            case GRASS -> {
                for (int i = 0; i < width / 50; i++) {
                    Util.drawImage(g, grassImage, x + i * 50, y, 50, height);
                }
            }
            // WATER and ROAD are handled in FroggerPanel -- kinda gross
            default -> {
            }
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
