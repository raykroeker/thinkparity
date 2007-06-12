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
 * <b>Title:</b>thinkParity CommonCodebase Properties Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
public final class PropertiesUtil {

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
     * Copy a property from one map to another. This is the same as calling
     * {@link #copy(Properties, Properties, String, Boolean.FALSE)}.
     * 
     * @param from
     *            The source <code>Properties</code>.
     * @param to
     *            The target <code>Properties</code>.
     * @param name
     *            The property name <code>String</code>.
     * @return The orignal value of the property, within the target, or null if
     *         no such property existed.
     */
    public static Object copy(final Properties from, final Properties to,
            final String name) {
        return copy(from, to, name, Boolean.FALSE);
    }

	/**
     * Copy a property from one map to another.
     * 
     * @param from
     *            The source <code>Properties</code>.
     * @param to
     *            The target <code>Properties</code>.
     * @param name
     *            The property name <code>String</code>.
     * @param verify
     *            Whether or not to verify the property existence from the
     *            source.
     * @return The orignal value of the property, within the target, or null if
     *         no such property existed.
     */
    public static Object copy(final Properties from, final Properties to,
            final String name, final Boolean verify) {
        if (verify.booleanValue())
            verify(from, name);
        if (from.containsKey(name)) {
            return to.setProperty(name, from.getProperty(name));
        } else {
            return null;
        }
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
			buffer.append("# ")
				.append(comments)
				.append(Separator.SystemNewLine);
		buffer.append("# ")
			.append(formatCurrentDateTime())
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
     * Verify that a named property exists.
     * 
     * @param properties
     *            A <code>Properties</code>.
     * @param name
     *            A property name <code>String</code>.
     */
    public static void verify(final Properties properties, final String name) {
        if (!properties.containsKey(name))
            throw new IllegalArgumentException("Properties must contain \"" + name + "\".");
    }

	/**
	 * Create a default comment of the current date\time.
	 * 
	 * @return A default comment of the current date\time.
	 */
	private static String formatCurrentDateTime() {
		final Calendar now = DateUtil.getInstance();
		synchronized(simpleDateFormatLock) {
			return simpleDateFormat.format(now.getTime());
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
