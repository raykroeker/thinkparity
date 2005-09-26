/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity;


/**
 * ParityInitialize
 * ParityInitialize is responsible for initializing the client application for
 * the first time.  It writes a version and various other preferences to the
 * java preferences api.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityInitialize {

	/**
	 * Initialize the client application the first time it's run.  This includes
	 * the following:<ol>
	 * <li>Set the current version number.
	 * <li>Prompt the user to select a home directory.
	 */
	public static void initialize() {
		throw new RuntimeException("No longer valid.");
	}

	/**
	 * Create a ParityInitialize [Singleton]
	 */
	private ParityInitialize() { super(); }
}
