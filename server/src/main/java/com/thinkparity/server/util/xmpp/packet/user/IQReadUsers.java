/*
 * Mar 1, 2006
 */
package com.thinkparity.desdemona.util.xmpp.packet.user;

import java.util.List;

import org.dom4j.Element;


import com.thinkparity.desdemona.model.user.User;
import com.thinkparity.desdemona.util.dom4j.ElementBuilder;
import com.thinkparity.desdemona.util.dom4j.ElementName;
import com.thinkparity.desdemona.util.dom4j.NamespaceName;
import com.thinkparity.desdemona.util.xmpp.packet.IQParity;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadUsers extends IQParity {

	/**
	 * Create a IQReadUsers.
	 * @param action
	 */
	public IQReadUsers(final List<User> users) {
		super(Action.READUSERS);

		final Element element = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_READ_USERS.getName());

		// users
		final Element usersElement = ElementBuilder.addElement(element, ElementName.USERS);
		Element userElement;
		Element vCardElement;
		for(final User user : users) {
			// user
			userElement = ElementBuilder.addElement(usersElement, ElementName.USER);
			// jid
			ElementBuilder.addElement(userElement, ElementName.JID, user.getId().getQualifiedJabberId());
			// vcard
			vCardElement = ElementBuilder.addElement(userElement, ElementName.VCARD);
			vCardElement.add(user.getVCard());
		}
	}
}
