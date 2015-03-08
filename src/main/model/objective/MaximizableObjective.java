package model.objective;

public abstract class MaximizableObjective extends Objective {
    @Override
    protected int signum() {
        return 1;
    }
}
