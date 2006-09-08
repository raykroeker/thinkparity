/*
 * Dec 7, 2005
 */
package com.thinkparity.desdemona.util.xmpp.packet;

import java.util.UUID;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.desdemona.util.dom4j.ElementBuilder;
import com.thinkparity.desdemona.util.dom4j.ElementName;
import com.thinkparity.desdemona.util.dom4j.NamespaceName;


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
				ElementName.QUERY.getName(),
				NamespaceName.IQ_KEY_REQUEST.getName());

		// add the uuid
		final String uuidElementText = getArtifactUUID().toString();
		ElementBuilder.addElement(queryElement, ElementName.UUID, uuidElementText);
	}
}
