/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.container.ContainerVersion;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.profile.ProfileEMail;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalSessionModel extends SessionModel implements InternalModel {

    /**
	 * Create a InternalSessionModel.
	 * 
	 * @param context
	 *            The model context.
	 */
	InternalSessionModel(final Workspace workspace, final Context context) {
		super(workspace);
		context.assertContextIsValid();
	}

    /**
     * Accept the contact invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedBy
     *            The invited by user id <code>JabberId</code>.
     * @param acceptedOn
     *            When the user accepted <code>Calendar</code>.
     */
    public void acceptContactInvitation(final JabberId userId,
            final JabberId invitedBy, final Calendar acceptedOn) {
        synchronized (getImplLock()) {
            getImpl().acceptContactInvitation(userId, invitedBy, acceptedOn);
        }
    }

    /**
     * Add an email to a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    public void addProfileEmail(final JabberId userId, final ProfileEMail email) {
        synchronized (getImplLock()) {
            getImpl().addProfileEmail(userId, email);
        }
    }

    /**
     * Add a team member. This will create the team member relationship in the
     * distributed network with a pending state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            A jabber id.
     */
    public void addTeamMember(final UUID uniqueId, final JabberId jabberId) {
        synchronized(getImplLock()) {
            getImpl().addTeamMember(uniqueId, jabberId);
        }
    }

    /**
     * Send an artifact received confirmation receipt.
     * 
     * @param receivedFrom
     *            From whom the artifact was received.
     * @param uniqueId
     *            The artifact unique id.
     */
    public void confirmArtifactReceipt(final JabberId receivedFrom,
            final UUID uniqueId, final Long versionId) throws SmackException {
        synchronized(getImplLock()) {
            getImpl().confirmArtifactReceipt(receivedFrom, uniqueId, versionId);
        }
    }

	/**
     * Send a creation packet to the parity server.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
	public void createArtifact(final UUID uniqueId) {
		synchronized (getImplLock()) { getImpl().createArtifact(uniqueId); }
	}

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void createDraft(final UUID uniqueId) {
        synchronized(getImplLock()) { getImpl().createDraft(uniqueId); }
    }

	/**
     * Delete an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void deleteArtifact(final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().deleteArtifact(uniqueId);
        }
    }

    /**
     * Delete a contact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void deleteContact(final JabberId userId, final JabberId contactId) {
        synchronized (getImplLock()) {
            getImpl().deleteContact(userId, contactId);
        }
    }

    /**
     * Delete a contact invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedAs
     *            The invitation <code>EMail</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    public void deleteContactInvitation(final JabberId userId,
            final EMail invitedAs, final Calendar deletedOn) {
        synchronized (getImplLock()) {
            getImpl().deleteContactInvitation(userId, invitedAs, deletedOn);
        }
    }

	/**
     * Delete a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void deleteDraft(final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().deleteDraft(uniqueId);
        }
    }

    /**
     * Extend an invitation to a contact.
     * 
     * @param extendTo
     *            An <code>EMail</code> to invite.
     */
    public void extendInvitation(final EMail extendTo) {
        synchronized(getImplLock()) { getImpl().extendInvitation(extendTo); }
    }

    /**
	 * Obtain the currently logged in user.
	 * 
	 * @return The logged in user.
	 * @throws ParityException
	 */
	public User getLoggedInUser() throws ParityException {
		synchronized(getImplLock()) { return getImpl().readUser(); }
	}

    /**
     * Publish a container version.
     * 
     * @param container
     *            A container version.
     * @param teamMembers
     *            A list of team members.
     * @param documents
     *            A list of documents and their input streams.
     * @param publishedBy
     *            The publisher.
     * @param publishedOn
     *            The publish date.
     */
    public void publish(final ContainerVersion container,
            final Map<DocumentVersion, InputStream> documents,
            final List<JabberId> publishTo, final JabberId publishedBy,
            final Calendar publishedOn) {
        synchronized (getImplLock()) {
            getImpl().publish(container, documents, publishTo, publishedBy, publishedOn);
        }
    }

    /**
     * Read the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return A list of jabber ids.
     */
    public List<JabberId> readArtifactTeam(final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readArtifactTeam(uniqueId);
        }
    }

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    public Contact readContact(final JabberId userId, final JabberId contactId) {
        synchronized (getImplLock()) {
            return getImpl().readContact(userId, contactId);
        }
    }

	/**
     * Read the user's contact list.
     * 
     * @return A list of contacts.
     * @throws ParityException
     */
    public List<Contact> readContactList(final JabberId userId) {
        synchronized(getImplLock()) { return getImpl().readContacts(userId); }
    }

    /**
     * Read the artifact key holder.
     * 
     * @param artifactUniqueId
     *            The artifact unique id.
     * @return The artifact key holder.
     * @throws ParityException
     */
    public JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readKeyHolder(userId, uniqueId);
        }
    }

    /**
     * Read the user's profile.
     * 
     * @return A profile.
     */
    public Profile readProfile() {
        synchronized (getImplLock()) {
            return getImpl().readProfile();
        }
    }

    /**
     * Read the user's profile email addresses.
     * 
     * @return A list of profile email addresses.
     */
    public List<EMail> readProfileEmails() {
        synchronized (getImplLock()) {
            return getImpl().readProfileEMails();
        }
    }

    /**
     * Read the user profile's security question.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A security question <code>String</code>.
     */
    public String readProfileSecurityQuestion(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readProfileSecurityQuestion(userId);
        }
    }

	/**
     * Read a thinkParity user from the server.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>
     */
    public User readUser(final JabberId userId) {
		synchronized (getImplLock()) {
            return getImpl().readUser(userId);
		}
	}

    /**
     * Read a set of users.
     * 
     * @param jabberIds
     *            A set of user ids.
     * @return A set of users.
     * @throws ParityException
     */
	public Set<User> readUsers(final Set<JabberId> jabberIds)
			throws ParityException {
		synchronized(getImplLock()) { return getImpl().readUsers(jabberIds); }
	}

    /**
     * Remove an email from a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    public void removeProfileEmail(final JabberId userId, final ProfileEMail email) {
        synchronized (getImplLock()) {
            getImpl().removeProfileEmail(userId, email);
        }
    }
	
    /**
     * Remove a team member from the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    public void removeTeamMember(final UUID uniqueId, final JabberId jabberId) {
        synchronized(getImplLock()) {
            getImpl().removeTeamMember(uniqueId, jabberId);
        }
    }

    /**
     * Reset the user's authentication credentials.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param securityAnswer
     *            A security question answer <code>String</code>.
     * @return A new password.
     */
    public String resetProfilePassword(final JabberId userId,
            final String securityAnswer) {
        synchronized (getImplLock()) {
            return getImpl().resetProfilePassword(userId, securityAnswer);
        }
    }

    /**
     * Send a container.
     * 
     * @param container
     *            A container.
     * @param documents
     *            A list of documents and their input.
     * @param sendTo
     *            A send to list.
     * @param sentBy
     *            A sent by id.
     * @param sentOn
     *            A sent on calendar.
     */
    public void send(final ContainerVersion container,
            final Map<DocumentVersion, InputStream> documents,
            final List<JabberId> sendTo, final JabberId sentBy,
            final Calendar sentOn) {
        synchronized(getImplLock()) {
            getImpl().send(container, documents, sendTo, sentBy, sentOn);
        }
    }

    /**
     * Update the a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param profile
     *            The user's profile.
     */
    public void updateProfile(final JabberId userId, final Profile profile) {
        synchronized (getImplLock()) {
            getImpl().updateProfile(userId, profile);
        }
    }

    /**
     * Update the profile's credentials.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param credentials
     *            A user's <code>Credentials</code>.
     */
    public void updateProfileCredentials(final JabberId userId,
            final Credentials credentials) {
        synchronized (getImplLock()) {
            getImpl().updateProfileCredentials(userId, credentials);
        }
    }

    /**
     * Verify an email in a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     * @param key
     *            A verification key <code>String</code>.
     */
    public void verifyProfileEmail(final JabberId userId,
            final ProfileEMail email, final String key) {
        synchronized (getImplLock()) {
            getImpl().verifyProfileEmail(userId, email, key);
        }
    }
}
