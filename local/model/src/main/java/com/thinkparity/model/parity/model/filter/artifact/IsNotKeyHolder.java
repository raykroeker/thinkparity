/*
 * Mar 28, 2006
 */
package com.thinkparity.model.parity.model.filter.artifact;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.filter.AbstractFilter;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IsNotKeyHolder extends AbstractFilter<Artifact> {

	/**
	 * Create a IsNotKeyHolder.
	 * 
	 */
	public IsNotKeyHolder() { super(); }

	/**
	 * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
	 * 
	 */
	public Boolean doFilter(final Artifact o) {
		return o.contains(ArtifactFlag.KEY);
	}
}
