/*
 * Dec 1, 2005
 */
package com.thinkparity.server.handler.user;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.IQAction;
import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UnsubscribeUser extends IQHandler {

	/**
	 * Create a UnsubscribeUser.
	 */
	public UnsubscribeUser() { super(IQAction.UNSUBSCRIBEUSER); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.server.model.session.Session)
	 */
	public IQ handleIQ(final IQ iq, final Session session) throws ParityServerModelException,
			UnauthorizedException {
		logger.info("[RMODEL] [USER] [UNSUBSCRIBE]");
		final ArtifactModel artifactModel = getArtifactModel(session);
		artifactModel.unsubscribe(extractArtifact(artifactModel, iq));
		return createResult(iq);
	}
}
