/*
 * Oct 16, 2005
 */
package com.thinkparity.model.xmpp.document;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
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
	 * @param flags
	 *            The document flags.
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
			final String description, final Collection<ArtifactFlag> flags,
			final String name, final UUID uniqueId, final String updatedBy,
			final Calendar updatedOn) {
		return new XMPPDocument(content, createdBy, createdOn, description,
				flags, name, uniqueId, updatedBy, updatedOn);
	}

	/**
	 * Create a new XMPPDocument based upon a parity document.
	 * 
	 * @param document
	 *            The parity document to base the xmpp document upon.
	 * @return The new xmpp document.
	 */
	public static XMPPDocument create(final Document document,
			final DocumentContent content) {
		return XMPPDocument.create(content.getContent(), document.getCreatedBy(),
				document.getCreatedOn(), document.getDescription(),
				document.getFlags(), document.getName(), document.getUniqueId(),
				document.getUpdatedBy(), document.getUpdatedOn());
	}

	private final byte[] content;
	private final String createdBy;
	private final Calendar createdOn;
	private final String description;
	private final Collection<ArtifactFlag> flags;
	private final String name;
	private final UUID uniqueId;
	private final String updatedBy;
	private final Calendar updatedOn;

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
	 */
	private XMPPDocument(final byte[] content, final String createdBy,
			final Calendar createdOn, final String description,
			final Collection<ArtifactFlag> flags, final String name,
			final UUID uniqueId, final String updatedBy,
			final Calendar updatedOn) {
		super();
		this.content = new byte[content.length];
		System.arraycopy(content, 0, this.content, 0, content.length);
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.description = description;
		this.flags = new Vector<ArtifactFlag>(flags.size());
		add(flags);
		this.name = name;
		this.uniqueId = uniqueId;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
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
	 * Obtain the flags.
	 * 
	 * @return The flags.
	 */
	public Collection<ArtifactFlag> getFlags() {
		return Collections.unmodifiableCollection(flags);
	}

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
	 * Add a flag to the xmpp document.
	 * 
	 * @param flag
	 *            The flag to add.
	 */
	private void add(final ArtifactFlag flag) {
		Assert.assertNotTrue("add(ArtifactFlag)", flags.contains(flag));
		flags.add(flag);
	}

	/**
	 * Add a list of flags.
	 * 
	 * @param flags
	 *            The flags to add.
	 */
	private void add(final Collection<ArtifactFlag> flags) {
		for(ArtifactFlag flag : flags) { add(flag); }
	}
}
