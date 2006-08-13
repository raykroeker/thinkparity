/*
 * Feb 6, 2005
 */
package com.thinkparity.model.xmpp;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.profile.Profile;
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
     * Add a team member. This will create the team member relationship in the
     * distributed network with a pending state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            A jabber id.
     * @throws SmackException
     */
    public void addTeamMember(final UUID artifactUniqueId,
            final JabberId jabberId) throws SmackException;

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
     */
    public void createArtifact(final UUID uniqueId) throws SmackException;

    /**
     * Create a draft for an artifact.
     * 
     * @param artifact
     *            An artifact.
     */
    public void createDraft(final UUID uniqueId);

    /**
     * Decline an invitation from a user.
     * 
     * @param jabberId
     *            The user id.
     * @throws SmackException
     */
    public void declineInvitation(final JabberId jabberId) throws SmackException;

    /**
     * Delete an artifact
     * 
     * @param uniqueId
     *            The artifact unique id.
     */
    public void deleteArtifact(final UUID uniqueId);

    /**
     * Invite a contact.
     * 
     * @param email
     *            An e-mail address.
     * @throws SmackException
     */
	public void inviteContact(final String email) throws SmackException;

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
     * Publish a container.
     * 
     * @param version
     *            A container version.
     * @param documentVersions
     *            A list of document versions; and their content input streams.
     * @throws SmackException
     */
    public void publish(final ContainerVersion version,
            final Map<DocumentVersion, InputStream> documentVersions,
            final JabberId publishedBy, final Calendar publishedOn)
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
     * Read the artifact key holder.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return A jabber id.
     */
	public JabberId readKeyHolder(final UUID uniqueId);

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

    public void removeListener(final XMPPArtifactListener l);

    public void removeListener(final XMPPContactListener l);
    public void removeListener(final XMPPExtensionListener l);

    public void removeListener(final XMPPSessionListener l);

    /**
     * Remove a team member from the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    public void removeTeamMember(final UUID uniqueId, final JabberId jabberId);

    /**
     * Send a container to a user.
     * 
     * @param version
     *            A container version.
     * @param documentVersions
     *            A list of document versions.
     * @param user
     *            A user.
     * @param sentBy
     *            By whom the container was sent.
     * @param sentOn
     *            When the container was sent.
     */
    public void send(final ContainerVersion version,
            final Map<DocumentVersion, InputStream> documentVersions,
            final User user, final JabberId sentBy, final Calendar sentOn)
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
