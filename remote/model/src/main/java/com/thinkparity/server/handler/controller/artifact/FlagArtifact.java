/*
 * Dec 1, 2005
 */
package com.thinkparity.server.handler.controller.artifact;

import org.dom4j.Element;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.ElementName;
import com.thinkparity.server.handler.controller.IQController;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.artifact.ParityObjectFlag;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FlagArtifact extends IQController {

	/**
	 * Create a FlagArtifact.
	 */
	public FlagArtifact() { super(); }

	/**
	 * @see com.thinkparity.server.handler.controller.IQController#handleIQ(org.xmpp.packet.IQ)
	 */
	public IQ handleIQ(final IQ iq) throws ParityServerModelException,
			UnauthorizedException {
		logger.info("handleIQ(IQ)");
		final ArtifactModel artifactModel = getArtifactModel();
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
		final Element child = iq.getChildElement();
		final Element flagElement = child.element(ElementName.FLAG.getElementName());
		return ParityObjectFlag.valueOf((String) flagElement.getData());
	}
}
