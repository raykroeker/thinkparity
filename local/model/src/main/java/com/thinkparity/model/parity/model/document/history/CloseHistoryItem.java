/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document.history;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CloseHistoryItem extends HistoryItem {

	private User closedBy;

	/**
	 * Create a CloseHistoryItem.
	 * 
	 */
	CloseHistoryItem() { super(); }

	/**
	 * @return Returns the closedBy.
	 */
	public User getClosedBy() {
		return closedBy;
	}

	/**
	 * @param closedBy The closedBy to set.
	 */
	public void setClosedBy(final User closedBy) {
		this.closedBy = closedBy;
	}
}
