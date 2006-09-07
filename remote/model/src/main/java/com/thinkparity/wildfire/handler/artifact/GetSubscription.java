/*
 * Feb 16, 2006
 */
package com.thinkparity.wildfire.handler.artifact;

import java.util.List;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.model.ParityServerModelException;
import com.thinkparity.model.artifact.Artifact;
import com.thinkparity.model.artifact.ArtifactModel;
import com.thinkparity.model.artifact.ArtifactSubscription;
import com.thinkparity.model.session.Session;

import com.thinkparity.server.org.xmpp.packet.IQGetSubscription;
import com.thinkparity.wildfire.handler.IQAction;
import com.thinkparity.wildfire.handler.IQHandler;

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
	 * @see com.thinkparity.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ, com.thinkparity.model.session.Session)
	 */
	public IQ handleIQ(IQ iq, Session session)
			throws ParityServerModelException, UnauthorizedException {
        logApiId();
		final ArtifactModel artifactModel = getArtifactModel(session);
		final Artifact artifact = artifactModel.read(extractUniqueId(iq));
		final List<ArtifactSubscription> subscriptions = artifactModel.getSubscription(extractUniqueId(iq));
		return createResult(iq, session, artifact, subscriptions);
	}

	private IQ createResult(final IQ iq, final Session session,
			final Artifact artifact, final List<ArtifactSubscription> subscriptions) {
		final IQ result = new IQGetSubscription(artifact.getUniqueId(), subscriptions);
		result.setID(iq.getID());
		result.setTo(session.getJID());
		result.setFrom(session.getJID());
		return result;
	}
}
