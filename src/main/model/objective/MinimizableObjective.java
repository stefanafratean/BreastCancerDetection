package model.objective;

public abstract class MinimizableObjective extends Objective {

    @Override
    protected int signum() {
        return -1;
    }
}
