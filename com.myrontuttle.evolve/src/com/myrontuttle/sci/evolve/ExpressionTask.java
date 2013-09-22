package com.myrontuttle.sci.evolve;

import java.util.concurrent.Callable;

/**
 * Callable task for performing parallel expressions.
 * @param <T> The type of entity that is expressed
 * @author Myron Tuttle
 */
class ExpressionTask<T> implements Callable<ExpressedCandidate<T>>
{
    private final ExpressionStrategy<T> expressionStrategy;
    private final T candidate;
    private final String populationId;

    /**
     * Creates a task for performing expressions.
     * @param expressionStrategy The fitness function used to determine candidate fitness.
     * @param candidate The candidate to evaluate.
     * @param population The entire current population.  This will include all
     * of the candidates to evaluate along with any other individuals that are
     * not being evaluated by this task.
     */
    ExpressionTask(ExpressionStrategy<T> expressionStrategy,
                           T candidate,
                           String populationId) {
        this.expressionStrategy = expressionStrategy;
        this.candidate = candidate;
        this.populationId = populationId;
    }

    public ExpressedCandidate<T> call() {
        return expressionStrategy.express(candidate, populationId);
    }
}
