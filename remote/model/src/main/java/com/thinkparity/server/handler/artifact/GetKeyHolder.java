/*
 * Feb 14, 2006
 */
package com.thinkparity.server.handler.artifact;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.server.handler.IQAction;
import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.org.jivesoftware.messenger.JIDBuilder;
import com.thinkparity.server.org.xmpp.packet.IQGetKeyHolder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GetKeyHolder extends IQHandler {

	/**
	 * Create a GetKeyHolder.
	 * @param action
	 */
	public GetKeyHolder() { super(IQAction.GETKEYHOLDER); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.server.model.session.Session)
	 *      
	 */
	public IQ handleIQ(IQ iq, Session session)
			throws ParityServerModelException, UnauthorizedException {
		logger.info("handleIQ(IQ,Session)");
		logger.debug(iq);
		logger.debug(session);
		final ArtifactModel artifactModel = getArtifactModel(session);
		final Artifact artifact = artifactModel.get(extractUniqueId(iq));
		return createResult(iq, session, artifact);
	}

	private IQ createResult(final IQ iq, final Session session,
			final Artifact artifact) {
		// send the requestor an acceptance packet
		final JID jid = JIDBuilder.build(artifact.getArtifactKeyHolder());
		final IQ  result = new IQGetKeyHolder(artifact.getArtifactUUID(), jid);
		result.setID(iq.getID());
		result.setTo(session.getJID());
		result.setFrom(session.getJID());
		return result;
	}
}
