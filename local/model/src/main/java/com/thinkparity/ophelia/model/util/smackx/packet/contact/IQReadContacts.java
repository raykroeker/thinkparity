/*
 * Mar 1, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet.contact;

import com.thinkparity.ophelia.model.util.smackx.packet.IQParity;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadContacts extends IQParity {

	/**
	 * Create a IQReadContacts.
	 * 
	 */
	public IQReadContacts() { super(Action.READCONTACTS); }

}
