/*
 * Created On: Jun 22, 2006 3:18:05 PM
 * $Id$
 */
package com.thinkparity.server.handler.container;

import java.util.Calendar;
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
    public Reactivate() { super("container:reactivate"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     * 
     */
    public void service() {
        logger.info(getApiId("[SERVICE]"));
        reactivate(readUUID(Xml.Artifact.UNIQUE_ID),
                readLong(Xml.Artifact.VERSION_ID),
                readString(Xml.Artifact.NAME),
                readJabberIds(ReactivateXml.TEAM, ReactivateXml.TEAM_MEMBER),
                readJabberId(ReactivateXml.REACTIVATED_BY),
                readCalendar(ReactivateXml.REACTIVATED_ON));
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
    private void reactivate(final UUID uniqueId, final Long versionId,
            final String name, final List<JabberId> team,
            final JabberId reactivatedBy, final Calendar reactivatedOn) {
        try {
            getContainerModel().reactivate(uniqueId, versionId, name, team,
                    reactivatedBy, reactivatedOn);
        }
        catch(final ParityServerModelException psmx) {
            logger.error(getApiId("[REACTIVATE] [REMOTE MODEL ERROR]"), psmx);
            throw new RuntimeException(psmx);
        }
    }

    /** Custom api xml constants. */
    public static final class ReactivateXml {
        public static final String REACTIVATED_BY = "reactivatedBy";
        public static final String REACTIVATED_ON = "reactivatedOn";
        public static final String TEAM = "team";
        public static final String TEAM_MEMBER = "teamMember";
    }
}
