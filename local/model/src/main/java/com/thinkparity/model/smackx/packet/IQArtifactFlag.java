/*
 * Nov 30, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

import com.thinkparity.model.artifact.ArtifactFlag;
import com.thinkparity.model.xmpp.XMPPSession;

/**
 * A jabber iq extension for parity. This extension is used to flag artifacts
 * across the distributed network.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see IQArtifact
 * @see XMPPSession#flag(UUID, ArtifactFlag)
 */
public class IQArtifactFlag extends IQArtifact {

	/**
	 * Flag to apply to the parity artifact.
	 */
	private final ArtifactFlag artifactFlag;

	/**
	 * Create a IQArtifactFlag.
	 */
	public IQArtifactFlag(final UUID artifactUUID,
			final ArtifactFlag artifactFlag) {
		super(Action.FLAGARTIFACT, artifactUUID);
		this.artifactFlag = artifactFlag;
	}

	/**
	 * Obtain the artifact flag.
	 * 
	 * @return Returns the artifact flag.
	 */
	public ArtifactFlag getArtifactFlag() { return artifactFlag; }

	/**
	 * @see com.thinkparity.model.smackx.packet.IQArtifact#getChildElementXML()
	 */
	public String getChildElementXML() {
		final String xml = new StringBuffer(startQueryXML())
			.append(getActionXML())
			.append(getArtifactUUIDXML())
			.append(getFlagXML())
			.append(finishQueryXML()).toString();
		logger.debug(xml);
		return xml;
	}

	protected StringBuffer getFlagXML() {
		return new StringBuffer("<flag>")
			.append(getArtifactFlag().toString())
			.append("</flag>");
	}
}
