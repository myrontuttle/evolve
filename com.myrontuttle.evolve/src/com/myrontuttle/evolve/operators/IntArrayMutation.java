package com.myrontuttle.evolve.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import com.myrontuttle.evolve.EvolutionaryOperator;
import com.myrontuttle.evolve.factories.IntArrayFactory;

/**
 * Mutation of individual integers in an array according to some
 * probability.
 * @author Myron Tuttle
 */
public class IntArrayMutation implements EvolutionaryOperator<int[]> {
	
    private final IntArrayFactory factory;
    private final NumberGenerator<Probability> mutationProbability;

    /**
     * Creates a mutation operator that is applied with the given
     * probability and draws its integers from the specified factory.
     * @param factory Holds the permitted values for each integer in an array.
     * @param mutationProb The probability that a given integer
     * is changed.
     */
    public IntArrayMutation(IntArrayFactory factory, double mutationProb) {
    	this(factory, new Probability(mutationProb));
    }

    /**
     * Creates a mutation operator that is applied with the given
     * probability and draws its integers from the specified factory.
     * @param factory Holds the permitted values for each integer in an array.
     * @param mutationProbability The probability that a given integer
     * is changed.
     */
    public IntArrayMutation(IntArrayFactory factory, Probability mutationProbability) {
        this(factory, new ConstantGenerator<Probability>(mutationProbability));
    }


    /**
     * Creates a mutation operator that is applied with the given
     * probability and draws its integers from the specified factory.
     * @param factory Holds the permitted values for each integer in an array.
     * @param mutationProbability The (possibly variable) probability that a
     * given integer is changed.
     */
    public IntArrayMutation(IntArrayFactory factory,
                          NumberGenerator<Probability> mutationProbability) {
        this.factory = factory;
        this.mutationProbability = mutationProbability;
    }


    public List<int[]> apply(List<int[]> selectedCandidates, Random rng) {
        List<int[]> mutatedPopulation = new ArrayList<int[]>(selectedCandidates.size());
        for (int[] c : selectedCandidates)
        {
            mutatedPopulation.add(mutateArray(c, rng));
        }
        return mutatedPopulation;
    }


    /**
     * Mutate a single array.  Zero or more integers may be modified.  The
     * probability of any given integer being modified is governed by the
     * probability generator configured for this mutation operator.
     * @param c The integer array to mutate.
     * @param rng A source of randomness.
     * @return The mutated array.
     */
    private int[] mutateArray(int[] c, Random rng) {
    	int[] mutated = c.clone();
    	
    	for (int i=0; i<c.length; i++) {
    		if (mutationProbability.nextValue().nextEvent(rng)) {
    			mutated[i] = factory.generateRandomInt(rng, i);
    		}
    	}
    	
        return mutated;
    }
}
