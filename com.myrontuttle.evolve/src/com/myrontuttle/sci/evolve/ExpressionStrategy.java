package com.myrontuttle.sci.evolve;

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
    ExpressedCandidate<T> express(T candidate, String populationId);
    
    /**
     * Invoked after the entire population has been expressed
     * @param expressedPopulation The list of expressed candidates
     */
    void candidatesExpressed(List<ExpressedCandidate<T>> expressedCandidates);

}
