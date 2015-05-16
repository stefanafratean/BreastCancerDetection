package model.functions;

public class Xor implements ThreeArgumentsFunction{
    @Override
    public double compute(double operator1, double operator2, double operator3) {
        double sum = operator1 + operator2 + operator3;
        if (sum == 1 || sum == 3) {
            return 1d;
        }
        return 0d;
    }
}
