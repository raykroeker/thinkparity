/*
 * Oct 16, 2005
 */
package com.thinkparity.model.xmpp.document;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
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
	 * Create a new XMPPDocument based upon a parity document.
	 * 
	 * @param document
	 *            The parity document to base the xmpp document upon.
	 * @return The new xmpp document.
	 */
	public static XMPPDocument create(final Document document,
			final DocumentContent content) {
		return new XMPPDocument(content.getContent(), document.getCreatedBy(),
				document.getCreatedOn(), document.getDescription(),
				document.getId(), document.getName());
	}

	private final byte[] content;
	private final String createdBy;
	private final Calendar createdOn;
	private final String description;
	private final UUID id;
	private final String name;

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
	 * @param id
	 *            The xmpp document's id.
	 * @param name
	 *            The xmpp document's name.
	 */
	private XMPPDocument(final byte[] content, final String createdBy,
			final Calendar createdOn, final String description, final UUID id,
			final String name) {
		super();
		this.content = new byte[content.length];
		System.arraycopy(content, 0, this.content, 0, content.length);
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.description = description;
		this.id = id;
		this.name = name;
	}

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
	 * Obtain the unique id of the xmpp document.
	 * 
	 * @return The unique id.
	 */
	public UUID getId() { return id; }

	/**
	 * Obtain the name of the xmpp document.
	 * 
	 * @return The name.
	 */
	public String getName() { return name; }
}
