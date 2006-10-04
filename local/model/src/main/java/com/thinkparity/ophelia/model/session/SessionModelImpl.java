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
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.Session;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.events.SessionListener;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The implementation of the parity session interface.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.37
 */
class SessionModelImpl extends AbstractModelImpl<SessionListener> {

    /**
	 * Create a SessionModelImpl
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	SessionModelImpl(final Environment environment, final Workspace workspace) {
		super(environment, workspace);
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
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
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
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("jabberId", jabberId);
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
     * Archive an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    void archiveArtifact(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.archiveArtifact(userId, uniqueId);
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
            final Long versionId, final JabberId receivedBy,
            final Calendar receivedOn) {
	    logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("receivedBy", receivedBy);
        logger.logVariable("receivedOn", receivedOn);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
    	    synchronized(xmppSession) {
    	        xmppSession.confirmArtifactReceipt(userId, uniqueId, versionId, receivedBy, receivedOn);
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
		logger.logApiId();
        logger.logVariable("userId", userId);
		logger.logVariable("uniqueId", uniqueId);
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
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
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
        logger.logApiId();
        logger.logVariable("invitedAs", invitedAs);
        logger.logVariable("invitedBy", invitedBy);
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
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
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
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("contactId", contactId);
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
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("invitedAs", invitedAs);
        logger.logVariable("deletedOn", deletedOn);
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
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
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
        logger.logApiId();
        logger.logVariable("extendTo", extendTo);
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
        logger.logApiId();
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
        logger.logApiId();
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
        logger.logApiId();
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
        logger.logApiId();
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
        logger.logApiId();
        logger.logVariable("credentials", credentials);
        login(1, credentials);
    }

	/**
	 * Terminate the current session.
	 * 
	 */
	void logout() {
        logger.logApiId();
		try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
		    synchronized (xmppSession) {
		        xmppSession.logout();
            }
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    InputStream openArchiveDocumentVersion(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.openArchiveDocumentVersion(userId, uniqueId, versionId);
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
        logger.logApiId();
        logger.logVariable("container", container);
        logger.logVariable("documents", documents);
        logger.logVariable("publishTo", publishTo);
        logger.logVariable("publishedBy", publishedBy);
        logger.logVariable("publishedOn", publishedOn);
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

    Container readArchiveContainer(final JabberId userId,
            final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveContainer(userId, uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the archived containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A list of conatiners.
     */
    List<Container> readArchiveContainers(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveContainers(userId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    List<ContainerVersion> readArchiveContainerVersions(
            final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveContainerVersions(userId,
                        uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    List<Document> readArchiveDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveDocuments(userId, uniqueId,
                        versionId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    List<DocumentVersion> readArchiveDocumentVersions(
            final JabberId userId, final UUID uniqueId, final Long versionId,
            final UUID documentUniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveDocumentVersions(userId,
                        uniqueId, versionId, documentUniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    List<JabberId> readArchiveTeamIds(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                return xmppSession.readArchiveTeamIds(userId, uniqueId);
            }
        } catch (final Throwable t) {
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
		logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
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
        logger.logApiId();
        throw Assert.createNotYetImplemented("SessionModelImpl#readContact()");
    }

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
	Contact readContact(final JabberId userId, final JabberId contactId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("contactId", contactId);
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
		logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
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
        logger.logApiId();
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
        logger.logApiId();
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
        logger.logApiId();
        logger.logVariable("userId", userId);
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
        logger.logApiId();
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
        logger.logApiId();
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
        logger.logApiId();
        logger.logVariable("userId", userId);
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
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
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
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("userId", userId);
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
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("securityAnswer", "XXXXX");
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
     * Archive an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    void restoreArtifact(final JabberId userId, final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                xmppSession.restoreArtifact(userId, uniqueId);
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
        logger.logApiId();
        logger.logVariable("container", container);
        logger.logVariable("documents", documents);
        logger.logVariable("sendTo", sendTo);
        logger.logVariable("sentBy", sentBy);
        logger.logVariable("sentOn", sentOn);
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
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("profile", profile);
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
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("credentials", credentials);
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
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
        logger.logVariable("key", key);
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
     * @param attempt
     *            The attempt number.
     * @param environment
     *            The environment to login to.
     * @param credentials
     *            The credentials to login with.
     */
    private void login(final Integer attempt, final Credentials credentials) {
        logger.logVariable("attempt", attempt);
        logger.logVariable("environment", environment);
        logger.logVariable("credentials", credentials);
        try {
            assertNotIsOnline();
            assertIsReachable(environment);
            final XMPPSession xmppSession = workspace.getXMPPSession();
            synchronized (xmppSession) {
                // check that the user's credentials match
                final Credentials storedCredentials = readCredentials();
                if(null != storedCredentials) {
                    Assert.assertTrue(
                            storedCredentials.getUsername().equals(credentials.getUsername()) &&
                                storedCredentials.getPassword().equals(credentials.getPassword()),
                            "Credentials {0} do not match stored credentials {1}.",
                            credentials, storedCredentials);
                    credentials.setResource(storedCredentials.getResource());
                }
                // register with xmpp event listeners
                new SessionModelEventDispatcher(workspace, internalModelFactory, xmppSession);

                // login
                xmppSession.login(environment, credentials);

                // save the user's credentials
                if(null == storedCredentials) {
                    createCredentials(credentials);
                }
                xmppSession.processOfflineQueue(localUserId());
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }
}
