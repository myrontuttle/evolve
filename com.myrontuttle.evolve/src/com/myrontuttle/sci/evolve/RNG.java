/**
 * A variety of random number generators available
 */
package com.myrontuttle.sci.evolve;

import java.security.GeneralSecurityException;
import java.util.Random;

import org.uncommons.maths.random.AESCounterRNG;
import org.uncommons.maths.random.CMWC4096RNG;
import org.uncommons.maths.random.CellularAutomatonRNG;
import org.uncommons.maths.random.JavaRNG;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.XORShiftRNG;

/**
 * @author Myron Tuttle
 *
 */
public enum RNG {
	AESCOUNTER, CELLULARAUTOMATON, CMWC4096, JAVA, MARSENNETWISTER, XORSHIFT;
	
	public static Random getRNG(RNG rng) {
        switch (rng) {
    		case AESCOUNTER:
    			try {
    				return new AESCounterRNG();
    			} catch (GeneralSecurityException e) {
    				e.printStackTrace();
    			}
        	case CELLULARAUTOMATON:
        		return new CellularAutomatonRNG();
        	case CMWC4096:
        		return new CMWC4096RNG();
        	case JAVA:
        		return new JavaRNG();
        	case MARSENNETWISTER:
        		return new MersenneTwisterRNG();
        	case XORSHIFT:
        		return new XORShiftRNG();
        	default:
        		return new JavaRNG();
        }
	}
}
