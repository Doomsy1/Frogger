/*
 * Car.java
 * Ario Barin Ostovary
 * This class controls the car's drawing
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class Car extends MovingObject {
    public static final int WHITE = 0, PINK = 1, YELLOW = 2, RED = 3, GREEN = 4;
    private static final int CAR_TYPE_COUNT = 5;
    public static BufferedImage[] carImages;

    static {
        carImages = new BufferedImage[CAR_TYPE_COUNT];
        for (int i = 0; i < CAR_TYPE_COUNT; i++) {
            carImages[i] = Util.loadImage("src/assets/Cars/Car" + i + ".png");
        }
    }

    private final int color;

    public Car(int x, int y, int width, int speed, boolean direction, int color) {
        super(x, y, width, speed, direction);
        this.color = color;
    }

    public void draw(Graphics g) {
        Util.drawImage(g, carImages[color], x, y + 4, width, HEIGHT - 8);
    }
}
