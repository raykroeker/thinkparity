/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

import org.jivesoftware.messenger.PacketRouter;
import org.jivesoftware.messenger.XMPPServer;
import org.xmpp.packet.IQ;

import com.thinkparity.server.ParityServerConstants;
import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.io.sql.artifact.ArtifactSql;
import com.thinkparity.server.model.io.sql.artifact.ArtifactSubscriptionSql;
import com.thinkparity.server.model.user.User;
import com.thinkparity.server.packet.IQArtifact;
import com.thinkparity.server.packet.IQArtifactFlag;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ArtifactModelImpl extends AbstractModelImpl {

	/**
	 * Artifact sql interface.
	 */
	private final ArtifactSql artifactSql;

	/**
	 * Artifact subscription sql.
	 */
	private final ArtifactSubscriptionSql artifactSubscriptionSql;

	/**
	 * Handle to the xmpp server.
	 */
	private final XMPPServer xmppServer;

	/**
	 * Create a ArtifactModelImpl.
	 */
	ArtifactModelImpl(final XMPPServer xmppServer) {
		super();
		this.artifactSql = new ArtifactSql();
		this.artifactSubscriptionSql = new ArtifactSubscriptionSql();
		this.xmppServer = xmppServer;
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
			artifactSql.insert(artifactUUID);
			return artifactSql.select(artifactUUID);
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
			final PacketRouter router = xmppServer.getPacketRouter();
			final UUID artifactUUID = artifact.getArtifactUUID();
			IQArtifact iqArtifact;
			for(ArtifactSubscription subscription : subscriptions) {
				iqArtifact = createIQArtifactFlag(artifactUUID, artifactFlag, subscription);
				logger.debug(iqArtifact);
				router.route(iqArtifact);
			}
		}
		catch(SQLException sqlx) {
			logger.error("flag(Artifact,ArtifactFlag)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
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
	 * Subscribe a user to an artifact.
	 * 
	 * @param user
	 *            The user to subscribe.
	 * @param artifact
	 *            The artifact to subscribe the user to.
	 * @throws ParityServerModelException
	 */
	void subscribe(final User user, final Artifact artifact)
			throws ParityServerModelException {
		logger.info("subscribe(User,Artifact)");
		logger.debug(user);
		logger.debug(artifact);
		try {
			final Integer artifactId = artifact.getArtifactId();
			final String username = user.getUsername();
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
	 * @param user
	 *            The user to unsubscribe.
	 * @param artifact
	 *            The artifact to unsubscribe the user from.
	 * @throws ParityServerModelException
	 */
	void unsubscribe(final User user, final Artifact artifact)
			throws ParityServerModelException {
		logger.info("unsubscribe(User,Artifact)");
		logger.debug(user);
		logger.debug(artifact);
		try {
			final Integer artifactId = artifact.getArtifactId();
			final String username = user.getUsername();
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
	 * Create a flag packet to send to the subscribed users.
	 * 
	 * @return The flag iq packet.
	 */
	private IQArtifact createIQArtifactFlag(final UUID artifactUUID,
			final ParityObjectFlag artifactFlag,
			final ArtifactSubscription subscription) {
		final IQArtifact iqArtifact = new IQArtifactFlag(artifactUUID, artifactFlag);
		iqArtifact.setTo(xmppServer.createJID(subscription.getUsername(), ParityServerConstants.CLIENT_RESOURCE));
		iqArtifact.setType(IQ.Type.set);
		return iqArtifact;
	}
}
