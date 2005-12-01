/*
 * Nov 29, 2005
 */
package com.thinkparity.server.log4j.or;

import java.text.SimpleDateFormat;

import com.thinkparity.codebase.StringUtil.Separator;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public interface IRendererConstants {
	
	public static final Separator EMPTY_STRING = Separator.EmptyString;

	public static final String ID = "id:";

	public static final String NULL = "null";
	
	public static final String PREFIX_SUFFIX = "[";

	public static final String SUFFIX = "]";

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
