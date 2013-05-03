/*
 * Created On:  7-Nov-06 8:37:31 AM
 */
package com.thinkparity.ophelia.model.container.monitor;

import com.thinkparity.ophelia.model.util.Step;

/**
 * <b>Title:</b>thinkParity OpheliaModel Publish Process Step<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum PublishStep implements Step {
    ENCRYPT_DOCUMENT_VERSION_BYTES, PUBLISH, RESET_UPLOAD_DOCUMENT_VERSION,
    UPLOAD_DOCUMENT_VERSION, UPLOAD_DOCUMENT_VERSION_BYTES,
    UPLOAD_DOCUMENT_VERSIONS
}
