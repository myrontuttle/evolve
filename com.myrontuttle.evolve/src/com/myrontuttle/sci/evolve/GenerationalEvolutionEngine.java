//=============================================================================
// Copyright 2006-2010 Daniel W. Dyer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//=============================================================================
package com.myrontuttle.sci.evolve;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.Random;

import com.myrontuttle.sci.evolve.interactive.InteractiveSelection;

/**
 * <p>This class implements a general-purpose generational evolutionary algorithm.
 * It supports optional concurrent fitness evaluations to take full advantage of
 * multi-processor, multi-core and hyper-threaded machines.</p>
 *
 * <p>If multi-threading is enabled, evolution (mutation, cross-over, etc.) occurs
 * on the request thread but fitness evaluations are delegated to a pool of worker
 * threads. All of the host's available processing units are used (i.e. on a quad-core
 * machine there will be four fitness evaluation worker threads).</p>
 *
 * <p>If multi-threading is disabled, all work is performed synchronously on the
 * request thread.  This strategy is suitable for restricted/managed environments where
 * it is not permitted for applications to manage their own threads.  If there are no
 * restrictions on concurrency, applications should enable multi-threading for improved
 * performance.</p>
 *
 * @param <T> The type of entity that is to be evolved.
 * @see SteadyStateEvolutionEngine
 * @see EvolutionStrategyEngine
 * @author Daniel Dyer
 */
public class GenerationalEvolutionEngine<T> extends AbstractEvolutionEngine<T>
{
    private final EvolutionaryOperator<T> evolutionScheme;
    private final FitnessEvaluator<? super T> fitnessEvaluator;
    private final ExpressedFitnessEvaluator<T> expressedFitnessEvaluator;
    private final SelectionStrategy<? super T> selectionStrategy;

    /**
     * Creates a new evolution engine by specifying the various components required by
     * a generational evolutionary algorithm.
     * @param candidateFactory Factory used to create the initial population that is
     * iteratively evolved.
     * @param evolutionScheme The combination of evolutionary operators used to evolve
     * the population at each generation.
     * @param fitnessEvaluator A function for assigning fitness scores to candidate
     * solutions.
     * @param selectionStrategy A strategy for selecting which candidates survive to
     * be evolved.
     * @param rng The source of randomness used by all stochastic processes (including
     * evolutionary operators and selection strategies).
     */
    public GenerationalEvolutionEngine(CandidateFactory<T> candidateFactory,
                                       EvolutionaryOperator<T> evolutionScheme,
                                       FitnessEvaluator<? super T> fitnessEvaluator,
                                       SelectionStrategy<? super T> selectionStrategy,
                                       Random rng)
    {
        super(candidateFactory, fitnessEvaluator, rng);
        this.evolutionScheme = evolutionScheme;
        this.fitnessEvaluator = fitnessEvaluator;
        this.selectionStrategy = selectionStrategy;
        this.expressedFitnessEvaluator = null;
    }
    

    public GenerationalEvolutionEngine(CandidateFactory<T> candidateFactory,
                                       EvolutionaryOperator<T> evolutionScheme,
                                       ExpressedFitnessEvaluator<T> expressedFitnessEvaluator,
                                       ExpressionStrategy<T> expressionStrategy,
                                       SelectionStrategy<? super T> selectionStrategy,
                                       Random rng)
    {
        super(candidateFactory, expressedFitnessEvaluator, expressionStrategy, rng);
        this.evolutionScheme = evolutionScheme;
        this.fitnessEvaluator = null;
        this.expressedFitnessEvaluator = expressedFitnessEvaluator;
        this.selectionStrategy = selectionStrategy;
    }


    /**
     * Creates a new evolution engine for an interactive evolutionary algorithm.  It
     * is not necessary to specify a fitness evaluator for interactive evolution.
     * @param candidateFactory Factory used to create the initial population that is
     * iteratively evolved.
     * @param evolutionScheme The combination of evolutionary operators used to evolve
     * the population at each generation.
     * @param selectionStrategy Interactive selection strategy configured with appropriate
     * console.
     * @param rng The source of randomness used by all stochastic processes (including
     * evolutionary operators and selection strategies).
     */
    public GenerationalEvolutionEngine(CandidateFactory<T> candidateFactory,
                                       EvolutionaryOperator<T> evolutionScheme,
                                       InteractiveSelection<T> selectionStrategy,
                                       Random rng)
    {
        this(candidateFactory,
             evolutionScheme,
             new NullFitnessEvaluator(), // No fitness evaluations to perform.
             selectionStrategy,
             rng);
    }


    /**
     * {@inheritDoc} 
     */
    @Override
    protected List<EvaluatedCandidate<T>> nextEvolutionStep(String populationId,
    						List<EvaluatedCandidate<T>> evaluatedPopulation,
                                                            int eliteCount,
                                                            Random rng)
    {
        List<T> population = new ArrayList<T>(evaluatedPopulation.size());

        // First perform any elitist selection.
        List<T> elite = new ArrayList<T>(eliteCount);
        Iterator<EvaluatedCandidate<T>> iterator = evaluatedPopulation.iterator();
        while (elite.size() < eliteCount)
        {
            elite.add(iterator.next().getCandidate());
        }
        // Then select candidates that will be operated on to create the evolved
        // portion of the next generation.
        population.addAll(selectionStrategy.select(evaluatedPopulation,
                                                   fitnessEvaluator.isNatural(),
                                                   evaluatedPopulation.size() - eliteCount,
                                                   rng));
        // Then evolve the population.
        population = evolutionScheme.apply(population, rng);
        // When the evolution is finished, add the elite to the population.
        population.addAll(elite);
        
        if (includeExpression()) {

            // Express each candidate in the population
            List<ExpressedCandidate<T>> expressedCandidates = 
            					expressPopulation(population, populationId);
            
            notifyPopulationExpressed(expressedCandidates);
            
            //Calculate the fitness scores for each member of the expressed population.
            return evaluateExpressedPopulation(expressedCandidates);
        } else {

            //Calculate the fitness scores for each member of the population.
        	return evaluatePopulation(population);
        }
    }

	@Override
	protected List<ExpressedCandidate<T>> nextExpressionStep(
			List<ExpressedCandidate<T>> candidates, 
			int eliteCount,
			String populationId, Random rng) {
		
		List<EvaluatedCandidate<T>> evaluatedPopulation = 
				evaluateExpressedPopulation(candidates);

        EvolutionUtils.sortEvaluatedPopulation(evaluatedPopulation, expressedFitnessEvaluator.isNatural());
        PopulationStats<T> stats = EvolutionUtils.getPopulationStats(
        										  populationId,
        										  evaluatedPopulation,
        										  expressedFitnessEvaluator.isNatural(),
                                                  eliteCount,
                                                  getCurrentGenerationIndex(),
                                                  getStartTime());
        
        // Notify observers of the state of the population.
        notifyPopulationChange(stats);

        List<TerminationCondition> satisfiedConditions = EvolutionUtils.shouldContinue(stats, getTerminationConditions());
        if (satisfiedConditions == null) {

    		List<T> population = new ArrayList<T>(evaluatedPopulation.size());

            // First perform any elitist selection.
            List<T> elite = new ArrayList<T>(eliteCount);
            Iterator<EvaluatedCandidate<T>> iterator = evaluatedPopulation.iterator();
            while (elite.size() < eliteCount) {
                elite.add(iterator.next().getCandidate());
            }
            // Then select candidates that will be operated on to create the evolved
            // portion of the next generation.
            population.addAll(selectionStrategy.select(evaluatedPopulation,
            										expressedFitnessEvaluator.isNatural(),
                                                    evaluatedPopulation.size() - eliteCount,
                                                    rng));
            // Then evolve the population.
            population = evolutionScheme.apply(population, rng);
            // When the evolution is finished, add the elite to the population.
            population.addAll(elite);
            

            // Express each candidate in the population
            List<ExpressedCandidate<T>> newExpressedCandidates = 
            					expressPopulation(population, populationId);
            
            notifyPopulationExpressed(newExpressedCandidates);
            
    		return newExpressedCandidates;
        } else {
            this.satisfiedTerminationConditions = satisfiedConditions;
        	return candidates;
        }
	}
}
