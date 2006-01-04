/*
 * Nov 2, 2005
 */
package com.thinkparity.codebase;

import java.util.Vector;

import com.thinkparity.codebase.CompressionUtil.Level;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CompressionUtilTest extends CodebaseTestCase {

	private class CompressData {
		private final byte[] bytes;
		private final Level level;
		private CompressData(final byte[] bytes, final Level level) {
			this.bytes = new byte[bytes.length];
			System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
			this.level = level;
		}
	}

	private class DecompressData {
		private final byte[] bytes;
		private final byte[] compressedBytes;
		private DecompressData(final byte[] bytes, final byte[] compressedBytes) {
			this.bytes = new byte[bytes.length];
			System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
			this.compressedBytes = new byte[compressedBytes.length];
			System.arraycopy(compressedBytes, 0, this.compressedBytes, 0, compressedBytes.length);
		}
	}

	private Vector<CompressData> compressData;
	private Vector<DecompressData> decompressData;

	/**
	 * Create a CompressionUtilTest.
	 */
	public CompressionUtilTest() {
		super("Compression util test.");
	}

	public void testCompress() {
		try {
			byte[] compressedBytes;
			byte[] decompressedBytes;
			for(CompressData data : compressData) {
				compressedBytes = CompressionUtil.compress(data.bytes, data.level);
				CompressionUtilTest.assertNotNull(compressedBytes);

				decompressedBytes = CompressionUtil.decompress(compressedBytes);
				CompressionUtilTest.assertNotNull(decompressedBytes);
				CompressionUtilTest.assertEquals(data.bytes.length, decompressedBytes.length);
				for(int i = 0; i < data.bytes.length; i++) {
					CompressionUtilTest.assertEquals(data.bytes[i], decompressedBytes[i]);
				}
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	public void testDecompress() {
		try {
			byte[] decompressedBytes;
			for(DecompressData data : decompressData) {
				decompressedBytes = CompressionUtil.decompress(data.compressedBytes);
				CompressionUtilTest.assertNotNull(decompressedBytes);

				CompressionUtilTest.assertEquals(decompressedBytes.length, data.bytes.length);
				for(int i = 0; i < data.bytes.length; i++) {
					CompressionUtilTest.assertEquals(data.bytes[i], decompressedBytes[i]);
				}
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setUpCompress();
		setUpDecompress();
	}

	protected void setUpCompress() throws Exception {
		compressData = new Vector<CompressData>(getJUnitTestFilesSize());
		byte[] bytes;
		
		for(CodebaseTestFile testFile : getJUnitTestFiles()) {
			bytes = FileUtil.readBytes(testFile.getFile());

			compressData.add(new CompressData(bytes, Level.Nine));
		}
	}

	protected void setUpDecompress() throws Exception {
		decompressData = new Vector<DecompressData>(getJUnitTestFilesSize());
		byte[] bytes;
		byte[] compressedBytes;
		
		for(CodebaseTestFile testFile : getJUnitTestFiles()) {
			bytes = FileUtil.readBytes(testFile.getFile());
			compressedBytes = CompressionUtil.compress(bytes, Level.Nine);
			
			decompressData.add(new DecompressData(bytes, compressedBytes));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownCompress();
		tearDownDecompress();
	}

	protected void tearDownCompress() throws Exception {
		compressData.clear();
		compressData = null;
	}

	protected void tearDownDecompress() throws Exception {
		decompressData.clear();
		decompressData = null;
	}

}
