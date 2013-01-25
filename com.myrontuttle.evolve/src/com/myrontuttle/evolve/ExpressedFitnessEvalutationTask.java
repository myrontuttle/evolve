package com.myrontuttle.evolve;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Callable task for performing parallel fitness evaluations.
 * @param ExpressedCandidate<T> The type of entity for which fitness is calculated.
 * @author Myron Tuttle
 */
class ExpressedFitnessEvalutationTask<T> implements Callable<EvaluatedCandidate<T>>
{
    private final ExpressedFitnessEvaluator<T> fitnessEvaluator;
    private final ExpressedCandidate<T> candidate;
    private final List<ExpressedCandidate<T>> population;

    /**
     * Creates a task for performing fitness evaluations.
     * @param fitnessEvaluator The fitness function used to determine candidate fitness.
     * @param candidate The candidate to evaluate.
     * @param population The entire current population.  This will include all
     * of the candidates to evaluate along with any other individuals that are
     * not being evaluated by this task.
     */
    ExpressedFitnessEvalutationTask(ExpressedFitnessEvaluator<T> fitnessEvaluator,
                           ExpressedCandidate<T> candidate,
                           List<ExpressedCandidate<T>> population)
    {
        this.fitnessEvaluator = fitnessEvaluator;
        this.candidate = candidate;
        this.population = population;
    }


    public EvaluatedCandidate<T> call()
    {
        return new EvaluatedCandidate<T>(candidate,
                                         fitnessEvaluator.getFitness(candidate, population));
    }
}
