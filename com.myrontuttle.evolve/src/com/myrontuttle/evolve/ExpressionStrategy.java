package com.myrontuttle.evolve;

import java.util.List;

/**
 * Strategy interface for candidate expression.
 * @param <T> The type of evolved entity that we are expressing.
 * @author Myron Tuttle
 */
public interface ExpressionStrategy<T>
{
    /**
     * <p>Express a candidate.</p>
     * @param <T> The candidate that we are expressing.
     * @return The expressed candidate.
     */
    ExpressedCandidate<T> express(T candidate);
    
    /**
     * Invoked after the entire population has been expressed
     * @param expressedPopulation The list of expressed candidates
     */
    void populationExpressed(List<ExpressedCandidate<T>> expressedPopulation,
    							ExpressedPopulation<T> stats);
    
    /**
     * Imports a population from a file of expressed candidates to be 
     * evaluated and evolved
     * @param Location of the population file
     * @return The expressed population
     */
    ExpressedPopulation<int[]> importPopulation(String fileName);
}
