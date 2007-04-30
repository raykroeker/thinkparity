/*
 * Created On: Jul 6, 2006 11:51:59 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
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
            final Calendar publishedOn, final List<EMail> publishToEMails,
            final List<User> publishToUsers) {
        final XMPPMethod publish = xmppCore.createMethod("container:publish");
        publish.setParameter("userId", userId);
        publish.setParameter("version", version);
        publish.setDocumentVersionsStreamIdsParameter("documentVersions", documentVersions);
        publish.setTeamMembersParameter("teamMembers", teamMembers);
        publish.setParameter("publishedBy", publishedBy);
        publish.setParameter("publishedOn", publishedOn);
        publish.setEMailsParameter("publishToEMails", publishToEMails);
        publish.setUsersParameter("publishToUsers", publishToUsers);
        execute(publish);
    }

    // TODO-javadoc XMPPContainer#publishVersion()
    void publishVersion(final JabberId userId, final ContainerVersion version,
            final Map<DocumentVersion, String> documentVersions,
            final List<TeamMember> teamMembers,
            final List<ArtifactReceipt> receivedBy, final JabberId publishedBy,
            final Calendar publishedOn, final List<EMail> publishToEMails,
            final List<User> publishToUsers) {
        final XMPPMethod publish = xmppCore.createMethod("container:publishversion");
        publish.setParameter("userId", userId);
        publish.setParameter("version", version);
        publish.setDocumentVersionsStreamIdsParameter("documentVersions", documentVersions);
        publish.setTeamMembersParameter("teamMembers", teamMembers);
        publish.setArtifactReceiptParameter("receivedBy", receivedBy);
        publish.setParameter("publishedBy", publishedBy);
        publish.setParameter("publishedOn", publishedOn);
        publish.setEMailsParameter("publishToEMails", publishToEMails);
        publish.setUsersParameter("publishToUsers", publishToUsers);
        execute(publish);
    }
}
