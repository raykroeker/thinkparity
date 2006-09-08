/*
 * Mar 30, 2006
 */
package com.thinkparity.desdemona.util.xmpp.packet.artifact;

import java.util.UUID;

import org.dom4j.Element;
import org.xmpp.packet.IQ;


import com.thinkparity.desdemona.model.contact.Contact;
import com.thinkparity.desdemona.util.dom4j.ElementBuilder;
import com.thinkparity.desdemona.util.dom4j.ElementName;
import com.thinkparity.desdemona.util.dom4j.NamespaceName;
import com.thinkparity.desdemona.util.xmpp.packet.IQArtifact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQNotifyTeamMemberAdded extends IQArtifact {

	/**
	 * Create a IQNotifyTeamMemberAdded.
	 * 
	 */
	public IQNotifyTeamMemberAdded(final UUID artifactUniqueId,
			final Contact newTeamMember) {
		super(Action.NOTIFYTEAMMEMBERADDED, artifactUniqueId);
		setType(IQ.Type.set);

		final Element query = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_NOTIFY_TEAM_MEMBER_ADDED.getName());

		// artifact unique id
		final String artifactUniqueIdText = getArtifactUUID().toString();
		ElementBuilder.addElement(query, ElementName.UUID, artifactUniqueIdText);

		// the new team member
		final Element contact = ElementBuilder.addElement(query, ElementName.CONTACT);
		ElementBuilder.addElement(contact, ElementName.JID, newTeamMember.getId().getQualifiedJabberId());
		final Element vCard = ElementBuilder.addElement(contact, ElementName.VCARD);
		vCard.add(newTeamMember.getVCard());
	}
}
