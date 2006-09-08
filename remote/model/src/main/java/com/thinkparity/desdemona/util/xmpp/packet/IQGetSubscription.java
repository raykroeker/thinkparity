/*
 * Feb 16, 2006
 */
package com.thinkparity.desdemona.util.xmpp.packet;

import java.util.List;
import java.util.UUID;

import org.dom4j.Element;
import org.xmpp.packet.IQ;


import com.thinkparity.desdemona.model.artifact.ArtifactSubscription;
import com.thinkparity.desdemona.util.dom4j.ElementBuilder;
import com.thinkparity.desdemona.util.dom4j.ElementName;
import com.thinkparity.desdemona.util.dom4j.NamespaceName;
import com.thinkparity.desdemona.wildfire.JIDBuilder;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetSubscription extends IQArtifact {

	/**
	 * Create a IQGetSubscription.
	 * @param action
	 * @param artifactUUID
	 */
	public IQGetSubscription(final UUID artifactUniqueId,
			final List<ArtifactSubscription> subscriptions) {
		super(Action.GETSUBSCRIPTION, artifactUniqueId);
		setType(IQ.Type.get);
	
		final Element element = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_GET_SUBSCRIPTION.getName());

		// uuid
		final String uuidElementText = getArtifactUUID().toString();
		ElementBuilder.addElement(element, ElementName.UUID, uuidElementText);

		// jids
		final Element jids = ElementBuilder.addElement(element, ElementName.JIDS);
		for(final ArtifactSubscription subscription : subscriptions) {
			ElementBuilder.addElement(jids,
					ElementName.JID,
					JIDBuilder.build(subscription.getUsername()).toString());
		}
	}

}
