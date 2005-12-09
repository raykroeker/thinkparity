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
public class GetKeyHolder extends IQHandler {

	/**
	 * Create a GetKeyHolder.
	 */
	public GetKeyHolder() { super(IQAction.GETKEYHOLDER); }

	/**
	 * 
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.server.model.session.Session)
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
		logger.info("handleIQ(IQ,Session)");
		logger.debug(iq);
		logger.debug(session);
		final ArtifactModel artifactModel = getArtifactModel(session);
		final JID keyHolder =
			artifactModel.getKeyHolder(extractArtifact(artifactModel, iq));
		return createResult(iq, keyHolder);
	}

	/**
	 * Create a resultant iq xml document containing the keyholder.
	 * 
	 * @param iq
	 *            The request iq xml document.
	 * @param keyHolder
	 *            the keyholder.
	 * @return The resultant iq document.
	 */
	private IQ createResult(final IQ iq, final JID keyHolder) {
		final IQ result = createResult(iq);
		final Element childElement = result.getChildElement();
		final Element keyHolderElement =
			addElement(childElement, ElementName.KEYHOLDER);
		keyHolderElement.setData(keyHolder.getNode());
		return result;
	}
}
