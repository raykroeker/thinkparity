/*
 * Nov 30, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

/**
 * An abstraction of the artifact jabber iq extensions for parity. This includes
 * the create,falg,subscribe and unsubscribe extensions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class IQArtifact extends IQParity {

	/**
	 * Finish xml tag for actionUUID.
	 */
	private static final String XML_UUID_FINISH = "</uuid>";

	/**
	 * Start xml tag for actionUUID.
	 */
	private static final String XML_UUID_START = "<uuid>";

	/**
	 * Artifact unique id.
	 */
	private UUID artifactUUID;

	/**
	 * Create a IQArtifact.
	 */
	protected IQArtifact(final Action action, final UUID artifactUUID) {
		super(action);
		setArtifactUUID(artifactUUID);
	}

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public UUID getArtifactUUID() { return artifactUUID; }

	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 */
	public String getChildElementXML() {
		final String xml = new StringBuffer(startQueryXML())
			.append(getArtifactUUIDXML())
			.append(finishQueryXML()).toString();
		logger.debug(xml);
		return xml;
	}

	/**
	 * Set the artifact unique id.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 */
	public void setArtifactUUID(final UUID artifactUUID) {
		this.artifactUUID = artifactUUID;
	}

	public void setUUID(final UUID uuid) { setArtifactUUID(uuid); }

	/**
	 * Obtain the xml tag for the artifact unique id.
	 * 
	 * @return The xml tag for the artifact unique id.
	 */
	protected String getArtifactUUIDXML() {
		return new StringBuffer(IQArtifact.XML_UUID_START)
			.append(getArtifactUUID().toString())
			.append(IQArtifact.XML_UUID_FINISH).toString();
	}
}
