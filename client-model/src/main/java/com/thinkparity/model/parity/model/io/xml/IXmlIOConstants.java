/*
 * 26-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import com.thinkparity.codebase.StringUtil.Charset;

import com.thinkparity.model.parity.IParityConstants;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface IXmlIOConstants {
	public static final Charset DEFAULT_CHARSET = Charset.ISO_8859_1;
	public static final String FILE_EXTENSION_DOCUMENT = ".document";
	public static final String FILE_EXTENSION_DOCUMENT_VERSION = ".documentversion";
	public static final String FILE_EXTENSION_PROJECT = ".project";
	public static final String XML_FILE_NAME_ROOT_PROJECT =
		IParityConstants.ROOT_PROJECT_NAME + FILE_EXTENSION_PROJECT;
}
