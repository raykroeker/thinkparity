/*
 * Nov 28, 2005
 */
package com.thinkparity.server.handler;

import java.util.UUID;

import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import com.thinkparity.server.ParityServerConstants;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.flag.Flag;
import com.thinkparity.server.model.flag.FlagModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQParityFlagHandler extends IQParityAbstractHandler {

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
	private final FlagModel flagModel;

	/**
	 * Create a IQParityFlagHandler.
	 * @param name
	 */
	public IQParityFlagHandler() {
		super(HANDLER_NAME);
		this.flagModel = getFlagModel();
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
			try { flagModel.flag(getArtifactId(packet), getFlag(packet)); }
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

	private UUID getArtifactId(final IQ iq) { return null; }

	private Flag getFlag(final IQ iq) { return null; }
}
