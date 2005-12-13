/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.io.sql.artifact.ArtifactSql;
import com.thinkparity.server.model.io.sql.artifact.ArtifactSubscriptionSql;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.org.xmpp.packet.IQAcceptKeyRequest;
import com.thinkparity.server.org.xmpp.packet.IQArtifact;
import com.thinkparity.server.org.xmpp.packet.IQArtifactFlag;
import com.thinkparity.server.org.xmpp.packet.IQDenyKeyRequest;
import com.thinkparity.server.org.xmpp.packet.IQKeyRequest;

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

	/**
	 * Artifact sql interface.
	 */
	private final ArtifactSql artifactSql;

	/**
	 * Artifact subscription sql.
	 */
	private final ArtifactSubscriptionSql artifactSubscriptionSql;

	/**
	 * Create a ArtifactModelImpl.
	 */
	ArtifactModelImpl(final Session session) {
		super(session);
		this.artifactSql = new ArtifactSql();
		this.artifactSubscriptionSql = new ArtifactSubscriptionSql();
	}

	/**
	 * Deny the key request for the artifact from the jid.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 * @param jid
	 *            The requestor's jive id.
	 * @throws ParityServerModelException
	 */
	void denyKeyRequest(final UUID artifactUUID, final JID jid)
			throws ParityServerModelException {
		logger.info("denyKeyRequest(UUID,JID)");
		logger.debug(artifactUUID);
		logger.debug(jid);
		try {
			// send the requestor a denial
			final IQ iq = new IQDenyKeyRequest(artifactUUID);
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
	 * Accept the key request.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 * @param jid
	 *            The requestor jive id.
	 * @throws ParityServerModelException
	 */
	void acceptKeyRequest(final UUID artifactUUID, final JID jid)
			throws ParityServerModelException {
		logger.info("acceptKeyRequest(UUID,JID)");
		logger.debug(artifactUUID);
		logger.debug(jid);
		try {
			assertEquals(
					ASSERT_KEYHOLDER_SESSION,
					getKeyHolder(artifactUUID), session.getJID());
			final Artifact artifact = get(artifactUUID);
			final Integer artifactId = artifact.getArtifactId();
			final String username = jid.getNode();
			artifactSql.updateKeyHolder(artifactId, username);
			// send the requestor an acceptance packet
			final IQ iq = new IQAcceptKeyRequest(artifactUUID);
			iq.setTo(jid);
			iq.setFrom(session.getJID());
			send(jid, iq);
		}
		catch(SQLException sqlx) {
			logger.error("acceptKeyRequest(UUID,JID)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(UnauthorizedException ux) {
			logger.error("acceptKeyRequest(UUID,JID)", ux);
			throw ParityErrorTranslator.translate(ux);
		}
		catch(RuntimeException rx) {
			logger.error("acceptKeyRequest(UUID,JID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Create an artifact.
	 * 
	 * @param artifactUUID
	 *            The artifact id.
	 * @return The new artifact.
	 * @throws ParityServerModelException
	 */
	Artifact create(final UUID artifactUUID) throws ParityServerModelException {
		logger.info("create(UUID)");
		logger.debug(artifactUUID);
		try {
			final String artifactKeyHolder = session.getJID().getNode();
			artifactSql.insert(artifactUUID, artifactKeyHolder);
			final Artifact artifact = artifactSql.select(artifactUUID);
			// also add a subscription for the creator
			final Integer artifactId = artifact.getArtifactId();
			final String username = session.getJID().getNode();
			artifactSubscriptionSql.insert(artifactId, username);
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
		logger.info("flag(Artifact,ArtifactFlag)");
		logger.debug(artifact);
		logger.debug(artifactFlag);
		try {
			final Integer artifactId = artifact.getArtifactId();
			final Collection<ArtifactSubscription> subscriptions =
				artifactSubscriptionSql.select(artifactId);

			// send an IQFlag packet to each subscribed user
			final UUID artifactUUID = artifact.getArtifactUUID();
			IQ iq;
			for(ArtifactSubscription subscription : subscriptions) {
				iq = createFlag(artifactUUID, artifactFlag, subscription);
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
	 * @param artifactUUID
	 *            An artifact unique id.
	 * @throws ParityServerModelException
	 */
	Artifact get(final UUID artifactUUID) throws ParityServerModelException {
		logger.info("get(UUID)");
		logger.debug(artifactUUID);
		try { return artifactSql.select(artifactUUID); }
		catch(SQLException sqlx) {
			logger.error("get(UUID)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(RuntimeException rx) {
			logger.error("get(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Set the keyholder for the given artifact.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 * @param jid
	 *            The new keyholder's jid.
	 * @return The previous keyholder's JID.
	 * @throws ParityServerModelException
	 */
	JID getKeyHolder(final UUID artifactUUID) throws ParityServerModelException {
		logger.info("getKeyHolder(UUID)");
		logger.debug(artifactUUID);
		try {
			final Artifact artifact = artifactSql.select(artifactUUID);
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

	/**
	 * Request the key from the artifact's key holder. If the key holder is
	 * currently online; the request will be routed to them; otherwise it will
	 * be queued until the user comes online.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 * @throws ParityServerModelException
	 */
	void requestKey(final UUID artifactUUID) throws ParityServerModelException {
		logger.info("requestKey(UUID)");
		logger.debug(artifactUUID);
		try {
			final JID keyHolderJID = getKeyHolder(artifactUUID);
			final IQ iq = new IQKeyRequest(artifactUUID);
			iq.setTo(keyHolderJID);
			iq.setFrom(session.getJID());
			send(keyHolderJID, iq);
		}
		catch(UnauthorizedException ux) {
			logger.error("requestKey(Artifact)", ux);
			throw ParityErrorTranslator.translate(ux);
		}
		catch(RuntimeException rx) {
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
		logger.info("subscribe(User,Artifact)");
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
			else { artifactSubscriptionSql.insert(artifactId, username); }
		}
		catch(SQLException sqlx) {
			logger.error("subscribe(User,Artifact)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(RuntimeException rx) {
			logger.error("subscribe(User,Artifact)", rx);
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
		logger.info("unsubscribe(User,Artifact)");
		logger.debug(artifact);
		try {
			final Integer artifactId = artifact.getArtifactId();
			final String username = session.getJID().getNode();
			final Integer rowCount =
				artifactSubscriptionSql.selectCount(artifactId, username);
			if(0 == rowCount) {
				final StringBuffer warning =
					new StringBuffer("unsubscribe(User,Artifact):  ")
						.append("User does not have a subscription.");
				logger.warn(warning);
			}
			else { artifactSubscriptionSql.delete(artifactId, username); }
		}
		catch(SQLException sqlx) {
			logger.error("unsubscribe(User,Artifact)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(RuntimeException rx) {
			logger.error("unsubscribe(User,Artifact)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Create a flag iq packet to send to a subscription.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 * @param artifactFlag
	 *            The flag.
	 * @param subscription
	 *            The subscription.
	 * @return The flag iq packet.
	 */
	private IQArtifact createFlag(final UUID artifactUUID,
			final ParityObjectFlag artifactFlag,
			final ArtifactSubscription subscription) {
		final IQArtifactFlag iqArtifactFlag = new IQArtifactFlag(artifactUUID, artifactFlag);
		iqArtifactFlag.setTo(buildJID(subscription.getUsername()));
		iqArtifactFlag.setFrom(session.getJID());
		return iqArtifactFlag;
	}
}
