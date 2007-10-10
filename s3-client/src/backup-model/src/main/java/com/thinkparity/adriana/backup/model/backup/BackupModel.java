/*
 * Created On:  29-Sep-07 3:24:41 PM
 */
package com.thinkparity.adriana.backup.model.backup;

import java.util.List;

import com.thinkparity.adriana.backup.model.util.Session;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface BackupModel {

    /**
     * Archive a list of resources.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param resourceList
     *            A <code>List<Resource></code>.
     */
    void archive(Session session, List<Resource> resourceList);

    /**
     * Backup an archive.
     * 
     * @param session
     *            A <code>Session</code>.
     */
    void backup(Session session);

    /**
     * Instantiate a session.
     * 
     * @return A <code>Session</code>.
     */
    Session newSession();
}
