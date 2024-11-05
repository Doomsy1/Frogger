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
                    return 0x00FFFFFF & rgb;  // Make black pixels transparent
                }
                return rgb;  // Keep other pixels unchanged
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
}