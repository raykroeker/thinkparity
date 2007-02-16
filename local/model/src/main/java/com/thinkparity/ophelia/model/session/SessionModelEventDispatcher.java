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
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ProductReleaseDeployedEvent;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
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
        xmppSession.addListener(ArtifactPublishedEvent.class,
                new XMPPEventListener<ArtifactPublishedEvent>() {
            public void handleEvent(final ArtifactPublishedEvent event) {
                logger.logApiId();
                getArtifactModel().handlePublished(event);
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
        xmppSession.addListener(ContactDeletedEvent.class,
                new XMPPEventListener<ContactDeletedEvent>() {
            public void handleEvent(final ContactDeletedEvent event) {
                logger.logApiId();
                getContactModel().handleContactDeleted(event);
            }});
        xmppSession.addListener(ContactDeletedEvent.class,
                new XMPPEventListener<ContactDeletedEvent>() {
            public void handleEvent(final ContactDeletedEvent event) {
                logger.logApiId();
                getContactModel().handleContactDeleted(event);
            }});
        xmppSession.addListener(ContactInvitationAcceptedEvent.class,
                new XMPPEventListener<ContactInvitationAcceptedEvent>() {
            public void handleEvent(final ContactInvitationAcceptedEvent event) {
                logger.logApiId();
                getContactModel().handleInvitationAccepted(event);
            }});
        xmppSession.addListener(ContactInvitationDeclinedEvent.class,
                new XMPPEventListener<ContactInvitationDeclinedEvent>() {
            public void handleEvent(final ContactInvitationDeclinedEvent event) {
                logger.logApiId();
                getContactModel().handleInvitationDeclined(event);
            }});
        xmppSession.addListener(ContactInvitationDeletedEvent.class,
                new XMPPEventListener<ContactInvitationDeletedEvent>() {
            public void handleEvent(final ContactInvitationDeletedEvent event) {
                logger.logApiId();
                getContactModel().handleInvitationDeleted(event);
            }});
        xmppSession.addListener(ContactInvitationExtendedEvent.class,
                new XMPPEventListener<ContactInvitationExtendedEvent>() {
            public void handleEvent(final ContactInvitationExtendedEvent event) {
                logger.logApiId();
                getContactModel().handleInvitationExtended(event);
            }});
        xmppSession.addListener(ContactUpdatedEvent.class,
                new XMPPEventListener<ContactUpdatedEvent>() {
            public void handleEvent(final ContactUpdatedEvent event) {
                logger.logApiId();
                getContactModel().handleContactUpdated(event);
            }});
        xmppSession.addListener(ContainerPublishedEvent.class,
                new XMPPEventListener<ContainerPublishedEvent>() {
            public void handleEvent(final ContainerPublishedEvent event) {
                logger.logApiId();
                getContainerModel().handlePublished(event);
            }});
        xmppSession.addListener(ProductReleaseDeployedEvent.class,
                new XMPPEventListener<ProductReleaseDeployedEvent>() {
            public void handleEvent(final ProductReleaseDeployedEvent event) {
                logger.logApiId();
                getMigratorModel().handleProductReleaseDeployed(event);
            }});
        xmppSession.addListener(new SessionListener() {
            public void sessionEstablished() {
                logger.logApiId();
                getSessionModel().handleSessionEstablished();
            }
            public void sessionTerminated() {
                logger.logApiId();
                getSessionModel().handleSessionTerminated();
            }
            public void sessionTerminated(final Exception x) {
                logger.logApiId();
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

    private InternalMigratorModel getMigratorModel() {
        return modelFactory.getMigratorModel();
    }

    private InternalSessionModel getSessionModel() {
        return modelFactory.getSessionModel();
    }
}
