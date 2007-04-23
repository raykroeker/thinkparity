/*
 * Feb 6, 2005
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

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
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.TemporaryCredentials;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.Token;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.util.xmpp.event.SessionListener;
import com.thinkparity.ophelia.model.util.xmpp.event.XMPPEventListener;

/**
 * XMPPSession The XMPPSession is the main interface with which the client
 * application interacts. It provides functionality for connectivity,
 * registration of listeners for session events, roster events as well as
 * obtaining roster and user information. Additionally the session provides the
 * capability to send notes\files to other smack users.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2.1.37
 */
public interface XMPPSession {

    /**
     * Accept the e-mail invitation.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation<code>.
     * @param acceptedOn
     *            The accepted on <code>Calendar</code>.
     */
    public void acceptInvitation(final JabberId userId,
            final IncomingEMailInvitation invitation, final Calendar acceptedOn);

    /**
     * Accept the e-mail invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation<code>.
     * @param acceptedOn
     *            The accepted on <code>Calendar</code>.
     */
    public void acceptInvitation(final JabberId userId,
            final IncomingUserInvitation invitation, final Calendar acceptedOn);

    /**
     * Add an xmpp event listener.
     * 
     * @param <T>
     *            An <code>XMPPEvent</code> type.
     * @param eventClass
     *            An event <code>Class</code>.
     * @param listener
     *            An <code>XMPPEventListener</code>.
     */
    public <T extends XMPPEvent> void addListener(final Class<T> eventClass,
            final XMPPEventListener<T> listener);

    /**
     * Add an xmpp session event listener.
     * 
     * @param l
     *            An xmpp session event listener.
     */
    public void addListener(final SessionListener listener);

    /**
     * Add an email to a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEmail</code>.
     */
    public void addProfileEmail(final JabberId userId, final EMail email);

    /**
     * Add a team member. This will create the team member relationship in the
     * distributed network with a pending state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            A jabber id.
     * @throws SmackException
     */
    public void addTeamMember(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final JabberId teamMemberId);

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

    /**
     * Clear all xmpp session listeners.
     *
     */
    public void clearListeners();


    public void confirmArtifactReceipt(final JabberId userId,
            final UUID uniqueId, final Long versionId,
            final JabberId publishedBy, final Calendar publishedOn,
            final List<JabberId> publishedTo, final JabberId receivedBy,
            final Calendar receivedOn);

    /**
     * Create an artifact
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    public void createArtifact(final JabberId userId, final UUID uniqueId,
            final Calendar createdOn);

    /**
     * Open a document version's content.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A document unique id <code>UUID</code>.
     * @param versionId
     *            A document version id <code>Long</code>.
     * @return The document version's content.
     */
    public void createBackupStream(final JabberId userId,
            final String streamId, final UUID uniqueId, final Long versionId);

    // TODO-javadoc XMPPSession#createDraft()
    public void createDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final Calendar createdOn);

    /**
     * Create an outgoing e-mail invitation.
     *
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    public void createInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation);

    /**
     * Create an outgoing user invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    public void createInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation);

    public void createMigratorProduct(final JabberId userId,
            final Product product);

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
    public void createMigratorStream(final JabberId userId,
            final String streamId, final Product product,
            final Release release, final List<Resource> resources);

    public void createProfile(final JabberId userId,
            final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final String securityQuestion,
            final String securityAnswer);

    public TemporaryCredentials createProfileCredentials(
            final String profileKey, final String securityAnswer,
            final Calendar createdOn);

    public EMailReservation createProfileEMailReservation(
            final JabberId userId, final EMail email, final Calendar reservedOn);

    public UsernameReservation createProfileUsernameReservation(
            final JabberId userId, final String username,
            final Calendar reservedOn);

    /**
     * Create a stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param session
     *            A <code>StreamSession</code>.
     * @return A stream id <code>String</code>.
     */
    public String createStream(final JabberId userId,
            final StreamSession session);

    /**
     * Create a stream session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>StreamSession</code>.
     */
    public StreamSession createStreamSession(final JabberId userId);

    /**
     * Create a new token for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Token</code>.
     */
    public Token createToken(final JabberId userId);

    /**
     * Accept the e-mail invitation.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation<code>.
     * @param acceptedOn
     *            The accepted on <code>Calendar</code>.
     */
    public void declineInvitation(final JabberId userId,
            final IncomingEMailInvitation invitation, final Calendar declinedOn);

    /**
     * Accept the e-mail invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation<code>.
     * @param acceptedOn
     *            The accepted on <code>Calendar</code>.
     */
    public void declineInvitation(final JabberId userId,
            final IncomingUserInvitation invitation, final Calendar declinedOn);

    /**
     * Delete a contact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void delete(final JabberId userId, final JabberId contactId);

    /**
     * Archive an artifact. This will simply apply the archived flag within the
     * backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    public void deleteArtifact(final JabberId userId, final UUID uniqueId);

    /**
     * Delete an artifact draft.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void deleteDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final Calendar deletedOn);

    /**
     * Delete a contact invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    public void deleteInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation, final Calendar deletedOn);

    /**
     * Delete a contact invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    public void deleteInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation, final Calendar deletedOn);

    /**
     * Delete a stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param session
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    public void deleteStream(final JabberId userId,
            final StreamSession session, final String streamId);

    /**
     * Delete a stream session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    public void deleteStreamSession(final JabberId userId,
            final StreamSession session);

    /**
     * Deploy a migrator release.
     * 
     * @param userId
     *            A user id <coder>JabberId</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    public void deployMigrator(final JabberId userId, final Product product,
            final Release release, final List<Resource> resources,
            final String streamId);

    /**
     * Determine if the backup is online.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the backup is online.
     */
    public Boolean isBackupOnline(final JabberId userId);
    
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
     * Determine if the session is online.
     * 
     * @return True if the connection is established and authenticated.
     */
    public Boolean isOnline();

    /**
     * Determine if publish is restricted for the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param publishFrom
     *            The publish from user id <code>JabberId</code>.
     * @param publishTo
     *            The publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    public Boolean isPublishRestricted(final JabberId userId,
            final JabberId publishFrom, final JabberId publishTo);

    /**
     * Log an error.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param error
     *            An <code>Error</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     */
    public void logError(final JabberId userId, final Product product,
            final Release release, final Error error);

    /**
     * Login.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code> to login to.
     * @param credentials
     *            The user's <code>Credentials</code>.
     * @throws SmackException
     */
	public void login(final Environment environment,
            final Credentials credentials) throws InvalidCredentialsException;

    /**
     * Logout.
     * 
     */
	public void logout();

    /**
     * Process the remotely queued events.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    public void processEventQueue(final ProcessMonitor monitor,
            final JabberId userId);

    /**
     * Publish a container version. The latest version is optional and required
     * when publishing a new version only, the received by list is required when
     * publishing an existing version only.
     * 
     * @param userId
     *            The session user id <code>JabberId</code>.
     * @param version
     *            The <code>ContainerVersion</code> to publish.
     * @param latestVersion
     *            The optional latest <code>ContainerVersion</code>.
     * @param documents
     *            A <code>Map</code> of <code>DocumentVersion</code>s to
     *            their corresponding stream id <code>String</code>s.
     * @param teamMembers
     *            A <code>List</code> of <code>TeamMember</code>s.
     * @param receivedBy
     *            An optional <code>List</code> of
     *            <code>ArtifactReceipt</code>s where the recipient has
     *            already received the artifact.
     * @param publishedBy
     *            A published by user id <code>JabberId</code>.
     * @param publishedOn
     *            A published on <code>Calendar</code>.
     * @param publishedToEMails
     *            A <code>List</code> of <code>EMail</code> addresses to
     *            publish to.
     * @param publishedToUsers
     *            A <code>List</code> of <code>User</code>s to publish to.
     */
    public void publish(final JabberId userId, final ContainerVersion version,
            final ContainerVersion latestVersion,
            final Map<DocumentVersion, String> documents,
            final List<TeamMember> teamMembers,
            final List<ArtifactReceipt> receivedBy, final JabberId publishedBy,
            final Calendar publishedOn, final List<EMail> publishedToEMails,
            final List<User> publishedToUsers);


    /**
     * Read the archive's containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A list of containers.
     */
    public Container readArchiveContainer(final JabberId userId, final UUID uniqueId);

    /**
     * Read the archive's containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A list of containers.
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
            final JabberId userId, final UUID uniqueId, final Long compareVersionId);

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
     * Read the archive team for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return An archive team.
     */
    public List<JabberId> readArchiveTeamIds(final JabberId userId,
            final UUID uniqueId);

	/**
     * Read the backup's containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A list of containers.
     */
    public Container readBackupContainer(final JabberId userId, final UUID uniqueId);

    /**
     * Read the backup's containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readBackupContainers(final JabberId userId);

    /**
     * Read the backup containers versions.
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
     * Read the backup's documents.
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
     * Read the backup's document versions.
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
     * Read the backup's team for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A backup team.
     */
    public List<JabberId> readBackupTeamIds(final JabberId userId,
            final UUID uniqueId);

    /**
     * Read a user's contact.
     * 
     * @param userId
     *            A user id.
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    public Contact readContact(final JabberId userId, final JabberId contactId);

    /**
     * Read a user's contact ids.
     * 
     * @param userId
     *            A user id.
     * @return A <code>List</code> of contact id <code>JabberId</code>s.
     */
    public List<JabberId> readContactIds(final JabberId userId);

    /**
     * Read a user's contacts.
     * 
     * @param userId
     *            A user id.
     * @return A list of contacts.
     */
    public List<Contact> readContacts(final JabberId userId);

    /**
     * Obtain the remote date/time.
     * 
     * @return A <code>Calendar</code>.
     */
    public Calendar readDateTime(final JabberId userId);

    /**
     * Obtain the size of the event queue.
     * 
     * @return The size of the event queue.
     */
    public Integer readEventQueueSize(final JabberId userId);

    /**
     * Read all incoming e-mail invitations.
     * 
     * @return A <code>List</code> of <code>IncomingInvitation</code>s.
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations(
            final JabberId userId);

    /**
     * Read all incoming user invitations.
     * 
     * @return A <code>List</code> of <code>IncomingInvitation</code>s.
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations(
            final JabberId userId);

	/**
     * Read the artifact key holder.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return A jabber id.
     */
	public JabberId readKeyHolder(final JabberId userId, final UUID uniqueId);

    /**
     * Read the latest release.
     * 
     * @param productName
     *            A product name <code>String</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public Release readMigratorLatestRelease(final JabberId userId,
            final String productName, final OS os);

    /**
     * Read a migrator product.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param name
     *            A product name.
     * @return A <code>Product</code>.
     */
    public Product readMigratorProduct(final JabberId userId, final String name);

    /**
     * Read the migrator product features.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param name
     *            A product name <code>String</code>.
     * @return A <code>List</code> of <code>Feature</code>.
     */
    public List<Feature> readMigratorProductFeatures(final JabberId userId,
            final String name);

    /**
     * Read a migrator release.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param productName
     *            A product name <code>String</code>.
     * @param name
     *            A release name.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public Release readMigratorRelease(final JabberId userId,
            final String productName, final String name, final OS os);

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
    public List<Resource> readMigratorResources(final JabberId userId,
            final String productName, final String releaseName, final OS os);

    /**
     * Read all outgoing e-mail invitations.
     * 
     * @return A <code>List</code> of <code>OutgoingEMailInvitation</code>s.
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(final JabberId userId);
    /**
     * Read all outgoing user invitations.
     * 
     * @return A <code>List</code> of <code>OutgoingUserInvitation</code>s.
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations(final JabberId userId);

    /**
     * Read the user's profile.
     * 
     * @return A profile.
     */
    public Profile readProfile();

    /**
     * Read the user's profile emails addresses.
     * 
     * @return A <code>List</code> of <code>ProfileEMail</code>s.
     */
    public List<ProfileEMail> readProfileEMails();

    /**
     * Read a profile's features.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> readProfileFeatures(final JabberId userId);

    /**
     * Read the user profile's security question.
     * 
     * @param profileKey
     *            A profile key can be either a username or an e-mail address.
     * @return A security question <code>String</code>.
     */
    public String readProfileSecurityQuestion(final String profileKey);

    /**
     * Read the backup statistics.
     * 
     * @return The <code>Statistics</code>.
     */
    public Statistics readStatistics(final JabberId userId);

    /**
     * Read a stream session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A stream session id <code>String</code>.
     * @return A <code>StreamSession</code>.
     */
    public StreamSession readStreamSession(final JabberId userId,
            final String sessionId);

    /**
     * Read a user's token.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A user's <code>Token</code>.
     */
    public Token readToken(final JabberId userId);

    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * 
     * @return A <code>User</code>.
     */
    public User readUser(final JabberId userId);

    /**
     * Register the remote queue listener. The queue listener will monitor the
     * session and when remote events are queued will fire the appropriate local
     * event notification.
     * 
     */
    public void registerQueueListener();

    /**
     * Remove a session listener.
     * 
     * @param listener
     *            A <code>SessionListener</code>.
     */
    public void removeListener(final SessionListener listener);

    /**
     * Remove an email from a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEmail</code>.
     */
    public void removeProfileEmail(final JabberId userId, final EMail email);

    /**
     * Remove a team member from the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    public void removeTeamMember(final JabberId userId,
            final List<JabberId> team, final UUID uniqueId,
            final JabberId teamMemberId);

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
     * Update the user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param profile
     *            The user's <code>Profile</code>.
     */
    public void updateProfile(final JabberId userId, final Profile profile);

    /**
     * Update a profile's password.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param credentials
     *            A profile's <code>Credentials</code>.
     * @param newPassword
     *            A new password <code>String</code>.
     */
    public void updateProfilePassword(final JabberId userId,
            final Credentials credentials, final String newPassword);

    /**
     * Update the profile's password using a set of temporary credentials.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param credentials
     *            A profile's <code>TemporaryCredentials</code>.
     * @param newPassword
     *            A new password <code>String</code>.
     */
    public void updateProfilePassword(final JabberId userId,
            final TemporaryCredentials credentials, final String newPassword);

    /**
     * Verify an email in a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            A <code>ProfileEmail</code>.
     * @param key
     *            A verification key <code>String</code>.
     */
    public void verifyProfileEmail(final JabberId userId, final EMail email,
            final String key);
}
