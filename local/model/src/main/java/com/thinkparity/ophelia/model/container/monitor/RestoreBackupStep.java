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
    DELETE_CONTAINER, DELETE_CONTAINERS, FINALIZE_RESTORE,
    RESET_RESTORE_DOCUMENT_VERSION, RESTORE_CONTAINER, RESTORE_CONTAINERS,
    RESTORE_DOCUMENT_VERSION, RESTORE_DOCUMENT_VERSION_DECRYPT_BYTES,
    RESTORE_DOCUMENT_VERSION_DOWNLOAD_BYTES, RESTORE_DOCUMENT_VERSIONS,
    RESTORE_DOCUMENTS;
}
