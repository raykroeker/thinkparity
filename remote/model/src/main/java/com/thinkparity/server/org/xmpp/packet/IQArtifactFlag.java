/*
 * Nov 30, 2005
 */
package com.thinkparity.server.org.xmpp.packet;

import java.util.UUID;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.model.artifact.ParityObjectFlag;

import com.thinkparity.server.org.dom4j.ElementBuilder;
import com.thinkparity.server.org.dom4j.ElementName;
import com.thinkparity.server.org.dom4j.NamespaceName;

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
		setType(IQ.Type.set);
		final Element queryElement = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_ARTIFACT_FLAG.getName());

		// add the artifact unique id.
		final String uuidElementText = getArtifactUUID().toString();
		ElementBuilder.addElement(queryElement, ElementName.UUID, uuidElementText);

		// add the flag
		final String flagElementText = getArtifactFlag().toString();
		ElementBuilder.addElement(queryElement, ElementName.FLAG, flagElementText);
	}

	/**
	 * Obtain the artifact flag.
	 * 
	 * @return Returns the artifact flag.
	 */
	public ParityObjectFlag getArtifactFlag() { return artifactFlag; }
}
