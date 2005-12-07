/*
 * Nov 30, 2005
 */
package com.thinkparity.server.org.xmpp.packet;

import java.util.UUID;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.server.ParityServerConstants;
import com.thinkparity.server.model.artifact.ParityObjectFlag;

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

	private static final String ELEMENT_NAME_FLAG = "flag";

	private static final String ELEMENT_NAME_UUID = "uuid";

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
		setType(IQ.Type.set);
		final Element queryElement = setChildElement(
				ParityServerConstants.IQ_PARITY_INFO_NAME,
				ParityServerConstants.IQ_PARITY_INFO_NAMESPACE);
		final Element uuidElement = queryElement.addElement(ELEMENT_NAME_UUID);
		uuidElement.setData(getArtifactUUID().toString());
		final Element flagElement = queryElement.addElement(ELEMENT_NAME_FLAG);
		flagElement.setData(getArtifactFlag().toString());
	}

	/**
	 * Obtain the artifact flag.
	 * 
	 * @return Returns the artifact flag.
	 */
	public ParityObjectFlag getArtifactFlag() { return artifactFlag; }
}
