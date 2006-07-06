/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.server.model.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.org.xmpp.packet.container.IQReactivateContainer;

/**
 * <b>Title:</b>thinkParity Container Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ContainerModelImpl extends AbstractModelImpl {

   /**
     * Obtain an apache api log id.
     * 
     * @param api
     *            The api.
     * @return The log id.
     */
    private static StringBuffer getApiId(final String api) {
        return getModelId("[CONTAINER]").append(" ").append(api);
    }

    /**
     * Create ContainerModelImpl.
     *
     * @param session
     *		The user's session.
     */
    ContainerModelImpl(final Session session) { super(session); }

    /**
     * Reactivate a container. Send a re-activation packet to each of the team
     * members.
     * 
     * @param uniqueId
     *            The container unique id.
     * @param versionId
     *            The container version id.
     * @param name
     *            The container name.
     * @param team
     *            The container team.
     * @param reactivatedBy
     *            Who reactivated the container.
     * @param reactivatedOn
     *            When the container was reactivated.
     * @throws ParityServerModelException
     */
    void reactivate(final UUID uniqueId, final Long versionId,
            final String name, final List<JabberId> team,
            final JabberId reactivatedBy, final Calendar reactivatedOn)
            throws ParityServerModelException {
        logger.info(getApiId("[REACTIVATE]"));
        logger.debug(uniqueId);
        logger.debug(versionId);
        logger.debug(name);
        logger.debug(team);
        logger.debug(reactivatedBy);
        logger.debug(reactivatedOn);

        final IQReactivateContainer query = new IQReactivateContainer();
        query.setUniqueId(uniqueId);
        query.setVersionId(versionId);
        query.setName(name);
        query.setTeam(team);
        query.setReactivatedBy(reactivatedBy);
        query.setReactivatedOn(reactivatedOn);
        for(final JabberId jabberId : team) {
            // don't send re-activation to self and don't send it if
            // the user is no longer active
            if(!jabberId.equals(session.getJabberId())) {
                if(isActive(jabberId)) {
                    query.setFrom(reactivatedBy.getJID());
                    query.setTo(jabberId.getJID());
                    try { send(jabberId, query); }
                    catch(final UnauthorizedException ux) {
                        throw ParityErrorTranslator.translate(ux);
                    }
                }
            }
        }
    }
}
