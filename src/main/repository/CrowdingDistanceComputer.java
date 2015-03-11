package repository;


import model.Chromosome;

import java.util.List;

class CrowdingDistanceComputer {

    public double getCrowdingDistance(List<Chromosome> chromosomes, int chromosomeIndex) {
        double leftDistance = computeDistanceToLeftNeighbour(chromosomes, chromosomeIndex);
        double rightDistance = computeDistanceToRightNeighbour(chromosomes, chromosomeIndex);

        if (isBorderChromosome(chromosomes, chromosomeIndex)) {
            return leftDistance + rightDistance;
        }

        return (leftDistance + rightDistance) / 2d;
    }

    private double computeDistanceToRightNeighbour(List<Chromosome> chromosomes, int index) {
        if (!isLastFromFront(chromosomes, index)) {
            return getManhattanDistance(chromosomes.get(index), chromosomes.get(index + 1));
        }
        return 0;
    }

    private double computeDistanceToLeftNeighbour(List<Chromosome> chromosomes, int index) {
        if (!isFirstFromFront(index)) {
            return getManhattanDistance(chromosomes.get(index), chromosomes.get(index - 1));
        }
        return 0;
    }

    private boolean isBorderChromosome(List<Chromosome> chromosomes, int index) {
        return isFirstFromFront(index) || isLastFromFront(chromosomes, index);
    }

    private boolean isLastFromFront(List<Chromosome> chromosomes, int index) {
        return index == chromosomes.size() - 1;
    }

    private boolean isFirstFromFront(int index) {
        return index == 0;
    }

    private double getManhattanDistance(Chromosome chromosome1, Chromosome chromosome2) {
//        double heightDistance = Math.abs(chromosome1.getDepth() - chromosome2.getDepth());
        double performanceDistances = 0;
        for (int i = 0; i < chromosome1.getPerformanceMeasures().size(); i++) {
            performanceDistances += Math.abs(chromosome1.getPerformanceMeasures().get(i).getValue() - chromosome2.getPerformanceMeasures().get(i).getValue());
        }
//        return heightDistance + performanceDistances;
        return performanceDistances;
    }
}
