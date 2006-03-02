/*
 * Feb 28, 2006
 */
package com.thinkparity.model.smackx.packet.contact;

import com.thinkparity.model.smackx.packet.IQParity;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class IQContact extends IQParity {

	/**
	 * The parity contact.
	 * 
	 */
	protected JabberId contact;

	/**
	 * Create a IQContact.
	 * 
	 * @param action
	 *            The contact action.
	 * @param contact
	 *            The contact.
	 */
	protected IQContact(final Action action, final JabberId contact) {
		super(action);
		this.contact = contact;
	}

	/**
	 * @see com.thinkparity.model.smackx.packet.IQParity#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() {
		final String xml = new StringBuffer(startQueryXML())
			.append(getJabberIdXML())
			.append(finishQueryXML())
			.toString();
		logger.debug(xml);
		return xml;
	}

	private String getJabberIdXML() {
		return new StringBuffer("<jid>")
		.append(contact.getQualifiedJabberId())
		.append("</jid>")
		.toString();
	}
}
