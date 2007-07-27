/*
 * Created On:  2-Mar-07 1:23:56 PM
 */
package com.thinkparity.ophelia.model.workspace.monitor;

import com.thinkparity.ophelia.model.util.Step;

/**
 * <b>Title:</b>thinkParity OpheliaModel Workspace Initialize Step<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum InitializeStep implements Step {
    CONTACT_DOWNLOAD, CONTAINER_RESTORE_BACKUP, PERSISTENCE_INITIALIZE,
    PROFILE_CREATE, PUBLISH_WELCOME, SESSION_LOGIN, SESSION_PROCESS_QUEUE;
}
