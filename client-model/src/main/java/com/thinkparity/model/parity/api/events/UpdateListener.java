/*
 * Apr 20, 2005
 */
package com.thinkparity.model.parity.api.events;

/**
 * UpdateListener
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface UpdateListener {
	public void objectClosed(final CloseEvent closeEvent);
	public void objectDeleted(final DeleteEvent deleteEvent);
	public void objectReceived(final UpdateEvent updateEvent);
	public void objectUpdated(final UpdateEvent updateEvent);
}
