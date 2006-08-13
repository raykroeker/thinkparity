/*
 * Created On: Aug 7, 2006 11:47:07 AM
 */
package com.thinkparity.server.handler.container;

import java.util.UUID;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Publish extends AbstractController {

    /** Create Publish. */
    public Publish() { super("container:publish"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        publish(readUUID(Xml.Artifact.UNIQUE_ID));
    }

    /**
     * Publish the artifact version.
     * 
     * @param uniqueId
     *            The unique id.
     */
    private void publish(final UUID uniqueId) {
        getContainerModel().publish(uniqueId);
    }
}
