/*
 * May 30, 2005
 */
package com.thinkparity.codebase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * CompressionUtil
 * @author raykroeker@gmail.com
 *
 */
public class CompressionUtil {

	public enum Level {
		One(1), Two(2), Three(3),
		Four(4), Five(5), Six(6),
		Seven(7), Eight(8), Nine(9);

		/**
		 * Integer representation of the level.
		 */
		private Integer level;

		/**
		 * Create a Level
		 * @param level <code>java.lang.Integer</code>
		 */
		Level(final Integer level) { this.level = level; }

		/**
		 * Obtain level.
		 * @return <code>Integer</code>
		 */
		public Integer getLevel() { return level; }
		
	}

	/**
	 * Compress bytes using the maximum compression possible.
	 * @param bytes <code>byte[]</code>
	 * @return <code>byte[]</code>
	 * @throws IOException
	 */
	public static synchronized byte[] compress(final byte[] bytes,
			final Level level) throws IOException {
		final ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		final Deflater deflater = new Deflater(level.getLevel());
		final DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(
				bytesOut, deflater, bytes.length);
		deflaterOutputStream.write(bytes);
		deflaterOutputStream.finish();
		return bytesOut.toByteArray();
	}

	/**
	 * Decompress bytes.
	 * @param bytes <code>byte[]</code>
	 * @return <code>byte[]</code>
	 * @throws IOException
	 */
	public static synchronized byte[] decompress(final byte[] bytes)
			throws IOException {
		final ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
		final Inflater inflater = new Inflater();
		final InflaterInputStream inflaterInputStream = new InflaterInputStream(
				bytesIn, inflater, bytes.length);
		byte[] decompressedContent = new byte[0];
		byte[] inflatedBytes = new byte[1024];
		byte[] tempoararyBytes;
		int bytesRead = inflaterInputStream.read(inflatedBytes);
		int offset = 0;
		while (-1 != bytesRead) {
			/* Copy that which is already read to a temp location. */
			tempoararyBytes = new byte[decompressedContent.length];
			System.arraycopy(decompressedContent, 0, tempoararyBytes, 0,
					decompressedContent.length);
			/* Enlarge and copy that in the temp location back. */
			decompressedContent = new byte[tempoararyBytes.length + bytesRead];
			System.arraycopy(tempoararyBytes, 0, decompressedContent, 0,
					tempoararyBytes.length);
			/* Copy that which was just read. */
			System.arraycopy(inflatedBytes, 0, decompressedContent, offset,
					bytesRead);
			bytesRead = inflaterInputStream.read(inflatedBytes);
			offset += bytesRead;
		}
		return decompressedContent;
	}

	/**
	 * Create a CompressionUtil [Singleton]
	 */
	private CompressionUtil() { super(); }

}
