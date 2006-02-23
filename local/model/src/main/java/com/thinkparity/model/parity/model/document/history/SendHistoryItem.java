/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document.history;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendHistoryItem extends HistoryItem {

	private final List<User> sentTo;

	/**
	 * Create a SendHistoryItem.
	 * 
	 */
	SendHistoryItem() {
		super();
		this.sentTo = new LinkedList<User>();
	}

	public Boolean addSentTo(final User sentTo) {
		return this.sentTo.add(sentTo);
	}

	/**
	 * @return Returns the sentTo.
	 */
	public List<User> getSentTo() {
		return sentTo;
	}

	public Boolean removeSentTo(final User sentTo) {
		return this.sentTo.remove(sentTo);
	}
}
