/*
 * Created On:  4-Jun-07 11:07:52 AM
 */
package com.thinkparity.ophelia.model.queue;

import java.util.List;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.ophelia.model.util.ProcessMonitor;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.SUPPORTED)
public interface InternalQueueModel extends QueueModel {

    /**
     * Delete an event from the queue.
     * 
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    public void deleteEvent(final XMPPEvent event);

    /**
     * Process the event queue.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void process(final ProcessMonitor monitor);

    /**
     * Read the events from the queue.
     * 
     * @return A <code>List<XMPPEvent></code>.
     */
    public List<XMPPEvent> readEvents();

    /**
     * Read the size of the event queue.
     * 
     * @return A size <code>Integer</code>.
     */
    public Integer readSize();

    /**
     * Start a notification client. If one is already running, it will be
     * destroyed. Create an instance of a notification session; an instance of a
     * notification client thread; then start the thread.
     * 
     */
    public void startNotificationClient();

    /**
     * Stop the currently running queue processor.
     * 
     */
    public void stopProcessor();
}
