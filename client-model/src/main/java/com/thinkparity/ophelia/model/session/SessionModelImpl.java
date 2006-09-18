/*
 * Created On: Mar 7, 2005
 */
package com.thinkparity.ophelia.model.session;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.Session;
import com.thinkparity.codebase.model.session.Environment.Protocol;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.events.SessionListener;
import com.thinkparity.ophelia.model.util.EventNotifier;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The implementation of the parity session interface.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.37
 */
class SessionModelImpl extends AbstractModelImpl<SessionListener> {

    /** The remote environment. */
    private final Environment environment;

    /**
	 * Create a SessionModelImpl
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	SessionModelImpl(final Workspace workspace) {
		super(workspace);
        this.environment = new Environment();
        this.environment.setServerHost(workspace.getPreferences().getServerHost());
        this.environment.setServerPort(workspace.getPreferences().getServerPort());
        this.environment.setServerProtocol(
                Protocol.valueOf(workspace.getPreferences().getServerProtocol()));
	}

	/**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    public Contact readContact(final JabberId userId, final JabberId contactId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("contactId", contactId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readContact(userId, contactId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.AbstractModelImpl#addListener(com.thinkparity.ophelia.model.util.EventListener)
     */
    @Override
    protected boolean addListener(final SessionListener listener) {
        return super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.AbstractModelImpl#removeListener(com.thinkparity.ophelia.model.util.EventListener)
     */
    @Override
    protected boolean removeListener(final SessionListener listener) {
        return super.removeListener(listener);
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
    void acceptContactInvitation(final JabberId userId, final JabberId invitedBy,
            final Calendar acceptedOn) {
	    try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
	        synchronized (xmppSession) {
                xmppSession.acceptContactInvitation(userId, invitedBy, acceptedOn);
	        }
        } catch (final Throwable t) {
            throw translateError(t);
	    }
	}

    /**
     * Add an email to a user's profile.
     * 
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    void addProfileEmail(final JabberId userId, final ProfileEMail email) {
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.addProfileEmail(userId, email.getEmail());
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    void addTeamMember(final UUID uniqueId, final JabberId jabberId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("jabberId", jabberId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.addTeamMember(uniqueId, jabberId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
	void confirmArtifactReceipt(final JabberId userId, final UUID uniqueId,
            final Long versionId, final JabberId receivedBy) {
	    logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("receivedBy", receivedBy);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
    	    synchronized(xmppSession) {
    	        xmppSession.confirmArtifactReceipt(userId, uniqueId, versionId, receivedBy);
    	    }
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}

	/**
     * Send an artifact creation packet to the parity server.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
	void createArtifact(final JabberId userId, final UUID uniqueId) {
		logApiId();
        logVariable("userId", userId);
		logVariable("uniqueId", uniqueId);
		try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
		    synchronized (xmppSession) {
		        xmppSession.createArtifact(userId, uniqueId);
		    }
		} catch (final Throwable t) {
            throw translateError(t);
        }
	}

	/**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void createDraft(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.createDraft(uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
	 * Decline a user's invitation to their contact list.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 * @throws ParityException
	 */
	void declineInvitation(final EMail invitedAs, final JabberId invitedBy) {
        logApiId();
        logVariable("invitedAs", invitedAs);
        logVariable("invitedBy", invitedBy);
		try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
		    synchronized(xmppSession) {
		        xmppSession.declineInvitation(invitedAs, invitedBy);
            }
		} catch(final Throwable t) {
            throw translateError(t);
		}
	}

	/**
     * Delete an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void deleteArtifact(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.deleteArtifact(uniqueId);
            }
        }
        catch(final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Delete a contact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    void deleteContact(final JabberId userId, final JabberId contactId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("contactId", contactId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.deleteContact(userId, contactId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    void deleteContactInvitation(final JabberId userId, final EMail invitedAs,
            final Calendar deletedOn) {
        logApiId();
        logVariable("userId", userId);
        logVariable("invitedAs", invitedAs);
        logVariable("deletedOn", deletedOn);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.deleteContactInvitation(userId, invitedAs, deletedOn);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void deleteDraft(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        final XMPPSession xmppSession = workspace.getXMPPSession();
        synchronized (xmppSession) {
            xmppSession.deleteDraft(uniqueId);
        }
    }

	/**
	 * Extend an invitation to a contact.
	 * 
	 * @param extendTo
	 *            An <code>EMail</code> to invite.
	 */
	void extendInvitation(final EMail extendTo) {
        logApiId();
        logVariable("extendTo", extendTo);
		try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
		    synchronized (xmppSession) {
		        xmppSession.extendInvitation(localUserId(), extendTo,
                        currentDateTime());
		    }
		} catch(final Throwable t) {
			throw translateError(t);
		}
	}

    /**
     * Handle the session established remote event.
     *
     */
    void handleSessionEstablished() {
        logApiId();
        notifyListeners(new EventNotifier<SessionListener>() {
            public void notifyListener(final SessionListener listener) {
                listener.sessionEstablished();
            }
        });
    }

    /**
     * Handle the remote session terminated event.
     *
     */
    void handleSessionTerminated() {
        logApiId();
        notifyListeners(new EventNotifier<SessionListener>() {
            public void notifyListener(final SessionListener listener) {
                listener.sessionTerminated();
            }
        });
    }

    /**
     * Handle the remote session terminated event.
     * 
     * @param cause
     *            The cause of the termination.
     * 
     */
    void handleSessionTerminated(final Exception cause) {
        logApiId();
        notifyListeners(new EventNotifier<SessionListener>() {
            public void notifyListener(final SessionListener listener) {
                listener.sessionTerminated(cause);
            }
        });
    }

	/**
	 * Determine whether or not a user is logged in.
	 * 
	 * @return True if the user is logged in, false otherwise.
	 */
	Boolean isLoggedIn() {
        final XMPPSession xmppSession = workspace.getXMPPSession();
		synchronized (xmppSession) {
            return xmppSession.isLoggedIn();
		}
	}

    /**
     * Establish a new xmpp session.
     * 
     * @throws ParityException
     */
    void login() {
        logApiId();
        login(readCredentials());
    }

    /**
     * Establish a new xmpp session.
     * 
     * @param credentials
     *            The user's credentials.
     * @throws ParityException
     */
    void login(final Credentials credentials) {
        logApiId();
        logVariable("credentials", credentials);
        login(environment, credentials);
    }

    /**
	 * Terminate the current session.
	 * 
	 */
	void logout() {
        logApiId();
		try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
		    synchronized (xmppSession) {
		        xmppSession.logout();
            }
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    /**
     * Publish a container.
     * 
     * @param container
     *            A container.
     * @param documents
     *            A list of documents and their content.
     * @param publishTo
     *            A list of ids to publish to.
     * @param publishedBy
     *            By whom the container was published.
     * @param publishedOn
     *            When the container was published.
     */
    void publish(final ContainerVersion container,
            final Map<DocumentVersion, InputStream> documents,
            final List<JabberId> publishTo, final JabberId publishedBy,
            final Calendar publishedOn) {
        logApiId();
        logVariable("container", container);
        logVariable("documents", documents);
        logVariable("publishTo", publishTo);
        logVariable("publishedBy", publishedBy);
        logVariable("publishedOn", publishedOn);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.publish(container, documents, publishTo,
                        publishedBy, publishedOn);
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Read the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return A list of jabber ids.
     */
	List<JabberId> readArtifactTeamIds(final UUID uniqueId) {
		logApiId();
        logVariable("uniqueId", uniqueId);
		try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
		    synchronized (xmppSession) {
    			return xmppSession.readArtifactTeamIds(uniqueId);
		    }
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    /**
     * Read the session user's contact info.
     * 
     * @return The user's contact info.
     */
    Contact readContact() {
        logApiId();
        throw Assert.createNotYetImplemented("SessionModelImpl#readContact()");
    }

    /**
     * Read the logged in user's contacts.
     * 
     * @return A set of contacts.
     * @throws ParityException
     */
	List<Contact> readContacts(final JabberId userId) {
		try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
		    synchronized(xmppSession) {
		        return xmppSession.readContacts(userId);
		    }
        } catch (final Throwable t) {
            throw translateError(t);
		}
	}

    /**
     * Read the artifact key holder from the server.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact key holder.
     * @throws ParityException
     */
    JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
		logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
		try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
		    synchronized (xmppSession) {
		        return xmppSession.readKeyHolder(userId, uniqueId);
		    }
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    /**
     * Read the logged in user's profile.
     * 
     * @return A profile.
     */
    Profile readProfile() {
        logApiId();
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readProfile();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<EMail> readProfileEMails() {
        logApiId();
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readProfileEMails();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the user profile's security question.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A security question <code>String</code>.
     */
    String readProfileSecurityQuestion(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readProfileSecurityQuestion(userId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Read the logged in user's session.
     * 
     * @return The logged in user's session.
     */
    Session readSession() {
        logApiId();
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                final Session session = new Session();
                session.setJabberId(xmppSession.readCurrentUser().getId());
                return session;
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Read the session user's user info.
     * 
     * @return thinkParity user info.
     * @throws ParityException
     */
	User readUser() {
        logApiId();
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readCurrentUser();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Read a single user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
	User readUser(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readUser(userId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    void removeProfileEmail(final JabberId userId, final ProfileEMail email) {
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.removeProfileEmail(userId, email.getEmail());
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Remove a team member from the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    void removeTeamMember(final UUID uniqueId, final JabberId userId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("userId", userId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.removeTeamMember(uniqueId, userId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    String resetProfilePassword(final JabberId userId,
            final String securityAnswer) {
        logApiId();
        logVariable("userId", userId);
        logVariable("securityAnswer", "XXXXX");
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.resetProfilePassword(userId, securityAnswer);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Send a container version.
     * 
     * @param version
     *            A container version.
     * @param documentVersions
     *            A list of document versions.
     * @param user
     *            A user.
     */
    void send(final ContainerVersion container,
            final Map<DocumentVersion, InputStream> documents,
            final List<JabberId> sendTo, final JabberId sentBy,
            final Calendar sentOn) {
        logApiId();
        logVariable("container", container);
        logVariable("documents", documents);
        logVariable("sendTo", sendTo);
        logVariable("sentBy", sentBy);
        logVariable("sentOn", sentOn);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.send(container, documents, sendTo, sentBy, sentOn);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
	 * Send the parity log file. To be used in order to troubleshoot remote
	 * problems.
	 * 
	 * @throws ParityException
	 */
	void sendLogFileArchive() throws ParityException {
	    throw Assert
                .createNotYetImplemented("SessionModelImpl#sendLogFileArchive");
	}

    /**
     * Update the session user's contact info.
     * 
     * @param contact
     *            The user's contact info.
     */
    void updateContact(final Contact contact) {
        throw Assert.createNotYetImplemented("SessionModelImpl#updateContact(Contact)");
    }

    /**
     * Update a user's profile.
     * 
     * @param userId
     *            The user id <code>JabberId</code>.
     * @param profile
     *            The user's <code>Profile</code>.
     */
    void updateProfile(final JabberId userId, final Profile profile) {
        logApiId();
        logVariable("userId", userId);
        logVariable("profile", profile);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.updateProfile(userId, profile);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    void updateProfileCredentials(final JabberId userId,
            final Credentials credentials) {
        logApiId();
        logVariable("userId", userId);
        logVariable("credentials", credentials);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.updateProfileCredentials(userId, credentials);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    void verifyProfileEmail(final JabberId userId,
            final ProfileEMail email, final String key) {
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        logVariable("key", key);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.verifyProfileEmail(userId, email.getEmail(), key);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Login to the environment with the credentials.
     * 
     * @param environment
     *            The environment to login to.
     * @param credentials
     *            The credentials to login with.
     */
    private void login(final Environment environment,
            final Credentials credentials) {
        login(1, environment, credentials);
    }

    /**
     * Login to the environment with the credentials.
     * 
     * @param attempt
     *            The attempt number.
     * @param environment
     *            The environment to login to.
     * @param credentials
     *            The credentials to login with.
     */
    private void login(final Integer attempt, final Environment environment,
            final Credentials credentials) {
        logger.debug(environment);
        logger.debug(credentials);
        try {
            assertNotIsOnline();
            assertIsReachable(environment);
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                // check that the user's credentials match
                final Credentials storedCredentials = readCredentials();
                if(null != storedCredentials) {
                    Assert.assertTrue(
                            "CANNOT MATCH USER CREDENTIALS",
                            storedCredentials.equals(credentials));
                }
                // register with xmpp event listeners
                new SessionModelEventDispatcher(workspace, internalModelFactory, xmppSession);

                // login
                xmppSession.login(environment, credentials);

                // save the user's credentials
                if(null == storedCredentials) {
                    createCredentials(
                            credentials.getUsername(), credentials.getPassword());
                }
                xmppSession.processOfflineQueue();
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }
}
