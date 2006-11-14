/*
 * Created On: Sep 12, 2006 2:33:07 PM
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.util.xmpp.event.ArtifactListener;
import com.thinkparity.ophelia.model.util.xmpp.event.ContactListener;
import com.thinkparity.ophelia.model.util.xmpp.event.ContainerListener;
import com.thinkparity.ophelia.model.util.xmpp.event.SessionListener;
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
            public void handleDraftCreated(final ArtifactDraftCreatedEvent event) {
                getArtifactModel().handleDraftCreated(event);
            }
            public void handleDraftDeleted(final ArtifactDraftDeletedEvent event) {
                getArtifactModel().handleDraftDeleted(event);
            }
            public void handlePublished(final ArtifactPublishedEvent event) {
                getArtifactModel().handlePublished(event);
            }
            public void handleReceived(final ArtifactReceivedEvent event) {
                getArtifactModel().handleReceived(event);
            }
            public void handleTeamMemberAdded(final ArtifactTeamMemberAddedEvent event) {
                getArtifactModel().handleTeamMemberAdded(event);
            }
            public void handleTeamMemberRemoved(final ArtifactTeamMemberRemovedEvent event) {
                getArtifactModel().handleTeamMemberRemoved(event);
            }
        });
        xmppSession.addListener(new ContainerListener() {
            public void handleArtifactPublished(
                    final ContainerArtifactPublishedEvent event) {
                getContainerModel().handleArtifactPublished(event);
            }
            public void handlePublished(final ContainerPublishedEvent event) {
                getContainerModel().handlePublished(event);
            }
        });
        xmppSession.addListener(new ContactListener() {
            public void handleDeleted(final ContactDeletedEvent event) {
                getContactModel().handleContactDeleted(event);
            }
            public void handleUpdated(final ContactUpdatedEvent event) {
                getContactModel().handleContactUpdated(event);
            }
            public void handleInvitationAccepted(
                    final ContactInvitationAcceptedEvent event) {
                getContactModel().handleInvitationAccepted(event);
            }
            public void handleInvitationDeclined(
                    final ContactInvitationDeclinedEvent event) {
                getContactModel().handleInvitationDeclined(event);
            }
            public void handleInvitationDeleted(
                    final ContactInvitationDeletedEvent event) {
                getContactModel().handleInvitationDeleted(event);
            }
            public void handleInvitationExtended(
                    final ContactInvitationExtendedEvent event) {
                getContactModel().handleInvitationExtended(event);
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

    private InternalSessionModel getSessionModel() {
        return modelFactory.getSessionModel();
    }
}
