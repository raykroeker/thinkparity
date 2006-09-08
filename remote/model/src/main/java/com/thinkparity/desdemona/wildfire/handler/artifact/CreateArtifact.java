/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;


import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.wildfire.handler.IQAction;
import com.thinkparity.desdemona.wildfire.handler.IQHandler;

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
	 * @see com.thinkparity.desdemona.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.desdemona.model.session.Session)
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
        logApiId();
		final ArtifactModel artifactModel = getArtifactModel(session);
		artifactModel.create(extractUniqueId(iq));
		return createResult(iq);
	}
}
