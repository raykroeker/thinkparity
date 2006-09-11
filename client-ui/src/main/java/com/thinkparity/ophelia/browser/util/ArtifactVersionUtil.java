/*
 * Nov 20, 2005
 */
package com.thinkparity.ophelia.browser.util;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;

import com.thinkparity.ophelia.model.document.DocumentVersion;

/**
 * Utility methods that operate on the parity version objects.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class ArtifactVersionUtil {

	/**
	 * Singleton instance.
	 */
	private static final ArtifactVersionUtil singleton;

	static {
		singleton = new ArtifactVersionUtil();
	}

	/**
	 * Determine whether or not the version has any action data associated with
	 * it.
	 * 
	 * @param version
	 *            The version.
	 * @return True if it contains action data; false otherwise.
	 */
	public static Boolean containsActionData(final ArtifactVersion version) {
		return singleton.containsActionDataImpl(version);
	}

	/**
	 * Create a ArtifactVersionUtil.
	 */
	private ArtifactVersionUtil() { super(); }

	/**
	 * Determine whether or not the version has any action data associated with
	 * it.
	 * 
	 * @param version
	 *            The version.
	 * @return True if it contains action data; false otherwise.
	 */
	private Boolean containsActionDataImpl(final ArtifactVersion version) {
		// TODO Not sure how to model this correctly
		if(version instanceof DocumentVersion) {
			return Boolean.FALSE;
		}
		throw Assert.createUnreachable("containsActionDataImpl(ParityObjectVersion)");
	}
}
