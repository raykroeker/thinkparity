/*
 * Feb 13, 2006
 */
package com.thinkparity.ophelia.model.session;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.OS;
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
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.util.ProcessMonitor;

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

    // TODO-javadoc InternalSessionModel#createDraft()
    public void createDraft(final List<JabberId> team, final UUID uniqueId,
            final Calendar createdOn);

    /**
     * Create a migrator stream for a list of resources.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     */
    public void createMigratorStream(final String streamId,
            final List<Resource> resources);

    public String createStream(final StreamSession session);

    /**
     * Initialize a stream.
     * 
     * @return A <code>StreamSession</code>.
     */
    public StreamSession createStreamSession();

    // TODO-javadoc InternalSessionModel#declineContactEMailInvitation
    public void declineContactEMailInvitation(final EMail invitedAs,
            final JabberId invitedBy, final Calendar declinedOn);

    // TODO-javadoc InternalSessionModel#declineContactUserInvitation
    public void declineContactUserInvitation(final JabberId invitedBy,
            final Calendar declinedOn);

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
     * @param invitedAs
     *            The invitation <code>EMail</code> address.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    public void deleteContactEMailInvitation(final EMail invitedAs,
            final Calendar deletedOn);

    
    /**
     * Delete a contact invitation.
     * 
     * @param invitedAs
     *            The invitation user id <code>JabberId</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    public void deleteContactUserInvitation(final JabberId invitedAs,
            final Calendar deletedOn);

    // TODO-javadoc InternalSessionModel#deleteDraft()
    public void deleteDraft(final UUID uniqueId, final Calendar deletedOn);

    /**
     * Delete a stream session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    public void deleteStreamSession(final StreamSession session);

	/**
     * Deploy a migrator release.
     * 
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    public void deployMigrator(final Product product, final Release release,
            final List<Resource> resources, final String streamId);

    /**
     * Extend an invitation to a user.
     * 
     * @param extendTo
     *            An <code>EMail</code> address.
     * @param extendedOn
     *            The extended on <code>Calendar</code>.
     */
    public void extendContactEMailInvitation(final EMail extendTo,
            final Calendar extendedOn);

    /**
     * Extend an invitation to a user.
     * 
     * @param extendTo
     *            A user id <code>JabberId</code>.
     * @param extendedOn
     *            The extended on <code>Calendar</code>.
     */
    public void extendContactUserInvitation(final JabberId extendTo,
            final Calendar extendedOn);

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
     * Determine if publish is restricted to the publish to user.
     * 
     * @param publishTo
     *            A publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    public Boolean isPublishRestricted(final JabberId publishTo);

    /**
     * Login for the first time in this workspace.
     * 
     * @param credentials
     *            A set of <code>Credentials</code>.
     * @throws InvalidCredentialsException
     *             if either the username or password do not match
     */
    public void login(final Credentials credentials)
            throws InvalidCredentialsException;

    /**
     * Process the remote event queue.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void processQueue(final ProcessMonitor monitor);

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
     * Read the contact ids.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List</code> of contact id <code>JabberId</code>s.
     */
    public List<JabberId> readContactIds();

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
     * Read the latest release.
     * 
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public Release readMigratorLatestRelease(final UUID productUniqueId,
            final OS os);

    /**
     * Read a migrator product.
     * 
     * @param name
     *            A product name.
     * @return A <code>Product</code>.
     */
    public Product readMigratorProduct(final String name);

    /**
     * Read a migrator release.
     * 
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @param name
     *            A release name.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public Release readMigratorRelease(final UUID productUniqueId,
            final String name, final OS os);

    /**
     * Read migrator release resources.
     * 
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @param name
     *            A release name.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public List<Resource> readMigratorResources(final UUID productUniqueId,
            final String releaseName, final OS os);

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
     * Register the remote event queue listener. 
     *
     */
    public void registerQueueListener();
	
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
    public void updateProfilePassword(final String password,
            final String newPassword);

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
