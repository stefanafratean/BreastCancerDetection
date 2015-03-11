package util;

public class MathUtil {
    private MathUtil() {

    }

    public static double sigmoid(double x) {
        return 1d / (1d + Math.exp(-x));
    }
}
