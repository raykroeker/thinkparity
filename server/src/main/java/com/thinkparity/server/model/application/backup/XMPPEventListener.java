/*
 * Created On:  20-Apr-07 10:16:55 AM
 */
package com.thinkparity.desdemona.model.backup;

import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Backup XMPP Event Listener<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface XMPPEventListener {

    /**
     * Handle the xmpp event for the user.
     * 
     * @param user
     *            A <code>User</code>.
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    public void handleEvent(final User user, final XMPPEvent event);
}
