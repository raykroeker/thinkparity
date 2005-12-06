/*
 * Dec 1, 2005
 */
package com.thinkparity.server.handler.controller.user;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.controller.IQController;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.ArtifactModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SubscribeUser extends IQController {

	/**
	 * Create a SubscribeUser.
	 */
	public SubscribeUser() { super(); }

	/**
	 * @see com.thinkparity.server.handler.controller.IQController#handleIQ(org.xmpp.packet.IQ)
	 */
	public IQ handleIQ(IQ iq) throws ParityServerModelException,
			UnauthorizedException {
		logger.info("handleIQ(IQ)");
		final ArtifactModel artifactModel = getArtifactModel();
		artifactModel.subscribe(extractArtifact(artifactModel, iq));
		return createResult(iq);
	}
}
