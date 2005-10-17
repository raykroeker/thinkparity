/*
 * Oct 16, 2005
 */
package com.thinkparity.model.smackx;

import java.nio.charset.Charset;

import com.thinkparity.codebase.StringUtil;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public interface ISmackXConstants {

	/**
	 * Namespace for all think parity packet extensions.
	 */
	public static final String NAMESPACE =
		"http://thinkparity.com/parity/xmpp/extensions";

	/**
	 * Character set to use when converting xml to and from bytes arrays.
	 */
	public static final Charset CHARSET =
		StringUtil.Charset.ISO_8859_1.getCharset();
}
