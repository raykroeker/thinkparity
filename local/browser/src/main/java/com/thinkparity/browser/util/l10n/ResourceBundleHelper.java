/*
 * Oct 7, 2005
 */
package com.thinkparity.browser.util.l10n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * ResourceBundleHelper
 * This helper class is used to obtain sets of resources from a bundle within
 * a set context.  This is useful when the eventual user of a resource string is
 * a ui class that maps key names to variable names.
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ResourceBundleHelper {

	/**
	 * Used to separate the context from the local key.
	 */
	private static final String DOT = ".";

	/**
	 * Handle to the bunde from which to extract text.
	 */
	private final ResourceBundle bundle;

	/**
	 * Context within which to obtain keys in the resource bundle.
	 */
	private final String context;

	/**
	 * Create a ResourceBundleHelper
	 * 
	 * @param bundle
	 *            The bundle to use.
	 * @param context
	 *            The context to use.
	 */
	public ResourceBundleHelper(final ResourceBundle bundle,
			final String context) {
		super();
		this.bundle = bundle;
		this.context = context;
	}

	/**
	 * Obtain the string for the given local key. This will attempt to locate
	 * the resource within the context for the key.
	 * 
	 * @param localKey
	 *            The local key.
	 * @return The string referenced by the local key within the context.
	 */
	public String getString(final String localKey) {
		final String qualifiedKey = getQualifiedKey(localKey);
		try { return bundle.getString(qualifiedKey); }
		catch(MissingResourceException mrx) { return "!" + qualifiedKey + "!"; }
	}

	/**
	 * Obtain the formatted string for the given local key. This will attempt to
	 * locate the resource within the context for the key, then format the
	 * message.
	 * 
	 * @param localKey
	 *            The local key.
	 * @param stringData
	 *            The message data to insert.
	 * @return The formatted string.
	 */
	public String getString(final String localKey, final Object[] stringData) {
		final String qualifiedKey = getQualifiedKey(localKey);
		final String string = getString(localKey);
		try { return MessageFormat.format(string, stringData); }
		catch(IllegalArgumentException iax) { return "!!" + qualifiedKey + "!!"; }
	}

	/**
	 * Get the qualified key. This will simply prefix the local key with a the
	 * context.
	 * 
	 * @param localKey
	 *            The key for which to find the text.
	 * @return The fully qualified key.
	 */
	private String getQualifiedKey(final String localKey) {
		if(isSetContext()) {
			return new StringBuffer(context)
				.append(DOT)
				.append(localKey).toString();
		}
		else { return localKey; }
	}

	/**
	 * Determine whether or not the context has been set.
	 * 
	 * @return True if it has, false otherwise.
	 */
	private Boolean isSetContext() {
		if(null != context && 0 < context.length()) { return Boolean.TRUE; }
		else { return Boolean.FALSE; }
	}
}
