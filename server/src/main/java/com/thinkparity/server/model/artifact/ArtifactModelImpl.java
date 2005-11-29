/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

import org.jivesoftware.messenger.PacketRouter;
import org.jivesoftware.messenger.XMPPServer;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.io.sql.artifact.ArtifactSubscriptionSql;
import com.thinkparity.server.packet.IQFlag;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ArtifactModelImpl extends AbstractModelImpl {

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
		this.artifactSubscriptionSql = new ArtifactSubscriptionSql();
		this.xmppServer = xmppServer;
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
	void flag(final Artifact artifact, final ArtifactFlag flag)
			throws ParityServerModelException {
		logger.info("flag(Artifact,ArtifactFlag)");
		logger.debug(artifact);
		logger.debug(flag);
		try {
			final Collection<ArtifactSubscription> subscriptions =
				artifactSubscriptionSql.select(artifact);

			// send an IQFlag packet to each subscribed user
			final PacketRouter router = xmppServer.getPacketRouter();
			for(ArtifactSubscription subscription : subscriptions) {
				router.route(createIQFlag(subscription, flag));
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
	 * Create a parity flag IQ based upon a subscription and a flag.
	 * 
	 * @param subscription
	 *            The subscription.
	 * @param flag
	 *            The flag.
	 * @return The parity flag IQ.
	 */
	private IQFlag createIQFlag(final ArtifactSubscription subscription,
			final ArtifactFlag flag) {
		final UUID id = subscription.getArtifact().getId().getId();
		final IQFlag iqFlag = new IQFlag(flag, id);
		iqFlag.setTo(subscription.getUser().getUsername());
		return iqFlag;
	}
}
