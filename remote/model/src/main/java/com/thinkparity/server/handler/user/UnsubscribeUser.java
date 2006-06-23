/*
 * Dec 1, 2005
 */
package com.thinkparity.server.handler.user;

import java.util.UUID;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactModel;

/**
 * Handle the removal of a user from an artifact; AKA team member
 * creation; AKA artifact contacts.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UnsubscribeUser extends AbstractController {

	/** Create UnsubscribeUser. */
	public UnsubscribeUser() { super("unsubscribeuser"); }

	/**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     * 
     */
    public void service() {
        logger.info(getControllerId("[UNSUBSCRIBE USER]").append(" [SERVICE]"));
        unsubscribeUser(readUUID(Xml.Artifact.UNIQUE_ID));
    }

    private void unsubscribeUser(final UUID uniqueId) {
        final ArtifactModel aModel = getArtifactModel();
        try {
            final Artifact artifact = aModel.get(uniqueId);
            aModel.unsubscribe(artifact);
        }
        catch(final ParityServerModelException psmx) {
            logger.error("", psmx);
            throw new RuntimeException(psmx);
        }
    }
}
