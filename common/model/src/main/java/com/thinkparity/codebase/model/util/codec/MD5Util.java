/*
 * Jun 19, 2005
 */
package com.thinkparity.codebase.model.util.codec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.thinkparity.codebase.model.ThinkParityException;

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
     * Create and MD5 hex encoded checksum of a byte channel.
     * 
     * @param byteChannel
     *            A <code>ByteChannel</code>.
     * @param bufferSize
     *            A buffer size <code>Integer</code> to use.
     * @return An MD5 hex encoded checksum.
     * @throws IOException
     */
    public static String md5Hex(final ReadableByteChannel channel,
            final Integer bufferSize) throws IOException {
        final MessageDigest messageDigest = getDigest();
        final byte[] bytes = new byte[bufferSize];
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int bytesRead;
        while (-1 != (bytesRead = channel.read(buffer))) {
            buffer.position(0);
            buffer.limit(bytesRead);
            messageDigest.update(bytes, 0, bytesRead);
        }
        return new String(Hex.encodeHex(messageDigest.digest()));
    }
    
    /**
     * Create an MD5 hex encoded checksum of an input stream.
     * 
     * @param inputStream
     *            An <code>InputStream</code>.
     * @param bufferSize
     *            A buffer size <code>Integer</code> to use.
     * @return An MD5 hex encoded checksum.
     * @throws IOException
     */
    public static String md5Hex(final InputStream inputStream,
            final Integer bufferSize) throws IOException {
        final MessageDigest messageDigest = getDigest();
        final byte[] bytes = new byte[bufferSize];
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
            throw new ThinkParityException(nsax);
        }
    }

	/**
	 * Create a MD5Util [Singleton]
     * 
	 */
	private MD5Util() { super(); }
}
