/*
 * Created On: Aug 1, 2006 5:54:22 PM
 */
package com.thinkparity.wildfire.handler.artifact;

import java.util.UUID;

import com.thinkparity.model.Constants.Xml;

import com.thinkparity.wildfire.handler.AbstractHandler;

/** 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateDraft extends AbstractHandler {

    /** Create Reactivate. */
    public CreateDraft() { super("artifact:createdraft"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     * 
     */
    public void service() {
        logApiId();
        createDraft(readUUID(Xml.Artifact.UNIQUE_ID));
    }

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     *            The artifact bytes.
     */
    private void createDraft(final UUID uniqueId) {
        getArtifactModel().createDraft(uniqueId);
    }
}
