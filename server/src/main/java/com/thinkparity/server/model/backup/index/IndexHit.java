/*
 * Mar 7, 2006
 */
package com.thinkparity.ophelia.model.index;


/**
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class IndexHit<T> {

	/** The index entry. */
	private T entry;

	/** Create IndexHit. */
	public IndexHit() { super(); }

    /**
     * Obtain the entry
     *
     * @return The T.
     */
    public T getEntry() {
        return entry;
    }

    /**
     * Set entry.
     *
     * @param entry The T.
     */
    public void setEntry(final T entry) {
        this.entry = entry;
    }
}
