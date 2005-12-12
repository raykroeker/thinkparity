/*
 * Oct 5, 2005
 */
package com.thinkparity.codebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import com.thinkparity.codebase.DateUtil.DateImage;
import com.thinkparity.codebase.StringUtil.Separator;


/**
 * PropertiesUtil
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class PropertiesUtil {

	/**
	 * Simple format for the date.
	 */
	private static final SimpleDateFormat simpleDateFormat;

	/**
	 * Syncrhonization lock for the date format.
	 */
	private static final Object simpleDateFormatLock;

	static {
		simpleDateFormat = new SimpleDateFormat(DateImage.ISO.toString());
		simpleDateFormatLock = new Object();
	}

	/**
	 * Print the specified properties to the given buffer; with a default
	 * comment of the current date\time.
	 * 
	 * @param buffer
	 *            The buffer to print to.
	 * @param properties
	 *            The properties to print.
	 */
	public static void print(final StringBuffer buffer,
			final Properties properties) {
		print(buffer, null, properties);
	}

	/**
	 * Print the specified properties to the given buffer.
	 * 
	 * @param buffer
	 *            The buffer to print to.
	 * @param comments
	 *            The comments to print.
	 * @param properties
	 *            The properties to print.
	 */
	public static void print(final StringBuffer buffer, final String comments,
			final Properties properties) {
		if(null == buffer) throw new NullPointerException();
		if(null == properties) throw new NullPointerException();
		if(null != comments)
			buffer.append(comments)
				.append(Separator.SystemNewLine);
		buffer.append(formatCurrentDateTime())
			.append(Separator.SystemNewLine);
		final Collection<String> keys = sortedKeys(properties);
		for(String key : keys) {
			buffer.append(key)
				.append(Separator.FullColon)
				.append(properties.getProperty(key))
				.append(Separator.SystemNewLine);
		}
	}

	/**
	 * Create a default comment of the current date\time.
	 * 
	 * @return A default comment of the current date\time.
	 */
	private static String formatCurrentDateTime() {
		final Calendar now = DateUtil.getInstance();
		synchronized(simpleDateFormatLock) {
			return new StringBuffer("# ")
				.append(simpleDateFormat.format(now.getTime())).toString();
		}
	}

	/**
	 * Extract the property keys into a collection and sort them.
	 * 
	 * @param properties
	 *            The properties.
	 * @return A sorted collection of the property keys.
	 */
	private static Collection<String> sortedKeys(final Properties properties) {
		final Vector<String> keys = new Vector<String>(properties.size());
		final Set<Object> keySet = properties.keySet();
		for(Object key : keySet) { keys.add(key.toString()); }
		Collections.sort(keys);
		return keys;
	}

	/**
	 * Create a new PropertiesUtil [Singleton]
	 */
	private PropertiesUtil() { super(); }
}
