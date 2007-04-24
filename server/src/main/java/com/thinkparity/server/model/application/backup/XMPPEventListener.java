/*
 * Created On:  20-Apr-07 10:16:55 AM
 */
package com.thinkparity.desdemona.model.backup;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Backup XMPP Event Listener<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface XMPPEventListener {

    /**
     * Handle the xmpp event.
     * 
     * @param session
     *            A user <code>Session</code>.
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    public void handleEvent(final Session session, final XMPPEvent event);
}
