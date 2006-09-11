/*
 * Mar 1, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet.user;

import java.util.Set;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.util.smackx.packet.IQParity;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadUsers extends IQParity {

	private static final String XML_JABBER_ID_FINISH = "</jid>";

	private static final String XML_JABBER_ID_START = "<jid>";

	private static final String XML_JABBER_IDS_FINISH = "</jids>";

	private static final String XML_JABBER_IDS_START = "<jids>";

	private final Set<JabberId> jabberIds;
	/**
	 * Create a IQReadUsers.
	 * @param action
	 */
	public IQReadUsers(final Set<JabberId> jabberIds) {
		super(Action.READUSERS);
		this.jabberIds = jabberIds;
	}

	/**
	 * @see com.thinkparity.ophelia.model.util.smackx.packet.IQParity#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() {
		final String xml = new StringBuffer(startQueryXML())
			.append(getJabberIdsXML())
			.append(finishQueryXML()).toString();
		logger.debug(xml);
		return xml;
	}

	private String getJabberIdsXML() {
		final StringBuffer buffer = new StringBuffer(XML_JABBER_IDS_START);
		for(final JabberId jabberId : jabberIds) {
			buffer.append(XML_JABBER_ID_START)
				.append(jabberId.getQualifiedJabberId())
				.append(XML_JABBER_ID_FINISH);
		}
		return buffer.append(XML_JABBER_IDS_FINISH)
			.toString();
	}
}
