/*
 * Mar 22, 2006
 */
package com.thinkparity.model.parity.model.filter;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageFilter extends Filter {

	/**
	 * Create a SystemMessageFilter.
	 */
	public SystemMessageFilter() {
		super();
	}

	public SystemMessageFilter filterByArtifactId(final Long artifactId) {
		return filterByArtifactId(artifactId, Operation.AND);
	}

	public SystemMessageFilter filterByArtifactId(final Long artifactId,
			final Operation operation) {
		addFilter("ARTIFACT_ID", artifactId, operation);
		return this;
	}
}
