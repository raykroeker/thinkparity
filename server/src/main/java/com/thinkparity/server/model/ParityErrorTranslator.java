/*
 * 20-Oct-2005
 */
package com.thinkparity.server.model;

import java.io.IOException;
import java.sql.SQLException;

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
	 * Create a parity error based upon an io error.
	 * 
	 * @param iox
	 *            The java io error.
	 * @return The parity error.
	 */
	public static ParityServerModelException translate(final IOException iox) {
		synchronized(singletonLock) { return singleton.translateImpl(iox); }
	}

	/**
	 * Create a parity error based upon a java runtime error.
	 * 
	 * @param rx
	 *            The java runtime error.
	 * @return The parity error.
	 */
	public static ParityServerModelException translate(final RuntimeException rx) {
		synchronized(singletonLock) { return singleton.translateImpl(rx); }
	}

	/**
	 * Create a parity error based upon a java sql error.
	 * 
	 * @param sqlx
	 *            The java sql error.
	 * @return The parity error.
	 */
	public static ParityServerModelException translate(final SQLException sqlx) {
		synchronized(singletonLock) { return singleton.translateImpl(sqlx); }
	}

	/**
	 * Create a ParityErrorTranslator [Singleton]
	 */
	private ParityErrorTranslator() { super(); }

	/**
	 * Create a parity error based upon an io error.
	 * 
	 * @param iox
	 *            The java io error.
	 * @return The parity error.
	 */
	private ParityServerModelException translateImpl(final IOException iox) {
		return new ParityServerModelException(iox);
	}

	/**
	 * Create a parity error based upon a java runtime error.
	 * 
	 * @param rx
	 *            The java runtime error.
	 * @return The parity error.
	 */
	private ParityServerModelException translateImpl(final RuntimeException rx) {
		return new ParityServerModelException(rx);
	}

	/**
	 * Create a parity error based upon a java sql error.
	 * 
	 * @param sqlx
	 *            The java sql error.
	 * @return The parity error.
	 */
	private ParityServerModelException translateImpl(final SQLException sqlx) {
		return new ParityServerModelException(sqlx);
	}
}
