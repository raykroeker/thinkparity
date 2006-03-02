/*
 * Feb 17, 2006
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
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.org.xmpp.packet.IQGetKeys;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GetArtifactKeys extends IQHandler {

	/**
	 * Create a GetArtifactKeys.
	 * 
	 */
	public GetArtifactKeys() { super(IQAction.GETKEYS); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.server.model.session.Session)
	 * 
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
		logger.info("[RMODEL] [ARTIFACT] [GET KEYS]");
		final ArtifactModel artifactModel = getArtifactModel(session);
		final List<Artifact> artifacts = artifactModel.listForKeyHolder();
		return createResult(iq, session, artifacts);
	}

	private IQ createResult(final IQ iq, final Session session,
			final List<Artifact> artifacts) {
		final IQ result = new IQGetKeys(artifacts);
		result.setID(iq.getID());
		result.setTo(session.getJID());
		result.setFrom(session.getJID());
		return result;
	}
}
