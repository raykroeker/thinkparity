/*
 * Jun 19, 2005
 */
package com.thinkparity.codebase.model.util.codec;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * <b>Title:</b>thinkParity MD5 Util<br>
 * <b>Description:</b>Calculate an MD5 checksum from a variety of sources.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
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
     * Create an MD5 hex encoded checksum of an input stream.
     * 
     * @param inputStream
     *            An <code>InputStream</code>.
     * @return An MD5 hex encoded checksum.
     * @throws IOException
     */
    public static String md5Hex(final InputStream inputStream) throws IOException {
        final MessageDigest messageDigest = getDigest();
        final byte[] bytes = new byte[512];
        int bytesRead;
        while (-1 != (bytesRead = inputStream.read(bytes))) {
            messageDigest.update(bytes, 0, bytesRead);
        }
        return new String(Hex.encodeHex(messageDigest.digest()));
    }

	/**
     * Create and MD5 hex encoded checksum of string data.
     * 
     * @param data
     *            A data <code>String</code>.
     * @return An MD5 hex encoded checksum.
     */
    public static String md5Hex(final String data) {
        final MessageDigest messageDigest = getDigest();
        final byte[] bytes = data.getBytes();
        final int bytesRead = bytes.length;
        messageDigest.update(bytes, 0, bytesRead);
        return new String(Hex.encodeHex(messageDigest.digest()));
    }

    /**
     * Create an instance of a message digest using the MD5 algorithm.
     * 
     * @return An instance of <code>MessageDigest</code>.
     */
    private static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException nsax) {
            throw new RuntimeException(nsax);
        }
    }

	/**
	 * Create a MD5Util [Singleton]
     * 
	 */
	private MD5Util() { super(); }
}
