/*
 * Created On: Jun 22, 2006 3:18:05 PM
 * $Id$
 */
package com.thinkparity.server.handler.document;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;
import com.thinkparity.server.model.ParityServerModelException;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Reactivate extends AbstractController {

    /**
     * Obtain an api log id.
     * 
     * @param api
     *            An api.
     * @return An api log id.
     */
    private static StringBuffer getApiId(final String api) {
        return getControllerId("[REACTIVATE]").append(" ").append(api);
    }

    /** Create Reactivate. */
    public Reactivate() { super("document:reactivate"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     * 
     */
    public void service() {
        logger.info(getApiId("[SERVICE]"));
        reactivate(readJabberIds(Xml.User.JABBER_IDS, Xml.User.JABBER_ID),
                readUUID(Xml.Artifact.UNIQUE_ID),
                readLong(Xml.Artifact.VERSION_ID),
                readString(Xml.Artifact.NAME),
                readByteArray(Xml.Artifact.BYTES));
    }

    /**
     * Reactivate an artifact for a given team.
     * 
     * @param team
     *            The artifact team.
     * @param uniqueId
     *            The artifact unique id.
     * @param versionId
     *            The artifact version id.
     * @param name
     *            The artifact name.
     * @param bytes
     *            The artifact bytes.
     */
    private void reactivate(final List<JabberId> team, final UUID uniqueId,
            final Long versionId, final String name, final byte[] bytes) {
        try { getArtifactModel().reactivate(team, uniqueId, versionId, name, bytes); }
        catch(final ParityServerModelException psmx) {
            logger.error("", psmx);
            throw new RuntimeException(psmx);
        }
    }
}
