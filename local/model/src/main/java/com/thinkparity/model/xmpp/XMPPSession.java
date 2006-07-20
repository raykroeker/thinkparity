/*
 * Feb 6, 2005
 */
package com.thinkparity.model.xmpp;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.events.XMPPArtifactListener;
import com.thinkparity.model.xmpp.events.XMPPContactListener;
import com.thinkparity.model.xmpp.events.XMPPContainerListener;
import com.thinkparity.model.xmpp.events.XMPPDocumentListener;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;

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
     * Accept an invitation from a user.
     * 
     * @param jabberId
     *            The user id.
     * @throws SmackException
     */
    public void acceptInvitation(final JabberId jabberId) throws SmackException;

    /**
     * Add the logged in user as a team member.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @throws SmackException
     */
    public void addArtifactTeamMember(final UUID artifactUniqueId)
            throws SmackException;

    /**
     * Add an xmpp artifact event listener.
     * 
     * @param l
     *            The xmpp artifact event listener.
     */
    public void addListener(final XMPPArtifactListener l);

    /**
     * Add an xmpp contact event listener.
     * 
     * @param l
     *            The xmpp contact event listener.
     */
    public void addListener(final XMPPContactListener l);

    /**
     * Add an xmpp container event listener.
     * 
     * @param l
     *            The xmpp container event listener.
     */
    public void addListener(final XMPPContainerListener l);

	/**
     * Add an xmpp document event listener.
     * 
     * @param l
     *            The xmpp document event listener.
     */
    public void addListener(final XMPPDocumentListener l);

    public void addListener(final XMPPExtensionListener l);
	public void addListener(final XMPPSessionListener l);

    /**
     * Close an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @throws SmackException
     */
	public void closeArtifact(final UUID uniqueId) throws SmackException;

    /**
     * Confirm artifact receipt.
     * 
     * @param receivedFrom
     *            From whom the artifact was received.
     * @param uniqueId
     *            The artifact unique id.
     * @throws SmackException
     */
    public void confirmArtifactReceipt(final JabberId receivedFrom,
            final UUID uniqueId, final Long versionId) throws SmackException;

    /**
     * Create an artifact
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @throws SmackException
     */
	public void createArtifact(final UUID uniqueId) throws SmackException;

    /**
     * Decline an invitation from a user.
     * 
     * @param jabberId
     *            The user id.
     * @throws SmackException
     */
    public void declineInvitation(final JabberId jabberId) throws SmackException;

    /**
     * Determine if the user is logged in.
     * 
     * @return True if the user is logged in; false otherwise.
     */
    public Boolean isLoggedIn();

    /**
     * Login.
     * 
     * @param host
     *            The host.
     * @param port
     *            The port.
     * @param username
     *            The username.
     * @param password
     *            The password.
     * @throws SmackException
     */
	public void login(final String host, final Integer port,
			final String username, final String password) throws SmackException;

    /**
     * Logout.
     * 
     * @throws SmackException
     */
	public void logout() throws SmackException;

    /**
     * Process events queued on the server since last login.
     * 
     * @throws SmackException
     */
    public void processOfflineQueue() throws SmackException;

    /**
     * Reactivate a container version.
     * 
     * @param version
     *            The container version.
     * @param documentVersions
     *            The document versions.
     * @param team
     *            The container team.
     * @param reactivatedBy
     *            Who reactivated the container.
     * @param reactivatedOn
     *            When the container was reactivated.
     * @throws SmackException
     */
    public void reactivate(final ContainerVersion version,
            final List<DocumentVersionContent> documentVersions,
            final List<JabberId> team, final JabberId reactivatedBy,
            final Calendar reactivatedOn) throws SmackException;

    /**
     * Read the artifact key holder.
     * 
     * @param artifactUniqueId
     *            The artifact unique id.
     * @return The artifact key holder user info.
     * @throws SmackException
     */
	public User readArtifactKeyHolder(final UUID uniqueId)
            throws SmackException;

	/**
     * Read the artifact team.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return The users representing the artifact team.
     * @throws SmackException
     */
    public List<User> readArtifactTeam(final UUID artifactUniqueId)
            throws SmackException;

    /**
     * Read the logged in user's contacts.
     * 
     * @return The logge in user's contacts.
     * @throws SmackException
     */
    public List<Contact> readContacts() throws SmackException;

    /**
     * Read the logged in user.
     * 
     * @return The user info.
     * @throws SmackException
     */
	public User readCurrentUser() throws SmackException;

    /**
     * Read the user's profile.
     * 
     * @return A profile.
     */
    public Profile readProfile() throws SmackException;

	/**
     * Read a set of users.
     * 
     * @param jabberIds
     *            The user ids to read.
     * @return A set of users.
     * @throws SmackException
     */
    public Set<User> readUsers(final Set<JabberId> jabberIds)
			throws SmackException;

    /**
     * Remove the logged in user from the artifact team.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @throws SmackException
     */
    public void removeArtifactTeamMember(final UUID uniqueId)
            throws SmackException;
    public void removeListener(final XMPPArtifactListener l);
    public void removeListener(final XMPPContactListener l);
    public void removeListener(final XMPPExtensionListener l);

    public void removeListener(final XMPPSessionListener l);

    /**
     * Request the key for an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @throws SmackException
     */
    public void requestArtifactKey(final UUID uniqueId) throws SmackException;

    /**
     * Execute a remote method call to reactivate a document.
     * 
     * @throws SmackException
     */
    public void sendDocumentReactivate(final List<JabberId> team,
            final UUID uniqueId, final Long versionId, final String name,
            final byte[] bytes) throws SmackException;

    /**
     * Send a document across the parity xmpp network.
     * 
     * @param sendTo
     *            The ids to send the document to.
     * @param uniqueId
     *            The document unique id.
     * @param versionId
     *            The document version id.
     * @param name
     *            The document name.
     * @param content
     *            The document content.
     * @throws SmackException
     */
	public void sendDocumentVersion(final Set<JabberId> sendTo,
            final UUID uniqueId, final Long versionId, final String name,
            final byte[] content) throws SmackException;

    /**
     * Send an invitation to a user.
     * 
     * @param jabberId
     *            The user id.
     * @throws SmackException
     */
	public void sendInvitation(final JabberId jabberId) throws SmackException;

    public void sendKeyResponse(final UUID artifactUniqueId,
            final KeyResponse keyResponse, final JabberId jabberId)
            throws SmackException;

    public void sendLogFileArchive(final File logFileArchive, final User user)
			throws SmackException;

    /**
     * Update the current user.
     * 
     * @param user
     *            The user.
     * @throws SmackException
     */
    public void updateUser(final User user) throws SmackException;
}
