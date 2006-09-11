/*
 * Mar 28, 2006
 */
package com.thinkparity.ophelia.model.util.filter.artifact;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;

import com.thinkparity.ophelia.model.util.filter.AbstractFilter;

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
     * @see com.thinkparity.ophelia.model.util.filter.Filter#debug(org.apache.log4j.Logger)
     * 
     */
    public void debug(final Logger logger) {
        logger.debug("[LMODEL] [FILTER] [ARTIFACT] [NOT CONTAINS FLAG KEY]");
    }

    /**
	 * @see com.thinkparity.ophelia.model.util.filter.Filter#doFilter(T)
	 * 
	 */
	public Boolean doFilter(final Artifact o) {
		return o.contains(ArtifactFlag.KEY);
	}
}
