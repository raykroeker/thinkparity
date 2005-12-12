/*
 * Dec 7, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

import com.thinkparity.model.parity.model.session.KeyResponse;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQKeyResponse extends IQArtifact {

	/**
	 * The response.
	 */
	private KeyResponse keyResponse;

	/**
	 * Create a IQKeyRequest.
	 * 
	 * @param parityArtifactUUID
	 *            The parity artifact unique id.
	 */
	public IQKeyResponse(final UUID artifactUUID, final KeyResponse keyResponse) {
		super(Action.REQUESTKEY, artifactUUID);
		setKeyResponse(keyResponse);
	}

	/**
	 * @see com.thinkparity.model.smackx.packet.IQArtifact#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() {
		final String xml = new StringBuffer(startQueryXML())
			.append(getActionXML())
			.append(getArtifactUUIDXML())
			.append(getResponseXML())
			.append(finishQueryXML()).toString();
		logger.debug(xml);
		return xml;
	}

	/**
	 * Obtain the key response.
	 * 
	 * @return The key response.
	 */
	public KeyResponse getKeyResponse() { return keyResponse; }

	/**
	 * Set the key response.
	 * 
	 * @param keyResponse
	 *            The key response.
	 */
	public void setKeyResponse(final KeyResponse keyResponse) {
		this.keyResponse = keyResponse;
	}

	/**
	 * Obtain the response xml stanza for the key response.
	 * 
	 * @return The response xml stanza.
	 */
	private String getResponseXML() {
		return new StringBuffer("<response>")
			.append(getKeyResponse().toString())
			.append("</response>").toString();
	}
}
