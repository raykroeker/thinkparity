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

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.ParityException;

/**
 * <b>Title:</b>thinkParity Internal Session Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.28
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalSessionModel extends SessionModel {

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
            final JabberId invitedBy, final Calendar acceptedOn);

    /**
     * Add an email to a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    public void addProfileEmail(final JabberId userId, final ProfileEMail email);

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
            final JabberId jabberId);

    /**
     * Archive an artifact. This will simply apply the archived flag within the
     * backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    public void archiveArtifact(final JabberId userId, final UUID uniqueId);

    public void confirmArtifactReceipt(final JabberId userId,
            final UUID uniqueId, final Long versionId,
            final JabberId publishedBy, final Calendar publishedOn,
            final List<JabberId> publishedTo, final JabberId receivedBy,
            final Calendar receivedOn);

    public void createArchiveStream(final JabberId userId,
            final String streamId, final UUID uniqueId, final Long versionId);

    /**
     * Send a creation packet to the parity server.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
	public void createArtifact(final JabberId userId, final UUID uniqueId,
            final Calendar createdOn);

    public void createBackupStream(final JabberId userId,
            final String streamId, final UUID uniqueId, final Long versionId);

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void createDraft(final List<JabberId> team, final UUID uniqueId);

    public String createStream(final StreamSession session);

	/**
     * Initialize a stream.
     * 
     * @return A <code>StreamSession</code>.
     */
    public StreamSession createStreamSession();

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
    public void declineInvitation(final EMail invitedAs, final JabberId invitedBy);

	/**
     * Delete an artifact from the backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    public void deleteArtifact(final JabberId userId, final UUID uniqueId);

    /**
     * Delete a contact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void deleteContact(final JabberId userId, final JabberId contactId);

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
            final EMail invitedAs, final Calendar deletedOn);

    /**
     * Delete a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void deleteDraft(final UUID uniqueId);

	/**
     * Delete a stream session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    public void deleteStreamSession(final StreamSession session);

    /**
     * Extend an invitation to a contact.
     * 
     * @param extendTo
     *            An <code>EMail</code> to invite.
     */
    public void extendInvitation(final EMail extendTo);

    /**
     * Handle the remote session established event.
     *
     */
    public void handleSessionEstablished();

    /**
     * Handle the remote session terminated event.
     *
     */
    public void handleSessionTerminated();

    /**
     * Handle the remote session terminated event.
     * 
     * @param cause
     *            The cause of the termination.
     * 
     */
    public void handleSessionTerminated(final Exception cause);

    /**
     * Determine the availability of an e-mail address.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @return True if the email is not being used.
     */
    public Boolean isEmailAvailable(final JabberId userId, final EMail email);

    /**
     * Process the remote event queue.
     * 
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void processQueue();

    // TODO-javadoc InternalSessionModel#publish
    public void publish(final ContainerVersion container,
            final Map<DocumentVersion, String> documents,
            final List<TeamMember> teamMembers, final JabberId publishedBy,
            final Calendar publishedOn, final List<User> publishedTo);

    public Container readArchiveContainer(final JabberId userId,
            final UUID uniqueId);

    /**
     * Read the archived containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A list of conatiners.
     */
    public List<Container> readArchiveContainers(final JabberId userId);

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
            final JabberId userId, final UUID uniqueId);

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
            final UUID uniqueId, final Long versionId);

    public DocumentVersion readArchiveDocumentVersion(final JabberId userId,
            final UUID uniqueId, final UUID documentUniqueId,
            final Long documentVersionId);

    public Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final JabberId userId, final UUID uniqueId,
            final Long compareVersionId);

    public Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final JabberId userId, final UUID uniqueId,
            final Long compareVersionId, final Long compareToVersionId);

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
            final JabberId userId, final UUID uniqueId, final Long versionId);

    public Map<User, ArtifactReceipt> readArchivePublishedTo(
            final JabberId userId, final UUID uniqueId, final Long versionId);

    public List<TeamMember> readArchiveTeam(final JabberId userId,
            final UUID uniqueId);

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
            final UUID uniqueId);

    public Container readBackupContainer(final JabberId userId,
            final UUID uniqueId);

    /**
     * Read the backup containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readBackupContainers(final JabberId userId);

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
            final JabberId userId, final UUID uniqueId);

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
            final UUID uniqueId, final Long versionId);

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
            final JabberId userId, final UUID uniqueId, final Long versionId);

    public Map<User, ArtifactReceipt> readBackupPublishedTo(
            final JabberId userId, final UUID uniqueId, final Long versionId);

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
            final UUID uniqueId);

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    public Contact readContact(final JabberId userId, final JabberId contactId);

    /**
     * Read the contacts.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List</code> of <code>Contact</code>s.
     */
    public List<Contact> readContactList(final JabberId userId);

    /**
     * Return the remote date and time.
     * 
     * @return A <code>Calendar</code>.
     */
    public Calendar readDateTime();

    /**
     * Read the artifact key holder.
     * 
     * @param artifactUniqueId
     *            The artifact unique id.
     * @return The artifact key holder.
     * @throws ParityException
     */
    public JabberId readKeyHolder(final JabberId userId, final UUID uniqueId);

	/**
     * Read the user's profile.
     * 
     * @return A profile.
     */
    public Profile readProfile();

    /**
     * Read the user's profile email addresses.
     * 
     * @return A list of profile email addresses.
     */
    public List<EMail> readProfileEMails();

    /**
     * Read the user profile's security question.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A security question <code>String</code>.
     */
    public String readProfileSecurityQuestion(final JabberId userId);

    public Integer readQueueSize();

	/**
     * Read a thinkParity user from the server.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>
     */
    public User readUser(final JabberId userId);

    /**
     * Remove an email from a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    public void removeProfileEmail(final JabberId userId, final ProfileEMail email);
	
    /**
     * Remove a team member from the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    public void removeTeamMember(final UUID uniqueId,
            final List<JabberId> team, final JabberId jabberId);

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
            final String securityAnswer);

    /**
     * Restore an artifact. This will simply remove the archived flag within the
     * backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>Long</code>.
     */
    public void restoreArtifact(final JabberId userId, final UUID uniqueId);

    /**
     * Update the a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param profile
     *            The user's profile.
     */
    public void updateProfile(final JabberId userId, final Profile profile);

    /**
     * Update the profile's credentials.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param credentials
     *            A user's <code>Credentials</code>.
     */
    public void updateProfileCredentials(final JabberId userId,
            final Credentials credentials);

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
            final ProfileEMail email, final String key);
}
