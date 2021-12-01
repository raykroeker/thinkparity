/*
 * Mar 29, 2006
 */
package com.thinkparity.codebase;


/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class Pair<T, U> {

    /** A first object. */
    private T one;

    /** A second object. */
    private U two;

	/**
     * Create a Pair.
     * 
     * @param first
     *            The first object.
     * @param second
     *            The second object.
     */
	public Pair(final T object1, final U object2) {
		super();
        this.one = object1;
        this.two = object2;
	}

    /**
     * Obtain the one
     *
     * @return The T.
     */
    public T getOne() {
        return one;
    }

    /**
     * Set one.
     *
     * @param one The T.
     */
    public void setOne(final T object1) {
        this.one = object1;
    }

    /**
     * Obtain the two
     *
     * @return The U.
     */
    public U getTwo() {
        return two;
    }

    /**
     * Set two.
     *
     * @param two The U.
     */
    public void setTwo(final U object2) {
        this.two = object2;
    }
}
