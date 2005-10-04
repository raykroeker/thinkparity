/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.StringUtil;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.api.document.xml.DocumentXml;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.util.MD5Util;

/**
 * Document
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Document extends ParityObject {

	public enum Action { Send };

	private byte[] content;
	private String contentChecksum;
	private File directory;

	Document(final Document document) {
		super(document.getParent(), document.getName(), document
				.getDescription(), document.getCreatedOn(), document
				.getCreatedBy(), document.getKeyHolder(), document.getId());
		this.directory = document.getDirectory();
		this.content = document.getContent();
		this.contentChecksum = document.getContentChecksum();
	}

	/**
	 * Create a Document
	 */
	public Document(final Project parent, final String name,
			final Calendar createdOn, final String createdBy,
			final String keyHolder, final String description,
			final File directory, final UUID id, final byte[] content) {
		super(parent, name, description, createdOn, createdBy, keyHolder, id);
		this.directory = directory;
		this.content = content;
		this.contentChecksum = MD5Util.md5Hex(content);
	}

	/**
	 * Create a Document
	 */
	public Document(final String name,
			final Calendar createdOn, final String createdBy,
			final String keyHolder, final String description,
			final File directory, final UUID id, final byte[] content,
			final String contentChecksum) {
		super(null, name, description, createdOn, createdBy, keyHolder, id);
		this.directory = directory;
		this.content = content;
		this.contentChecksum = contentChecksum;
	}

	/**
	 * Set the value of content.
	 * @param content <code>byte[]</code>
	 */
	void setContent(byte[] content) { this.content = content; }

	/**
	 * Set the directory within which this document resides.
	 * @param project - the project within which this document will reside.
	 */
	void setDirectory(final Project project) {
		this.directory = project.getDirectory();
	}

	/**
	 * Add a version to this document.
	 * 
	 * @param documentVersion
	 *            <code>com.thinkparity.model.parity.api.document.DocumentVersion</code>
	 */
	public void add(final DocumentVersion documentVersion) {
		super.add(documentVersion);
	}

	/**
	 * Obtain content.
	 * @return <code>byte[]</code>
	 */
	public byte[] getContent() { return content; }

	public String getContentChecksum() { return contentChecksum; }

	public byte[] getContentChecksumBytes() throws UnsupportedEncodingException {
		return StringUtil.convertToBytes(contentChecksum);
	}

	/**
	 * @see com.thinkparity.model.parity.api.ParityObject#getDirectory()
	 */
	public File getDirectory() { return directory; }

	public String getDocumentAbsolutePath() {
		return new StringBuffer(directory.getAbsolutePath())
			.append(File.separator)
			.append(getName()).toString();
	}

	/**
	 * @see com.thinkparity.model.parity.api.ParityObject#getPath()
	 */
	public StringBuffer getPath() {
		return getParent().getPath().append("/").append(getCustomName());
	}

	/**
	 * @see com.thinkparity.model.parity.api.ParityObject#getType()
	 */
	public ParityObjectType getType() { return ParityObjectType.DOCUMENT; }

	/**
	 * @see com.thinkparity.codebase.log4j.Loggable#logMe()
	 */
	public StringBuffer logMe() { return DocumentXml.toXml(this); }

	public void setProject(final Project project) { setParent(project); }
}
