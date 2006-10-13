/*
 * 20-Oct-2005
 */
package com.thinkparity.ophelia.model;

import java.io.IOException;

import com.thinkparity.codebase.model.Context;

import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ParityErrorTranslator {

	/**
	 * Singleton instance.
	 * @see ParityErrorTranslator#singletonLock
	 */
	private static final ParityErrorTranslator singleton;

	static { singleton = new ParityErrorTranslator(); }

	/**
	 * Create a parity error based upon an io error.
	 * 
	 * @param iox
	 *            The java io error.
	 * @return The parity error.
	 */
	public static ParityException translate(final IOException iox) {
		return singleton.doTranslate(iox);
	}

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param context
     *            A thinkParity context.
     * @param t
     *            An error.
     * @return A thinkParity unchecked error.
     */
    public static ParityUncheckedException translateUnchecked(
            final Context context, final Object errorId, final Throwable t) {
        return singleton.doTranslateUnchecked(errorId, t);
    }

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param context
     *            A thinkParity context.
     * @param t
     *            An error.
     * @return A thinkParity unchecked error.
     */
    public static ParityUncheckedException translateUnchecked(
            final Workspace workspace, final Object errorId, final Throwable t) {
        return singleton.doTranslateUnchecked(errorId, t);
    }

	/** Create ParityErrorTranslator. */
	private ParityErrorTranslator() { super(); }

    /**
     * Create a parity error base upon a smack error.
     * 
     * @param sx
     *            The smack error.
     * @return The parity error.
     */
    private ParityException doTranslate(final Throwable t) {
        return new ParityException(t);
    }

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     * @return A parity unchecked error.
     */
    private ParityUncheckedException doTranslateUnchecked(final Object errorId,
            final Throwable t) {
        return new ParityUncheckedException(errorId.toString(), t);
    }
}
