/*
 * Nov 20, 2005
 */
package com.thinkparity.browser.model.util;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.api.ParityObjectVersion;
import com.thinkparity.model.parity.model.document.DocumentActionData;
import com.thinkparity.model.parity.model.document.DocumentVersion;

/**
 * Utility methods that operate on the parity version objects.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class ParityObjectVersionUtil {

	/**
	 * Singleton instance.
	 */
	private static final ParityObjectVersionUtil singleton;

	static {
		singleton = new ParityObjectVersionUtil();
	}

	/**
	 * Determine whether or not the version has any action data associated with
	 * it.
	 * 
	 * @param version
	 *            The version.
	 * @return True if it contains action data; false otherwise.
	 */
	public static Boolean containsActionData(final ParityObjectVersion version) {
		return singleton.containsActionDataImpl(version);
	}

	/**
	 * Create a ParityObjectVersionUtil.
	 */
	private ParityObjectVersionUtil() { super(); }

	/**
	 * Determine whether or not the version has any action data associated with
	 * it.
	 * 
	 * @param version
	 *            The version.
	 * @return True if it contains action data; false otherwise.
	 */
	private Boolean containsActionDataImpl(final ParityObjectVersion version) {
		// HACK:  Not sure how to model this correctly
		if(version instanceof DocumentVersion) {
			final DocumentVersion dVersion = (DocumentVersion) version;
			final DocumentActionData actionData = dVersion.getActionData();
			return actionData.containsData();
		}
		throw Assert.createUnreachable("containsActionDataImpl(ParityObjectVersion)");
	}
}
