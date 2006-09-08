/*
 * Jun 19, 2005
 */
package com.thinkparity.desdemona.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5Util
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MD5Util {

	/**
	 * Calculates the md5 digest and returns the value as a 16 element byte[].
	 * @param data - <code>byte[]</code>
	 * @return <code>byte[]</code>
	 */
	public static byte[] md5(final byte[] data) {
		return DigestUtils.md5(data);
	}

	/**
	 * Calculates the md5 digest and returns the value as a 32 character hex string.
	 * @param data <code>byte[]</code>
	 * @return <code>java.lang.String</code>
	 */
	public static String md5Hex(final byte[] data) {
		return DigestUtils.md5Hex(data);
	}

	/**
	 * Create a MD5Util [Singleton]
	 */
	private MD5Util() { super(); }
}
