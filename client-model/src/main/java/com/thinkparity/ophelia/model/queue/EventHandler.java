/*
 * Created On: Sep 12, 2006 2:33:07 PM
 */
package com.thinkparity.ophelia.model.queue;

import java.text.MessageFormat;

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

/**
 * <b>Title:</b>thinkParity Ophelia Model Queue Model Event Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class EventHandler {

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** An <code>InternalModelFactory</code>. */
    private final InternalModelFactory modelFactory;

    /**
     * Create SessionModelEventDispatcher.
     * 
     * @param modelFactory
     *            An <code>InternalModelFactory</code>.
     */
    EventHandler(final InternalModelFactory modelFactory) {
        super();
        this.logger = new Log4JWrapper();
        this.modelFactory = modelFactory;
    }

    /**
     * Handle the xmpp event.
     *
     */
    public void handleEvent(final XMPPEvent event) {
        if (event.getClass() == ArtifactDraftCreatedEvent.class) {
            logger.logInfo("Handling artifact draft created.");
            getArtifactModel().handleDraftCreated((ArtifactDraftCreatedEvent) event);
        } else if (event.getClass() == ArtifactDraftDeletedEvent.class) {
            logger.logInfo("Handling artifact draft deleted.");
            getArtifactModel().handleDraftDeleted((ArtifactDraftDeletedEvent) event);
        } else if (event.getClass() == ArtifactReceivedEvent.class) {
            logger.logInfo("Handling artifact draft received.");
            getArtifactModel().handleReceived((ArtifactReceivedEvent) event);
        } else if (event.getClass() == ArtifactTeamMemberAddedEvent.class) {
            logger.logInfo("Handling artifact team member added.");
            getArtifactModel().handleTeamMemberAdded((ArtifactTeamMemberAddedEvent) event);
        } else if (event.getClass() == ArtifactTeamMemberRemovedEvent.class) {
            logger.logInfo("Handling artifact team member removed.");
            getArtifactModel().handleTeamMemberRemoved((ArtifactTeamMemberRemovedEvent) event);
        } else if (event.getClass() == BackupStatisticsUpdatedEvent.class) {
            logger.logInfo("Handling backup statistics updated.");
            getBackupModel().handleStatisticsUpdated((BackupStatisticsUpdatedEvent) event);
        } else if (event.getClass() == ContactDeletedEvent.class) {
            logger.logInfo("Handling contact deleted.");
            getContactModel().handleContactDeleted((ContactDeletedEvent) event);
        } else if (event.getClass() == ContactEMailInvitationDeclinedEvent.class) {
            logger.logInfo("Handling contact e-mail invitation declined.");
            getContactModel().handleEMailInvitationDeclined((ContactEMailInvitationDeclinedEvent) event);
        } else if (event.getClass() == ContactEMailInvitationDeletedEvent.class) {
            logger.logInfo("Handling contact e-mail invitation deleted.");
            getContactModel().handleEMailInvitationDeleted((ContactEMailInvitationDeletedEvent) event);
        } else if (event.getClass() == ContactEMailInvitationExtendedEvent.class) {
            logger.logInfo("Handling contact e-mail invitation extended.");
            getContactModel().handleEMailInvitationExtended((ContactEMailInvitationExtendedEvent) event);
        } else if (event.getClass() == ContactInvitationAcceptedEvent.class) {
            logger.logInfo("Handling contact invitation accepted.");
            getContactModel().handleInvitationAccepted((ContactInvitationAcceptedEvent) event);
        } else if (event.getClass() == ContactUpdatedEvent.class) {
            logger.logInfo("Handling contact updated.");
            getContactModel().handleContactUpdated((ContactUpdatedEvent) event);
        } else if (event.getClass() == ContactUserInvitationDeclinedEvent.class) {
            logger.logInfo("Handling contact user invitation declined.");
            getContactModel().handleUserInvitationDeclined((ContactUserInvitationDeclinedEvent) event);
        } else if (event.getClass() == ContactUserInvitationDeletedEvent.class) {
            logger.logInfo("Handling contact user invitation deleted.");
            getContactModel().handleUserInvitationDeleted((ContactUserInvitationDeletedEvent) event);
        } else if (event.getClass() == ContactUserInvitationExtendedEvent.class) {
            logger.logInfo("Handling contact user invitation extended.");
            getContactModel().handleUserInvitationExtended((ContactUserInvitationExtendedEvent) event);
        } else if (event.getClass() == PublishedEvent.class) {
            logger.logInfo("Handling container published.");
            getContainerModel().handleEvent((PublishedEvent) event);
        } else if (event.getClass() == PublishedNotificationEvent.class) {
            logger.logInfo("Handling container published notification.");
            getContainerModel().handleEvent((PublishedNotificationEvent) event);
        } else if (event.getClass() == VersionPublishedEvent.class) {
            logger.logInfo("Handling container verison published.");
            getContainerModel().handleEvent((VersionPublishedEvent) event);
        } else if (event.getClass() == VersionPublishedNotificationEvent.class) {
            logger.logInfo("Handling container version published notification.");
            getContainerModel().handleEvent((VersionPublishedNotificationEvent) event);
        } else if (event.getClass() == ProductReleaseDeployedEvent.class) {
            logger.logInfo("Handling migrator product release deployed.");
            getMigratorModel().handleProductReleaseDeployed((ProductReleaseDeployedEvent) event);
        } else {
            final String message = MessageFormat.format("No handler for event {0}.", event);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Obtain an internal artifact model.
     * 
     * @return An instance of <code>InternalArtifactModel</code>.
     */
    private InternalArtifactModel getArtifactModel() {
        return modelFactory.getArtifactModel();
    }

    /**
     * Obtain an internal backup model.
     * 
     * @return An instance of <code>InternalBackupModel</code>.
     */
    private InternalBackupModel getBackupModel() {
        return modelFactory.getBackupModel();
    }

    /**
     * Obtain an internal contact model.
     * 
     * @return An instance of <code>InternalContactModel</code>.
     */
    private InternalContactModel getContactModel() {
        return modelFactory.getContactModel();
    }

    /**
     * Obtain an internal container model.
     * 
     * @return An instance of <code>InternalContainerModel</code>.
     */
    private InternalContainerModel getContainerModel() {
        return modelFactory.getContainerModel();
    }

    /**
     * Obtain an internal migrator model.
     * 
     * @return An instance of <code>InternalMigratorModel</code>.
     */
    private InternalMigratorModel getMigratorModel() {
        return modelFactory.getMigratorModel();
    }
}
