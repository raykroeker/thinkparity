/*
 * Nov 30, 2005
 */
package com.thinkparity.server.handler;

import org.dom4j.Element;
import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.jivesoftware.messenger.user.UserNotFoundException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import com.thinkparity.server.ParityServerConstants;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.artifact.ParityObjectFlag;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQParityArtifactHandler extends IQParityAbstractHandler {

	/**
	 * List of possible actions available within the parity artifact handler.
	 * 
	 */
	private enum Action { CREATE, FLAG, SUBSCRIBE, UNSUBSCRIBE }

	/**
	 * Xml element name for the action.
	 */
	private static final String ELEMENT_NAME_ACTION = "action";

	/**
	 * Xml element name for the flag.
	 */
	private static final String ELEMENT_NAME_FLAG = "flag";

	private static final String HANDLER_NAME;

	private static final IQHandlerInfo IQ_INFO;

	private static final String IQ_INFO_NAME;

	private static final String IQ_INFO_NAMESPACE;

	static {
		HANDLER_NAME = ParityServerConstants.IQ_PARITY_ARTIFACT_HANDER_NAME;

		IQ_INFO_NAME = ParityServerConstants.IQ_PARITY_INFO_NAME;
		IQ_INFO_NAMESPACE = ParityServerConstants.IQ_PARITY_ARTIFACT_HANDLER_INFO_NAMESPACE;
		IQ_INFO = new IQHandlerInfo(IQ_INFO_NAME, IQ_INFO_NAMESPACE);
	}

	/**
	 * Handle to the artifact model api.
	 */
	private final ArtifactModel artifactModel;

	/**
	 * Create a IQParityArtifactHandler.
	 */
	public IQParityArtifactHandler() {
		super(HANDLER_NAME);
		this.artifactModel = getArtifactModel();
	}

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#getInfo()
	 */
	public IQHandlerInfo getInfo() { return IQ_INFO; }

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
	 */
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		logger.info("handleIQ(IQ)");
		logger.debug(packet);
		final IQ result = createResult(packet);
		if(isTypeSet(packet)) {
			try {
				switch(getAction(packet)) {
				case CREATE:
					artifactModel.create(getArtifactUUID(packet));
					break;
				case FLAG:
					artifactModel.flag(getArtifact(packet), getFlag(packet));
					break;
				case SUBSCRIBE:
					artifactModel.subscribe(getFromUser(packet), getArtifact(packet));
					break;
				case UNSUBSCRIBE:
					artifactModel.unsubscribe(getFromUser(packet), getArtifact(packet));
					break;
				default:
					result.setError(PacketError.Condition.bad_request);
				}
			}
			catch(UserNotFoundException unfx) {
				logger.error("handleIQ(IQ)", unfx);
				result.setError(translate("handleIQ(IQ)", unfx));
			}
			catch(ParityServerModelException psmx) {
				logger.error("handleIQ(IQ)", psmx);
				result.setError(translate("handleIQ(IQ)", psmx));
			}
		}
		else { result.setError(PacketError.Condition.bad_request); }
		return result;
	}

	/**
	 * Extract the action (api) from the iq xml document.
	 * 
	 * @param iq
	 *            The iq xml document.
	 * @return The action.
	 */
	private Action getAction(final IQ iq) {
		final Element child = iq.getChildElement();
		final Element actionElement = child.element(
				IQParityArtifactHandler.ELEMENT_NAME_ACTION);
		return Action.valueOf((String) actionElement.getData());
	}

	/**
	 * Extract the flag from the internet query.
	 * 
	 * @param iq
	 *            The internet query.
	 * @return The unique id.
	 */
	private ParityObjectFlag getFlag(final IQ iq) {
		final Element child = iq.getChildElement();
		final Element flagElement = child.element(IQParityArtifactHandler.ELEMENT_NAME_FLAG);
		return ParityObjectFlag.valueOf((String) flagElement.getData());
	}
}
