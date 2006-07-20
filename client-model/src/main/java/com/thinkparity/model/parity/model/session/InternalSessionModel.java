/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
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
	 * Obtain the currently logged in user.
	 * 
	 * @return The logged in user.
	 * @throws ParityException
	 */
	public User getLoggedInUser() throws ParityException {
		synchronized(getImplLock()) { return getImpl().readUser(); }
	}

    /**
     * Reactivate a container version.
     * 
     * @param version
     *            The version.
     * @param documentVersions
     *            The document versions.
     * @param team
     *            The team.
     * @param reactivatedBy
     *            Who reactivated.
     * @param reactivatedOn
     *            When reactivated.
     * @throws ParityException
     */
    public void reactivate(final ContainerVersion version,
            final List<DocumentVersionContent> documentVersions,
            final List<JabberId> team, final JabberId reactivatedBy,
            final Calendar reactivatedOn) throws ParityException {
        synchronized(getImplLock()) {
            getImpl().reactivate(version, documentVersions, team,
                    reactivatedBy, reactivatedOn);
        }
    }

	/**
     * Read the artifact key holder.
     * 
     * @param artifactUniqueId
     *            The artifact unique id.
     * @return The artifact key holder.
     * @throws ParityException
     */
    public JabberId readArtifactKeyHolder(final UUID artifactUniqueId)
            throws ParityException {
        synchronized(getImplLock()) {
            return getImpl().readArtifactKeyHolder(artifactUniqueId);
        }
    }

    public List<User> readArtifactTeamList(final Long artifactId) throws ParityException {
        synchronized(getImplLock()) { return getImpl().readArtifactTeam(artifactId); }
    }

	/**
     * Read the user's contact list.
     * 
     * @return A list of contacts.
     * @throws ParityException
     */
    public List<Contact> readContactList() throws ParityException {
        synchronized(getImplLock()) { return getImpl().readContacts(); }
    }

    /**
     * Read the user's profile.
     * 
     * @return A profile.
     */
    public Profile readProfile() {
        synchronized(getImplLock()) { return getImpl().readProfile(); }
    }

	public User readUser(final JabberId jabberId) throws ParityException {
		synchronized(getImplLock()) { return getImpl().readUser(jabberId); }
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
	 * Send a close packet to the parity server.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user is offline.
	 *             <li>If the logged in user is not the key holder.
	 *             </ul>
	 * @throws ParityException
	 */
	public void sendClose(final Long artifactId) throws ParityException {
		synchronized(getImplLock()) { getImpl().sendClose(artifactId); }
	}

    /**
	 * Send a creation packet to the parity server.
	 * 
	 * @param document
	 *            The document.
	 * @throws ParityException
	 */
	public void sendCreate(final Artifact artifact) throws ParityException {
		synchronized(getImplLock()) { getImpl().sendCreate(artifact); }
	}

    /**
	 * Send a delete packet to the parity server.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user is offline.
	 *             </ul>
	 */
	public void sendDelete(final Long artifactId) throws ParityException {
		synchronized(getImplLock()) { getImpl().sendDelete(artifactId); }
	}

    /**
     * Send the artifact key to a user.
     * 
     * @param artifactUniqueId
     *            The artifact unique id.
     * @param jabberId
     *            The user to send the key to.
     */
    public void sendKey(final UUID artifactUniqueId, final JabberId jabberId)
            throws ParityException {
        synchronized(getImplLock()) { getImpl().sendKey(artifactUniqueId, jabberId); }
    }

    /**
     * Send a key response [ACCEPT,DENY] to a user.
     * 
     * @param keyResponse
     *            The key response.
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            A jabber id.
     * @throws ParityException
     */
    public void sendKeyResponse(final KeyResponse keyResponse,
            final Long artifactId, final JabberId jabberId)
            throws ParityException {
        synchronized(getImplLock()) {
            getImpl().sendKeyResponse(keyResponse, artifactId, jabberId);
        }
    }

    /**
     * Send a key response.
     * 
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            The user's jabber id.
     * @param keyRepsonse
     *            The response [ACCEPT,DENY]
     * @throws ParityException
     */
    public void sendKeyResponse(final Long artifactId,
            final JabberId jabberId, final KeyResponse keyResponse)
            throws ParityException {
        synchronized(getImplLock()) {
            getImpl().sendKeyResponse(keyResponse, artifactId, jabberId);
        }
    }

    /**
     * Subscribe to an artifact. The parity server is notified and will create a
     * subscription entry for the logged in user.
     * 
     * @param artifact
     *            The artifact to subscribe to.
     * @throws ParityException
     */
    public void sendSubscribe(final Artifact artifact) throws ParityException {
        synchronized(getImplLock()) { getImpl().sendSubscribe(artifact); }
    }

    /**
     * Update the local user's remote information.
     * 
     * @param user
     *            The user.
     * @throws ParityException
     */
    public void updateUser(final User user) throws ParityException {
        synchronized(getImplLock()) {
            getImpl().updateUser(user);
        }
    }
}
