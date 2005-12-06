/*
 * Dec 1, 2005
 */
package com.thinkparity.server.handler.controller.artifact;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.controller.IQController;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.ArtifactModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateArtifact extends IQController {

	/**
	 * Create a CreateArtifact.
	 */
	public CreateArtifact() { super(); }

	/**
	 * @see com.thinkparity.server.handler.controller.IQController#handleIQ(org.xmpp.packet.IQ)
	 */
	public IQ handleIQ(final IQ iq) throws ParityServerModelException,
			UnauthorizedException {
		logger.info("handleIQ(IQ)");
		final ArtifactModel artifactModel = getArtifactModel();
		artifactModel.create(extractUUID(iq));
		return createResult(iq);
	}
}
