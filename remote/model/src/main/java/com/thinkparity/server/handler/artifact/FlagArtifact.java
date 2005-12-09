/*
 * Dec 1, 2005
 */
package com.thinkparity.server.handler.artifact;

import org.dom4j.Element;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.IQAction;
import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.artifact.ParityObjectFlag;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.org.dom4j.ElementName;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FlagArtifact extends IQHandler {

	/**
	 * Create a FlagArtifact.
	 */
	public FlagArtifact() { super(IQAction.FLAGARTIFACT); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.server.model.session.Session)
	 */
	public IQ handleIQ(final IQ iq, final Session session) throws ParityServerModelException,
			UnauthorizedException {
		logger.info("handleIQ(IQ,Session)");
		logger.debug(iq);
		logger.debug(session);
		final ArtifactModel artifactModel = getArtifactModel(session);
		artifactModel.flag(extractArtifact(artifactModel, iq), extractFlag(iq));
		return createResult(iq);
	}

	/**
	 * Extract the flag from the iq.
	 * 
	 * @param iq
	 *            The iq.
	 * @return The flag.
	 */
	private ParityObjectFlag extractFlag(final IQ iq) {
		final Element childElement = iq.getChildElement();
		final Element flagElement = getElement(childElement, ElementName.FLAG);
		return ParityObjectFlag.valueOf((String) flagElement.getData());
	}
}
