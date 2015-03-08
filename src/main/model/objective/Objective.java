package model.objective;

import model.Chromosome;

public abstract class Objective {

    public int compare(Chromosome chromosome1, Chromosome chromosome2) {
        return signum() * compareChromosomes(chromosome1, chromosome2);
    }

    protected abstract int signum();

    protected abstract int compareChromosomes(Chromosome chromosome1, Chromosome chromosome2);
}
