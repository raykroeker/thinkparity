/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.model.artifact;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.jivesoftware.messenger.vcard.VCardManager;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.model.AbstractModelImpl;
import com.thinkparity.model.ParityErrorTranslator;
import com.thinkparity.model.ParityServerModelException;
import com.thinkparity.model.contact.Contact;
import com.thinkparity.model.io.sql.ArtifactSql;
import com.thinkparity.model.io.sql.ArtifactSubscriptionSql;
import com.thinkparity.model.session.Session;
import com.thinkparity.model.user.User;
import com.thinkparity.model.xmpp.IQWriter;

import com.thinkparity.server.org.xmpp.packet.IQArtifact;
import com.thinkparity.server.org.xmpp.packet.IQArtifactFlag;
import com.thinkparity.server.org.xmpp.packet.artifact.IQConfirmReceipt;
import com.thinkparity.server.org.xmpp.packet.document.IQReactivateDocument;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.5
 */
class ArtifactModelImpl extends AbstractModelImpl {

    /** Artifact sql io. */
	private final ArtifactSql artifactSql;

    /** Artifact subscription sql io. */
	private final ArtifactSubscriptionSql artifactSubscriptionSql;

	/** The jive VCard manager. */
	private final VCardManager vCardManager;

	/**
	 * Create a ArtifactModelImpl.
	 */
	ArtifactModelImpl(final Session session) {
		super(session);
		this.artifactSql = new ArtifactSql();
		this.artifactSubscriptionSql = new ArtifactSubscriptionSql();
		this.vCardManager = VCardManager.getInstance();
	}

    /**
     * Add a user to an artifact's team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A user's jabber id.
     */
    void addTeamMember(final UUID uniqueId, final JabberId jabberId) {
        logApiId();
        logger.debug(uniqueId);
        logger.debug(jabberId);
        try {
            final Artifact artifact = read(uniqueId);
            final User user = getUserModel().readUser(jabberId);
            final String username = jabberId.getUsername();
            artifactSubscriptionSql.insert(artifact.getId(), username,
                    session.getJabberId());
            notifyTeamMemberAdded(read(uniqueId), user);
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Confirm an artifact receipt.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param receivedFrom
     *            The original sender of the document.
     * @throws ParityServerModelException
     */
	void confirmReceipt(final UUID uniqueId, final Long versionId,
            final JabberId receivedFrom) throws ParityServerModelException {
        logApiId();
        logger.debug(uniqueId);
        logger.debug(versionId);
        logger.debug(receivedFrom);
        try {
            final IQConfirmReceipt iq = new IQConfirmReceipt();
            iq.setUniqueId(uniqueId);
            iq.setVersionId(versionId);
            iq.setConfirmedBy(session.getJabberId());
            iq.setFrom(session.getJID());
            setTo(iq, receivedFrom);

            send(receivedFrom, iq);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
	 * Create an artifact.
	 * 
	 * @param uniqueId
	 *            The artifact id.
	 * @return The new artifact.
	 * @throws ParityServerModelException
	 */
	Artifact create(final UUID uniqueId) throws ParityServerModelException {
		logApiId();
		logger.debug(uniqueId);
		try {
            final JabberId sessionJabberId = session.getJabberId();
			artifactSql.insert(uniqueId, sessionJabberId.getUsername(),
					ArtifactState.ACTIVE, session.getJabberId());
			final Artifact artifact = artifactSql.select(uniqueId);
			return artifact;
		}
		catch(SQLException sqlx) {
			logger.error("create(UUID)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(RuntimeException rx) {
			logger.error("create(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     */
    void createDraft(final UUID uniqueId) {
        logApiId();
        logger.debug(uniqueId);
        try {
            assertSystemIsKeyHolder("[SYSTEM IS NOT KEY HOLDER]", uniqueId);
            final Artifact artifact = read(uniqueId);
            artifactSql.updateKeyHolder(
                    artifact.getId(), session.getJabberId().getUsername(),
                    session.getJabberId());
            notifyDraftCreated(artifact, session.getJabberId(), DateUtil
                    .getInstance());
        }
        catch(final ParityServerModelException psmx) {
            throw translateError(psmx);
        }
        catch(final SQLException sqlx) {
            throw translateError(sqlx);
        }
        catch(final UnauthorizedException ux) {
            throw translateError(ux);
        }
    }

    /**
     * Delete a draft from an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void deleteDraft(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        try {
            final Artifact artifact = read(uniqueId);
            assertIsKeyHolder(artifact);
            final JabberId sessionJabberId = session.getJabberId();
            artifactSql.updateKeyHolder(artifact.getId(), User.THINK_PARITY, sessionJabberId);
            notifyDraftDeleted(artifact, session.getJabberId(), DateUtil.getInstance());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
	 * Flag an artifact.
	 * 
	 * @param artifact
	 *            An <code>Artifact</code>.
	 * @param artifactFlag
	 *            A <code>ArtifactFlag</code>.
	 * @throws ParityServerModelException
	 */
	void flag(final Artifact artifact, final ArtifactFlag artifactFlag) {
        logApiId();
		logVariable("artifact", artifact);
        logVariable("artifactFlag", artifactFlag);
		try {
			final Long artifactId = artifact.getId();
			final Collection<ArtifactSubscription> subscriptions =
				artifactSubscriptionSql.select(artifactId);

			// send an IQFlag packet to each subscribed user
			final UUID artifactUniqueId = artifact.getUniqueId();
			IQ iq;
			for(ArtifactSubscription subscription : subscriptions) {
				iq = createFlag(artifactUniqueId, artifactFlag, subscription);
				// send the parity iq
				send(JabberIdBuilder.parseUsername(subscription.getUsername()), iq);
			}
		} catch(final Throwable t) {
			throw translateError(t);
		}
	}

    /**
	 * Obtain a handle to an artifact for a given artifact unique id.
	 * 
	 * @param uniqueId
	 *            An artifact unique id.
	 */
	Artifact read(final UUID uniqueId) {
        logApiId();
		logVariable("uniqueId", uniqueId);
		try {
            return artifactSql.select(uniqueId);
		} catch (final Throwable t) {
            throw translateError(t);
		}
	}

    /**
	 * Set the keyholder for the given artifact.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @return The previous keyholder's JID.
	 * @throws ParityServerModelException
	 */
	JID getKeyHolder(final UUID artifactUniqueId) throws ParityServerModelException {
        logApiId();
		logger.debug(artifactUniqueId);
		try {
			final Artifact artifact = artifactSql.select(artifactUniqueId);
			final Long artifactId = artifact.getId();
			final String previousKeyHolder = artifactSql.selectKeyHolder(artifactId);
			return buildJID(previousKeyHolder);
		}
		catch(SQLException sqlx) {
			logger.error("getKeyHolder(Artifact)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(RuntimeException rx) {
			logger.error("getKeyHolder(Artifact)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

    List<ArtifactSubscription> getSubscription(
			final UUID uniqueId) throws ParityServerModelException {
        logApiId();
		logVariable("uniqueId", uniqueId);
		try {
			final Artifact artifact = read(uniqueId);
            if (null == artifact) {
                return Collections.emptyList();
            } else {
                return artifactSubscriptionSql.select(artifact.getId());
            }
		}
		catch(final SQLException sqlx) {
			logger.error("getArtifactSubscription(UUID)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(final RuntimeException rx) {
			logger.error("getArtifactSubscription(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	List<Artifact> listForKeyHolder() throws ParityServerModelException {
        logApiId();
		try { return artifactSql.listForKeyHolder(session.getJID()); }
		catch(final SQLException sqlx) {
			logger.error("Could not obtain artifacts for key holder.", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(final RuntimeException rx) {
			logger.error("Could not obtain artifacts for key holder.", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	void reactivate(final List<JabberId> team, final UUID uniqueId,
            final Long versionId, final String name, final byte[] bytes)
            throws ParityServerModelException {
        logApiId();
        logger.debug(team);
        logger.debug(uniqueId);
        logger.debug(versionId);
        logger.debug(name);
        logger.debug(bytes);
        create(uniqueId);

        final IQReactivateDocument iq = new IQReactivateDocument();
        iq.setContent(bytes);
        iq.setName(name);
        iq.setUniqueId(uniqueId);
        iq.setVersionId(versionId);
        for(final JabberId jabberId : team) {
            try {
                // don't send reactivation to self
                if(!jabberId.equals(session.getJabberId())) {
                    if(isActive(jabberId)) {
                        iq.setFrom(session.getJID());
                        setTo(iq, jabberId);
                        send(jabberId, iq);
                    }
                }
            }
            catch(final UnauthorizedException ux) {
                throw ParityErrorTranslator.translate(ux);
            }
        }
    }

	List<Contact> readContacts(final UUID artifactUniqueId) {
        logApiId();
		logger.debug(artifactUniqueId);
		try {
			final List<ArtifactSubscription> s = getSubscription(artifactUniqueId);
			final List<Contact> contacts = new LinkedList<Contact>();
			Contact contact;
			for(final ArtifactSubscription as : s) {
				contact = new Contact();
				contact.setId(as.getJabberId());
				contact.setVCard(vCardManager.getVCard(as.getJabberId().getUsername()));
				contacts.add(contact);
			}
			return contacts;
		} catch( final Throwable t) {
            throw translateError(t);
		}
	}

	/**
     * Remove a user from an artifact's team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A user's jabber id.
     */
    void removeTeamMember(final UUID uniqueId, final JabberId jabberId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("jabberId", jabberId);
        try {
            final Artifact artifact = read(uniqueId);
            final Long artifactId = artifact.getId();
            final ArtifactSubscription subscription =
                artifactSubscriptionSql.read(artifactId, jabberId);
            if(null != subscription) {
                artifactSubscriptionSql.delete(artifactId, jabberId.getUsername());
                // if all subscriptions are deleted; delete the artifact
                if (!artifactSubscriptionSql.existSubscriptions(artifactId)) {
                    artifactSql.delete(artifactId);
                }
            }
            final User user = getUserModel().readUser(jabberId);
            notifyTeamMemberRemoved(artifact, user);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
	 * Create a flag iq packet to send to a subscription.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @param artifactFlag
	 *            The flag.
	 * @param subscription
	 *            The subscription.
	 * @return The flag iq packet.
	 */
	private IQArtifact createFlag(final UUID artifactUniqueId,
			final ArtifactFlag artifactFlag,
			final ArtifactSubscription subscription) {
		final IQArtifactFlag iqArtifactFlag = new IQArtifactFlag(artifactUniqueId, artifactFlag);
		iqArtifactFlag.setTo(buildJID(subscription.getUsername()));
		iqArtifactFlag.setFrom(session.getJID());
		return iqArtifactFlag;
	}

	/**
     * Notify the team a a draft was created.
     * 
     * @param artifact
     *            The <code>Artifact</code>.
     * @param createdBy
     *            The draft creator <code>JabberId</code>.
     * @param createdOn
     *            The draft creation <code>Calendar</code>.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyDraftCreated(final Artifact artifact,
            final JabberId createdBy, final Calendar createdOn)
            throws ParityServerModelException, UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:draftcreated");
        notification.writeUniqueId("uniqueId", artifact.getUniqueId());
        notification.writeJabberId("createdBy", createdBy);
        notification.writeCalendar("createdOn", createdOn);
        notifyTeam(artifact.getUniqueId(), notification.getIQ());
    }

    /**
     * Notify an artifact's team a draft was deleted.
     * @param artifact An <code>Artifact</code>.
     * @param deletedBy The <code>JabberId</code> of the deleting user.
     * @param deletedOn When the draft was deleted.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyDraftDeleted(final Artifact artifact,
            final JabberId deletedBy, final Calendar deletedOn)
            throws ParityServerModelException, UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:draftdeleted");
        notification.writeUniqueId("uniqueId", artifact.getUniqueId());
        notification.writeJabberId("deletedBy", deletedBy);
        notification.writeCalendar("deletedOn", deletedOn);
        notifyTeam(artifact.getUniqueId(), notification.getIQ());
    }

    /**
     * Notify an artifact's team; a team member was added.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @param user
     *            A <code>User</code>.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyTeamMemberAdded(final Artifact artifact, final User user)
            throws ParityServerModelException, UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:teammemberadded");
        notification.writeUniqueId("uniqueId", artifact.getUniqueId());
        notification.writeJabberId("jabberId", user.getId());
        notifyTeam(artifact.getUniqueId(), notification.getIQ());
    }

    /**
     * Notify an artifact's team; a team member was removed.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @param user
     *            A <code>user</code>.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyTeamMemberRemoved(final Artifact artifact,
            final User user) throws ParityServerModelException,
            UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:teammemberremoved");
        notification.writeUniqueId("uniqueId", artifact.getUniqueId());
        notification.writeJabberId("jabberId", user.getId());
        notifyTeam(artifact.getUniqueId(), notification.getIQ());
    }

    /**
     * Read the key holder for an artifact.
     * 
     * @param userId
     *            The user id <code>JabberId</code>.
     * @param uniqueId
     *            The artifact unique id <code>UUID</code>.
     * @return The artifact key holder <code>JabberId</code>.
     */
    JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        assertIsAuthenticatedUser(userId);
        try {
            return artifactSql.readKeyHolder(uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
}
