/*
 * Dec 12, 2005
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
public class AcceptKeyRequest extends IQHandler {

	/**
	 * Create a AcceptKeyRequest.
	 * @param action
	 */
	public AcceptKeyRequest() {	super(IQAction.ACCEPTKEYREQUEST); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ, com.thinkparity.server.model.session.Session)
	 */
	public IQ handleIQ(IQ iq, Session session)
			throws ParityServerModelException, UnauthorizedException {
        logApiId();
		final ArtifactModel artifactModel = getArtifactModel(session);
		artifactModel.acceptKeyRequest(extractUniqueId(iq), extractJID(iq));
		return createResult(iq);
	}
}
