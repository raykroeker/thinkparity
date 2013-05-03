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
    CREATE_VERSION, PUBLISH, UPLOAD_STREAM
}
