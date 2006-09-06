/*
 * Mar 30, 2006
 */
package com.thinkparity.server.org.xmpp.packet.artifact;

import java.util.UUID;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.model.contact.Contact;

import com.thinkparity.server.org.dom4j.ElementBuilder;
import com.thinkparity.server.org.dom4j.ElementName;
import com.thinkparity.server.org.dom4j.NamespaceName;
import com.thinkparity.server.org.xmpp.packet.IQArtifact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQNotifyTeamMemberRemoved extends IQArtifact {

	/**
	 * Create a IQNotifyTeamMemberAdded.
	 * 
	 */
	public IQNotifyTeamMemberRemoved(final UUID artifactUniqueId,
			final Contact teamMember) {
		super(Action.NOTIFYTEAMMEMBERREMOVED, artifactUniqueId);
		setType(IQ.Type.set);

		final Element query = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_NOTIFY_TEAM_MEMBER_REMOVED.getName());

		// artifact unique id
		final String artifactUniqueIdText = getArtifactUUID().toString();
		ElementBuilder.addElement(query, ElementName.UUID, artifactUniqueIdText);

		// the team member
		final Element contact = ElementBuilder.addElement(query, ElementName.CONTACT);
		ElementBuilder.addElement(contact, ElementName.JID, teamMember.getId().getQualifiedJabberId());
		final Element vCard = ElementBuilder.addElement(contact, ElementName.VCARD);
		vCard.add(teamMember.getVCard());
	}
}
