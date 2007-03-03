/*
 * Created On:  2-Mar-07 2:35:27 PM
 */
package com.thinkparity.ophelia.model.container.monitor;

import com.thinkparity.ophelia.model.util.Step;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum RestoreBackupStep implements Step {
    DELETE_LOCAL_CONTAINER, RESTORE_REMOTE_CONTAINER;
}
