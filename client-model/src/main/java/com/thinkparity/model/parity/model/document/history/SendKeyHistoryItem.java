/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document.history;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendKeyHistoryItem extends HistoryItem {

	private User sentTo;

	/**
	 * Create a SendKeyHistoryItem.
	 * 
	 */
	SendKeyHistoryItem() { super(); }

	/**
	 * @return Returns the sentTo.
	 */
	public User getSentTo() {
		return sentTo;
	}

	/**
	 * @param sentTo The sentTo to set.
	 */
	public void setSentTo(User sentTo) {
		this.sentTo = sentTo;
	}
}
