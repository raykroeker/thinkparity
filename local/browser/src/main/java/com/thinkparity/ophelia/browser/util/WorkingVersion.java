/*
 * Feb 17, 2006
 */
package com.thinkparity.ophelia.browser.util;


import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.util.UUIDGenerator;

/**
 * Used as a place holder in the ui for working versions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WorkingVersion extends DocumentVersion {

	/**
	 * The working version of a document version.
	 * 
	 */
	private static DocumentVersion workingVersion;

	/**
	 * Obtain the working version singleton instance.
	 * 
	 * @return The working version singleton instance.
	 */
	public static DocumentVersion getWorkingVersion() {
		if(null == workingVersion) {
			workingVersion = new WorkingVersion();
			workingVersion.setArtifactUniqueId(UUIDGenerator.nextUUID());
			workingVersion.setVersionId(0L);
		}
		return workingVersion;
	}

	/**
	 * Create a WorkingVersion [Singleton]
	 * 
	 */
	private WorkingVersion() { super(); }

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(Object obj) { return obj instanceof WorkingVersion; }

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() { return getArtifactUniqueId().hashCode(); }
}
