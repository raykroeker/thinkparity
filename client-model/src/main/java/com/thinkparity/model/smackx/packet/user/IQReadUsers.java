/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.user;

import java.util.List;

import com.thinkparity.model.smackx.packet.IQParity;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadUsers extends IQParity {

	private static final String XML_JABBER_ID_FINISH = "</jid>";

	private static final String XML_JABBER_ID_START = "<jid>";

	private static final String XML_JABBER_IDS_FINISH = "</jids>";

	private static final String XML_JABBER_IDS_START = "<jids>";

	private final List<JabberId> jabberIds;
	/**
	 * Create a IQReadUsers.
	 * @param action
	 */
	public IQReadUsers(final List<JabberId> jabberIds) {
		super(Action.READUSERS);
		this.jabberIds = jabberIds;
	}

	/**
	 * @see com.thinkparity.model.smackx.packet.IQParity#getChildElementXML()
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
