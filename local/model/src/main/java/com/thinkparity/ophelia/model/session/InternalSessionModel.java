/*
 * Feb 13, 2006
 */
package com.thinkparity.ophelia.model.session;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.workspace.Workspace;

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
	InternalSessionModel(final Environment environment,
            final Workspace workspace, final Context context) {
		super(environment, workspace);
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
    public void addTeamMember(final UUID uniqueId, final List<JabberId> team,
            final JabberId jabberId) {
        synchronized(getImplLock()) {
            getImpl().addTeamMember(uniqueId, team, jabberId);
        }
    }

    /**
     * Archive an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    public void archiveArtifact(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().archiveArtifact(userId, uniqueId);
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
    public void confirmArtifactReceipt(final JabberId userId,
            final UUID uniqueId, final Long versionId,
            final JabberId receivedBy, final Calendar receivedOn) {
        synchronized(getImplLock()) {
            getImpl().confirmArtifactReceipt(userId, uniqueId, versionId, receivedBy, receivedOn);
        }
    }

    public void createArchiveStream(final JabberId userId,
            final String streamId, final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            getImpl().createArchiveStream(userId, streamId, uniqueId, versionId);
        }
    }

    /**
     * Send a creation packet to the parity server.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
	public void createArtifact(final JabberId userId, final UUID uniqueId) {
		synchronized (getImplLock()) {
            getImpl().createArtifact(userId, uniqueId);
		}
	}

	public void createBackupStream(final JabberId userId,
            final String streamId, final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            getImpl().createBackupStream(userId, streamId, uniqueId, versionId);
        }
    }

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void createDraft(final List<JabberId> team, final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().createDraft(team, uniqueId);
        }
    }

	public String createStream(final StreamSession session) {
        synchronized (getImplLock()) {
            return getImpl().createStream(session);
        }
    }

    /**
     * Initialize a stream.
     * 
     * @return A <code>StreamSession</code>.
     */
    public StreamSession createStreamSession() {
        synchronized (getImplLock()) {
            return getImpl().createStreamSession();
        }
    }

    /**
     * Deny the presence visibility request from user to the currently logged
     * in user.
     * 
     * @param user
     *            The user who's presence request the currently logged in user
     *            will deny.
     * @see SessionModel#acceptPresence(User)
     * @throws ParityException
     */
    public void declineInvitation(final EMail invitedAs, final JabberId invitedBy) {
        synchronized(getImplLock()) { getImpl().declineInvitation(invitedAs, invitedBy); }
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
     * Delete a stream session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    public void deleteStreamSession(final StreamSession session) {
        synchronized (getImplLock()) {
            getImpl().deleteStreamSession(session);
        }
    }

    /**
     * Extend an invitation to a contact.
     * 
     * @param extendTo
     *            An <code>EMail</code> to invite.
     */
    public void extendInvitation(final EMail extendTo) {
        synchronized (getImplLock()) {
            getImpl().extendInvitation(extendTo);
        }
    }

    /**
     * Handle the remote session established event.
     *
     */
    public void handleSessionEstablished() {
        synchronized (getImplLock()) {
            getImpl().handleSessionEstablished();
        }
    }

    /**
     * Handle the remote session terminated event.
     *
     */
    public void handleSessionTerminated() {
        synchronized (getImplLock()) {
            getImpl().handleSessionTerminated();
        }
    }

    /**
     * Handle the remote session terminated event.
     * 
     * @param cause
     *            The cause of the termination.
     * 
     */
    public void handleSessionTerminated(final Exception cause) {
        synchronized (getImplLock()) {
            getImpl().handleSessionTerminated(cause);
        }
    }

    /**
     * Process the remote event queue.
     * 
     */
    public void processQueue() {
        synchronized (getImplLock()) {
            getImpl().processQueue();
        }
    }

    /**
     * Publish a container version.
     * 
     * @param container
     *            A container version.
     * @param teamMembers
     *            A list of team members.
     * @param documents
     *            A list of documents and their stream ids.
     * @param publishedBy
     *            The publisher.
     * @param publishedOn
     *            The publish date.
     */
    public void publish(final ContainerVersion container,
            final Map<DocumentVersion, String> documents,
            final List<JabberId> team, final List<JabberId> publishTo,
            final JabberId publishedBy, final Calendar publishedOn) {
        synchronized (getImplLock()) {
            getImpl().publish(container, documents, team, publishTo,
                    publishedBy, publishedOn);
        }
    }

    public Container readArchiveContainer(final JabberId userId,
            final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveContainer(userId, uniqueId);
        }
    }

    /**
     * Read the archived containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A list of conatiners.
     */
    public List<Container> readArchiveContainers(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveContainers(userId);
        }
    }

    /**
     * Read the archived containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readArchiveContainerVersions(
            final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveContainerVersions(userId, uniqueId);
        }
    }

    /**
     * Read the archived containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;Document&gt;</code>.
     */
    public List<Document> readArchiveDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveDocuments(userId, uniqueId, versionId);
        }
    }

    /**
     * Read the archived document versions.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param documentUniqueId
     *            A document unique id <code>UUID</code>.
     * @return A <code>List&lt;DocumentVersion&gt;</code>.
     */
    public List<DocumentVersion> readArchiveDocumentVersions(
            final JabberId userId, final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveDocumentVersions(userId, uniqueId,
                    versionId);
        }
    }

    /**
     * Read the archive team.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A list of jabber ids.
     */
    public List<JabberId> readArchiveTeamIds(final JabberId userId,
            final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveTeamIds(userId, uniqueId);
        }
    }

    public Container readBackupContainer(final JabberId userId,
            final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readBackupContainer(userId, uniqueId);
        }
    }

    /**
     * Read the backup containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readBackupContainers(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readBackupContainers(userId);
        }
    }

    /**
     * Read the backup containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readBackupContainerVersions(
            final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readBackupContainerVersions(userId, uniqueId);
        }
    }

    /**
     * Read the backup containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;Document&gt;</code>.
     */
    public List<Document> readBackupDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readBackupDocuments(userId, uniqueId, versionId);
        }
    }

    /**
     * Read the backup document versions.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param documentUniqueId
     *            A document unique id <code>UUID</code>.
     * @return A <code>List&lt;DocumentVersion&gt;</code>.
     */
    public List<DocumentVersion> readBackupDocumentVersions(
            final JabberId userId, final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readBackupDocumentVersions(userId, uniqueId,
                    versionId);
        }
    }

    /**
     * Read the backup team.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A list of jabber ids.
     */
    public List<JabberId> readBackupTeamIds(final JabberId userId,
            final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readBackupTeamIds(userId, uniqueId);
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

    public Integer readQueueSize() {
        synchronized (getImplLock()) {
            return getImpl().readQueueSize();
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
    public void removeTeamMember(final UUID uniqueId,
            final List<JabberId> team, final JabberId jabberId) {
        synchronized (getImplLock()) {
            getImpl().removeTeamMember(uniqueId, team, jabberId);
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

    public void restoreArtifact(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().restoreArtifact(userId, uniqueId);
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
