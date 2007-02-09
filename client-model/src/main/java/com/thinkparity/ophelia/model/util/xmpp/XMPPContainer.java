/*
 * Created On: Jul 6, 2006 11:51:59 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.xmpp.event.ContainerListener;

/**
 * <b>Title:</b>thinkParity XMPP Container <br>
 * <b>Description:</b>The container remote interface implemenation. Handles all
 * remote method innvocations to the thinkParity server for the container
 * artifact. Also handles the remote events generated for the container.
 * 
 * @author raymond@thinkparity.com
 * @version
 * @see XMPPCore
 */
final class XMPPContainer extends AbstractXMPP<ContainerListener> {

    /**
     * Create XMPPContainer.
     * 
     */
    XMPPContainer(final XMPPCore xmppCore) {
        super(xmppCore);
    }

    // TODO-javadoc XMPPContainer#publish
    void publish(final JabberId userId, final ContainerVersion version,
            final Map<DocumentVersion, String> documentVersions,
            final List<TeamMember> teamMembers, final JabberId publishedBy,
            final Calendar publishedOn, final List<User> publishedTo) {
        logger.logApiId();
        logger.logVariable("version", version);
        logger.logVariable("documentVersions", documentVersions);
        logger.logVariable("teamMembers", teamMembers);
        logger.logVariable("publishedBy", publishedBy);
        logger.logVariable("publishedOn", publishedOn);
        logger.logVariable("publishedTo", publishedTo);
        final XMPPMethod publish = new XMPPMethod("container:publish");
        publish.setParameter("userId", userId);
        publish.setParameter("version", version);
        publish.setDocumentVersionsStreamIdsParameter("documentVersions", documentVersions);
        publish.setTeamMembersParameter("teamMembers", teamMembers);
        publish.setParameter("publishedBy", publishedBy);
        publish.setParameter("publishedOn", publishedOn);
        publish.setUsersParameter("publishedTo", publishedTo);
        execute(publish);
    }
}
