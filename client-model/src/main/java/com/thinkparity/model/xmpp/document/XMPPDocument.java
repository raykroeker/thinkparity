/*
 * Oct 16, 2005
 */
package com.thinkparity.model.xmpp.document;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.xmpp.XMPPSerializable;

/**
 * An xmpp document is a read-only version of a document that can be sent to
 * another parity user over the xmpp network. This object is what is serialized
 * by the xmpp session implementation and read-back by the same.
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class XMPPDocument implements XMPPSerializable {

	/**
	 * Create a new xmpp document.
	 * 
	 * @param content
	 *            The document content.
	 * @param createdBy
	 *            The document creator.
	 * @param createdOn
	 *            The document creation date.
	 * @param description
	 *            The document description.
	 * @param name
	 *            The document name.
	 * @param uniqueId
	 *            The document unique id.
	 * @param updatedBy
	 *            The document updator.
	 * @param updatedOn
	 *            The document update date.
	 * @return The new xmpp document.
	 */
	public static XMPPDocument create(final byte[] content,
			final String createdBy, final Calendar createdOn,
			final String description, final String name, final UUID uniqueId,
			final String updatedBy, final Calendar updatedOn,
			final Long versionId) {
		return new XMPPDocument(content, createdBy, createdOn, description,
				name, uniqueId, updatedBy, updatedOn, versionId);
	}

	/**
	 * Create a new XMPPDocument based upon a parity document.
	 * 
	 * @param document
	 *            The parity document to base the xmpp document upon.
	 * @return The new xmpp document.
	 */
	public static XMPPDocument create(final Document document,
			final DocumentContent content, final DocumentVersion version) {
		return XMPPDocument.create(content.getContent(), document.getCreatedBy(),
				document.getCreatedOn(), document.getDescription(),
				document.getName(), document.getUniqueId(),
				document.getUpdatedBy(), document.getUpdatedOn(),
				version.getVersionId());
	}

	private byte[] content;
	private String createdBy;
	private Calendar createdOn;
	private String description;
	private String name;
	private UUID uniqueId;
	private String updatedBy;
	private Calendar updatedOn;
	private Long versionId;

	/**
	 * Create an XMPPDocument.
	 * 
	 * @param content
	 *            The xmpp document's content.
	 * @param createdBy
	 *            The xmpp document's creator.
	 * @param createdOn
	 *            The xmpp document's creation date.
	 * @param description
	 *            The xmpp document's description.
	 * @param name
	 *            The xmpp document's name.
	 * @param uniqueId
	 *            The xmpp document's unique id.
	 * @param updatedBy
	 *            The xmpp document's updator.
	 * @param updatedOn
	 *            The xmpp document's update date.
	 * @param versionId
	 *            The artifact's version id.
	 */
	private XMPPDocument(final byte[] content, final String createdBy,
			final Calendar createdOn, final String description,
			final String name, final UUID uniqueId, final String updatedBy,
			final Calendar updatedOn, final Long versionId) {
		super();
		this.content = new byte[content.length];
		System.arraycopy(content, 0, this.content, 0, content.length);
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.description = description;
		this.name = name;
		this.uniqueId = uniqueId;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.versionId = versionId;
	}

	public XMPPDocument() { super(); }

	/**
	 * Obtain the content of the xmpp document.
	 * @return The content.
	 */
	public byte[] getContent() { return content; }

	/**
	 * Obtain the creator of the xmpp document.
	 * 
	 * @return The creator.
	 */
	public String getCreatedBy() { return createdBy; }

	/**
	 * Obtain the creation date of the xmpp documen.t
	 * 
	 * @return The creation date.
	 */
	public Calendar getCreatedOn() { return createdOn; }

	/**
	 * Obtain the description of the xmpp document.
	 * 
	 * @return The description.
	 */
	public String getDescription() { return description; }

	/**
	 * Obtain the name of the xmpp document.
	 * 
	 * @return The name.
	 */
	public String getName() { return name; }

	/**
	 * Obtain the xmpp document unique id.
	 * 
	 * @return The xmpp document unique id.
	 */
	public UUID getUniqueId() { return uniqueId; }

	/**
	 * Obtain the updator.
	 * 
	 * @return The updator.
	 */
	public String getUpdatedBy() { return updatedBy; }

	/**
	 * Obtain the update date.
	 * 
	 * @return The update date.
	 */
	public Calendar getUpdatedOn() { return updatedOn; }

	/**
	 * Obtain the document's version.
	 * 
	 * @return The document's version.
	 */
	public Long getVersionId() { return versionId; }

	/**
	 * @param content The content to set.
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @param createdOn The createdOn to set.
	 */
	public void setCreatedOn(Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param uniqueId The uniqueId to set.
	 */
	public void setUniqueId(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * @param updatedBy The updatedBy to set.
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @param updatedOn The updatedOn to set.
	 */
	public void setUpdatedOn(Calendar updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * @param versionId The versionId to set.
	 */
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

}
