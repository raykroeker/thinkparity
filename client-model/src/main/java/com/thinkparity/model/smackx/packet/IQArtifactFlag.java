/*
 * Nov 30, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObjectFlag;

/**
 * A jabber iq extension for parity. This extension is used to flag artifacts
 * across the distributed network.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see IQArtifact
 * @see XMPPSessionImpl#flag(UUID, ParityObjectFlag)
 */
public class IQArtifactFlag extends IQArtifact {

	/**
	 * Flag to apply to the parity artifact.
	 */
	private final ParityObjectFlag artifactFlag;

	/**
	 * Create a IQArtifactFlag.
	 */
	public IQArtifactFlag(final UUID artifactUUID,
			final ParityObjectFlag artifactFlag) {
		super(Action.FLAG, artifactUUID);
		this.artifactFlag = artifactFlag;
	}

	/**
	 * Obtain the artifact flag.
	 * 
	 * @return Returns the artifact flag.
	 */
	public ParityObjectFlag getArtifactFlag() { return artifactFlag; }

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
