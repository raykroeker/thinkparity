/*
 * Oct 7, 2005
 */
package com.thinkparity.browser.platform.util.l10n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

/**
 * ResourceBundleHelper This helper class is used to obtain sets of resources
 * from a bundle within a set l18Context. This is useful when the eventual user
 * of a resource string is a ui class that maps key names to variable names.
 * 
 * @author raykroeker@gmail.com
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
	private final String l18Context;

	/**
	 * An apache logger.
	 * 
	 */
	private final Logger logger;

	/**
	 * Create a ResourceBundleHelper
	 * 
	 * @param bundle
	 *            The bundle to use.
	 * @param l18Context
	 *            The l18Context to use.
	 */
	public ResourceBundleHelper(final ResourceBundle bundle,
			final String l18Context) {
		super();
		this.bundle = bundle;
		this.l18Context = l18Context;
		this.logger = LoggerFactory.getLogger(getClass());
		logger.info("l18Context:" + l18Context);
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
			logger.warn("!" + qualifiedKey + "!");
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
			logger.warn("!!" + qualifiedKey + "!!");
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
			return new StringBuffer(l18Context)
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
		if(null != l18Context && 0 < l18Context.length()) { return Boolean.TRUE; }
		else { return Boolean.FALSE; }
	}
}
