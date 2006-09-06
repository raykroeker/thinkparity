/*
 * Dec 1, 2005
 */
package com.thinkparity.wildfire.handler.artifact;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;

import com.thinkparity.model.ParityServerModelException;
import com.thinkparity.model.artifact.ArtifactModel;
import com.thinkparity.model.session.Session;

import com.thinkparity.wildfire.handler.IQAction;
import com.thinkparity.wildfire.handler.IQHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateArtifact extends IQHandler {

	/**
	 * Create a CreateArtifact.
	 */
	public CreateArtifact() { super(IQAction.CREATEARTIFACT); }

	/**
	 * @see com.thinkparity.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.model.session.Session)
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
        logApiId();
		final ArtifactModel artifactModel = getArtifactModel(session);
		artifactModel.create(extractUniqueId(iq));
		return createResult(iq);
	}
}
