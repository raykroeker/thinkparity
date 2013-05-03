/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.queue;

import java.util.List;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.queue.notification.NotificationSession;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity Desdemona Queue Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.SUPPORTED)
public interface QueueModel {

    /**
     * Create a notification session.
     * 
     * @return A <code>NotificationSession</code>.
     */
    public NotificationSession createNotificationSession();

    /**
     * Delete an event for the model user.
     * 
     * @param event
     *            The <code>XMPPEvent</code>.
     */
	public void deleteEvent(final XMPPEvent event);

    /**
     * Read events for the model user.
     * 
     * @return A <code>List</code> of <code>XMPPEvent</code>s.
     */
	public List<XMPPEvent> readEvents();

    /**
     * Read the queue size for the model user.
     * 
     * @return The size of the queue.
     */
    public Integer readSize();
}
