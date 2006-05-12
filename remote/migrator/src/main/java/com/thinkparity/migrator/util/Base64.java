/*
 * Created On: May 30, 2005
 * $Id$
 */
package com.thinkparity.migrator.util;

/**
 * Provide Base64 encoding and decoding via the encoder in jdk.
 *
 * // TODO Fix this such that it's either using commons-codec or some other
 * supported class.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Base64 {

    /**
     * Decodes Base64 data into octects
     *
     * @param encoded
     *      Encoded Base64 data.
     * @return Array containind decoded data.
     */
	public static byte[] decode(final String encoded) {
        return com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(encoded);
	}

    /**
     * Encodes hex octects into Base64
     *
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
	public static String encode(final byte[] binaryData) {
		return com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(binaryData);
	}

	/** Create Base64. */
	private Base64() { super(); }
}
