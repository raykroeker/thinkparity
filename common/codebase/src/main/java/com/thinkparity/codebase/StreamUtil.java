/*
 * Dec 19, 2003
 */
package com.thinkparity.codebase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

/**
 * Stream utility functions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class StreamUtil {
	
    /**
     * Copy an input stream to a byte channel.
     * 
     * @param is
     *            An <code>InputStream</code>.
     * @param bc
     *            A <code>ByteChannel</code>.
     * @param bufferSize
     *            A buffer size <code>Integer</code>.
     * @throws IOException
     */
    public static void copy(final InputStream is, final ByteChannel bc,
            final Integer bufferSize) throws IOException {
        final byte[] bytes = new byte[bufferSize];
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int bytesRead;
        while (-1 != (bytesRead = is.read(bytes))) {
            byteBuffer.position(0);
            byteBuffer.limit(bytesRead);
            bc.write(byteBuffer);
        }
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
		copy(is, os, 512);    // BUFFER 512B
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
