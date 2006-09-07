/*
 * Feb 17, 2006
 */
package com.thinkparity.server.org.xmpp.packet;

import java.util.List;

import org.dom4j.Element;

import com.thinkparity.model.artifact.Artifact;

import com.thinkparity.server.org.dom4j.ElementBuilder;
import com.thinkparity.server.org.dom4j.ElementName;
import com.thinkparity.server.org.dom4j.NamespaceName;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetKeys extends IQArtifact {

	/**
	 * Create a IQGetKeys.
	 */
	public IQGetKeys(final List<Artifact> artifacts) {
		super(Action.GETKEYS, null);

		final Element element = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_GET_KEYS.getName());

		// uuids
		final Element uuids = ElementBuilder.addElement(element, ElementName.UUIDS);
		for(final Artifact artifact : artifacts) {
			ElementBuilder.addElement(uuids,
					ElementName.UUID, artifact.getUniqueId().toString());
		}
	}
}
