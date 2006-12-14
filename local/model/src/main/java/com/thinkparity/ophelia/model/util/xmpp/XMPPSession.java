/*
 * Feb 6, 2005
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.util.xmpp.event.SessionListener;
import com.thinkparity.ophelia.model.util.xmpp.event.XMPPEventListener;

/**
 * XMPPSession
 * The XMPPSession is the main interface with which the client application
 * interacts.  It provides functionality for connectivity, registration of
 * listeners for session events, roster events as well as obtaining roster and
 * user information.  Additionally the session provides the capability to send
 * notes\files to other smack users.
 * @author raykroeker@gmail.com
 * @version 1.7
 */
public interface XMPPSession {

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
     * Archive an artifact.
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

    /**
     * Confirm artifact receipt.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @param versionId
     *            An artifact version id.
     * @param receivedBy
     *            By whom the artifact was received <code>JabberId</code>.
     */
    public void confirmArtifactReceipt(final JabberId userId,
            final List<JabberId> team, final UUID uniqueId,
            final Long versionId, final Calendar publishedOn,
            final JabberId receivedBy, final Calendar receivedOn);

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
    public void createArchiveStream(final JabberId userId,
            final String streamId, final UUID uniqueId, final Long versionId);

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

    /**
     * Create a draft for an artifact.
     * 
     * @param artifact
     *            An artifact.
     */
    public void createDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId);

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
     * Decline an invitation.
     * 
     * @param invitedAs
     *            The orginal invitation e-mail address.
     * @param invitedBy
     *            The inviting user.
     * @throws SmackException
     */
    public void declineInvitation(final EMail invitedAs,
            final JabberId invitedBy);

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
    public void deleteContactInvitation(final JabberId userId, final EMail invitedAs,
            final Calendar deletedOn);

    /**
     * Delete an artifact draft.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void deleteDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId);

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
     * Extend an invitation for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param extendedTo
     *            An <code>EMail</code> to extend the invitation to.
     * @param extendedOn
     *            The date <code>Calendar</code>.
     */
    public void extendInvitation(final JabberId userId, final EMail extendedTo,
            final Calendar extendedOn);

    /**
     * Determine if the user is logged in.
     * 
     * @return True if the user is logged in; false otherwise.
     */
    public Boolean isLoggedIn();

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
            final Credentials credentials);

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
     */
    public void processEventQueue(final JabberId userId);

    /**
     * Publish a container.
     * 
     * @param container
     *            A container.
     * @param documents
     *            A list of documents and their content.
     * @param team
     *            A <code>JabberId</code> list of the entire team.
     * @param publishTo
     *            A <code>JabberId</code> list of whom to publish to.
     * @param publishedBy
     *            By whom the container was published.
     * @param publishedOn
     *            When the container was published.
     */
    public void publish(final ContainerVersion container,
            final Map<DocumentVersion, String> documents,
            final List<JabberId> team, final List<JabberId> publishTo,
            final JabberId publishedBy, final Calendar publishedOn);

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
    public Map<User, ArtifactReceipt> readArchivePublishedTo(
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

    public Map<User, ArtifactReceipt> readBackupPublishedTo(
            final JabberId userId, final UUID uniqueId, final Long versionId);

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
     * Read the artifact key holder.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return A jabber id.
     */
	public JabberId readKeyHolder(final JabberId userId, final UUID uniqueId);

    /**
     * Read the user's profile.
     * 
     * @return A profile.
     */
    public Profile readProfile();

    /**
     * Read the user's profile emails addresses.
     * 
     * @return A list of profile emails addresses.
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
     * Reset a user's credentials.
     * 
     * @param userId
     *            A user id <code>JabberId</code>
     * @param securityAnswer
     *            A security question answer <code>String</code>.
     */
    public String resetProfilePassword(final JabberId userId,
            final String securityAnswer);

    /**
     * Restore an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
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
     * Update a user's credentials.
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
     *            A <code>ProfileEmail</code>.
     * @param key
     *            A verification key <code>String</code>.
     */
    public void verifyProfileEmail(final JabberId userId, final EMail email,
            final String key);
}
