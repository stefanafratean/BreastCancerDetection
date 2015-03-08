package model.objective;

import model.Chromosome;

public class HeightObjective extends MinimizableObjective {

    @Override
    protected int compareChromosomes(Chromosome chromosome1, Chromosome chromosome2) {
        return Double.valueOf(chromosome1.getDepth()).compareTo(Double.valueOf(chromosome2.getDepth()));
    }
}
