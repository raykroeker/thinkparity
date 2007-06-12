/*
 * Created On:  4-Jun-07 11:07:52 AM
 */
package com.thinkparity.ophelia.model.queue;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

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
     * Read the size of the event queue.
     * 
     * @return A size <code>Integer</code>.
     */
    public Integer readSize();

    /**
     * Process the event queue.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void process(final ProcessMonitor monitor);

    /**
     * Start a notification client. If one is already running, it will be
     * destroyed. Create an instance of a notification session; an instance of a
     * notification client thread; then start the thread.
     * 
     */
    public void startNotificationClient();
}
