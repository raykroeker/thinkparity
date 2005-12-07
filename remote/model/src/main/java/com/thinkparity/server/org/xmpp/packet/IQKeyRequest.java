/*
 * Dec 7, 2005
 */
package com.thinkparity.server.org.xmpp.packet;

import java.util.UUID;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.server.ParityServerConstants;
import com.thinkparity.server.handler.ElementName;
import com.thinkparity.server.org.dom4j.ElementBuilder;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQKeyRequest extends IQArtifact {

	/**
	 * Create a IQKeyRequest.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 */
	public IQKeyRequest(final UUID artifactUUID) {
		super(Action.REQUESTKEY, artifactUUID);
		setType(IQ.Type.get);
		final Element queryElement = setChildElement(
				ParityServerConstants.IQ_PARITY_INFO_NAME,
				ParityServerConstants.IQ_PARITY_INFO_NAMESPACE);
		final Element uuidElement =
			ElementBuilder.addElement(queryElement, ElementName.UUID);
		uuidElement.setData(getArtifactUUID().toString());
	}
}
