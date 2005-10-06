/*
 * Oct 5, 2005
 */
package com.thinkparity.codebase;

import com.thinkparity.codebase.DateUtil.DateImage;
import com.thinkparity.codebase.StringUtil.Separator;

/**
 * PropertiesUtil
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class PropertiesUtil {

	/**
	 * Date format.
	 * @see DateUtil.DateImage#YearMonthDayHourMinute
	 */
	private static final DateImage commentDateFormat =
		DateImage.YearMonthDayHourMinute;

	/**
	 * Obtain a default properties style comment.
	 * 
	 * @return The comment.
	 */
	public static String generateComment() {
		return new StringBuffer("# Parity Software International Inc.")
			.append(Separator.SystemNewLine)
			.append("# www.thinkparity.com")
			.append(Separator.SystemNewLine)
			.append("# ")
			.append(DateUtil.format(
					System.currentTimeMillis(), commentDateFormat)).toString();
	}

	/**
	 * Create a new PropertiesUtil [Singleton]
	 */
	private PropertiesUtil() { super(); }
}
