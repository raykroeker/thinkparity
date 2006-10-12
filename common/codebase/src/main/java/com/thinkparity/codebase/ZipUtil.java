/*
 * Jan 31, 2006
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class ZipUtil {

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
		Assert.assertNotTrue(zipFile.exists(),
                "Zip file ''{0}'' already exists.", zipFile);
		Assert.assertTrue(zipFile.createNewFile(),
                "Zip file ''{0}'' could not be created.", zipFile);
		final ZipOutputStream zipStream =
			new ZipOutputStream(new FileOutputStream(zipFile));
        zipStream.setMethod(ZipOutputStream.DEFLATED);
        zipStream.setLevel(9);
		final File[] files = new FileSystem(inputDirectory).listFiles("/", Boolean.TRUE);
		try {
		    FileInputStream fileStream = null;
			for (final File file : files) {
                // dont' add the zipFile
                if (file.getAbsolutePath().equals(zipFile.getAbsolutePath()))
                    break;

                zipStream.putNextEntry(new ZipEntry(resolveName(inputDirectory, file)));
                fileStream  = new FileInputStream(file);
				try {
                    StreamUtil.copy(fileStream, zipStream);
				} finally {
                    fileStream.close();
				}
                zipStream.closeEntry();
			}
            zipStream.flush();
		} finally {
            zipStream.close();
		}
	}

    /**
     * Resolve the zip entry name.
     * 
     * @param zipRoot
     *            The root of the zip archive.
     * @param zipEntry
     *            The zip archive entry.
     * @return The zip entry name.
     */
    private static String resolveName(final File zipRoot, final File zipEntry) {
        final String relativePath = StringUtil.searchAndReplace(zipEntry.getAbsolutePath(),
                zipRoot.getAbsolutePath(), "").toString();
        if (relativePath.startsWith(File.separator)) {
            return relativePath.substring(1);
        } else {
            return relativePath;
        }
    }

	/**
	 * Create a ZipUtil [Singleton]
	 * 
	 */
	private ZipUtil() { super(); }
}
