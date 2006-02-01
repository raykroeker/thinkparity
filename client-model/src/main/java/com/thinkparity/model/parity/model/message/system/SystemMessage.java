/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import com.thinkparity.model.parity.model.artifact.ArtifactId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessage {

	/**
	 * The related artifact unique id.
	 * 
	 */
	private ArtifactId artifactId;

	/**
	 * The message body.
	 * 
	 */
	private String body;

	/**
	 * The message header.
	 * 
	 */
	private String header;

	/**
	 * The message id.
	 * 
	 */
	private SystemMessageId id;

	/**
	 * The message sender.
	 * 
	 */
	private User sender;

	/**
	 * Create a Message.
	 */
	public SystemMessage() {
		super();
		this.id = new SystemMessageId();
	}

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public ArtifactId getArtifactId() { return artifactId; }

	/**
	 * Obtain the message body.
	 * @return The message body.
	 */
	public String getBody() { return body; }

	/**
	 * Obtain the message header.
	 * 
	 * @return The message header.
	 */
	public String getHeader() { return header; }

	/**
	 * Obtain the message id.
	 * 
	 * @return The message id.
	 */
	public SystemMessageId getId() { return id; }

	/**
	 * Obtain the sender.
	 * 
	 * @return The sender.
	 */
	public User getSender() { return sender; }

	/**
	 * Determine whether or not the artifact id is set.
	 * 
	 * @return True; if the artifact id is set; false otherwise.
	 */
	public Boolean isSetArtifactId() { return null != artifactId; }

	/**
	 * Set the artifact unique id.
	 * 
	 * @param artifactId
	 *            The artifact unique id.
	 */
	public void setArtifactId(final ArtifactId artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * Set the message body.
	 * 
	 * @param body
	 *            The message body.
	 */
	public void setBody(final String body) { this.body = body; }

	/**
	 * Set the message header.
	 * @param header The message header.
	 */
	public void setHeader(final String header) { this.header = header; }

	/**
	 * Set the message id.
	 * 
	 * @param id
	 *            The message id.
	 */
	public void setId(final SystemMessageId id) { this.id = id; }

	/**
	 * Set the message sender.
	 * 
	 * @param sender
	 *            The message sender.
	 */
	public void setSender(final User sender) { this.sender = sender; }
}
