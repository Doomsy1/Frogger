import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Util {
    private static final Random RANDOM = new Random();

    public static final int RED_FONT = 0, WHITE_FONT = 1, YELLOW_FONT = 2;

    // Images (src/assets/Fonts/Red/0.png, 1.png, 2.png, etc.)
    // [color][char]
    public static final BufferedImage[][] FONT_IMAGES = new BufferedImage[3][37];

    static {
        loadFonts();
    }

    private static void loadFonts() {
        // 0-9
        for (int i = 0; i < 10; i++) {
            FONT_IMAGES[RED_FONT][i] = loadImage("src/assets/Fonts/Red/" + i + ".png");
            FONT_IMAGES[WHITE_FONT][i] = loadImage("src/assets/Fonts/White/" + i + ".png");
            FONT_IMAGES[YELLOW_FONT][i] = loadImage("src/assets/Fonts/Yellow/" + i + ".png");
        }

        // A-Z
        for (int i = 0; i < 26; i++) {
            char c = (char)  ('A' + i);
            FONT_IMAGES[RED_FONT][i + 10] = loadImage("src/assets/Fonts/Red/" + c + ".png");
            FONT_IMAGES[WHITE_FONT][i + 10] = loadImage("src/assets/Fonts/White/" + c + ".png");
            FONT_IMAGES[YELLOW_FONT][i + 10] = loadImage("src/assets/Fonts/Yellow/" + c + ".png");
        }
        
        // '-'
        FONT_IMAGES[RED_FONT][36] = loadImage("src/assets/Fonts/Red/-.png");
        FONT_IMAGES[WHITE_FONT][36] = loadImage("src/assets/Fonts/White/-.png");
        FONT_IMAGES[YELLOW_FONT][36] = loadImage("src/assets/Fonts/Yellow/-.png");
    }

    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static double randomDouble(double min, double max) {
        return min + (max - min) * RANDOM.nextDouble();
    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("bruh", e);
        }
    }

    public static void drawImage(Graphics g, BufferedImage img, int x, int y, int width, int height) {
        // Create a filter to make black pixels transparent
        ImageFilter filter = new RGBImageFilter() {
            private final int transparentColor = Color.BLACK.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == transparentColor) {
                    return 0x00FFFFFF & rgb; // Make black pixels transparent
                }
                return rgb; // Keep other pixels unchanged
            }
        };

        // Apply the transparency filter
        ImageProducer ip = new FilteredImageSource(img.getSource(), filter);
        Image transparentImg = Toolkit.getDefaultToolkit().createImage(ip);

        // Draw the image with nearest neighbor interpolation
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.drawImage(transparentImg, x, y, width, height, null);
    }

    private static BufferedImage getFontImage(int color, char c) {
        if (c >= '0' && c <= '9') {
            return FONT_IMAGES[color][c - '0'];
        } else if (c >= 'A' && c <= 'Z') {
            return FONT_IMAGES[color][c - 'A' + 10];
        } else {
            return FONT_IMAGES[color][36];
        }
    }

    public static void writeText(Graphics g, String text, int x, int y, int height, int color) {
        writeText(g, text, x, y, text.length() * height, height, color);
    }

    public static void writeText(Graphics g, String text, int x, int y, int width, int height, int color) {
        int charWidth = width / text.length();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                continue;
            }

            char c = Character.toUpperCase(text.charAt(i));
            BufferedImage img = getFontImage(color, c);
            drawImage(g, img, x + i * charWidth, y, charWidth, height);
        }
    }
}