import java.util.Random;

public class Util {
    private static final Random RANDOM = new Random();

    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static double randomDouble(double min, double max) {
        return min + (max - min) * RANDOM.nextDouble();
    }
}