/*
 * Nov 29, 2005
 */
package com.thinkparity.server.packet;

import java.util.UUID;

import com.thinkparity.server.model.artifact.ArtifactFlag;

/**
 * The iq flag is an xmpp internet query specific to a parity server. It is used
 * to request that the server flag a parity object.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQFlag extends IQParity {

	/**
	 * Flag to set.
	 */
	private ArtifactFlag flag;

	/**
	 * Artifact to flag.
	 */
	private UUID id;

	/**
	 * Create a IQFlag.
	 */
	public IQFlag(final ArtifactFlag flag, final UUID id) {
		super();
		setFlag(flag);
		setId(id);
	}

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 */
	public String getChildElementXML() {
		final String xml = new StringBuffer("<query xmlns=\"")
			.append(getRootNamespace())
			.append(":flag\">")
			.append("<id>")
			.append(getId().toString())
			.append("</id>")
			.append("<flag>")
			.append(getFlag().toString())
			.append("</flag>")
			.append("</query>").toString();
		logger.debug(xml);
		return xml;
	}

	/**
	 * Obtain the flag.
	 * 
	 * @return The flag.
	 */
	public ArtifactFlag getFlag() { return flag; }

	/**
	 * Obtain the id.
	 * 
	 * @return The id.
	 */
	public UUID getId() { return id; }

	/**
	 * Set the flag.
	 * 
	 * @param flag
	 *            The flag to set.
	 */
	public void setFlag(final ArtifactFlag flag) { this.flag = flag; }

	/**
	 * Set the id.
	 * 
	 * @param id
	 *            The id to set.
	 */
	public void setId(final UUID id) { this.id = id; }
}
