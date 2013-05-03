/*
 * Jun 19, 2005
 */
package com.thinkparity.codebase.codec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.thinkparity.common.StringUtil;


import org.apache.commons.codec.binary.Base64;

/**
 * <b>Title:</b>thinkParity MD5 Util<br>
 * <b>Description:</b>Calculate an MD5 checksum from a variety of sources.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MD5Util {

    /**
     * Calculates the md5 digest and returns the value un-encoded.
     * 
     * @param data
     *            The data <code>byte[]</code>.
     * @return A digest <code>byte[]</code>.
     */
	public static byte[] md5(final byte[] data) {
        final MessageDigest digest = getDigest();
        digest.update(data);
        return digest.digest();
	}

    /**
     * Create an MD5 base 64 encoded checksum of a byte channel.
     * 
     * @param byteChannel
     *            A <code>ByteChannel</code>.
     * @param bufferSize
     *            A buffer size <code>Integer</code> to use.
     * @return An MD5 base 64 encoded checksum.
     * @throws IOException
     */
    public static String md5Base64(final ReadableByteChannel channel,
            final byte[] buffer) throws IOException {
        final MessageDigest digest = getDigest();
        final ByteBuffer bb = ByteBuffer.wrap(buffer);
        int read;
        while (true) {
            bb.clear();
            read = channel.read(bb);
            if (-1 == read) {
                break;
            }
            bb.flip();
            bb.limit(read);
            digest.update(buffer, 0, read);
        }
        return encode(digest.digest());
    }

    /**
     * Create an MD5 base 64 encoded checksum of string data.
     * 
     * @param data
     *            A <code>String</code>.
     * @return An MD5 base 64 encoded checksum.
     */
    public static String md5Base64(final String data) {
        final MessageDigest digest = getDigest();
        final byte[] bytes = data.getBytes();
        final int bytesRead = bytes.length;
        digest.update(bytes, 0, bytesRead);
        return encode(digest.digest());
    }

    /**
     * Encode the checksum bytes using a base 64 encoding.
     * 
     * @param bytes
     *            A checksum <code>byte[]</code>.
     * @return A <code>String</code>.
     */
    private static String encode(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes), getCharset());
    }

    /**
     * Obtain the string character set (encoding).
     * 
     * @return A <code>Charset</code>.
     */
    private static Charset getCharset() {
        return StringUtil.Charset.UTF_8.getCharset();
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
