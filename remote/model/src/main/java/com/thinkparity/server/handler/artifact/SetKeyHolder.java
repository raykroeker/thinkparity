/*
 * Dec 7, 2005
 */
package com.thinkparity.server.handler.artifact;

import org.dom4j.Element;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.server.handler.IQAction;
import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.org.dom4j.ElementName;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SetKeyHolder extends IQHandler {

	/**
	 * Create a SetKeyHolder.
	 */
	public SetKeyHolder() { super(IQAction.SETKEYHOLDER); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.server.model.session.Session)
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
		logger.info("handleIQ(IQ)");
		final ArtifactModel artifactModel = getArtifactModel(session);
		artifactModel.setKeyHolder(
				extractArtifact(artifactModel, iq), extractJID(iq));
		return createResult(iq);
	}

	/**
	 * Extract the jive id from the iq.
	 * 
	 * @param iq
	 *            The iq.
	 * @return The jive id.
	 */
	private JID extractJID(final IQ iq) {
		final Element childElement = iq.getChildElement();
		final Element jidElement = getElement(childElement, ElementName.USERNAME);
		return buildJID((String) jidElement.getData());
	}
}
