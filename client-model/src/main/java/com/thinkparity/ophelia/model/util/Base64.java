/*
 * May 30, 2005
 */
package com.thinkparity.ophelia.model.util;

import com.thoughtworks.xstream.core.util.Base64Encoder;

/**
 * Base64
 * Provides base64 encoding\decoding via the apache commons code libarary.
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class Base64 {

	/**
	 * Thoughtworks encoder used to encode\decode.
	 */
	private static final Base64Encoder thoughtworksBase64 = new Base64Encoder();

	/**
	 * Version id of this encoder.
	 */
	private static final long serialVersionId = 1;

	/**
	 * Decode a byte[] containing characters in the Base64 alphabet.
	 * 
	 * @param string
	 *            The input string to decode.
	 * @return The decoded bytes.
	 */
	public static byte[] decodeBytes(final String string) {
		return thoughtworksBase64.decode(string);
	}

	/**
	 * Encode bytes which contains characters in the Base64 alphabet.
	 * @param bytes <code>java.lang.String</code>
	 * @return <code>byte[]</code>
	 */
	public static String encodeBytes(final byte[] bytes) {
		return thoughtworksBase64.encode(bytes);
	}

	/**
	 * Obtain the name of the encoder used.
	 * @return <code>java.lang.String</code>
	 */
	public static String getEncoder() {
		return new StringBuffer(Base64.class.getName())
			.append(":")
			.append(Base64.serialVersionId).toString();
	}

	/**
	 * Create a Base64 [Singleton]
	 */
	private Base64() { super(); }
}
