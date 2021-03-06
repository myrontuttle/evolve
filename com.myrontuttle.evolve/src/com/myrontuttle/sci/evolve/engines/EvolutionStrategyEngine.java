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
package com.myrontuttle.sci.evolve.engines;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

import com.myrontuttle.sci.evolve.api.CandidateFactory;
import com.myrontuttle.sci.evolve.api.EvolutionEngine;
import com.myrontuttle.sci.evolve.api.EvolutionaryOperator;
import com.myrontuttle.sci.evolve.api.ExpressedCandidate;
import com.myrontuttle.sci.evolve.api.ExpressedFitnessEvaluator;
import com.myrontuttle.sci.evolve.api.ExpressionStrategy;
import com.myrontuttle.sci.evolve.api.FitnessEvaluator;
import com.myrontuttle.sci.evolve.api.TerminationCondition;
import com.myrontuttle.sci.evolve.api.EvaluatedCandidate;
import com.myrontuttle.sci.evolve.api.PopulationStats;
import com.myrontuttle.sci.evolve.util.EvolutionUtils;

/**
 * <p>General purpose engine for implementing Evolution Strategies.  Both (μ+λ) and (μ,λ)
 * strategies are supported (choose which to use by setting the boolean constructor parameter).</p>
 *
 * <p>Though this implementation accepts the {@code eliteCount} argument for each of its evolve
 * methods in common with other {@link EvolutionEngine} implementations, it has no effect for
 * evolution strategies.  Elitism is implicit in a (μ+λ) ES and undesirable for a (μ,λ) ES.</p>

 * @param <T> The type of entity that is to be evolved.
 * @see GenerationalEvolutionEngine
 * @see SteadyStateEvolutionEngine
 * @author Daniel Dyer
 */
public class EvolutionStrategyEngine<T> extends AbstractEvolutionEngine<T>
{
    private final EvolutionaryOperator<T> evolutionScheme;
    private final FitnessEvaluator<? super T> fitnessEvaluator;
    private final ExpressedFitnessEvaluator<T> expressedFitnessEvaluator;
    private final boolean plusSelection;
    private final int offspringMultiplier;


    /**
     * Creates a new engine for an evolution strategy.
     * @param candidateFactory Factory used to create the initial population that is
     * iteratively evolved.
     * @param evolutionScheme The combination of evolutionary operators used to evolve
     * the population at each generation.
     * @param fitnessEvaluator A function for assigning fitness scores to candidate
     * solutions.
     * @param plusSelection If true this object implements a (μ+λ) evolution strategy rather
     * than (μ,λ).  With plus-selection the parents are eligible for survival.  With
     * comma-selection only the offspring survive.
     * @param offspringMultiplier How many offspring to create for each member of the parent
     * population.  This parameter effectively defines a multiplier for μ that gives λ.
     * We define λ in this indirect way because we don't know the value of μ until
     * it is passed as an argument to one of the evolve methods. 
     * For a 1+1 ES this parameter would be set to one.  For other evolution strategies
     * a higher value might be better. Eiben & Smith suggest 7 as a good value. 
     * @param rng The source of randomness used by all stochastic processes (including
     * evolutionary operators and selection strategies).
     */
    public EvolutionStrategyEngine(CandidateFactory<T> candidateFactory,
                                   EvolutionaryOperator<T> evolutionScheme,
                                   FitnessEvaluator<? super T> fitnessEvaluator,
                                   boolean plusSelection,
                                   int offspringMultiplier,
                                   Random rng)
    {
        super(candidateFactory, fitnessEvaluator, rng);
        this.evolutionScheme = evolutionScheme;
        this.fitnessEvaluator = fitnessEvaluator;
        this.expressedFitnessEvaluator = null;
        this.plusSelection = plusSelection;
        this.offspringMultiplier = offspringMultiplier;
    }

    public EvolutionStrategyEngine(CandidateFactory<T> candidateFactory,
                                   EvolutionaryOperator<T> evolutionScheme,
                                   ExpressedFitnessEvaluator<T> expressedFitnessEvaluator,
                                   ExpressionStrategy<T> expressionStrategy,
                                   boolean plusSelection,
                                   int offspringMultiplier,
                                   Random rng)
    {
        super(candidateFactory, expressedFitnessEvaluator, expressionStrategy, rng);
        this.evolutionScheme = evolutionScheme;
        this.fitnessEvaluator = null;
        this.expressedFitnessEvaluator = expressedFitnessEvaluator;
        this.plusSelection = plusSelection;
        this.offspringMultiplier = offspringMultiplier;
    }


    /**
     * This method performs a single step/iteration of the evolutionary process.
     * @param evaluatedPopulation The population at the beginning of the process.
     * @param eliteCount Ignored by evolution strategies.  Elitism is implicit in a (μ+λ)
     * ES and undesirable for a (μ,λ) ES.
     * @param rng A source of randomness.
     * @return The updated population after the evolution strategy has advanced.
     */
    @Override
    protected List<EvaluatedCandidate<T>> nextEvolutionStep(long populationId,
    														List<EvaluatedCandidate<T>> evaluatedPopulation,
                                                            int eliteCount,
                                                            Random rng)  {
        // Elite count is ignored.  If it's non-zero it doesn't really matter, but if assertions are
        // enabled we will flag it as wrong.
        assert eliteCount == 0 : "Explicit elitism is not supported for an ES, eliteCount should be 0.";
        
        // Select candidates that will be operated on to create the offspring.
        int offspringCount = offspringMultiplier * evaluatedPopulation.size();
        List<T> parents = new ArrayList<T>(offspringCount);
        for (int i = 0; i < offspringCount; i++)
        {
            parents.add(evaluatedPopulation.get(rng.nextInt(evaluatedPopulation.size())).getCandidate());
        }

        // Then evolve the parents.
        List<T> offspring = evolutionScheme.apply(parents, rng);

        List<EvaluatedCandidate<T>> evaluatedOffspring;
        if (includeExpression()) {

            // Express each candidate in the population
            List<ExpressedCandidate<T>> expressedCandidates = 
            					expressPopulation(offspring, 0);
            
            //Calculate the fitness scores for each member of the expressed population.
            evaluatedOffspring = evaluateExpressedPopulation(expressedCandidates);
        } else {

            //Calculate the fitness scores for each member of the population.
        	evaluatedOffspring = evaluatePopulation(offspring);
        }
        
        if (plusSelection) // Plus-selection means parents are considered for survival as well as offspring.
        {
            evaluatedOffspring.addAll(evaluatedPopulation);
        }
        EvolutionUtils.sortEvaluatedPopulation(evaluatedOffspring, fitnessEvaluator.isNatural());
        // Retain the fittest of the candidates that are eligible for survival.
        return evaluatedOffspring.subList(0, evaluatedPopulation.size());
    }

	@Override
	protected List<ExpressedCandidate<T>> nextExpressionStep(
			List<ExpressedCandidate<T>> candidates, 
			int eliteCount,
			long populationId, Random rng) {

		List<EvaluatedCandidate<T>> evaluatedPopulation = 
				evaluateExpressedPopulation(candidates);

        EvolutionUtils.sortEvaluatedPopulation(evaluatedPopulation, expressedFitnessEvaluator.isNatural());
        PopulationStats<T> stats = EvolutionUtils.getPopulationStats(populationId,
        										  evaluatedPopulation,
        										  expressedFitnessEvaluator.isNatural(),
                                                  eliteCount,
                                                  getCurrentGenerationIndex(),
                                                  getStartTime());
        
        // Notify observers of the state of the population.
        notifyPopulationChange(stats);

        List<TerminationCondition> satisfiedConditions = EvolutionUtils.shouldContinue(stats, getTerminationConditions());
        if (satisfiedConditions == null) {
        	// Elite count is ignored.  If it's non-zero it doesn't really matter, but if assertions are
            // enabled we will flag it as wrong.
            assert eliteCount == 0 : "Explicit elitism is not supported for an ES, eliteCount should be 0.";
            
            // Select candidates that will be operated on to create the offspring.
            int offspringCount = offspringMultiplier * evaluatedPopulation.size();
            List<T> parents = new ArrayList<T>(offspringCount);
            for (int i = 0; i < offspringCount; i++) {
                parents.add(evaluatedPopulation.get(rng.nextInt(evaluatedPopulation.size())).getCandidate());
            }

            // Then evolve the parents.
            List<T> offspring = evolutionScheme.apply(parents, rng);

        	if (plusSelection) {
        		// Plus-selection means parents are considered for survival as well as offspring.
                for (ExpressedCandidate<T> c : candidates) {
                	offspring.add(c.getGenome());
                }
            }
            // Retain the fittest of the candidates that are eligible for survival.
            // Express each candidate in the population
            List<ExpressedCandidate<T>> newExpressedCandidates = 
            			expressPopulation(offspring.subList(0, evaluatedPopulation.size()), 
            								populationId);
            
            return newExpressedCandidates;
        } else {
            this.satisfiedTerminationConditions = satisfiedConditions;
        	return candidates;
        }
	}

}
