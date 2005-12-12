/*
 * 20-Oct-2005
 */
package com.thinkparity.server.model;

import java.sql.SQLException;

import org.dom4j.DocumentException;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.jivesoftware.messenger.user.UserNotFoundException;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ParityErrorTranslator {

	/**
	 * Singleton instance.
	 * 
	 * @see ParityErrorTranslator#singletonLock
	 */
	private static final ParityErrorTranslator singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 * @see ParityErrorTranslator#singleton
	 */
	private static final Object singletonLock;
	
	static {
		singleton = new ParityErrorTranslator();
		singletonLock = new Object();
	}

	/**
	 * Create a parity error based upon a dom4j
	 * 
	 * @param dx
	 *            The dom4j document exception.
	 * @return The parity error.
	 */
	public static ParityServerModelException translate(
			final DocumentException dx) {
		synchronized(singletonLock) { return singleton.translateImpl(dx); }
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
	 * Translate a jive messenger authorization error into a parity server
	 * error.
	 * 
	 * @param ux
	 *            The jive messenger authorization error.
	 * @return The parity server error.
	 */
	public static ParityServerModelException translate(
			final UnauthorizedException ux) {
		synchronized(singletonLock) { return singleton.translateImpl(ux); }
	}

	/**
	 * Translate a jive messenger user not found error into a parity server
	 * error.
	 * 
	 * @param unfx
	 *            The jive messenger user not found error.
	 * @return The parity server error.
	 */
	public static ParityServerModelException translate(
			final UserNotFoundException unfx) {
		synchronized(singletonLock) { return singleton.translateImpl(unfx); }
	}

	/**
	 * Create a ParityErrorTranslator [Singleton]
	 */
	private ParityErrorTranslator() { super(); }

	/**
	 * Create a parity error based upon a dom4j document exception.
	 * 
	 * @param dx
	 *            The dom4j document exception.
	 * @return The parity error.
	 */
	private ParityServerModelException translateImpl(final DocumentException dx) {
		return new ParityServerModelException(dx);
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

	/**
	 * Translate a jive messenger authorization error into a parity server
	 * error.
	 * 
	 * @param ux
	 *            The jive messenger authorization error.
	 * @return The parity server error.
	 */
	private ParityServerModelException translateImpl(
			final UnauthorizedException ux) {
		return new ParityServerModelException(ux);
	}

	/**
	 * Translate a jive messenger user not found error into a parity server
	 * error.
	 * 
	 * @param unfx
	 *            The jive messenger user not found error.
	 * @return The parity server error.
	 */
	private ParityServerModelException translateImpl(
			final UserNotFoundException unfx) {
		return new ParityServerModelException(unfx);
	}
}
