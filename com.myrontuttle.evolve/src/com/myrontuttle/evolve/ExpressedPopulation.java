package com.myrontuttle.evolve;

import java.util.List;

/**
 * Immutable data object containing statistics about the state of
 * an expressed population.
 * @param <T> The type of evolved entity present in the population
 * that this data describes.
 * @see EvolutionObserver
 * @author Myron Tuttle
 */
public final class ExpressedPopulation<T> {
	private final List<ExpressedCandidate<T>> expressedCandidates;
    private final boolean naturalFitness;
    private final int populationSize;
    private final int eliteCount;
    private final int generationNumber;
    private final long startTime;

	/**
     * @param naturalFitness True if higher fitness scores are better, false
     * otherwise. 
     * @param populationSize The number of individuals in the population.
     * @param generationNumber The (zero-based) number of the last generation
     * that was processed.
     */
    public ExpressedPopulation(
    						List<ExpressedCandidate<T>> expressedCandidates,
    						boolean naturalFitness,
    						int populationSize,
    						int eliteCount,
    						int generationNumber,
    						long elapsedTime) {
    	this.expressedCandidates = expressedCandidates;
        this.naturalFitness = naturalFitness;
        this.populationSize = populationSize;
        this.eliteCount = eliteCount;
        this.generationNumber = generationNumber;
        this.startTime = elapsedTime;
    }

    /**
     * @return List of expressed candidates
     */
    public List<ExpressedCandidate<T>> getExpressedCandidates() {
		return expressedCandidates;
	}

    /**
     * Indicates whether the fitness scores are natural or non-natural.
     * @return True if higher fitness scores indicate fitter individuals, false
     * otherwise.
     */
    public boolean isNaturalFitness() {
        return naturalFitness;
    }
    
    /**
     * @return The number of individuals in the current population.
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * @return The number of this generation (zero-based).
     */
    public int getGenerationNumber() {
        return generationNumber;
    }

    /**
     * @return The number of candidates preserved via elitism.
     */
    public int getEliteCount()
    {
        return eliteCount;
    }
    
    /**
     * Returns the time (in milliseconds) of the
     * start of the evolutionary algorithm's execution.
     * @return the time (in milliseconds) when the algorithm started.
     */
    public long getStartTime() {
        return startTime;
    }
}
