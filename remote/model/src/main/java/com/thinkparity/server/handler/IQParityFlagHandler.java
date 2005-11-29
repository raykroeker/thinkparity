/*
 * Nov 28, 2005
 */
package com.thinkparity.server.handler;

import java.util.UUID;

import org.dom4j.Element;
import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import com.thinkparity.server.ParityServerConstants;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactFlag;
import com.thinkparity.server.model.artifact.ArtifactId;
import com.thinkparity.server.model.artifact.ArtifactModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQParityFlagHandler extends IQParityAbstractHandler {

	/**
	 * ArtifactFlag xml element.
	 */
	private static final String ELEMENT_NAME_FLAG = "flag";

	/**
	 * Id xml element.
	 */
	private static final String ELEMENT_NAME_ID = "id";

	/**
	 * Name of the handler.
	 */
	private static final String HANDLER_NAME;

	/**
	 * Contains information representing the IQHandler.
	 */
	private static final IQHandlerInfo IQ_INFO;

	/**
	 * Name of the iq packet.
	 */
	private static final String IQ_INFO_NAME;

	/**
	 * Namespace of the iq packet.
	 */
	private static final String IQ_INFO_NAMESPACE;

	static {
		HANDLER_NAME = ParityServerConstants.IQ_PARITY_FLAG_HANDER_NAME;

		IQ_INFO_NAME = ParityServerConstants.IQ_PARITY_FLAG_HANDLER_INFO_NAME;
		IQ_INFO_NAMESPACE =
			ParityServerConstants.IQ_PARITY_FLAG_HANDLER_INFO_NAMESPACE;
		IQ_INFO = new IQHandlerInfo(IQ_INFO_NAME, IQ_INFO_NAMESPACE);		
	}

	/**
	 * Handle to the flag model api.
	 */
	private final ArtifactModel artifactModel;

	/**
	 * Create an IQParityFlagHandler.
	 * 
	 */
	public IQParityFlagHandler() {
		super(HANDLER_NAME);
		this.artifactModel = getArtifactModel();
	}

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#getInfo()
	 */
	public IQHandlerInfo getInfo() { return IQParityFlagHandler.IQ_INFO; }

	/**
	 * @see org.jivesoftware.messenger.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
	 */
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		logger.info("handlePacket(IQ)");
		logger.debug(packet);
		final IQ result = createResult(packet);
		if(isTypeSet(packet)) {
			try { artifactModel.flag(getArtifact(packet), getFlag(packet)); }
			catch(ParityServerModelException psmx) {
				logger.error("handleIQ(IQ)", psmx);
				result.setError(translate("handleIQ(IQ)", psmx));
			}
			catch(RuntimeException rx) {
				logger.error("handleIQ(IQ)", rx);
				result.setError(translate("handleIQ(IQ)", rx));
			}
		}
		else { result.setError(PacketError.Condition.bad_request); }
		return result;
	}

	/**
	 * @see com.thinkparity.server.handler.IQParityAbstractHandler#createResult(org.xmpp.packet.IQ)
	 */
	protected IQ createResult(IQ iq) {
		final IQ result = super.createResult(iq);
		result.setChildElement(
				IQParityFlagHandler.IQ_INFO_NAME,
				IQParityFlagHandler.IQ_INFO_NAME);
		return result;
	}

	/**
	 * Extract the artifact from the internet query.
	 * 
	 * @param iq
	 *            The internet query.
	 * @return The artifact.
	 */
	private Artifact getArtifact(final IQ iq) {
		final Element child = iq.getChildElement();
		final Element idElement = child.element(IQParityFlagHandler.ELEMENT_NAME_ID);
		final ArtifactId id = new ArtifactId(UUID.fromString((String) idElement.getData()));
		return new Artifact(id);
	}

	/**
	 * Extract the flag from the internet query.
	 * 
	 * @param iq
	 *            The internet query.
	 * @return The unique id.
	 */
	private ArtifactFlag getFlag(final IQ iq) {
		final Element child = iq.getChildElement();
		final Element flagElement = child.element(IQParityFlagHandler.ELEMENT_NAME_FLAG);
		return ArtifactFlag.valueOf((String) flagElement.getData());
	}
}
