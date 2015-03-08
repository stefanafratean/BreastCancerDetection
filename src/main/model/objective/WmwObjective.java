package model.objective;

import model.Chromosome;

public class WmwObjective extends MaximizableObjective {

    @Override
    protected int compareChromosomes(Chromosome chromosome1, Chromosome chromosome2) {
        return Double.valueOf(chromosome1.getWmw()).compareTo(chromosome2.getWmw());
    }
}
