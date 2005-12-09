/*
 * Dec 7, 2005
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * Classes which implement this interface handle key notification requests made
 * on behalf of other parity users.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 * @see KeyEvent
 * @see SessionModel#addListener(KeyListener)
 */
public interface KeyListener {
	public void keyRequested(final KeyEvent keyEvent);
	public void keyRequestAccepted(final KeyEvent keyEvent);
	public void keyRequestDenied(final KeyEvent keyEvent);
}
