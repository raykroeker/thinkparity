/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import org.dom4j.Element;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;

import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.util.dom4j.ElementName;
import com.thinkparity.desdemona.wildfire.handler.IQAction;
import com.thinkparity.desdemona.wildfire.handler.IQHandler;

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
	 * @see com.thinkparity.desdemona.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.desdemona.model.session.Session)
	 */
	public IQ handleIQ(final IQ iq, final Session session) throws ParityServerModelException,
			UnauthorizedException {
        logApiId();
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
	private ArtifactFlag extractFlag(final IQ iq) {
		final Element childElement = iq.getChildElement();
		final Element flagElement = getElement(childElement, ElementName.FLAG);
		return ArtifactFlag.valueOf((String) flagElement.getData());
	}
}
