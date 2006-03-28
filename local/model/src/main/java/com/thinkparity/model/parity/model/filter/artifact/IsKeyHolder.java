/*
 * Mar 28, 2006
 */
package com.thinkparity.model.parity.model.filter.artifact;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.filter.Filter;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IsKeyHolder implements Filter<Artifact> {

	/**
	 * Create a IsKeyHolder.
	 * 
	 */
	public IsKeyHolder() { super(); }

	/**
	 * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
	 * 
	 */
	public Boolean doFilter(final Artifact o) {
		return !o.contains(ArtifactFlag.KEY);
	}
}
