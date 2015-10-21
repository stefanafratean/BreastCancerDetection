package model.functions;

public class Tangent implements TwoArgumentsFunction{
    @Override
    public double compute(double operator1, double operator2) {
        return Math.tan(operator1 + operator2);
    }
}
