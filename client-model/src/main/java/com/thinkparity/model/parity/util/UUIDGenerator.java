/*
 * Apr 9, 2005
 */
package com.thinkparity.model.parity.util;

import java.util.UUID;

/**
 * UUIDGenerator
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UUIDGenerator {

	/**
	 * Version id of the uuid generator.
	 */
	private static final Long UUIDVersionId = 0L;

	public static Long getVersionId() { return UUIDGenerator.UUIDVersionId; }

	public static UUID nextUUID() { return UUID.randomUUID(); }

	/**
	 * Create a UUIDGenerator [Singleton]
	 */
	private UUIDGenerator() { super(); }
}
