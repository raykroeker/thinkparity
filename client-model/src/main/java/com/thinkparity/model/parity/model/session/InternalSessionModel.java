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

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.JabberId;
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
     * Accept an invitation to the user's contact list.
     * 
     * @param jabberId
     *            The user's jabber id.
     * @throws ParityException
     */
    public void acceptInvitation(final JabberId invitedBy) {
        synchronized (getImplLock()) {
            getImpl().acceptInvitation(invitedBy);
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
	 * Obtain the currently logged in user.
	 * 
	 * @return The logged in user.
	 * @throws ParityException
	 */
	public User getLoggedInUser() throws ParityException {
		synchronized(getImplLock()) { return getImpl().readUser(); }
	}

    /**
     * Add a roster entry for the user. This will send a presence request to
     * user.
     * 
     * @param user
     *            The user to add to the roster.
     * @throws ParityException
     */
    public void inviteContact(final EMail email) {
        synchronized(getImplLock()) { getImpl().inviteContact(email); }
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
    public Contact readContact(final JabberId contactId) {
        synchronized (getImplLock()) {
            return getImpl().readContact(contactId);
        }
    }

    /**
     * Read the user's contact list.
     * 
     * @return A list of contacts.
     * @throws ParityException
     */
    public List<Contact> readContactList() {
        synchronized(getImplLock()) { return getImpl().readContacts(); }
    }

    /**
     * Read the artifact key holder.
     * 
     * @param artifactUniqueId
     *            The artifact unique id.
     * @return The artifact key holder.
     * @throws ParityException
     */
    public JabberId readKeyHolder(final UUID uniqueId) {
        synchronized(getImplLock()) {
            return getImpl().readKeyHolder(uniqueId);
        }
    }

	/**
     * Read the user's profile.
     * 
     * @return A profile.
     */
    public Profile readProfile() {
        synchronized(getImplLock()) { return getImpl().readProfile(); }
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
     * Update the local user's profile.
     * 
     * @param profile
     *            The user's profile.
     */
    public void updateProfile(final Profile profile) {
        synchronized (getImplLock()) {
            getImpl().updateProfile(profile);
        }
    }
}
