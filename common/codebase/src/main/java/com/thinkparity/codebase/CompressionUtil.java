/*
 * May 30, 2005
 */
package com.thinkparity.codebase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Compression utility class.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class CompressionUtil {

	/**
	 * Enumeration of the various levels of compression available.
	 */
	public enum Level {
		Eight(8), Five(5), Four(4), Nine(9), One(1), Seven(7), Six(6), Three(3), Two(2);

		/**
		 * Integer representation of the compression level.
		 */
		private Integer level;

		/**
		 * Create a compression Level
		 * 
		 * @param level
		 *            The compression level.
		 */
		Level(final Integer level) { this.level = level; }

		/**
		 * Obtain the compression level.
		 * 
		 * @return The compression level.
		 */
		public Integer getLevel() { return level; }
	}

	/**
	 * Default buffer size to use.
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * Compress bytes using the specified level.
	 * 
	 * @param bytes
	 *            The bytes to compress.
	 * @param level
	 *            The level of compression to use.
	 * @return A compressed representation of the bytes.
	 * @throws IOException
	 */
	public static synchronized byte[] compress(final byte[] bytes,
			final Level level) throws IOException {
		final Deflater deflater = new Deflater();
		deflater.setLevel(level.getLevel());
		deflater.setInput(bytes);
		deflater.finish();
		final ByteArrayOutputStream byteOutput =
			new ByteArrayOutputStream(bytes.length);
		byte[] byteBuffer = new byte[BUFFER_SIZE];
	    while(!deflater.finished()) {
	        int count = deflater.deflate(byteBuffer);
	        byteOutput.write(byteBuffer, 0, count);
	    }
	    byteOutput.close();
	    return byteOutput.toByteArray();
	}

	/**
	 * Decompress a sequence of bytes.
	 * 
	 * @param bytes
	 *            A sequence of bytes to decompress.
	 * @return The decompressed bytes.
	 * @throws DataFormatException
	 * @throws IOException
	 */
	public static synchronized byte[] decompress(final byte[] bytes)
			throws DataFormatException, IOException {
		final Inflater inflater = new Inflater();
		inflater.setInput(bytes);
		final ByteArrayOutputStream byteOutput =
			new ByteArrayOutputStream(bytes.length);
		byte[] byteBuffer = new byte[1024];
	    while(!inflater.finished()) {
	    	int count = inflater.inflate(byteBuffer);
	    	byteOutput.write(byteBuffer, 0, count);
	    }
	    byteOutput.close();
	    return byteOutput.toByteArray();
	}

	/**
	 * Create a CompressionUtil [Singleton]
	 */
	private CompressionUtil() { super(); }

}
