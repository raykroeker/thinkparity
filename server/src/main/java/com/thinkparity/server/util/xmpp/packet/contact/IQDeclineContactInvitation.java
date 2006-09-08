/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.util.xmpp.packet.contact;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.util.dom4j.ElementBuilder;
import com.thinkparity.desdemona.util.dom4j.ElementName;
import com.thinkparity.desdemona.util.dom4j.NamespaceName;
import com.thinkparity.desdemona.util.xmpp.packet.IQParity;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQDeclineContactInvitation extends IQParity {

	/**
	 * Create a IQInviteContact.
	 */
	public IQDeclineContactInvitation(final JabberId contact) {
		super(Action.DECLINECONTACTINVITATION);
		setType(IQ.Type.get);
		final Element queryElement = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_DECLINE_CONTACT_INVITATION.getName());

		// add the jabber id
		final String uuidElementText = contact.getQualifiedJabberId();
		ElementBuilder.addElement(queryElement, ElementName.JID, uuidElementText);
	}
}
