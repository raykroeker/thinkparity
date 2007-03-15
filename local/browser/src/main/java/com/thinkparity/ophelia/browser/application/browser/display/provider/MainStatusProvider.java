/*
 * Created On:  14-Mar-07 2:33:10 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import java.util.List;

import com.thinkparity.codebase.filter.Filter;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;

/**
 * <b>Title:</b>thinkParity OpheliaUI Main Status Provider<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MainStatusProvider extends ContentProvider {

    /** An instance of <code>BackupModel</code>. */
    private final BackupModel backupModel;

    /** An instance of <code>ContactModel</code>. */
    private final ContactModel contactModel;

    /** An instance of <code>ContainerModel</code>. */
    private final ContainerModel containerModel;

    /** An instance of <code>ProfileModel</code>. */
    private final ProfileModel profileModel;

    /** An instance of <code>SessionModel</code>. */
    private final SessionModel sessionModel;

    /**
     * Create MainStatusProvider.
     * 
     * @param profile
     *            The user's <code>Profile</code>.
     * @param backupModel
     *            An instance of <code>BackupModel</code>.
     * @param contactModel
     *            An instance of <code>ContactModel</code>.
     * @param containerModel
     *            An instance of <code>ContainerModel</code>.
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     * @param sessionModel
     *            An instance of <code>SessionModel</code>.
     */
    public MainStatusProvider(final Profile profile,
            final BackupModel backupModel, final ContactModel contactModel,
            final ContainerModel containerModel,
            final ProfileModel profileModel, final SessionModel sessionModel) {
        super(profile);
        this.backupModel = backupModel;
        this.contactModel = contactModel;
        this.containerModel = containerModel;
        this.profileModel = profileModel;
        this.sessionModel = sessionModel;
    }

    /**
     * Determine if the user's backup is enabled.
     * 
     * @return True if the user's backup is enabled.
     */
    public Boolean isBackupEnabled() {
        return profileModel.isBackupEnabled();
    }

    /**
     * Determine if the user's backup is online.
     * 
     * @return True if the user's backup is online.
     */
    public Boolean isBackupOnline() {
        return backupModel.isOnline();
    }

    /**
     * Determine if the user's backup is online.
     * 
     * @return True if the user's backup is online.
     */
    public Boolean isOnline() {
        return sessionModel.isLoggedIn();
    }

    /**
     * Read the backup statistics.
     * 
     * @return The backup <code>Statistics</code>.
     */
    public Statistics readBackupStatistics() {
        return backupModel.readStatistics();
    }

    /**
     * Read the incoming e-mail invitations.
     * 
     * @return A <code>List</code> of <code>IncomingEMailInvitaiton</code>s.
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations() {
        return contactModel.readIncomingEMailInvitations();
    }

    /**
     * Read the incoming user invitations.
     * 
     * @return A <code>List</code> of <code>IncomingUserInvitaiton</code>s.
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations() {
        return contactModel.readIncomingUserInvitations();
    }

    /**
     * Read the user's profile.
     * 
     * @return A <code>Profile</code>.
     */
    public Profile readProfile() {
        return profileModel.read();
    }

    /**
     * Determine the unseen container count.
     * 
     * @return The number of unseen containers.
     */
    public List<Container> readUnseenContainers() {
        return containerModel.read(new Filter<Artifact>() {
            public Boolean doFilter(final Artifact o) {
                return Boolean.valueOf(o.isSeen().booleanValue());
            }
        });
    }
}
