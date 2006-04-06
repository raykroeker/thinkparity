/*
 * Feb 1, 2006
 */
package com.raykroeker.junitx;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import junit.framework.Assert;

/**
 * @author raykroeker@gmail.com
 */
public class TestSession {

	/**
	 * The JUnit session data.
	 * 
	 */
	private final Map<Object,Object> sessionData;

	/**
	 * Synchronization lock for the session data.
	 * 
	 */
	private final Object sessionDataLock;

	/**
	 * The JUnit session sessionDirectory.
	 * 
	 */
	private final File sessionDirectory;

	/**
	 * The JUnit session id.
	 * 
	 */
	private final String sessionId;

	/**
	 * Create a TestSession.
	 * 
	 */
	TestSession(final File rootDirectory) {
		super();
		this.sessionId = String.valueOf(System.currentTimeMillis());
		this.sessionData = new Hashtable<Object,Object>(20, 0.75F);
		this.sessionDataLock = new Object();
		this.sessionDirectory = createSessionDirectory(rootDirectory);
	}

	/**
	 * Obtain session data.
	 * 
	 * @param key
	 *            The data item key.
	 * @return The data value.
	 */
	public Object getData(final String key) {
		synchronized(sessionDataLock) {
			return sessionData.get(key);
		}
	}

	/**
	 * Obtain the session sessionDirectory.
	 * 
	 * @return The session sessionDirectory.
	 */
	public File getSessionDirectory() { return sessionDirectory; }

	/**
	 * Obtain the session id.
	 * 
	 * @return The session id.
	 */
	public String getSessionId() { return sessionId; }

	/**
	 * Set session data.
	 * 
	 * @param key
	 *            The data item key.
	 * @param value
	 *            The data item value.
	 * @return The previous value of the data item.
	 */
	public Object setData(final String key, final Object value) {
		synchronized(sessionDataLock) {
			return sessionData.put(key, value); 
		}
	}

	/**
	 * Clear the session data.
	 *
	 */
	void clearData() {
		synchronized(sessionDataLock) { sessionData.clear(); }
	}

	/**
	 * Create the session directory.
	 * 
	 * @return The session directory.
	 */
	private File createSessionDirectory(final File rootDirectory) {
		final File sessionDirectory = new File(rootDirectory, sessionId);
		Assert.assertTrue(sessionDirectory.mkdir());
		return sessionDirectory;
	}
}
