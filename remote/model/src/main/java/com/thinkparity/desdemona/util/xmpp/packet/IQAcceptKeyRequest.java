/*
 * Dec 12, 2005
 */
package com.thinkparity.desdemona.util.xmpp.packet;

import java.util.UUID;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.desdemona.util.dom4j.ElementBuilder;
import com.thinkparity.desdemona.util.dom4j.ElementName;
import com.thinkparity.desdemona.util.dom4j.NamespaceName;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQAcceptKeyRequest extends IQArtifact {

	/**
	 * Create a IQAcceptKeyRequest.
	 * 
	 * @param artifactUUID
	 * @param jid
	 */
	public IQAcceptKeyRequest(final UUID artifactUUID, final JID jid) {
		super(null, artifactUUID);
		setType(IQ.Type.set);

		final Element queryElement = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_ACCEPT_KEY_REQUEST.getName());

		// uuid
		final String uuidElementText = getArtifactUUID().toString();
		ElementBuilder.addElement(queryElement, ElementName.UUID, uuidElementText);

		// jid
		final String jidElementText = jid.toString();
		ElementBuilder.addElement(queryElement, ElementName.JID, jidElementText);
	}
}
