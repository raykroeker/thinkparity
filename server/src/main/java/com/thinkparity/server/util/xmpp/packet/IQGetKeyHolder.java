/*
 * Feb 14, 2006
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
public class IQGetKeyHolder extends IQArtifact {

	/**
	 * Create a IQGetKeyHolder.
	 * @param artifactUniqueId
	 * @param jid
	 */
	public IQGetKeyHolder(final UUID artifactUniqueId,
			final JID jid) {
		super(Action.GETKEYHOLDER, artifactUniqueId);
		setType(IQ.Type.get);

		final Element element = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_GET_KEYHOLDER.getName());

		// uuid
		final String uuidElementText = getArtifactUUID().toString();
		ElementBuilder.addElement(element, ElementName.UUID, uuidElementText);

		// jid
		final String jidElementText = jid.toString();
		ElementBuilder.addElement(element, ElementName.JID, jidElementText);
	}
}
