/*
 * Dec 19, 2003
 */
package com.thinkparity.codebase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

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
	 * Handle to config.
	 * 
	 */
	private static final Config STREAM_UTIL_CONFIG;

	static {
		STREAM_UTIL_CONFIG = ConfigFactory.newInstance(StreamUtil.class);

		BLOCK_SIZE = Integer.parseInt(STREAM_UTIL_CONFIG.getProperty("block.size"));		
	}

	/**
	 * Copy the input stream to the output stream using a default buffer size of
	 * 512 bytes. Note that the streams are note closed upon completion.
	 * 
	 * @param is
	 *            The input stream.
	 * @param os
	 *            The output stream.
	 * @throws IOException
	 */
	public static void copy(final InputStream is, final OutputStream os)
			throws IOException {
		copy(is, os, 512);
	}

	/**
	 * Copy bytes from an input stream to an output stream. Note that the
	 * streams are not closed upon completion.
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
		final byte[] b = new byte[bufferSize];
		while((len = is.read(b)) > 0) {
			os.write(b, 0, len);
		}
		os.flush();
	}

	/**
     * Read the stream into a byte array using the charset name.
     * 
     * @param inputStream
     *            An input stream.
     * @return A byte array.
     * @throws IOException
     */
	public static synchronized byte[] read(final InputStream inputStream)
            throws IOException {
		if(null == inputStream) { return null; }

        final ByteBuffer byteBuffer = ByteBuffer.allocate(inputStream.available());
        final byte[] streamContentBuffer = new byte[BLOCK_SIZE];
        int numBytesRead = inputStream.read(streamContentBuffer);
        while(-1 != numBytesRead) {
            byteBuffer.put(streamContentBuffer, 0, numBytesRead);
            // re-read from the stream
            numBytesRead = inputStream.read(streamContentBuffer);
        }
        return byteBuffer.array();
	}

	/**
	 * Create a new StreamUtil [Singleton]
	 */
	private StreamUtil() {super();}
}
