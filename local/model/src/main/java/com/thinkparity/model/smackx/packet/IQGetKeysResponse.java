/*
 * Feb 17, 2006
 */
package com.thinkparity.model.smackx.packet;

import java.util.List;
import java.util.UUID;

import org.jivesoftware.smack.packet.IQ;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetKeysResponse extends IQ {

	/**
	 * The artifact unique ids.
	 * 
	 */
	private final List<UUID> keys;

	/**
	 * Create a IQGetKeysResponse.
	 * 
	 * @param keys
	 *            The keys.
	 */
	public IQGetKeysResponse(final List<UUID> keys) {
		super();
		this.keys = keys;
	}

	
	/**
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 * 
	 */
	public String getChildElementXML() { return null; }

	/**
	 * @return Returns the keys.
	 */
	public List<UUID> getKeys() { return keys; }

}
