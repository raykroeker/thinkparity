/*
 * Dec 19, 2003
 */
package com.thinkparity.codebase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

/**
 * Stream utility functions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class StreamUtil {

	/**
	 * Block size.
	 * 
	 */
	private static final int BLOCK_SIZE;
	
	/**
	 * Default characterset name.
	 * 
	 */
	private static final String DEFAULT_CHARSET_NAME;

	/**
	 * Handle to config.
	 * 
	 */
	private static final Config STREAM_UTIL_CONFIG;

	static {
		STREAM_UTIL_CONFIG = ConfigFactory.newInstance(StreamUtil.class);

		BLOCK_SIZE = Integer.parseInt(STREAM_UTIL_CONFIG.getProperty("block.size"));		
		DEFAULT_CHARSET_NAME = STREAM_UTIL_CONFIG.getProperty("default.charset.name");
	}

	/**
	 * Copy the input stream to the output stream using a default buffer size of
	 * 512 bytes. The streams are note closed upon completion.
	 * 
	 * @param is
	 *            The input stream.
	 * @param os
	 *            The output stream.
	 */
	public static void copy(final InputStream is, final OutputStream os)
			throws IOException {
		copy(is, os, 512);
	}

	/**
	 * Copy bytes from an input stream to an output stream.
	 * 
	 * @param is
	 *            The input to copy from.
	 * @param os
	 *            The output to copy to.
	 * @param bufferSize
	 *            The size of the buffer to use when transferring.
	 * @throws IOException
	 */
	public static void copy(final InputStream is, final OutputStream os,
			final Integer bufferSize) throws IOException {
		int len;
		byte[] b = new byte[bufferSize];
		while((len = is.read(b)) > 0) {
			os.write(b, 0, len);
		}
	}

	/**
	 * Read the stream into a byte array using the default character set.
	 * @param inputStream <code>java.io.InputStream</code>
	 * @return <code>byte[]</code>
	 * @throws IOException
	 */
	public static synchronized byte[] read(
		InputStream inputStream) throws IOException {
		return read(inputStream, DEFAULT_CHARSET_NAME);
	}

	/**
	 * Read the stream into a byte array using the charset name.
	 * @param inputStream <code>java.io.InputStream</code>
	 * @param charsetName <code>java.lang.String</code>
	 * @return <code>byte[]</code>
	 * @throws IOException
	 */
	public static synchronized byte[] read(
		InputStream inputStream,
		String charsetName)
		throws IOException {
		if(null == inputStream)
			return null;
		byte[] stream = new byte[0];
		byte[] streamBytes = new byte[BLOCK_SIZE];
		byte[] tmpStream;
		int bytesRead = inputStream.read(streamBytes);
		int offset = 0;
		while(-1 != bytesRead) {
			/* Copy that which is already read to a temp location. */
			tmpStream = new byte[stream.length];
			System.arraycopy(stream, 0, tmpStream, 0, stream.length);
			/* Enlarge and copy that in the temp location back. */
			stream = new byte[tmpStream.length + bytesRead];
			System.arraycopy(tmpStream, 0, stream, 0, tmpStream.length);
			/* Copy that which was just read. */
			System.arraycopy(streamBytes, 0, stream, offset, bytesRead);
			bytesRead = inputStream.read(streamBytes);
			offset += bytesRead;
		}
		return stream;
	}

	/**
	 * Create a new StreamUtil [Singleton]
	 */
	private StreamUtil() {super();}

}
