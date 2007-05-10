/*
 * Sep 14, 2003
 */
package com.thinkparity.codebase;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.text.MessageFormat;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;

/**
 * File util contains io utilities as well as commonly used file routines.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2.2.9
 */
public abstract class FileUtil {

    /**
	 * Copy a file.
	 * 
	 * @param file
	 *            The file to copy.
	 * @param target
	 *            The target to copy the file to.
	 */
	public static void copy(final File file, final File target,
            final ByteBuffer buffer) throws FileNotFoundException, IOException {
		final InputStream input = new FileInputStream(file);
		Assert.assertTrue("copy(File,File)", target.createNewFile());
		final OutputStream output = new FileOutputStream(target);
		try {
            StreamUtil.copy(input, output, buffer);
		} finally {
			try {
                input.close();
            } finally {
                output.close();
            }
		}
	}

    /**
     * Copy a source file to a target file.
     * 
     * @param source
     *            The source <code>File</code>.
     * @param target
     *            The target <code>File</code>.
     * @param nice
     *            Whether or not to try incrementing names if the target exists.
     * @param niceMessage
     *            A message format to use when constructing a file name. The
     *            arguments will be
     *            <ol start="0">
     *            <li>name
     *            <li>index
     *            <li>extension
     *            </ol>
     */
    public static void copy(final File source, final File target,
            final ByteBuffer buffer, final Boolean nice,
            final String niceMessage) throws FileNotFoundException, IOException {
        final File realTarget;
        if (Boolean.TRUE == nice) {
            File niceTarget = target;
            final String name = getName(target);
            final String extension = getExtension(target);
            int index = 1;
            while (niceTarget.exists()) {
                niceTarget = new File(target.getParentFile(),
                        MessageFormat.format(niceMessage,
                                name, index++, extension));
            }
            realTarget = niceTarget;
        } else {
            realTarget = target;
        }
        copy(source, realTarget, buffer);
    }
	/**
	 * Delete a filesystem tree.
	 * 
	 * @param directory
	 *            The root directory of the tree.
	 * @throws NullPointerException
	 *             If directory is null.
	 * @throws IllegalArgumentException
	 *             If directory doesn't exist; is not a directory; cannot be
	 *             read from; or written to.
	 */
	public static void deleteTree(final File directory) {
		if(null == directory) { throw new NullPointerException(); }
		if(!(directory.exists() && directory.isDirectory() &&
				directory.canRead() && directory.canWrite())) {
			throw new IllegalArgumentException();
		}
		// grab all files/directories
		final File[] files = directory.listFiles();
		for(int i = 0; i < files.length; i++) {
			if(!files[i].isDirectory()) {
				Assert.assertTrue(
						"Could not delete file:  " + files[i].getAbsolutePath(),
						files[i].delete());
			}
			else { FileUtil.deleteTree(files[i]); }
		}
		Assert.assertTrue(
				"Could not delete directory:  " + directory.getAbsolutePath(),
				directory.delete());
	}
	
    /**
     * Format a file size.
     * 
     * @param bytes
     *            A number of bytes.
     * @return A formatted size <code>String</code>.
     */
    public static String formatSize(final Long bytes) {
        return new BytesFormat().format(bytes);
    }
    
	/**
	 * Obtain the extension portion of a file name. If none exists; the full
	 * name is returned.
	 * 
	 * @param file
	 *            The file.
	 * @return The file name's extension.
	 * @see FileUtil#getExtension(String)
	 */
	public static String getExtension(final File file) {
		if(null == file) { throw new NullPointerException(); }
		return FileUtil.getExtension(file.getName());
	}

	/**
	 * Obtain the extension portion of a file name. If none exists; an empty
     * string is returned.
	 * 
	 * @param fileName
	 *            The file name.
	 * @return The file name's extension.
	 */	
	public static String getExtension(String fileName) {
		if(-1 != fileName.indexOf(Separator.Period.toString()))
			return fileName.substring(
				fileName.lastIndexOf(Separator.Period.toString()));
		return "";
	}

	/**
	 * Obtain the file name without its extension, of the File. If there is not
	 * extension the entire name is returned.
	 * 
	 * @param file
	 *            The file.
	 * @return The name portion of the file name.
	 */
	public static String getName(final File file) {
		if(null == file) { throw new NullPointerException(); }
		return getName(file.getName());
	}

	/**
	 * Obtain the name portion of the file name.
	 * 
	 * @param fileName
	 *            The file name.
	 * @return The name portion of the file name.
	 */
	public static String getName(final String fileName) {
		if(-1 != fileName.indexOf(Separator.Period.toString()))
			return fileName.substring(
					0, fileName.lastIndexOf(Separator.Period.toString()));
		return fileName;
	}
	/**
     * Obtain the relative path of the file within the root using
     * <code>File.separatorChar</code> as the separator.
     * 
     * @param root
     *            A root directory <code>File</code>.
     * @param file
     *            A <code>File</code>.
     * @return A relative path <code>String</code>.
     */
    public static String getRelativePath(final File root, final File file) {
        return getRelativePath(root, file, File.separatorChar);
    }

	/**
     * Obtain the relative path of the file within the root.
     * 
     * @param root
     *            A root directory <code>File</code>.
     * @param file
     *            A <code>File</code>.
     * @param separatorChar
     *            The separator character to use in the relative path.
     * @return A relative path <code>String</code>.
     */
    public static String getRelativePath(final File root, final File file,
            final char separatorChar) {
        if (null == root)
            throw new NullPointerException("Root cannot be null.");
        if (null == file)
            throw new NullPointerException("File cannot be null.");
        if (!root.isDirectory())
            throw new IllegalArgumentException("Root must be a directory.");
        final String rootPath = root.getAbsolutePath();
        final String filePath = file.getAbsolutePath();
        if (!filePath.startsWith(rootPath))
            throw new IllegalArgumentException(MessageFormat.format(
                    "Path {0} does not contain {1}.", filePath, rootPath));
        String relativePath = filePath.replace(rootPath, "");
        relativePath = relativePath.replace(File.separatorChar, separatorChar);
        return relativePath;
    }

    /**
     * Test a potential directory name for validity.
     *
     * @param name
     *      A directory name.
     * @return True if a directory can be created with the name.
     */
    public static Boolean isDirectoryNameValid(final String name) {
        final File tempDir = new File(System.getProperty("java.io.tmpdir"));
        final File testDirectory = new File(tempDir, name);
        Boolean didCreate = Boolean.FALSE;
        didCreate = testDirectory.mkdir();
        if(didCreate) { testDirectory.delete(); }
        return didCreate;
    }

    /**
     * Test a potential file name for validity.
     *
     * @param name
     *      A file name.
     * @return True if a file can be created with the name.
     */
    public static Boolean isFileNameValid(final String name) {
        final File tempDir = new File(System.getProperty("java.io.tmpdir"));
        final File testFile = new File(tempDir, name);
        Boolean didCreate = Boolean.FALSE;
        try { didCreate = testFile.createNewFile(); }
        catch(final IOException iox) { didCreate = Boolean.FALSE; }
        if(didCreate) { testFile.delete(); }
        return didCreate;
    }

	/**
     * Modfiy the last update date of a directory.
     * 
     * @param file
     *            A file.
     */
    public static void touch(final File file) {
        if(null == file) { throw new NullPointerException(); }
        if(!(file.exists() &&  file.canRead() && file.canWrite())) {
            throw new IllegalArgumentException();
        }
        // TIME - This a local timestamp.
        file.setLastModified(System.currentTimeMillis());
    }

    /**
     * Write a byte channel to a file.
     * 
     * @param byteChannel
     *            A <code>ByteChannel</code>.
     * @param file
     *            A <code>File</code>.
     * @param bufferSize
     *            The size of the buffer to use when writing the file.
     * @throws IOException
     */
    public static void write(final ReadableByteChannel readChannel,
            final WritableByteChannel writeChannel, final ByteBuffer buffer)
            throws IOException {
        if (null == readChannel || null == writeChannel || null == buffer)
            throw new NullPointerException();
        if (!readChannel.isOpen())
            throw new IllegalArgumentException("Read channel is not open.");
        if (!writeChannel.isOpen())
            throw new IllegalArgumentException("Write channel is not open.");
        int read;
        while (true) {
            read = readChannel.read(buffer);
            if (-1 == read) {
                break;
            }
            buffer.flip();
            buffer.limit(read);
            while (buffer.hasRemaining()) {
                writeChannel.write(buffer);
            }
        }
    }

    /**
     * Write a stream to a file.
     * 
     * @param input
     *            An <code>InputStream</code>.
     * @param file
     *            A <code>File</code>.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(final InputStream input, final File file)
            throws FileNotFoundException, IOException {
        if (null == file)
            throw new NullPointerException();
        final FileOutputStream output = new FileOutputStream(file);
        try {
            StreamUtil.copy(input, output);
        } finally {
            output.flush();
            output.close();
        }
    }

    /**
     * Create FileUtil.
     *
     */
	private FileUtil() { super(); }
}
