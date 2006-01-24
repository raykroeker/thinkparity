/*
 * Jan 23, 2006
 */
package com.thinkparity.codebase.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.thinkparity.codebase.StringUtil.Separator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Log4JUtil {

	/**
	 * Log the class hierarchy of the given object.
	 * 
	 * @param logger
	 *            The logger.
	 * @param level
	 *            The log message level.
	 * @param object
	 *            The object.
	 */
	public static void logClassHierarchy(final Logger logger,
			final Level level, final Object object) {
		log(logger, level, fillInHierarchy(object));
	}

	/**
	 * Fill in the class hierarchy for the specified object.
	 * 
	 * @param object
	 *            The object to fill in.
	 * @return A formatted class hierarchy of the object; or null if the object
	 *         is null.
	 */
	private static StringBuffer fillInHierarchy(final Object object) {
		if(null == object) { return null; }
		else {
			final StringBuffer buffer = new StringBuffer(object.getClass().getSimpleName());
			Class parent = object.getClass().getSuperclass();
			while(null != parent) {
				buffer.append(Separator.SemiColon)
					.append(parent.getSimpleName());
				parent = parent.getSuperclass();
			}
			return buffer;
		}
	}

	/**
	 * Log a message to the provided logger.
	 * 
	 * @param logger
	 *            The logger.
	 * @param priority
	 *            The message priority.
	 * @param message
	 *            The message.
	 */
	private static void log(final Logger logger, final Priority priority,
			final StringBuffer message) {
		logger.log(priority, message);
	}

	/**
	 * Create a Log4JUtil [Singleton]
	 * 
	 */
	private Log4JUtil() { super(); }
}
