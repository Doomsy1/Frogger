/**
 * Utility class for graphics and random number generation.
 */
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

    public static final int RED_FONT = 0, WHITE_FONT = 1, YELLOW_FONT = 2, FROGGER_FONT = 3;

    // Images
    public static final BufferedImage[] FONT_IMAGES = new BufferedImage[4];

    static {
        loadFonts();
    }

    private static void loadFonts() {
        FONT_IMAGES[RED_FONT] = loadImage("src/assets/Fonts/red.png");
        FONT_IMAGES[WHITE_FONT] = loadImage("src/assets/Fonts/white.png");
        FONT_IMAGES[YELLOW_FONT] = loadImage("src/assets/Fonts/yellow.png");
        FONT_IMAGES[FROGGER_FONT] = loadImage("src/assets/Fonts/frogger.png");
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
            throw new RuntimeException("Could not load image at path: " + path, e);
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
        BufferedImage fontImage = FONT_IMAGES[color];
        String characters = color == FROGGER_FONT ? "FROGE" : "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-";

        int charIndex = characters.indexOf(c);
        if (charIndex == -1) {
            System.out.println("Character not found: " + c);
            return new BufferedImage(1, fontImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        int charWidth = fontImage.getWidth() / characters.length();
        int x = charIndex * charWidth;
        int y = 0;
        int width = charWidth;
        int height = fontImage.getHeight();

        return fontImage.getSubimage(x, y, width, height);
    }

    public static void writeRightText(Graphics g, String text, int x, int y, int height, int color) {
        // Writes text right-aligned to the x position
        int textWidth = text.length() * height;
        writeText(g, text, x - textWidth, y, textWidth, height, color);
    }

    public static void writeCenteredText(Graphics g, String text, int center_x, int center_y, int height, int color) {
        int textWidth = text.length() * height;
        int x = center_x - textWidth / 2;
        writeText(g, text, x, center_y, textWidth, height, color);
    }

    public static void writeText(Graphics g, String text, int x, int y, int height, int color) {
        writeText(g, text, x, y, text.length() * height, height, color);
    }

    public static void writeText(Graphics g, String text, int x, int y, int width, int height, int color) {
        int charWidth = width / text.length();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ') {
                continue;
            }

            c = Character.toUpperCase(c);
            BufferedImage img = getFontImage(color, c);
            drawImage(g, img, x + i * charWidth, y, charWidth, height);
        }
    }
}
