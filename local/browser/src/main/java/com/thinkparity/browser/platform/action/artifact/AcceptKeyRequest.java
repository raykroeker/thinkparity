/*
 * Feb 28, 2006
 */
package com.thinkparity.browser.platform.action.artifact;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.message.system.KeyRequestMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.session.KeyResponse;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AcceptKeyRequest extends AbstractAction {

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.ARTIFACT_ACCEPT_KEY_REQUEST;
		NAME = "Accept key request";
	}

	/**
	 * Create a AcceptKeyRequest.
	 * 
	 */
	public AcceptKeyRequest() { super("AcceptKeyRequest", ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long messageId = (Long) data.get(DataKey.SYSTEM_MESSAGE_ID);
		try {
			final SystemMessageModel sMModel = getSystemMessageModel();
			final KeyRequestMessage message =
				(KeyRequestMessage) sMModel.read(messageId);
			getSessionModel().sendKeyResponse(message.getArtifactId(),
					message.getRequestedBy(), KeyResponse.ACCEPT);
			sMModel.delete(messageId);
		}
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	public enum DataKey { SYSTEM_MESSAGE_ID }
}
