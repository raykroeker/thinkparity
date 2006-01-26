/*
 * Jan 26, 2006
 */
package com.thinkparity.codebase;

/**
 * The jvm unique id is a guaranteed unique id per running instance of the JVM.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * @see JVMUniqueId#nextId()
 */
public class JVMUniqueId {

	/**
	 * The current unique id.
	 * 
	 */
	private static long idCurrent;

	/**
	 * Synchronization lock for the JVM unique id.
	 * 
	 */
	private static final Object idLock;

	/**
	 * The seed value for the unique id.
	 * 
	 */
	private static final long idSeed;

	static {
		idLock = new Object();
		idSeed = System.currentTimeMillis();
		idCurrent = idSeed;
	}

	/**
	 * Obtain the next jvm unique id.
	 * 
	 * @return The next jvm unique id.
	 */
	public static JVMUniqueId nextId() {
		synchronized(idLock) { return new JVMUniqueId(++idCurrent); }
	}

	/**
	 * The unique id.
	 * 
	 */
	private final long uniqueId;

	/**
	 * Create a JVMUniqueId.
	 * 
	 * @param uniqueId
	 *            The unique id.
	 */
	private JVMUniqueId(final long uniqueId) {
		super();
		this.uniqueId = uniqueId;
	}

	/**
	 * Obtain the unique id.
	 * 
	 * @return The unique id.
	 */
	public String getId() { return String.valueOf(uniqueId); }
}
