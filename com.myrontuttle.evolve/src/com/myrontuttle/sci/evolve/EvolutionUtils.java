package com.myrontuttle.sci.evolve;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.uncommons.maths.statistics.DataSet;

public class EvolutionUtils {
    
    /**
     * Given data about the current state of evolution and a set of termination conditions, 
     * determines whether or not the evolution should continue.
     * @param stats The current state of evolution.
     * @param conditions One or more termination conditions.  The evolution should not continue if
     * any of these is satisfied.
     * @param <T> The type of entity that is being evolved.
     * @return A list of satisfied termination conditions if the evolution has reached some
     * pre-specified state, an empty list if the evolution should stop because of a thread
     * interruption, or null if the evolution should continue.
     */
    public static <T> List<TerminationCondition> shouldContinue(PopulationStats<T> stats,
                                                                TerminationCondition... conditions)
    {
        // If the thread has been interrupted, we should abort and return whatever
        // result we currently have.
        if (Thread.currentThread().isInterrupted())
        {
            return Collections.emptyList();
        }
        // Otherwise check the termination conditions for the evolution.
        List<TerminationCondition> satisfiedConditions = new LinkedList<TerminationCondition>();
        for (TerminationCondition condition : conditions)
        {
            if (condition.shouldTerminate(stats))
            {
                satisfiedConditions.add(condition);
            }
        }
        return satisfiedConditions.isEmpty() ? null : satisfiedConditions;
    }
    
    /**
     * Sorts an evaluated population in descending order of fitness
     * (descending order of fitness score for natural scores, ascending
     * order of scores for non-natural scores).
     *
     * @param evaluatedPopulation The population to be sorted (in-place).
     * @param naturalFitness True if higher fitness scores mean fitter individuals, false otherwise.
     * @param <T> The type of entity that is being evolved.
     */
    public static <T> void sortEvaluatedPopulation(List<EvaluatedCandidate<T>> evaluatedPopulation,
                                                   boolean naturalFitness)
    {
        // Sort candidates in descending order according to fitness.
        if (naturalFitness) // Descending values for natural fitness.
        {
            Collections.sort(evaluatedPopulation, Collections.reverseOrder());
        }
        else // Ascending values for non-natural fitness.
        {
            Collections.sort(evaluatedPopulation);
        }
    }
    

    /**
     * Gets data about the current population, including the fittest candidate
     * and statistics about the population as a whole.
     *
     * @param evaluatedPopulation Population of candidate solutions with their
     * associated fitness scores.
     * @param naturalFitness True if higher fitness scores mean fitter individuals, false otherwise.
     * @param eliteCount The number of candidates preserved via elitism.
     * @param iterationNumber The zero-based index of the current generation/epoch.
     * @param elapsedTime The time at which the evolution began, expressed as a number of milliseconds since
     * 00:00 on 1st January 1970.
     * @param <T> The type of entity that is being evolved.
     * @return Statistics about the current generation of evolved individuals.
     */
    public static <T> PopulationStats<T> getPopulationStats(String populationId,
    							List<EvaluatedCandidate<T>> evaluatedPopulation,
                                boolean naturalFitness,
                                int eliteCount,
                                int iterationNumber,
                                long startTime)  {
        DataSet stats = new DataSet(evaluatedPopulation.size());
        for (EvaluatedCandidate<T> candidate : evaluatedPopulation) {
            stats.addValue(candidate.getFitness());
        }
        return new PopulationStats<T>(populationId,
        							 evaluatedPopulation.get(0).getCandidate(),
                                     evaluatedPopulation.get(0).getFitness(),
                                     stats.getArithmeticMean(),
                                     stats.getStandardDeviation(),
                                     naturalFitness,
                                     stats.getSize(),
                                     eliteCount,
                                     iterationNumber,
                                     System.currentTimeMillis() - startTime);
    }
}
