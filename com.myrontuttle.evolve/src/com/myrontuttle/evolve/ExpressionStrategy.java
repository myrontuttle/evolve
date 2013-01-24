package com.myrontuttle.evolve;

/**
 * Strategy interface for candidate expression.
 * @param <T> The type of evolved entity that we are expressing.
 * @author Myron Tuttle
 */
public interface ExpressionStrategy<T>
{
    /**
     * <p>Express a candidate.</p>
     * <p>It is an error to call this method with an empty or null population.</p>
     * @param <T> The candidate that we are expressing.
     * @return The expressed candidate.
     */
    ExpressedCandidate<T> express(T candidate);
}
