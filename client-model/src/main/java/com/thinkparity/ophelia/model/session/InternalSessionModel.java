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
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.TemporaryCredentials;
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
     * @param invitation
     *            An <code>IncomingEMailInvitation<code>.
     * @param acceptedOn
     *            The accepted on <code>Calendar</code>.
     */
    public void acceptInvitation(final IncomingEMailInvitation invitation,
            final Calendar acceptedOn);

    /**
     * Accept the contact invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation<code>.
     * @param acceptedOn
     *            The accepted on <code>Calendar</code>.
     */
    public void acceptInvitation(final IncomingUserInvitation invitation,
            final Calendar acceptedOn);

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

    public void confirmArtifactReceipt(final UUID uniqueId,
            final Long versionId, final JabberId publishedBy,
            final Calendar publishedOn, final List<JabberId> publishedTo,
            final JabberId receivedBy, final Calendar receivedOn);

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
     * Create an outgoing e-mail invitation.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    public void createInvitation(final OutgoingEMailInvitation invitation);

    /**
     * Create an outgoing user invitation.
     * 
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    public void createInvitation(final OutgoingUserInvitation invitation);

    /**
     * Create a migrator stream for a list of resources.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     */
    public void createMigratorStream(final String streamId,
            final Product product, final Release release,
            final List<Resource> resources);

    public void createProfile(final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final String securityQuestion,
            final String securityAnswer);

    public TemporaryCredentials createProfileCredentials(
            final String profileKey, final String securityAnswer);

    public EMailReservation createProfileEMailReservation(final EMail email);

    public UsernameReservation createProfileUsernameReservation(
            final String username);

    /**
     * Create a stream.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @return A stream id <code>String</code>.
     */
    public String createStream(final StreamSession session);

    /**
     * Initialize a stream.
     * 
     * @return A <code>StreamSession</code>.
     */
    public StreamSession createStreamSession();

    /**
     * Decline an incoming e-mail invitation.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     * @param declinedOn
     *            A declined on <code>Calendar</code>.
     */
    public void declineInvitation(final IncomingEMailInvitation invitation,
            final Calendar declinedOn);

    /**
     * Decline an incoming user invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation</code>.
     * @param declinedOn
     *            A declined on <code>Calendar</code>.
     */
    public void declineInvitation(final IncomingUserInvitation invitation,
            final Calendar declinedOn);

    /**
     * Delete a contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void delete(final JabberId contactId);

    /**
     * Delete an artifact from the backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    public void deleteArtifact(final JabberId userId, final UUID uniqueId);

    // TODO-javadoc InternalSessionModel#deleteDraft()
    public void deleteDraft(final UUID uniqueId, final Calendar deletedOn);

    /**
     * Delete a contact invitation.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    public void deleteInvitation(final OutgoingEMailInvitation invitation,
            final Calendar deletedOn);

    /**
     * Delete a contact invitation.
     * 
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    public void deleteInvitation(final OutgoingUserInvitation invitation,
            final Calendar deletedOn);

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
     * Handle a remote session error.
     * 
     * @param cause
     *            The <code>Throwable</code> cause.
     */
    public void handleSessionError(final Throwable cause);
    
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
     * Determine if the backup is online.
     * 
     * @return True if the backup server is online.
     */
    public Boolean isBackupOnline();

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
     * Determine whether or not this is the first login.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if this is the first login.
     */
    public Boolean isFirstLogin(final JabberId userId);

    /**
     * Determine if publish is restricted to the publish to user.
     * 
     * @param publishTo
     *            A publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    public Boolean isPublishRestricted(final JabberId publishTo);

    /**
     * Determine whether or not the xmpp session is online.
     * 
     * @return True if the xmpp session is online.
     */
    public Boolean isXMPPOnline();

    /**
     * Log an error.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param error
     *            An <code>Error</code>.
     * @param occuredOn
     *            The date/time the error occured.
     */
    public void logError(final Product product, final Release release,
            final Error error);

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
     * Fire the client maintenance event.
     *
     */
    public void notifyClientMaintenance();

    /**
     * Process the remote event queue.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void processQueue(final ProcessMonitor monitor);

    // TODO-javadoc InternalSessionModel#publish
    public void publish(final ContainerVersion version,
            final ContainerVersion latestVersion,
            final Map<DocumentVersion, String> documents,
            final List<TeamMember> teamMembers,
            final List<ArtifactReceipt> receivedBy, final JabberId publishedBy,
            final Calendar publishedOn, final List<EMail> publishedToEMails,
            final List<User> publishedToUsers);

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

    public List<ArtifactReceipt> readBackupPublishedTo(final JabberId userId,
            final UUID uniqueId, final Long versionId);

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
     *            A contact id <code>JabberId</code>.
     * @return A <code>Contact</code>.
     */
    public Contact readContact(final JabberId contactId);

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
     * Read all incoming e-mail invitations.
     * 
     * @return A <code>List</code> of <code>IncomingEMailInvitation</code>s.
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations();

    /**
     * Read all incoming user invitations.
     * 
     * @return A <code>List</code> of <code>IncomingUserInvitation</code>s.
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations();

    /**
     * Read the artifact key holder.
     * 
     * @param artifactUniqueId
     *            The artifact unique id.
     * @return The artifact key holder.
     * @throws ParityException
     */
    public JabberId readKeyHolder(final UUID uniqueId);

    /**
     * Read the latest release.
     * 
     * @param productName
     *            A product name <code>String</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public Release readMigratorLatestRelease(final String productName,
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
     * Read a migrator product's features.
     * 
     * @param name
     *            A product name.
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> readMigratorProductFeatures(final String name);

    /**
     * Read a migrator release.
     * 
     * @param productName
     *            A product name <code>String</code>.
     * @param name
     *            A release name.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public Release readMigratorRelease(final String productName,
            final String name, final OS os);

    /**
     * Read migrator release resources.
     * 
     * @param productName
     *            A product name <code>String</code>.
     * @param name
     *            A release name <code>String</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public List<Resource> readMigratorResources(final String productName,
            final String releaseName, final OS os);

    /**
     * Read all outgoing e-mail invitations.
     * 
     * @return A <code>List</code> of <code>OutgoingEMailInvitation</code>s.
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations();

    /**
     * Read all outgoing user invitations.
     * 
     * @return A <code>List</code> of <code>OutgoingUserInvitation</code>s.
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations();

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
    public List<ProfileEMail> readProfileEMails();

    /**
     * Read a user's profile features.
     * 
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> readProfileFeatures(final JabberId userId);

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
     * Read the backup statistics.
     * 
     * @return The <code>Statistics</code>.
     */
    public Statistics readStatistics();

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
     * Update the profile's password.
     * 
     * @param credentials
     *            A user's <code>Credentials</code>.
     * @param newPassword
     *            The new profile password<code>String</code>.
     */
    public void updateProfilePassword(final Credentials credentials,
            final String newPassword);

    /**
     * Update the profile's credentials.
     * 
     * @param credentials
     *            A user's <code>TemporaryCredentials</code>.
     * @param newPassword
     *            The new profile password<code>String</code>.
     */
    public void updateProfilePassword(final TemporaryCredentials credentials,
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
