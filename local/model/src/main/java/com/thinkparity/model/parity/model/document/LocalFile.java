/*
 * Nov 16, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.*;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.Constants.DirectoryNames;
import com.thinkparity.model.Constants.IO;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.MD5Util;

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

    /**
	 * System runtime environment.
	 */
	private static final Runtime myRuntime;

	/** Windows wrapper dll. */
	private static final String OPEN_PARAM_WIN32_DLL = "rundll32.exe";

	/**
     * The dll to call; and the api to execute. This will automatically provide
     * the open-with dialogue if no application is associated with the file.
     */
	private static final String OPEN_PARAM_WIN32_PROTOCOL = "shell32.dll,ShellExec_RunDLL";

	static {
		myRuntime = Runtime.getRuntime();
	}

	/**
	 * Apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * The file representing the document's content.
	 */
	private final File file;

	/**
	 * File content.
	 */
	private byte[] fileBytes;

	/**
	 * File content checksum.
	 */
	private String fileChecksum;

	/**
	 * Create a LocalFile.
	 * 
	 * @param document
	 *            The document the local file represents.
	 */
	LocalFile(final Workspace workspace, final Document document) {
		super();
        this.logger = Logger.getLogger(getClass());
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
        this.logger = Logger.getLogger(getClass());
		this.file = getFile(workspace, document, version);
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
	 * Obtain the file bytes.
	 * 
	 * @return The file bytes.
	 */
	byte[] getFileBytes() { return fileBytes; }

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
	void open() throws IOException {
		switch(OSUtil.getOS()) {
		case WINDOWS_2000:
		case WINDOWS_XP:
			openWin32();
			break;
		case LINUX:
		default:
			Assert.assertNotYetImplemented("launchFile(File)");
			break;
		}
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
		fileBytes = FileUtil.readBytes(file);
		fileChecksum = MD5Util.md5Hex(fileBytes);
	}

    /**
     * Rename the file.
     * 
     * @param filename
     *            The new name.
     */
	void rename(final String filename) {
        if (file.exists()) {
    	    Assert.assertTrue("[CANNOT RENAME LOCAL FILE]",
                    file.renameTo(new File(file.getParentFile(), filename)));
        }
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
		return new File(getFileParent(workspace, document.getId()), document.getName());
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
		// MyDocument.v1.doc
		final String name = new StringBuffer(FileUtil.getName(document.getName()))
			.append(Separator.Period)
			.append("v")
			.append(version.getVersionId())
			.append(FileUtil.getExtension(document.getName())).toString();
		return new File(getFileParent(workspace, version.getArtifactId()), name);
	}

    /**
	 * Obtain the parent file for the document.
	 * 
	 * @param document
	 *            The document.
	 * @return The parent file.
	 */
	private File getFileParent(final Workspace workspace, final Long documentId) {
		final File cache = new File(workspace.getDataDirectory(),
                DirectoryNames.Workspace.Data.LOCAL);
		if(!cache.exists()) {
			Assert.assertTrue("getFileParent(Document)", cache.mkdir());
		}
		final File parent = new File(cache, documentId.toString());
		if(!parent.exists()) {
			Assert.assertTrue("getFileParent(Document)", parent.mkdir());
		}
		return parent;
	}

	/**
	 * Open the file in a win32 environment.
	 * 
	 * @throws IOException
	 */
	private void openWin32() throws IOException {
		myRuntime.exec(new String[] {
				OPEN_PARAM_WIN32_DLL,
				OPEN_PARAM_WIN32_PROTOCOL,
				file.getAbsolutePath() });
	}
}
