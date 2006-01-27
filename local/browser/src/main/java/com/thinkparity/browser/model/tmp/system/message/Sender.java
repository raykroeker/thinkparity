/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.model.tmp.system.message;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Sender extends User {

	/**
	 * Create a Sender.
	 */
	public Sender(final User user) {
		super(user.getName(), user.getUsername(), user.getPresence());
	}
}
