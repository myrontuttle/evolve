package com.myrontuttle.sci.evolve.api;

import java.util.List;

/**
 * Strategy interface for candidate expression.
 * @param <T> The type of evolved entity that we are expressing.
 * @author Myron Tuttle
 */
public interface ExpressionStrategy<T> {
	
    /**
     * <p>Express a candidate.</p>
     * @param <T> The candidate that we are expressing.
     * @param populationId The population this candidate belongs to
     * @return The expressed candidate.
     */
    ExpressedCandidate<T> express(T candidate, long populationId);
    
    /**
     * Provides the length of genome required for this expression
     * @param populationId The population this candidate belongs to
     * @return The length of the genome.
     */
    int getGenomeLength(long populationId);
    
    /**
     * Hook to handle any group-level events before expressing candidates
     * @param populationId The population that is about to be expressed
     */
    void beforeExpression(long populationId);
    
    /**
     * Invoked after the entire population has been expressed
     * @param expressedPopulation The list of expressed candidates
     * @param populationId The population these candidates belong to
     */
    void candidatesExpressed(List<ExpressedCandidate<T>> expressedCandidates, long populationId);

	
    /**
     * <p>Destroy a candidate.</p>
     * @param <T> The candidate that we are destroying.
     * @param populationId The population this candidate belongs to
     */
    void destroy(T candidate, long populationId);
}
