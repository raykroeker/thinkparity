/*
 * Dec 12, 2005
 */
package com.thinkparity.server.org.xmpp.packet;

import java.util.UUID;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.server.org.dom4j.ElementBuilder;
import com.thinkparity.server.org.dom4j.ElementName;
import com.thinkparity.server.org.dom4j.NamespaceName;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQDenyKeyRequest extends IQArtifact {

	/**
	 * Create a IQDenyKeyRequest.
	 * @param artifactUUID
	 */
	public IQDenyKeyRequest(final UUID artifactUUID) {
		super(null, artifactUUID);
		setType(IQ.Type.set);

		final Element queryElement = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_DENY_KEY_REQUEST.getName());

		// uuid
		final String uuidElementText = getArtifactUUID().toString();
		ElementBuilder.addElement(queryElement, ElementName.UUID, uuidElementText);
	}

}
