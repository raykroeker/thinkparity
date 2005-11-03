/*
 * 26-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import com.thinkparity.codebase.CompressionUtil.Level;
import com.thinkparity.codebase.StringUtil.Charset;

import com.thinkparity.model.parity.IParityModelConstants;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface IXmlIOConstants {
	public static final Charset DEFAULT_CHARSET = Charset.ISO_8859_1;
	public static final Level DEFAULT_COMPRESSION = Level.Nine;
	public static final String DIRECTORY_NAME_XML_DATA =
		IParityModelConstants.DIRECTORY_NAME_XML_DATA;
	public static final String FILE_EXTENSION_DOCUMENT = ".document";
	public static final String FILE_EXTENSION_DOCUMENT_CONTENT = ".documentcontent";
	public static final String FILE_EXTENSION_DOCUMENT_VERSION = ".documentversion";
	public static final String FILE_EXTENSION_PROJECT = ".project";
}
