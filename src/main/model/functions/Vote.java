package model.functions;

public class Vote implements ThreeArgumentsFunction{
    @Override
    public double compute(double operator1, double operator2, double operator3) {
        if (operator1 + operator2 + operator3 >= 2) {
            return 1d;
        }
        return 0d;
    }
}
