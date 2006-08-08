/*
 * Feb 18, 2006
 */
package com.thinkparity.server.handler.artifact;

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
public class CloseArtifact extends IQHandler {

	/**
	 * Create a CloseArtifact.
	 * 
	 */
	public CloseArtifact() { super(IQAction.CLOSEARTIFACT); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.server.model.session.Session)
	 * 
	 */
	public IQ handleIQ(IQ iq, Session session)
			throws ParityServerModelException, UnauthorizedException {
        logApiId();
		final ArtifactModel artifactModel = getArtifactModel(session);
		artifactModel.close(extractUniqueId(iq));
		return createResult(iq);
	}
}
