/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.util.MD5Util;

/**
 * Document
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Document extends ParityObject {

	/**
	 * The document content.
	 */
	private byte[] content;

	/**
	 * The document content's checksum.
	 */
	private String contentChecksum;

	/**
	 * Create a Document.
	 * 
	 * @param name
	 *            The document name.
	 * @param createdOn
	 *            The document creation date.
	 * @param createdBy
	 *            The document creator.
	 * @param description
	 *            The document description.
	 * @param id
	 *            The document id.
	 * @param content
	 *            The document content.
	 * @param contentChecksum
	 *            The document content's checksum.
	 */
	public Document(final String name, final Calendar createdOn,
			final String createdBy, final String description, final UUID id,
			final byte[] content, final String contentChecksum) {
		this(null, name, createdOn,createdBy, description, id, content);
	}

	/**
	 * Create a Document.
	 * 
	 * @param parent
	 *            The parent project.
	 * @param name
	 *            The document name.
	 * @param createdOn
	 *            The document creation date.
	 * @param createdBy
	 *            The document creator.
	 * @param description
	 *            The document description.
	 * @param id
	 *            The document id.
	 * @param content
	 *            The document content.
	 */
	Document(final Project parent, final String name, final Calendar createdOn,
			final String createdBy, final String description, final UUID id,
			final byte[] content) {
		super(parent, name, description, createdOn, createdBy, id);
		this.content = content;
		this.contentChecksum = MD5Util.md5Hex(content);
	}

	/**
	 * Obtain the document content.
	 * 
	 * @return The document content.
	 */
	public byte[] getContent() { return content; }

	/**
	 * Obtain the document content's checksum.
	 * 
	 * @return The document content's checksum.
	 */
	public String getContentChecksum() { return contentChecksum; }

	/**
	 * Obtain the path of the document.
	 * 
	 * @return The path of the document.
	 * @see com.thinkparity.model.parity.api.ParityObject#getPath()
	 */
	public StringBuffer getPath() {
		return getParent().getPath().append("/").append(getCustomName());
	}

	/**
	 * Obtain the type of parity object.
	 * 
	 * @return The type of parity object.
	 * @see ParityObject#getType()
	 * @see ParityObjectType#DOCUMENT
	 */
	public ParityObjectType getType() { return ParityObjectType.DOCUMENT; }

	/**
	 * Set the content of the document.
	 * 
	 * @param content
	 *            The content of the document.
	 */
	void setContent(byte[] content) {
		this.content = new byte[content.length];
		System.arraycopy(content, 0, this.content, 0, content.length);
	}
}
