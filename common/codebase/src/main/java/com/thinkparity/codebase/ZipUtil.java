/*
 * Jan 31, 2006
 */
package com.thinkparity.codebase;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ZipUtil {

	/**
	 * Assertion statement for the zip file creation.
	 * 
	 */
	private static final String ASSERT_NOT_ZIP_FILE_EXISTS;

	/**
	 * Assertion statement for the zip file creation.
	 * 
	 */
	private static final String ASSERT_ZIP_FILE_CREATE;

	static {
		ASSERT_NOT_ZIP_FILE_EXISTS = new StringBuffer("[")
			.append(Version.getName())
			.append("] Zip file ''{0}'' already exists.").toString();

		ASSERT_ZIP_FILE_CREATE = new StringBuffer("[")
			.append(Version.getName())
			.append("] Zip file ''{0}'' could not be created.").toString();
	}

	/**
	 * Create a zip file.
	 * 
	 * @param zipFile
	 *            The zip file to create.
	 * @param inputDirectory
	 *            The input directory of the zip file. All files will be
	 *            included in the zip file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void createZipFile(final File zipFile,
			final File inputDirectory) throws FileNotFoundException,
			IOException {
		Assert.assertNotTrue(
				formatAssertion(
						ASSERT_NOT_ZIP_FILE_EXISTS,
						new String[] {zipFile.getAbsolutePath()}),
						zipFile.exists());
		Assert.assertTrue(
				formatAssertion(
						ASSERT_ZIP_FILE_CREATE,
						new String[] {zipFile.getAbsolutePath()}),
						zipFile.createNewFile());
		final ZipOutputStream zipOutputStream =
			new ZipOutputStream(new FileOutputStream(zipFile));
		zipOutputStream.setLevel(9);
		final File[] inputFiles = inputDirectory.listFiles();
		BufferedInputStream bis = null;
		try {
			for(File inputFile : inputFiles) {
				zipOutputStream.putNextEntry(new ZipEntry(inputFile.getName()));

				bis =
					new BufferedInputStream(new FileInputStream(inputFile), 512);
				try { StreamUtil.copy(bis, zipOutputStream, 512); }
				finally { bis.close(); }

				zipOutputStream.closeEntry();
			}
			zipOutputStream.flush();
		}
		finally { zipOutputStream.close(); }
	}

	/**
	 * Format the assertion statement.
	 * 
	 * @param pattern
	 *            The assertion statement pattern.
	 * @param arguments
	 *            The assertion statement arguments.
	 * @return The formatted assertion statement.
	 */
	private static String formatAssertion(final String pattern,
			final Object[] arguments) {
		return MessageFormat.format(pattern, arguments);
	}

	/**
	 * Create a ZipUtil [Singleton]
	 * 
	 */
	private ZipUtil() { super(); }
}
