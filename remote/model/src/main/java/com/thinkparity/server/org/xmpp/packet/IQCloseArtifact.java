/*
 * Feb 19, 2006
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
public class IQCloseArtifact extends IQArtifact {

	/**
	 * Create a IQCloseArtifact.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 */
	public IQCloseArtifact(final UUID artifactUniqueId) {
		super(Action.CLOSE, artifactUniqueId);

		setType(IQ.Type.set);

		final Element query = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_CLOSE_ARTIFACT.getName());

		// uuid
		final String uuidElement = getArtifactUUID().toString();
		ElementBuilder.addElement(query, ElementName.UUID, uuidElement);
	}
}
