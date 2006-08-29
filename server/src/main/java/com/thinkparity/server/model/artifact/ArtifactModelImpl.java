/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

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
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.xmpp.IQWriter;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.contact.Contact;
import com.thinkparity.server.model.io.sql.artifact.ArtifactSql;
import com.thinkparity.server.model.io.sql.artifact.ArtifactSubscriptionSql;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.model.user.User;
import com.thinkparity.server.org.jivesoftware.messenger.JIDBuilder;
import com.thinkparity.server.org.xmpp.packet.IQAcceptKeyRequest;
import com.thinkparity.server.org.xmpp.packet.IQArtifact;
import com.thinkparity.server.org.xmpp.packet.IQArtifactFlag;
import com.thinkparity.server.org.xmpp.packet.IQCloseArtifact;
import com.thinkparity.server.org.xmpp.packet.IQDenyKeyRequest;
import com.thinkparity.server.org.xmpp.packet.IQKeyRequest;
import com.thinkparity.server.org.xmpp.packet.artifact.IQConfirmReceipt;
import com.thinkparity.server.org.xmpp.packet.artifact.IQNotifyTeamMemberAdded;
import com.thinkparity.server.org.xmpp.packet.artifact.IQNotifyTeamMemberRemoved;
import com.thinkparity.server.org.xmpp.packet.document.IQReactivateDocument;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.5
 */
class ArtifactModelImpl extends AbstractModelImpl {

    /**
	 * Assertion statement used when comparing the current key holder with
	 * the current session.
	 */
	private static final String ASSERT_KEYHOLDER_SESSION =
		"Cannot accept key request if the user is not the current keyholder.";

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
	 * Assert that the state transition from currentState to newState can be
	 * made safely.
	 * 
	 * @param currentState The artifact's current state.
	 * @param intentedState
	 *            The artifact's intended state.
	 * 
	 * @throws NotTrueAssertion
	 *             If the state cannot be moved.
	 */
	protected void assertStateTransition(final Artifact.State currentState,
			final Artifact.State intendedState) {
		switch(currentState) {
		case ACTIVE:
			// i can close it or delete it
			Assert.assertTrue(
					formatAssertion(currentState, intendedState, new Artifact.State[] {Artifact.State.CLOSED, Artifact.State.DELETED}),
					Artifact.State.CLOSED == intendedState || Artifact.State.DELETED == intendedState);
			break;
		case CLOSED:
			// i can delete it
			Assert.assertTrue(
					formatAssertion(currentState, intendedState, new Artifact.State[] {Artifact.State.DELETED}),
					Artifact.State.DELETED == intendedState);
			break;
		default: Assert.assertUnreachable("Unknown artifact state:  " + currentState);
		}
	}

	/**
	 * Accept the key request.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @param jid
	 *            The requestor jive id.
	 * @throws ParityServerModelException
	 */
	void acceptKeyRequest(final UUID artifactUniqueId, final JID jid)
			throws ParityServerModelException {
        logApiId();
		logger.debug(artifactUniqueId);
		logger.debug(jid);
		try {
			assertEquals(
					ASSERT_KEYHOLDER_SESSION,
					getKeyHolder(artifactUniqueId), session.getJID());
			final Artifact artifact = get(artifactUniqueId);
			final Integer artifactId = artifact.getArtifactId();
			final String username = jid.getNode();
			artifactSql.updateKeyHolder(artifactId, username, session.getJabberId());
			// send the requestor an acceptance packet
			final IQ iq = new IQAcceptKeyRequest(artifactUniqueId, session.getJID());
			iq.setTo(jid);
			iq.setFrom(session.getJID());
			send(jid, iq);
		} catch (final Throwable t) {
			throw translateError(t);
		}
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
            final Artifact artifact = get(uniqueId);
            final User user = getUserModel().readUser(jabberId);
            final String username = jabberId.getUsername();
            artifactSubscriptionSql.insert(artifact.getArtifactId(), username,
                    session.getJabberId());
            notifyTeamMemberAdded(get(uniqueId), user);
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    /**
	 * Close an artifact.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @throws ParityServerModelException
	 */
	void close(final UUID artifactUniqueId) throws ParityServerModelException {
		logApiId();
		logger.debug(artifactUniqueId);
		final Artifact artifact = get(artifactUniqueId);
		assertIsKeyHolder(artifact);
		try {
			updateState(artifact, Artifact.State.CLOSED);
			final IQ close = new IQCloseArtifact(artifactUniqueId);

			final List<ArtifactSubscription> subscription = getSubscription(artifactUniqueId);
			JID jid;
			for(final ArtifactSubscription s : subscription) {
				jid = JIDBuilder.build(s.getUsername());
                // fire distributed close events; save ourselves
				if(!jid.equals(session.getJID())) {
					close.setTo(jid);
					close.setFrom(session.getJID());
					send(jid, close);
				}
                // delete subscription
                artifactSubscriptionSql.delete(
                        artifact.getArtifactId(), s.getUsername());
			}
            // delete artifact
            artifactSql.delete(artifact.getArtifactId());
        } catch (final Throwable t) {
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
            iq.setTo(receivedFrom.getJID());

            send(receivedFrom, iq);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
	 * Create an artifact.
	 * 
	 * @param artifactUniqueId
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
					Artifact.State.ACTIVE, session.getJabberId());
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
            final Artifact artifact = get(uniqueId);
            artifactSql.updateKeyHolder(
                    artifact.getArtifactId(), session.getJabberId().getUsername(),
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
            final Artifact artifact = get(uniqueId);
            assertIsKeyHolder(artifact);
            final JabberId sessionJabberId = session.getJabberId();
            artifactSql.updateKeyHolder(artifact.getArtifactId(),
                    sessionJabberId.getUsername(), sessionJabberId);
            notifyDraftDeleted(artifact, session.getJabberId(), DateUtil.getInstance());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
	 * Deny the key request for the artifact from the jid.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @param jid
	 *            The requestor's jive id.
	 * @throws ParityServerModelException
	 */
	void denyKeyRequest(final UUID artifactUniqueId, final JID jid)
			throws ParityServerModelException {
        logApiId();
		logger.debug(artifactUniqueId);
		logger.debug(jid);
		try {
			// send the requestor a denial
			final IQ iq = new IQDenyKeyRequest(artifactUniqueId, session.getJID());
			iq.setTo(jid);
			iq.setFrom(session.getJID());
			send(jid, iq);
		}
		catch(UnauthorizedException ux) {
			logger.error("denyKeyRequest(UUID,JID)", ux);
			throw ParityErrorTranslator.translate(ux);
		}
		catch(RuntimeException rx) {
			logger.error("denyKeyRequest(UUID,JID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

    /**
	 * ArtifactFlag the artifact.
	 * 
	 * @param artifactId
	 *            The artifact to flag.
	 * @param flag
	 *            The flag to apply.
	 * @throws ParityServerModelException
	 */
	void flag(final Artifact artifact, final ParityObjectFlag artifactFlag)
			throws ParityServerModelException {
        logApiId();
		logger.debug(artifact);
		logger.debug(artifactFlag);
		try {
			final Integer artifactId = artifact.getArtifactId();
			final Collection<ArtifactSubscription> subscriptions =
				artifactSubscriptionSql.select(artifactId);

			// send an IQFlag packet to each subscribed user
			final UUID artifactUniqueId = artifact.getArtifactUUID();
			IQ iq;
			for(ArtifactSubscription subscription : subscriptions) {
				iq = createFlag(artifactUniqueId, artifactFlag, subscription);
				// send the parity iq
				send(iq.getTo(), iq);
			}
		}
		catch(SQLException sqlx) {
			logger.error("flag(Artifact,ArtifactFlag)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(UnauthorizedException ux) {
			logger.error("flag(Artifact,ArtifactFlag)", ux);
			throw ParityErrorTranslator.translate(ux);
		}
		catch(RuntimeException rx) {
			logger.error("flag(Artifact,ArtifactFlag)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

    /**
	 * Obtain a handle to an artifact for a given artifact unique id.
	 * 
	 * @param artifactUniqueId
	 *            An artifact unique id.
	 * @throws ParityServerModelException
	 */
	Artifact get(final UUID artifactUniqueId) {
        logApiId();
		logger.debug(artifactUniqueId);
		try { return artifactSql.select(artifactUniqueId); }
		catch(final Throwable t) {
            throw translateError(t);
		}
	}

    /**
	 * Set the keyholder for the given artifact.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @param jid
	 *            The new keyholder's jid.
	 * @return The previous keyholder's JID.
	 * @throws ParityServerModelException
	 */
	JID getKeyHolder(final UUID artifactUniqueId) throws ParityServerModelException {
        logApiId();
		logger.debug(artifactUniqueId);
		try {
			final Artifact artifact = artifactSql.select(artifactUniqueId);
			final Integer artifactId = artifact.getArtifactId();
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
			final Artifact artifact = get(uniqueId);
            if (null == artifact) {
                return Collections.emptyList();
            } else {
                return artifactSubscriptionSql.select(artifact.getArtifactId());
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
                        iq.setTo(jabberId.getJID());
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
            final Artifact artifact = get(uniqueId);
            final Integer artifactId = artifact.getArtifactId();
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
        }
        catch(final Throwable t) { throw translateError(t); }
    }

	/**
	 * Request the key from the artifact's key holder. If the key holder is
	 * currently online; the request will be routed to them; otherwise it will
	 * be queued until the user comes online.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @throws ParityServerModelException
	 */
	void requestKey(final UUID artifactUniqueId) throws ParityServerModelException {
        logApiId();
		logger.debug(artifactUniqueId);
		try {
			final JID keyHolderJID = getKeyHolder(artifactUniqueId);
			final IQ iq = new IQKeyRequest(artifactUniqueId);
			iq.setTo(keyHolderJID);
			iq.setFrom(session.getJID());
			send(keyHolderJID, iq);
		}
		catch(final UnauthorizedException ux) {
			logger.error("requestKey(Artifact)", ux);
			throw ParityErrorTranslator.translate(ux);
		}
		catch(final RuntimeException rx) {
			logger.error("requestKey(Artifact)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

    /**
	 * SubscribeUser a user to an artifact.
	 * 
	 * @param artifact
	 *            The artifact to subscribe the user to.
	 * @throws ParityServerModelException
	 */
	void subscribe(final Artifact artifact) throws ParityServerModelException {
        logApiId();
		logger.debug(artifact);
		try {
			final Integer artifactId = artifact.getArtifactId();
			final String username = session.getJID().getNode();
			final Integer rowCount =
				artifactSubscriptionSql.selectCount(artifactId, username);
			if(1 == rowCount) {
				final StringBuffer warning =
					new StringBuffer("subscribe(User,Artifact):  ")
						.append("User already has a subscription.");
				logger.warn(warning);
			}
			else {
				final List<ArtifactSubscription> subscrtipions =
					getSubscription(artifact.getArtifactUUID());
				artifactSubscriptionSql.insert(artifactId, username, session.getJabberId());
				// notify all team members re:  the new team member
				for(final ArtifactSubscription subscription : subscrtipions) {
					final IQ iq = new IQNotifyTeamMemberAdded(
							artifact.getArtifactUUID(),
							getContactModel().readContact(session.getJabberId()));
					iq.setTo(subscription.getJabberId().getJID());
					iq.setFrom(session.getJabberId().getJID());
					send(subscription.getJabberId(), iq);
				}
			}
		}
		catch(final SQLException sqlx) {
			logger.error("[RMODEL] [ARTIFACT] [SUBSCRIBE] [SQL ERROR]", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(final UnauthorizedException ux) {
			logger.error("[RMODEL] [ARTIFACT] [SUBSCRIBE] [AUTHORIZATION ERROR]", ux);
			throw ParityErrorTranslator.translate(ux);
		}
		catch(final RuntimeException rx) {
			logger.error("[RMODEL] [ARTIFACT] [SUBSCRIBE] [UNKNOWN ERROR]", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Unsubscribe a user from an artifact.
	 * 
	 * @param artifact
	 *            The artifact to unsubscribe the user from.
	 * @throws ParityServerModelException
	 */
	void unsubscribe(final Artifact artifact) throws ParityServerModelException {
		logApiId();
		logger.debug(artifact);
		try {
			final Integer artifactId = artifact.getArtifactId();
			final String username = session.getJID().getNode();
			final Integer rowCount =
				artifactSubscriptionSql.selectCount(artifactId, username);
			if(0 == rowCount) {
				final StringBuffer warning =
					new StringBuffer("[RMODEL] [ARTIFACT] [UNSUBSCRIBE USER]")
						.append("[NO EXISTING SUBSCRIPTION]");
				logger.warn(warning);
			}
			else {
				artifactSubscriptionSql.delete(artifactId, username);

				// notify all remaining team members re:  the removed team member
				final List<ArtifactSubscription> subscrtipions =
					getSubscription(artifact.getArtifactUUID());
				for(final ArtifactSubscription subscription : subscrtipions) {
					final IQ iq = new IQNotifyTeamMemberRemoved(
							artifact.getArtifactUUID(),
							getContactModel().readContact(session.getJabberId()));
					iq.setTo(subscription.getJabberId().getJID());
					iq.setFrom(session.getJabberId().getJID());
					send(subscription.getJabberId(), iq);
				}
			}
		}
		catch(final UnauthorizedException ux) {
			logger.error("[RMODEL] [ARTIFACT] [UNSUBSCRIBE USER] [UNAUTHORIZED]", ux);
			throw ParityErrorTranslator.translate(ux);
		}
		catch(final SQLException sqlx) {
			logger.error("[RMODEL] [ARTIFACT] [UNSUBSCRIBE USER] [SQL ERROR]", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(final RuntimeException rx) {
			logger.error("[RMODEL] [ARTIFACT] [UNSUBSCRIBE USER] [UNKNOWN ERROR]", rx);
			throw ParityErrorTranslator.translate(rx);
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
			final ParityObjectFlag artifactFlag,
			final ArtifactSubscription subscription) {
		final IQArtifactFlag iqArtifactFlag = new IQArtifactFlag(artifactUniqueId, artifactFlag);
		iqArtifactFlag.setTo(buildJID(subscription.getUsername()));
		iqArtifactFlag.setFrom(session.getJID());
		return iqArtifactFlag;
	}

	/**
	 * Format an assertion statement for the state transition assertion.
	 * 
	 * @param currentState
	 *            The artifact's current state.
	 * @param intendedState
	 *            The indented state to move to.
	 * @param allowedStates
	 *            The allowable states.
	 * @return A formatted assertion message.
	 */
	private String formatAssertion(final Artifact.State currentState,
			final Artifact.State intendedState,
			final Artifact.State[] allowedStates) {
		final StringBuffer assertion =
			new StringBuffer("Cannot move artifact state.  ")
			.append("Current State:  ").append(currentState)
			.append("  Attempted State:  ").append(intendedState)
			.append("  Allowed State(s):  ");
		int index = 0;
		for(final Artifact.State allowedState: allowedStates) {
			if(0 != index++) { assertion.append(","); }
			assertion.append(allowedState.toString());
		}
		return assertion.toString();
	}

    /**
     * Notify an artifact's team a draft was created.
     * 
     * @param teamIds
     *            A list of team members.
     * @param eventGenerator
     *            An artifact event generator.
     */
    private void notifyDraftCreated(final Artifact artifact,
            final JabberId createdBy, final Calendar createdOn)
            throws ParityServerModelException, UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:draftcreated");
        notification.writeUniqueId("uniqueId", artifact.getArtifactUUID());
        notification.writeJabberId("createdBy", createdBy);
        notification.writeCalendar("createdOn", createdOn);
        notifyTeam(artifact.getArtifactUUID(), notification.getIQ());
    }

    /**
     * Notify an artifact's team a draft was created.
     * 
     * @param teamIds
     *            A list of team members.
     * @param eventGenerator
     *            An artifact event generator.
     */
    private void notifyDraftDeleted(final Artifact artifact,
            final JabberId deletedBy, final Calendar deletedOn)
            throws ParityServerModelException, UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:draftdeleted");
        notification.writeUniqueId("uniqueId", artifact.getArtifactUUID());
        notification.writeJabberId("deletedBy", deletedBy);
        notification.writeCalendar("deletedOn", deletedOn);
        notifyTeam(artifact.getArtifactUUID(), notification.getIQ());
    }

    /**
     * Notify an artifact's team; a team member was added.
     * 
     * @param artifact
     *            An artifact.
     * @param user
     *            A user.
     * @param eventGenerator
     *            An event generator.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyTeamMemberAdded(final Artifact artifact, final User user)
            throws ParityServerModelException, UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:teammemberadded");
        notification.writeUniqueId("uniqueId", artifact.getArtifactUUID());
        notification.writeJabberId("jabberId", user.getId());
        notifyTeam(artifact.getArtifactUUID(), notification.getIQ());
    }

    /**
     * Notify an artifact's team; a team member was removed.
     * 
     * @param artifact
     *            An artifact.
     * @param user
     *            A user.
     * @param eventGenerator
     *            An event generator.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyTeamMemberRemoved(final Artifact artifact,
            final User user) throws ParityServerModelException,
            UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:teammemberremoved");
        notification.writeUniqueId("uniqueId", artifact.getArtifactUUID());
        notification.writeJabberId("jabberId", user.getId());
        notifyTeam(artifact.getArtifactUUID(), notification.getIQ());
    }

    /**
	 * Update the artifact's state.
	 * 
	 * @param artifact
	 *            The artifact.
	 * @param newState
	 *            The new state.
	 * @throws NotTrueAssertion
	 *             If the state movement is illegal.
	 */
	private void updateState(final Artifact artifact,
			final Artifact.State newState) throws SQLException {
		assertStateTransition(artifact.getArtifactState(), newState);
		artifactSql.updateState(artifact.getArtifactId(),
				artifact.getArtifactState(), newState, session.getJabberId());
	}
}
