/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx;

import com.thinkparity.codebase.log4j.Loggable;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * PacketX
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class PacketX implements Loggable, PacketExtension {

	/**
	 * Create a PacketX
	 */
	public PacketX() { super(); }
}
