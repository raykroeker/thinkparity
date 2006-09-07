/*
 * Mar 3, 2006
 */
package com.thinkparity.model.parity.model.io.pdf.fop.handler;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.ZipUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.Constants.DirectoryNames;
import com.thinkparity.model.artifact.ArtifactVersion;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class AbstractIOHandler {

	private static final String tempDirectoryNamePattern;

	private static final String zipFilenamePattern;

	static {
		tempDirectoryNamePattern = "{0}.{1}";
		zipFilenamePattern = "{0} [{1,date,MMM dd yyyy hh mm ss a}].zip";
	}

	/**
	 * The output directory of the pdfs.
	 * 
	 */
	private final File outputDirectory;

	/**
	 * Create a AbstractIOHandler.
	 * 
	 * @param outputDirectory
	 *            The pdf output directory.
	 */
	protected AbstractIOHandler(final File outputDirectory) {
		super();
		this.outputDirectory = outputDirectory;
	}

	protected void copyToTemp(final File tempDirectory, final String outputFilename, final byte[] bytes) {
		try {
			FileUtil.writeBytes(new File(tempDirectory, outputFilename), bytes);
		}
		catch(final IOException iox) { throw new FOPException(iox); }
	}

	protected File createTempDirectory() {
		final File tempRoot = new File(System.getProperty("java.io.tmpdir"));
		final String tempDirectoryName =
			MessageFormat.format(tempDirectoryNamePattern,
					new Object[] {
						DirectoryNames.Workspace.TEMP,
						System.currentTimeMillis() + ""});
		final File tempDirectory = new File(tempRoot, tempDirectoryName);
		Assert.assertTrue("Could not create temp directory.", tempDirectory.mkdir());
		return tempDirectory;
	}

	protected String createVersionName(final ArtifactVersion artifactVersion) {
		return new StringBuffer(FileUtil.getName(artifactVersion.getName()))
			.append(Separator.Period).append("v")
			.append(artifactVersion.getVersionId())
			.append(FileUtil.getExtension(artifactVersion.getName()))
			.toString();
	}

	protected void deleteTempDirectory(final File tempDirectory) {
		FileUtil.deleteTree(tempDirectory);
	}

	protected File zip(final File inputDirectory, final String zipName,
			final Calendar zipDateTime) {
		final String zipFilename = MessageFormat.format(
				zipFilenamePattern,
				new Object[] {zipName, zipDateTime.getTime()});
		final File zipFile = new File(outputDirectory, zipFilename);
		try { ZipUtil.createZipFile(zipFile, inputDirectory); }
		catch(final IOException iox) { throw new FOPException(iox); }
		return zipFile;
	}
}
