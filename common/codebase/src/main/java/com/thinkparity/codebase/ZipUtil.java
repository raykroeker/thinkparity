/*
 * Jan 31, 2006
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity Zip Util<br>
 * <b>Description:</b>Zip file creation/extraction utility methods.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ZipUtil {

    /** The stream buffer size used by add file. */
    private static final int ADD_FILE_BUFFER = 1024;

    /** The stream buffer size used by extract file. */
    private static final int EXTRACT_FILE_BUFFER = 1024;

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
        if (null == zipFile)
            throw new NullPointerException("zipFile cannot be null.");
        if (zipFile.exists())
            throw new IllegalArgumentException(
                    MessageFormat.format("{0} cannot exist.", zipFile.getAbsolutePath()));
        if (null == inputDirectory)
            throw new NullPointerException("inputDirectory cannot be null.");
        if (!inputDirectory.isDirectory())
            throw new IllegalArgumentException(
                    MessageFormat.format("{0} must be a directory.", inputDirectory.getAbsolutePath()));
		Assert.assertTrue(zipFile.createNewFile(),
                "Zip file ''{0}'' could not be created.", zipFile);
		final ZipOutputStream zipStream =
			new ZipOutputStream(new FileOutputStream(zipFile));
        zipStream.setMethod(ZipOutputStream.DEFLATED);
        zipStream.setLevel(9);
        /* NOTE zip specifically cannot contain directories; only files and
         * their paths */
		final File[] files = new FileSystem(inputDirectory).listFiles("/", Boolean.TRUE);
		try {
			for (final File file : files) {
                // dont' add the zipFile
                if (file.getAbsolutePath().equals(zipFile.getAbsolutePath()))
                    continue;

                addFile(inputDirectory, file, zipStream);
			}
            zipStream.flush();
		} finally {
            zipStream.close();
		}
	}

    /**
     * Extract a zip file.
     * 
     * @param zipFile
     *            The zip <code>File</code> to extract.
     * @param outputDirectory
     *            An output directory <code>File</code>.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void extractZipFile(final File zipFile,
            final File outputDirectory) throws FileNotFoundException,
            IOException {
        if (null == zipFile)
            throw new NullPointerException("zipFile cannot be null.");
        if (!zipFile.isFile())
            throw new IllegalArgumentException(MessageFormat.format(
                    "{0} is not a file.", zipFile.getAbsolutePath()));
        if (null == outputDirectory)
            throw new NullPointerException("outputDirectory cannot be null.");
        if (!outputDirectory.isDirectory())
            throw new IllegalArgumentException(MessageFormat.format(
                    "{0} is not a directory.", outputDirectory.getAbsolutePath()));
        final ZipFile zip = new ZipFile(zipFile);
        try {
            final Enumeration<? extends ZipEntry> entries = zip.entries();
            ZipEntry zipEntry;
            while (entries.hasMoreElements()) {
                zipEntry = entries.nextElement();
                if (zipEntry.isDirectory())
                    createDirectory(outputDirectory, zipEntry);
                else
                    extractFile(outputDirectory, zip, zipEntry);
            }
        } finally {
            zip.close();
        }
    }

	/**
     * Add a file to a zip output stream.
     * 
     * @param inputDirectory
     *            An input directory <code>File</code>.
     * @param file
     *            An input <code>File</code>.
     * @param zipOutputStream
     *            A <code>ZipOutputStream</code>.
     * @throws IOException
     */
    private static void addFile(final File inputDirectory, final File file,
            final ZipOutputStream zipOutputStream) throws IOException {
        zipOutputStream.putNextEntry(new ZipEntry(resolveName(inputDirectory, file)));
        try {
            final InputStream fileStream  = new FileInputStream(file);
            try {
                StreamUtil.copy(fileStream, zipOutputStream, ADD_FILE_BUFFER);
            } finally {
                fileStream.close();
            }
        } finally {
            zipOutputStream.closeEntry();
        }
    }

    /**
     * Create a directory for a zip entry.
     * 
     * @param outputDirectory
     *            An output directory <code>File</code>.
     * @param zipEntry
     *            A <code>ZipEntry</code>.
     */
    private static void createDirectory(final File outputDirectory,
            final ZipEntry zipEntry) {
        final File directory = new File(outputDirectory, zipEntry.getName());
        Assert.assertTrue(directory.mkdirs(),
                "Could not create directory for zip entry:  {0}.",
                zipEntry.getName());
    }

	/**
     * Extact a file for a zip entry.
     * 
     * @param outputDirectory
     *            An output directory <code>File</code>.
     * @param zipEntry
     *            A <code>ZipEntry</code>.
     */
    private static void extractFile(final File outputDirectory,
            final ZipFile zipFile, ZipEntry zipEntry) throws IOException {
        final InputStream zipEntryStream = zipFile.getInputStream(zipEntry);
        try {
            final File file = new File(outputDirectory, zipEntry.getName());
            final File parentFile = file.getParentFile();
            if (!parentFile.exists())
                Assert.assertTrue(parentFile.mkdirs(),
                        "Could not create parent file:  {0}",
                        parentFile.getAbsolutePath());
            final OutputStream fileStream = new FileOutputStream(file);
            try {
                StreamUtil.copy(zipEntryStream, fileStream, EXTRACT_FILE_BUFFER);
            } finally {
                fileStream.close();
            }
        } finally {
            zipEntryStream.close();
        }
    }

    /**
     * Resolve the zip entry name. In a win32 environment the zip entries must
     * not contain a leading slash. The java zip interface also states that in
     * order for a zip entry to be considered a directory it must end with the
     * '/' character.
     * 
     * @param zipRoot
     *            The root of the zip archive.
     * @param zipEntry
     *            The zip archive entry.
     * @return The zip entry name.
     * @see ZipEntry#isDirectory()
     */
    private static String resolveName(final File zipRoot, final File zipEntry) {
        final StringBuffer relativePath = StringUtil.searchAndReplace(
                zipEntry.getAbsolutePath(), zipRoot.getAbsolutePath(), "");
        if (relativePath.substring(0, 1).startsWith(File.separator)) {
            relativePath.replace(0, 1, "");
        }
        if (zipEntry.isDirectory())
            relativePath.append('/');
        return relativePath.toString();
    }

	/**
	 * Create a ZipUtil [Singleton]
	 * 
	 */
	private ZipUtil() { super(); }
}
