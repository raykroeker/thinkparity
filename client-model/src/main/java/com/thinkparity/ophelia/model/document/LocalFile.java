/*
 * Nov 16, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.Constants.IO;
import com.thinkparity.ophelia.model.util.MD5Util;
import com.thinkparity.ophelia.model.util.Opener;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The local file is an abstraction of the local file is the file on disk that
 * represent's a document or a document version's content that the user
 * interacts with. It is intended to be used only by the document model
 * implementation as a means to clean up some of its code.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
class LocalFile {

	/** An apache logger. */
	protected final Log4JWrapper logger;

	/** The local <code>File</code>. */
	private final File file;

    /** The file's content <code>byte[]</code>. */
	private byte[] fileBytes;

	/** The file content's checksum <code>String</code>. */
	private String fileChecksum;

	/** A document name generator. */ 
    private final DocumentNameGenerator nameGenerator;

	/**
	 * Create a LocalFile.
	 * 
	 * @param document
	 *            The document the local file represents.
	 */
	LocalFile(final Workspace workspace, final Document document) {
		super();
        this.logger = new Log4JWrapper();
        this.nameGenerator = new DocumentNameGenerator();
        this.file = getFile(workspace, document);
	}

	/**
	 * Create a LocalFile.
	 * 
	 * @param workspace
	 *            The workspace.
	 * @param version
	 *            The version.
	 */
	LocalFile(final Workspace workspace, final Document document,
			final DocumentVersion version) {
		super();
        this.logger = new Log4JWrapper();
        this.nameGenerator = new DocumentNameGenerator();
		this.file = getFile(workspace, document, version);
	}

	/**
     * Create a clone of the local file in the workspace's temp location.
     * 
     * @param workspace
     *            The thinkParity workspace.
     * @return A clone of the local file.
     * @throws IOException
     */
    File createTempClone(final Workspace workspace) throws IOException {
        final File tempFile = workspace.createTempFile(file.getName());
        final FileOutputStream outputStream = new FileOutputStream(tempFile);
        final InputStream inputStream = openStream();
        try {
            StreamUtil.copy(inputStream, outputStream, 1024);
            return tempFile;
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

	/**
	 * Delete the local file.
	 *
	 */
	void delete() {
		if(file.exists())
			Assert.assertTrue("delete()", file.delete());
	}

	/**
	 * Delete the local file's parent.
	 * 
	 */
	void deleteParent() {
		final File parent = file.getParentFile();
		if(parent.exists())
			Assert.assertTrue("deleteParent()", parent.delete());
	}

	/**
	 * Obtain the file checksum.
	 * 
	 * @return The file checksum.
	 */
	String getFileChecksum() { return fileChecksum; }

	/**
	 * Lock the file.
	 *
	 */
	void lock() { file.setReadOnly(); }

	/**
	 * Open the document local file.
	 * 
	 * @throws IOException
	 */
	void open(final Opener opener) {
        opener.open(file);
	}

	/**
     * Open the local file. Note that this stream is not buffered; and not
     * closed.
     * 
     * @return An input stream.
     * @throws FileNotFoundException
     */
    InputStream openStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

	/**
	 * Read the document local file into the bytes parameter. This will also
	 * calculate the content's checksum.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @see LocalFile#getFileBytes()
	 * @see LocalFile#getFileChecksum()
	 */
	void read() throws FileNotFoundException, IOException {
        if (file.exists()) {
            fileBytes = FileUtil.readBytes(file);
            fileChecksum = MD5Util.md5Hex(fileBytes);
        } else {
            fileBytes = null;
            fileChecksum = null;
        }
	}

    /**
     * Rename the file.
     * 
     * @param filename
     *            The new name.
     */
	void rename(final String filename) {
	    Assert.assertTrue("[CANNOT RENAME LOCAL FILE]",
                file.renameTo(new File(file.getParentFile(), filename)));
    }

    /**
	 * Write the document local file.
	 * 
	 * @param bytes
	 *            The bytes.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	void write(final byte[] bytes) throws FileNotFoundException, IOException {
		FileUtil.writeBytes(file, bytes);
	}

    /**
     * Write the input stream to the local file.
     * 
     * @param is
     *            The input stream containing new content.
     * @throws FileNotFoundException
     * @throws IOException
     */
    void write(final InputStream is) throws FileNotFoundException, IOException {
        final OutputStream os = createOutputStream();
        try { StreamUtil.copy(is, os, IO.BUFFER_SIZE); }
        finally { os.close(); }
    }

	/**
     * Create an output stream for the local file.
     * 
     * @return The output stream.
     * @throws FileNotFoundException
     */
    private OutputStream createOutputStream()
            throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(file));
    }

	/**
	 * Obtain the file for the document.
	 * 
	 * @param workspace
	 *            The workspace.
	 * @param document
	 *            The document.
	 * @return The file.
	 */
	private File getFile(final Workspace workspace, final Document document) {
        final String child = nameGenerator.fileName(document);
		return new File(getFileParent(workspace, document), child);
	}

	/**
	 * Obtain the file for the version.
	 * 
	 * @param workspace
	 *            The workspace.
	 * @param document
	 *            The document.
	 * @param version
	 *            The version.
	 * @return The file.
	 */
	private File getFile(final Workspace workspace, final Document document,
            final DocumentVersion version) {
        final String child = nameGenerator.localFileName(version);
		return new File(getFileParent(workspace, document), child);
	}

    /**
	 * Obtain the parent file for the document.
	 * 
	 * @param document
	 *            The document.
	 * @return The parent file.
	 */
	private File getFileParent(final Workspace workspace,
            final Document document) {
		final File cache = new File(workspace.getDataDirectory(),
                DirectoryNames.Workspace.Data.LOCAL);
		if(!cache.exists()) {
			Assert.assertTrue("getFileParent(Document)", cache.mkdir());
		}
		final File parent = new File(cache, nameGenerator.localDirectoryName(document));
		if(!parent.exists()) {
			Assert.assertTrue("getFileParent(Document)", parent.mkdir());
		}
		return parent;
	}
}
