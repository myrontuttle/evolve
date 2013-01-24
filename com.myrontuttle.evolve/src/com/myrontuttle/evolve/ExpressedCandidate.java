package com.myrontuttle.evolve;

/**
 * Creates a more complex object based on the candidate
 * @author Myron Tuttle
 * @param <T> The candidate type.
 */
public interface ExpressedCandidate<T> extends Comparable<ExpressedCandidate<T>> {

    /**
     * Compares this candidate's expression with that of the specified
     * candidate.
     * @param expressedCandidate The candidate to compare scores with.
     * @return -1, 0 or 1 if this candidate's expression is less than, equal to,
     * or greater than that of the specified candidate.  The comparison must
     * be defined by implementing classes.
     */
    public int compareTo(ExpressedCandidate<T> expressedCandidate);

    /**
     * Over-ride to be consistent with {@link #compareTo(EvaluatedCandidate)}.
     * @param o The object to check for equality.
     * @return true If this object is logically equivalent to {code o}.
     */
    @Override
    public boolean equals(Object o);

    /**
     * Over-ride to be consistent with {@link #equals(Object)}.
     * @return This object's hash code.
     */
    @Override
    public int hashCode();
}
