/*
 * Created On: Aug 1, 2006 5:54:22 PM
 */
package com.thinkparity.server.handler.artifact;

import java.util.UUID;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;

/** 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateDraft extends AbstractController {

    /** Create Reactivate. */
    public CreateDraft() { super("artifact:createdraft"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
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
