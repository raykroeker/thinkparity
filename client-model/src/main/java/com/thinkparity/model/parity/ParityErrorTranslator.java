/*
 * 20-Oct-2005
 */
package com.thinkparity.model.parity;

import java.io.IOException;

import com.thinkparity.model.smack.SmackException;

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

	/**
	 * Singleton synchronization lock.
	 * @see ParityErrorTranslator#singleton
	 */
	private static final Object singletonLock;
	
	static {
		singleton = new ParityErrorTranslator();
		singletonLock = new Object();
	}

	/**
	 * Create a parity error based upon a thread interruption error.
	 * 
	 * @param ix
	 *            The thead interruption error.
	 * @return The parity error.
	 */
	public static ParityException translate(final InterruptedException ix) {
		synchronized(singletonLock) { return singleton.translateImpl(ix); }
	}

	/**
	 * Create a parity error based upon an io error.
	 * 
	 * @param iox
	 *            The java io error.
	 * @return The parity error.
	 */
	public static ParityException translate(final IOException iox) {
		synchronized(singletonLock) { return singleton.translateImpl(iox); }
	}

	/**
	 * Create a parity error based upon a java runtime error.
	 * 
	 * @param rx
	 *            The java runtime error.
	 * @return The parity error.
	 */
	public static ParityException translate(final RuntimeException rx) {
		synchronized(singletonLock) { return singleton.translateImpl(rx); }
	}

	/**
	 * Create a parity error based upon a smack error.
	 * 
	 * @param sx
	 *            The smack error.
	 * @return The parity error.
	 */
	public static ParityException translate(final SmackException sx) {
		synchronized(singletonLock) { return singleton.translateImpl(sx);}
	}

	/**
	 * Create a ParityErrorTranslator [Singleton]
	 */
	private ParityErrorTranslator() { super(); }

	/**
	 * Create a parity error based upon a thread interruption error.
	 * 
	 * @param ix
	 *            The thead interruption error.
	 * @return The parity error.
	 */
	private ParityException translateImpl(final InterruptedException ix) {
		return new ParityException(ix);
	}

	/**
	 * Create a parity error based upon an io error.
	 * 
	 * @param iox
	 *            The java io error.
	 * @return The parity error.
	 */
	private ParityException translateImpl(final IOException iox) {
		return new ParityException(iox);
	}

	/**
	 * Create a parity error based upon a java runtime error.
	 * 
	 * @param rx
	 *            The java runtime error.
	 * @return The parity error.
	 */
	private ParityException translateImpl(final RuntimeException rx) {
		return new ParityException(rx);
	}

	/**
	 * Create a parity error base upon a smack error.
	 * 
	 * @param sx
	 *            The smack error.
	 * @return The parity error.
	 */
	private ParityException translateImpl(final SmackException sx) {
		return new ParityException(sx);
	}
}
