/**
 * 
 */
package com.myrontuttle.sci.evolve.factories;

import java.util.Random;

/**
 * General purpose candidate factory for generating bit strings for
 * genetic algorithms.
 * @author Myron Tuttle
 */
public class IntArrayFactory extends AbstractCandidateFactory<int[]> {

    private final int length;
    private final int upperBound;
    private final int lowerBound;
    private final int[] upperBounds;
    private final int[] lowerBounds;

    /**
     * @param length The length of all integer arrays created by this factory.
     * @param upperBound The upperBound of all integers in the array. 0 is 
     * lower bound.
     */
    public IntArrayFactory(int length, int upperBound) {
        this(length, upperBound, 0);
    }

    /**
     * @param length The length of all integer arrays created by this factory.
     * @param upperBound The upperBound of all integers in the array. 
     * @param lowerBound The lowerBound of all integers in the array.
     */
    public IntArrayFactory(int length, int upperBound, int lowerBound) {
    	this(length, upperBound, lowerBound, null, null);
    }

    /**
     * @param length The length of all integer arrays created by this factory.
     * @param upperBound The upperBound of all integers in the array. 
     * @param lowerBound The lowerBound of all integers in the array.
     * @param upperBounds The upperBounds of each position in the array. 
     * @param lowerBounds The lowerBounds of each position in the array.
     */
    public IntArrayFactory(int length, int upperBound, int lowerBound, 
    						int[] upperBounds) {
    	this(length, upperBound, lowerBound, upperBounds, new int[0]);
    }
    
    public IntArrayFactory(int[] upperBounds, int[] lowerBounds) {
		this(upperBounds.length, 1, -1, upperBounds, lowerBounds);
	}
    
    /**
     * @param length The length of all integer arrays created by this factory.
     * @param upperBound The upperBound of all integers in the array. 
     * @param lowerBound The lowerBound of all integers in the array.
     * @param upperBounds The upperBounds of each position in the array. 
     * @param lowerBounds The lowerBounds of each position in the array.
     */
    public IntArrayFactory(int length, int upperBound, int lowerBound, 
    						int[] upperBounds, int[] lowerBounds) {
    	this.length = length;
    	this.upperBound = upperBound;
    	this.lowerBound = lowerBound;
    	if (upperBounds != null && lowerBounds != null) {
        	if (upperBounds.length < length) {
        		int[] adjUpperBound = new int[length];
        		for (int i=0; i<upperBounds.length; i++) {
        			adjUpperBound[i] = upperBounds[i];
        		}
        		for (int i=upperBounds.length; i<length; i++) {
        			adjUpperBound[i] = upperBound;
        		}
        		this.upperBounds = adjUpperBound;
        	} else {
            	this.upperBounds = upperBounds;
        	}
        	if (lowerBounds.length < length) {
        		int[] adjLowerBound = new int[length];
        		for (int i=0; i<lowerBounds.length; i++) {
        			adjLowerBound[i] = lowerBounds[i];
        		}
        		for (int i=lowerBounds.length; i<length; i++) {
        			adjLowerBound[i] = lowerBound;
        		}
        		this.lowerBounds = adjLowerBound;
        	} else {
            	this.lowerBounds = lowerBounds;
        	}
    	} else {
    		this.upperBounds = upperBounds;
    		this.lowerBounds = lowerBounds;
    	}
    }

	/**
     * Generates a random integer array.
     * @param rng The source of randomness for setting the integers.
     * @return A random integer array of the length configured for this
     * factory.
     */
	@Override
	public int[] generateRandomCandidate(Random rng) {
		
		int[] candidate = new int[length];
		if (upperBounds == null || lowerBounds == null) {
			if (lowerBound == 0) {
				for (int i=0; i<length; i++) {
					candidate[i] = rng.nextInt(upperBound);
				}
			} else {
				for (int i=0; i<length; i++) {
					candidate[i] = rng.nextInt(upperBound - lowerBound) + lowerBound;
				}
			}
		} else {
			for (int i=0; i<length; i++) {
				candidate[i] = rng.nextInt(upperBounds[i] - lowerBounds[i]) + lowerBounds[i];
			}
		}
		
		return candidate;
	}
	
	public int generateRandomInt(Random rng, int loc) {

		if (upperBounds == null || lowerBounds == null) {
			if (lowerBound == 0) {
				return rng.nextInt(upperBound);
			} else {
				return rng.nextInt(upperBound - lowerBound) + lowerBound;
			}
		} else {
			return rng.nextInt(upperBounds[loc] - lowerBounds[loc]) + lowerBounds[loc];
		}
	}
}
