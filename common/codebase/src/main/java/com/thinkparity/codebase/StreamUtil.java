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
            final ByteBuffer buffer) throws IOException {
        int bytesRead;
        while (-1 != (bytesRead = is.read(buffer.array()))) {
            buffer.position(0);
            buffer.limit(bytesRead);
            bc.write(buffer);
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
	    // BUFFER - 512B - StreamUtil#copy(InputStream, OutputStream)
		copy(is, os, new byte[512]);
	}

	/**
	 * Copy bytes from an input stream to an output stream. Note that the
	 * streams are not closed upon completion.
	 * 
	 * @param is
	 *            The input to copy from.
	 * @param os
	 *            The output to copy to.
	 * @param buffer
	 *            A <code>ByteBuffer</code> to use.
	 * @throws IOException
	 */
	public static void copy(final InputStream is, final OutputStream os,
            final ByteBuffer buffer) throws IOException {
		if (!buffer.hasArray())
            throw new IllegalArgumentException("Buffer needs to be array based.");
        copy(is, os, buffer.array());
	}

    /**
     * Copy a stream from an input to an output using a pre-allocated buffer.
     * 
     * @param is
     *            An <code>InputStream</code>.
     * @param os
     *            An <code>OutputStream</code>.
     * @param buffer
     *            A buffer <code>byte[]</code>.
     * @throws IOException
     */
    private static void copy(final InputStream is, final OutputStream os,
            final byte[] buffer) throws IOException {
        int len;
        while((len = is.read(buffer)) > 0) {
            os.write(buffer, 0, len);
            os.flush();
        }
        os.flush();
    }

	/**
	 * Create a new StreamUtil [Singleton]
	 */
	private StreamUtil() {super();}
}
