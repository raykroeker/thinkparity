/*
 * Nov 16, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.MD5Util;

/**
 * The document local file is an abstraction of the local file is the file on
 * disk that represent's a document's content that the user interacts with. It
 * is intended to be used only by the document model implementation as a means
 * to clean up some of its code.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
class DocumentLocalFile {

	/**
	 * System runtime environment.
	 */
	private static final Runtime myRuntime;

	/**
	 * Windows executable to use when opening a file.
	 */
	private static final String OPEN_PARAM_WIN32_DLL = "rundll32.exe";

	/**
	 * Windows file path parameter name to use when opening a file.
	 */
	private static final String OPEN_PARAM_WIN32_PROTOCOL = "url.dll,FileProtocolHandler";

	static {
		myRuntime = Runtime.getRuntime();
	}

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
	DocumentLocalFile(final Workspace workspace, final Document document) {
		super();
		this.file = getFile(workspace, document);
	}

	/**
	 * Delete the document local file.
	 *
	 */
	void delete() {
		if(file.exists())
			Assert.assertTrue("delete()", file.delete());
		final File parent = file.getParentFile();
		if(parent.exists())
			Assert.assertTrue("delete()", parent.delete());
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
	 * Read the document local file.
	 * 
	 * @return The bytes.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	void read() throws FileNotFoundException, IOException {
		fileBytes = FileUtil.readFile(file);
		fileChecksum = MD5Util.md5Hex(fileBytes);
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
		FileUtil.writeFile(file, bytes);
	}

	/**
	 * Obtain the file for the document.
	 * @param document The document.
	 * @return The file.
	 */
	private File getFile(final Workspace workspace, final Document document) {
		return new File(getFileParent(workspace, document), document.getCustomName());
	}

	/**
	 * Obtain the parent file for the document.
	 * 
	 * @param document
	 *            The document.
	 * @return The parent file.
	 */
	private File getFileParent(final Workspace workspace, final Document document) {
		final File cache = new File(
				workspace.getDataURL().getFile(),
				IParityModelConstants.DIRECTORY_NAME_CACHE_DATA);
		if(!cache.exists()) {
			Assert.assertTrue("getFileParent(Document)", cache.mkdir());
		}
		final File parent = new File(cache, document.getId().toString());
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
