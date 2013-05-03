/*
 * Created On:  29-Sep-07 4:46:47 PM
 */
package com.thinkparity.adriana.backup.model.notify;

import com.thinkparity.adriana.backup.model.util.Session;

/**
 * <b>Title:</b>thinkParity Adriana Notify Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface NotifyModel {

    /**
     * Notify.
     * 
     * @param session
     *            A <code>Session</code>.
     */
    void notify(Session session);
}
