/*
 * Dec 19, 2003
 */
package com.thinkparity.codebase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Stream utility functions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class StreamUtil {
	
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
            os.flush();
		}
		os.flush();
	}

	/**
	 * Create a new StreamUtil [Singleton]
	 */
	private StreamUtil() {super();}
}
