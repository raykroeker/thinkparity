/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document.history;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReceiveHistoryItem extends HistoryItem {

	private User receivedFrom;

	/**
	 * Create a ReceiveHistoryItem.
	 * 
	 */
	ReceiveHistoryItem() { super(); }

	/**
	 * @return Returns the receivedFrom.
	 */
	public User getReceivedFrom() {
		return receivedFrom;
	}

	/**
	 * @param receivedFrom The receivedFrom to set.
	 */
	public void setReceivedFrom(User receivedFrom) {
		this.receivedFrom = receivedFrom;
	}
}
