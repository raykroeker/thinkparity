/*
 * Dec 19, 2003
 */
package com.thinkparity.codebase;

import java.io.IOException;
import java.io.InputStream;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

/**
 * <b>Title:</b>  StreamUtil
 * <br><b>Description:</b>  
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class StreamUtil {

	/**
	 * Handle to config.
	 */
	private static final Config STREAM_UTIL_CONFIG =
		ConfigFactory.newInstance(StreamUtil.class);
	
	/**
	 * Block size.
	 */
	private static final int BLOCK_SIZE =
		Integer.parseInt(STREAM_UTIL_CONFIG.getProperty("block.size"));

	/**
	 * Default characterset name.
	 */
	private static final String DEFAULT_CHARSET_NAME =
		STREAM_UTIL_CONFIG.getProperty("default.charset.name");

	/**
	 * Create a new StreamUtil [Singleton]
	 */
	private StreamUtil() {super();}

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

}
