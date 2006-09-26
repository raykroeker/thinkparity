/*
 * Created On: Sep 12, 2006 2:33:07 PM
 */
package com.thinkparity.ophelia.model.session;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.util.xmpp.events.ArtifactListener;
import com.thinkparity.ophelia.model.util.xmpp.events.ContactListener;
import com.thinkparity.ophelia.model.util.xmpp.events.ContainerListener;
import com.thinkparity.ophelia.model.util.xmpp.events.SessionListener;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class SessionModelEventDispatcher {

    /** An apache logger. */
    private final Log4JWrapper logger;

    private final InternalModelFactory modelFactory;

    SessionModelEventDispatcher(final Workspace workspace,
            final InternalModelFactory modelFactory,
            final XMPPSession xmppSession) {
        super();
        this.logger = new Log4JWrapper();
        this.modelFactory = modelFactory;
        logger.logApiId();
        xmppSession.clearListeners();
        xmppSession.addListener(new ArtifactListener() {
            public void confirmReceipt(final UUID uniqueId,
                    final Long versionId, final JabberId receivedFrom) {
                final InternalDocumentModel documentModel = getDocumentModel();
                final Document document = documentModel.get(uniqueId);
                documentModel.confirmSend(document.getId(), versionId, receivedFrom);
            }
            public void handleDraftCreated(final UUID uniqueId,
                    final JabberId createdBy, final Calendar createdOn) {
                getArtifactModel().handleDraftCreated(uniqueId, createdBy,
                        createdOn);
            }
            public void handleDraftDeleted(final UUID uniqueId,
                    final JabberId deletedBy, final Calendar deletedOn) {
                getArtifactModel().handleDraftDeleted(uniqueId, deletedBy,
                        deletedOn);
            }
            public void teamMemberAdded(final UUID uniqueId, final JabberId jabberId) {
                getArtifactModel().handleTeamMemberAdded(uniqueId, jabberId);
            }
            public void teamMemberRemoved(final UUID artifactUniqueId,
                    final JabberId jabberId) {
                getArtifactModel().handleTeamMemberRemoved(artifactUniqueId,
                        jabberId);
            }
        });
        xmppSession.addListener(new ContainerListener() {
            public void handleArtifactPublished(final JabberId publishedBy,
                    final Calendar publishedOn, final UUID containerUniqueId,
                    final Long containerVersionId, final String containerName,
                    final Integer containerArtifactCount,
                    final Integer containerArtifactIndex,
                    final UUID artifactUniqueId, final Long artifactVersionId,
                    final String artifactName, final ArtifactType artifactType,
                    final String artifactChecksum, final byte[] artifactBytes) {
                logger.logApiId();
                getContainerModel().handleArtifactPublished(containerUniqueId, containerVersionId,
                        containerName, artifactUniqueId, artifactVersionId,
                        artifactName, artifactType, artifactChecksum,
                        artifactBytes, publishedBy, publishedOn);
            }
            public void handleArtifactSent(final JabberId sentBy,
                    final Calendar sentOn, final UUID containerUniqueId,
                    final Long containerVersionId, final String containerName,
                    final Integer containerArtifactCount,
                    final Integer containerArtifactIndex,
                    final UUID artifactUniqueId, final Long artifactVersionId,
                    final String artifactName, final ArtifactType artifactType,
                    final String artifactChecksum, final byte[] artifactBytes) {
                getContainerModel().handleArtifactSent(containerUniqueId, containerVersionId, containerName,
                        artifactUniqueId, artifactVersionId, artifactName,
                        artifactType, artifactChecksum, artifactBytes,
                        sentBy, sentOn);
            }
            public void handlePublished(final UUID uniqueId,
                    final Long versionId, final String name,
                    final Integer artifactCount, final JabberId publishedBy,
                    final List<JabberId> publishedTo, final Calendar publishedOn) {
                getContainerModel().handlePublished(uniqueId, versionId, name,
                        artifactCount, publishedBy, publishedTo, publishedOn);
            }
            public void handleSent(final UUID uniqueId, final Long versionId,
                    final String name, final Integer artifactCount,
                    final JabberId sentBy, final Calendar sentOn,
                    final List<JabberId> sentTo) {
                getContainerModel().handleSent(uniqueId, versionId, name, artifactCount,
                        sentBy, sentOn, sentTo);
            }
        });
        xmppSession.addListener(new ContactListener() {
            public void handleContactDeleted(final JabberId deletedBy,
                    final Calendar deletedOn) {
                getContactModel().handleContactDeleted(deletedBy, deletedOn);
            }
            public void handleContactUpdated(final JabberId contactId,
                    final Calendar updatedOn) {
                getContactModel().handleContactUpdated(contactId, updatedOn);
            }
            public void handleInvitationAccepted(final JabberId acceptedBy,
                    final Calendar acceptedOn) {
                getContactModel().handleInvitationAccepted(acceptedBy, acceptedOn);
            }
            public void handleInvitationDeclined(final EMail invitedAs,
                    final JabberId declinedBy, final Calendar declinedOn) {
                getContactModel().handleInvitationDeclined(invitedAs, declinedBy, declinedOn);
            }
            public void handleInvitationDeleted(final EMail invitedAs,
                    final JabberId deletedBy, final Calendar deletedOn) {
                getContactModel().handleInvitationDeleted(invitedAs,
                        deletedBy, deletedOn);
            }
            public void handleInvitationExtended(final EMail invitedAs,
                    final JabberId invitedBy, final Calendar invitedOn) {
                getContactModel().handleInvitationExtended(invitedAs, invitedBy, invitedOn);
            }
        });
        xmppSession.addListener(new SessionListener() {
            public void sessionEstablished() {
                getSessionModel().handleSessionEstablished();
            }
            public void sessionTerminated() {
                getSessionModel().handleSessionTerminated();
            }
            public void sessionTerminated(final Exception x) {
                getSessionModel().handleSessionTerminated(x);
            }
        });
    }

    private InternalArtifactModel getArtifactModel() {
        return modelFactory.getArtifactModel();
    }

    private InternalContactModel getContactModel() {
        return modelFactory.getContactModel();
    }

    private InternalContainerModel getContainerModel() {
        return modelFactory.getContainerModel();
    }

    private InternalDocumentModel getDocumentModel() {
        return modelFactory.getDocumentModel();
    }

    private InternalSessionModel getSessionModel() {
        return modelFactory.getSessionModel();
    }
}
