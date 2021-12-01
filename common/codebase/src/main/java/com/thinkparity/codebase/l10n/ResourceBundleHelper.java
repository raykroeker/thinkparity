/*
 * Oct 7, 2005
 */
package com.thinkparity.codebase.l10n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * ResourceBundleHelper This helper class is used to obtain sets of resources
 * from a bundle within a set l18Context. This is useful when the eventual user
 * of a resource string is a ui class that maps key names to variable names.
 * 
 * @author raymond@raykroeker.com
 * @version 1.0
 */
public class ResourceBundleHelper {

	/**
	 * Used to separate the localization context from the local key.
	 * 
	 */
	private static final String DOT = ".";

	/**
	 * The resource bundle.
	 * 
	 */
	private final ResourceBundle bundle;

	/**
	 * The localization context for the user.
	 * 
	 */
	private final L18nContext l18nContext;

	/**
     * Create ResourceBundleHelper
     * 
     * @param bundle
     *            A resource bundle.
     * @param l18nContext
     *            A localization context.
     */
	public ResourceBundleHelper(final ResourceBundle bundle,
			final L18nContext l18nContext) {
		super();
		this.bundle = bundle;
		this.l18nContext = l18nContext;
	}

	/**
	 * Obtain the string for the given local key. This will attempt to locate
	 * the resource within the l18Context for the key.
	 * 
	 * @param localKey
	 *            The local key.
	 * @return The string referenced by the local key within the l18Context.
	 */
	public String getString(final String localKey) {
		final String qualifiedKey = getQualifiedKey(localKey);
		try { return bundle.getString(qualifiedKey); }
		catch(final MissingResourceException mrx) {
			return "!" + qualifiedKey + "!";
		}
	}

	/**
	 * Obtain the formatted string for the given local key. This will attempt to
	 * locate the resource within the l18Context for the key, then format the
	 * message.
	 * 
	 * @param localKey
	 *            The local key.
	 * @param arguments
	 *            The message data to insert.
	 * @return The formatted string.
	 */
	public String getString(final String localKey, final Object[] arguments) {
		final String qualifiedKey = getQualifiedKey(localKey);
		final String string = getString(localKey);
		try { return MessageFormat.format(string, arguments); }
		catch(final IllegalArgumentException iax) {
			return "!!" + qualifiedKey + "!!";
		}
	}

	/**
	 * Get the qualified key. This will simply prefix the local key with a the
	 * l18Context.
	 * 
	 * @param localKey
	 *            The key for which to find the text.
	 * @return The fully qualified key.
	 */
	private String getQualifiedKey(final String localKey) {
		if(isSetContext()) {
			return new StringBuffer(l18nContext.getLookupContext())
				.append(DOT)
				.append(localKey).toString();
		}
		else { return localKey; }
	}

	/**
	 * Determine whether or not the l18Context has been set.
	 * 
	 * @return True if it has, false otherwise.
	 */
	private Boolean isSetContext() {
		if(null != l18nContext && 0 < l18nContext.getLookupContext().length()) {
			return Boolean.TRUE;
		}
		else { return Boolean.FALSE; }
	}
}
