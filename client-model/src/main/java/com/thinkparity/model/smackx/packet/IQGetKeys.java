/*
 * Feb 17, 2006
 */
package com.thinkparity.model.smackx.packet;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetKeys extends IQParity {

	/**
	 * Create a IQGetKeys.
	 * 
	 */
	public IQGetKeys() { super(Action.GETKEYS); }
}
