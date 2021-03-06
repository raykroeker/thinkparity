/*
 * Created On:  14-Mar-07 2:33:10 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.filter.Filter;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.OfflineCode;
import com.thinkparity.ophelia.model.session.SessionModel;

/**
 * <b>Title:</b>thinkParity OpheliaUI Main Status Provider<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MainStatusProvider extends ContentProvider {

    /** An instance of <code>ContactModel</code>. */
    private final ContactModel contactModel;

    /** An instance of <code>ContainerModel</code>. */
    private final ContainerModel containerModel;

    /** An instance of <code>SessionModel</code>. */
    private final SessionModel sessionModel;

    /**
     * Create MainStatusProvider.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     * @param contactModel
     *            An instance of <code>ContactModel</code>.
     * @param containerModel
     *            An instance of <code>ContainerModel</code>.
     * @param sessionModel
     *            An instance of <code>SessionModel</code>.
     */
    public MainStatusProvider(final ProfileModel profileModel,
            final ContactModel contactModel,
            final ContainerModel containerModel,
            final SessionModel sessionModel) {
        super(profileModel);
        this.contactModel = contactModel;
        this.containerModel = containerModel;
        this.sessionModel = sessionModel;
    }

    /**
     * Obtain the session offline code.
     * 
     * @return The <code>OfflineCode</code>.
     */
    public OfflineCode getOfflineCode() {
        return sessionModel.getOfflineCode();
    }

    /**
     * Determine if the user's backup is online.
     * 
     * @return True if the user's backup is online.
     */
    public Boolean isOnline() {
        return sessionModel.isOnline();
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
     * Determine if the payment info is accessible.
     * 
     * @return True if the payment info is accessible.
     */
    public Boolean readIsAccessiblePaymentInfo() {
        return profileModel.isAccessiblePaymentInfo();
    }

    /**
     * Determine whether or not the profile's e-mail address has been verified.
     * 
     * @return True if it is verified.
     */
    public Boolean readIsEMailVerified() {
        return profileModel.readEMail().isVerified();
    }

    /**
     * Determine if the profile is active.
     * 
     * @return True if the profile is active.
     */
    public Boolean readIsProfileActive() {
        return profileModel.readIsActive();
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
     * Get the list of unseen container versions.
     * 
     * @return A <code>List</code> of <code>ContainerVersion</code>s.
     */
    public List<ContainerVersion> readUnseenContainerVersions() {
        final List<Container> unseenContainers = containerModel.read(new Filter<Artifact>() {
            public Boolean doFilter(final Artifact o) {
                return Boolean.valueOf(o.isSeen().booleanValue());
            }
        });
        final List<ContainerVersion> unseenContainerVersions = new ArrayList<ContainerVersion>();
        for (final Container container : unseenContainers) {
            unseenContainerVersions.addAll(containerModel.readVersions(
                    container.getId(), new Filter<ArtifactVersion>() {
                        public Boolean doFilter(final ArtifactVersion o) {
                            return Boolean.valueOf(o.isSeen().booleanValue());
                        }
                    }));
        }
        return unseenContainerVersions;
    }
}
