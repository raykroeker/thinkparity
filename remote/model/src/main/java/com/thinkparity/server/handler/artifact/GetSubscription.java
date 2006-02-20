/*
 * Feb 16, 2006
 */
package com.thinkparity.server.handler.artifact;

import java.util.List;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.IQAction;
import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.artifact.ArtifactSubscription;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.org.xmpp.packet.IQGetSubscription;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GetSubscription extends IQHandler {

	/**
	 * Create a GetSubscription.
	 * @param action
	 */
	public GetSubscription() { super(IQAction.GETSUBSCRIPTION); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ, com.thinkparity.server.model.session.Session)
	 */
	public IQ handleIQ(IQ iq, Session session)
			throws ParityServerModelException, UnauthorizedException {
		logger.info("handleIQ(IQ,Session)");
		logger.debug(iq);
		logger.debug(session);
		final ArtifactModel artifactModel = getArtifactModel(session);
		final Artifact artifact = artifactModel.get(extractUniqueId(iq));
		final List<ArtifactSubscription> subscriptions = artifactModel.getSubscription(extractUniqueId(iq));
		return createResult(iq, session, artifact, subscriptions);
	}

	private IQ createResult(final IQ iq, final Session session,
			final Artifact artifact, final List<ArtifactSubscription> subscriptions) {
		final IQ result = new IQGetSubscription(artifact.getArtifactUUID(), subscriptions);
		result.setID(iq.getID());
		result.setTo(session.getJID());
		result.setFrom(session.getJID());
		return result;
	}
}
