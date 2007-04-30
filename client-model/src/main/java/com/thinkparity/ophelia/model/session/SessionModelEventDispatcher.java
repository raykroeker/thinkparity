/*
 * Created On: Sep 12, 2006 2:33:07 PM
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.xmpp.event.*;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedNotificationEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedNotificationEvent;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.backup.InternalBackupModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.migrator.InternalMigratorModel;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.util.xmpp.event.SessionListener;
import com.thinkparity.ophelia.model.util.xmpp.event.XMPPEventListener;
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
        xmppSession.clearListeners();
        // artifact events
        xmppSession.addListener(ArtifactDraftCreatedEvent.class,
                new XMPPEventListener<ArtifactDraftCreatedEvent>() {
            public void handleEvent(final ArtifactDraftCreatedEvent event) {
                logger.logApiId();
                getArtifactModel().handleDraftCreated(event);
            }});
        xmppSession.addListener(ArtifactDraftDeletedEvent.class,
                new XMPPEventListener<ArtifactDraftDeletedEvent>() {
            public void handleEvent(final ArtifactDraftDeletedEvent event) {
                logger.logApiId();
                getArtifactModel().handleDraftDeleted(event);
            }});
        xmppSession.addListener(ArtifactReceivedEvent.class,
                new XMPPEventListener<ArtifactReceivedEvent>() {
            public void handleEvent(final ArtifactReceivedEvent event) {
                logger.logApiId();
                getArtifactModel().handleReceived(event);
            }});
        xmppSession.addListener(ArtifactTeamMemberAddedEvent.class,
                new XMPPEventListener<ArtifactTeamMemberAddedEvent>() {
            public void handleEvent(final ArtifactTeamMemberAddedEvent event) {
                logger.logApiId();
                getArtifactModel().handleTeamMemberAdded(event);
            }});
        xmppSession.addListener(ArtifactTeamMemberRemovedEvent.class,
                new XMPPEventListener<ArtifactTeamMemberRemovedEvent>() {
            public void handleEvent(final ArtifactTeamMemberRemovedEvent event) {
                logger.logApiId();
                getArtifactModel().handleTeamMemberRemoved(event);
            }});
        // backup events
        xmppSession.addListener(BackupStatisticsUpdatedEvent.class,
                new XMPPEventListener<BackupStatisticsUpdatedEvent>() {
            public void handleEvent(BackupStatisticsUpdatedEvent event) {
                logger.logApiId();
                getBackupModel().handleStatisticsUpdated(event);
            }});
        // contact events
        xmppSession.addListener(ContactDeletedEvent.class,
                new XMPPEventListener<ContactDeletedEvent>() {
            public void handleEvent(final ContactDeletedEvent event) {
                logger.logApiId();
                getContactModel().handleContactDeleted(event);
            }});
        xmppSession.addListener(ContactEMailInvitationDeclinedEvent.class,
                new XMPPEventListener<ContactEMailInvitationDeclinedEvent>() {
            public void handleEvent(final ContactEMailInvitationDeclinedEvent event) {
                logger.logApiId();
                getContactModel().handleEMailInvitationDeclined(event);
            }});
        xmppSession.addListener(ContactEMailInvitationDeletedEvent.class,
                new XMPPEventListener<ContactEMailInvitationDeletedEvent>() {
            public void handleEvent(final ContactEMailInvitationDeletedEvent event) {
                logger.logApiId();
                getContactModel().handleEMailInvitationDeleted(event);
            }});
        xmppSession.addListener(ContactEMailInvitationExtendedEvent.class,
                new XMPPEventListener<ContactEMailInvitationExtendedEvent>() {
            public void handleEvent(final ContactEMailInvitationExtendedEvent event) {
                logger.logApiId();
                getContactModel().handleEMailInvitationExtended(event);
            }});
        xmppSession.addListener(ContactInvitationAcceptedEvent.class,
                new XMPPEventListener<ContactInvitationAcceptedEvent>() {
            public void handleEvent(final ContactInvitationAcceptedEvent event) {
                logger.logApiId();
                getContactModel().handleInvitationAccepted(event);
            }});
        xmppSession.addListener(ContactUpdatedEvent.class,
                new XMPPEventListener<ContactUpdatedEvent>() {
            public void handleEvent(final ContactUpdatedEvent event) {
                logger.logApiId();
                getContactModel().handleContactUpdated(event);
            }});
        xmppSession.addListener(ContactUserInvitationDeclinedEvent.class,
                new XMPPEventListener<ContactUserInvitationDeclinedEvent>() {
            public void handleEvent(final ContactUserInvitationDeclinedEvent event) {
                logger.logApiId();
                getContactModel().handleUserInvitationDeclined(event);
            }});
        xmppSession.addListener(ContactUserInvitationDeletedEvent.class,
                new XMPPEventListener<ContactUserInvitationDeletedEvent>() {
            public void handleEvent(final ContactUserInvitationDeletedEvent event) {
                logger.logApiId();
                getContactModel().handleUserInvitationDeleted(event);
            }});
        xmppSession.addListener(ContactUserInvitationExtendedEvent.class,
                new XMPPEventListener<ContactUserInvitationExtendedEvent>() {
            public void handleEvent(final ContactUserInvitationExtendedEvent event) {
                logger.logApiId();
                getContactModel().handleUserInvitationExtended(event);
            }});
        // container events
        xmppSession.addListener(PublishedEvent.class,
                new XMPPEventListener<PublishedEvent>() {
            public void handleEvent(final PublishedEvent event) {
                logger.logApiId();
                getContainerModel().handleEvent(event);
            }});
        xmppSession.addListener(PublishedNotificationEvent.class,
                new XMPPEventListener<PublishedNotificationEvent>() {
            public void handleEvent(final PublishedNotificationEvent event) {
                logger.logApiId();
                getContainerModel().handleEvent(event);
            }});
        xmppSession.addListener(VersionPublishedEvent.class,
                new XMPPEventListener<VersionPublishedEvent>() {
            public void handleEvent(final VersionPublishedEvent event) {
                logger.logApiId();
                getContainerModel().handleEvent(event);
            }});
        xmppSession.addListener(VersionPublishedNotificationEvent.class,
                new XMPPEventListener<VersionPublishedNotificationEvent>() {
            public void handleEvent(final VersionPublishedNotificationEvent event) {
                logger.logApiId();
                getContainerModel().handleEvent(event);
            }});
        // migrator events
        xmppSession.addListener(ProductReleaseDeployedEvent.class,
                new XMPPEventListener<ProductReleaseDeployedEvent>() {
            public void handleEvent(final ProductReleaseDeployedEvent event) {
                logger.logApiId();
                getMigratorModel().handleProductReleaseDeployed(event);
            }});
        // session events
        xmppSession.addListener(new SessionListener() {
            public void sessionEstablished() {
                logger.logApiId();
                getSessionModel().handleSessionEstablished();
            }
            public void sessionTerminated() {
                logger.logApiId();
                getSessionModel().handleSessionTerminated();
            }
            public void sessionError(final Throwable t) {
                logger.logApiId();
                getSessionModel().handleSessionError(t);
            }
        });
    }

    private InternalArtifactModel getArtifactModel() {
        return modelFactory.getArtifactModel();
    }

    private InternalBackupModel getBackupModel() {
        return modelFactory.getBackupModel();
    }

    private InternalContactModel getContactModel() {
        return modelFactory.getContactModel();
    }

    private InternalContainerModel getContainerModel() {
        return modelFactory.getContainerModel();
    }

    private InternalMigratorModel getMigratorModel() {
        return modelFactory.getMigratorModel();
    }

    private InternalSessionModel getSessionModel() {
        return modelFactory.getSessionModel();
    }
}
