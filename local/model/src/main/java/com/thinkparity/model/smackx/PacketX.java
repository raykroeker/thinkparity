/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx;

import java.io.UnsupportedEncodingException;
import java.nio.charset.IllegalCharsetNameException;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.PacketExtension;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.util.Base64;

/**
 * PacketX
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class PacketX implements PacketExtension {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(PacketX.class);

	/**
	 * Create a PacketX
	 */
	protected PacketX() { super(); }

	/**
	 * Obtain the parity packet extensions namespace.
	 * @see org.jivesoftware.smack.packet.PacketExtension#getNamespace()
	 */
	public String getNamespace() { return ISmackXConstants.NAMESPACE; }

	/**
	 * Encode the decoded xml string. This api will convert the xml into a base
	 * 64 encoded xml string.
	 * 
	 * @param decodedXML
	 *            The decoded xml string.
	 * @return The base 64 encoded xml string.
	 * @throws IllegalCharsetNameException
	 *             If the charset listed in
	 *             <code>ISmackXConstants#CHARSET</code> cannot be used
	 */
	protected String encode(final String decodedXML) {
		logger.debug(decodedXML);
		try {
			final String encodedXML =
				Base64.encodeBytes(
						decodedXML.getBytes(ISmackXConstants.CHARSET.name()));
			logger.debug(encodedXML);
			return encodedXML;
		}
		catch(UnsupportedEncodingException uex) {
			logger.fatal(uex);
			throw new IllegalCharsetNameException(ISmackXConstants.CHARSET.name());
		}
	}
}
