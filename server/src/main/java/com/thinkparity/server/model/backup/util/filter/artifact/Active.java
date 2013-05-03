/*
 * Mar 28, 2006
 */
package com.thinkparity.ophelia.model.util.filter.artifact;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactState;

import com.thinkparity.ophelia.model.util.filter.AbstractFilter;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Active extends AbstractFilter<Artifact> {

	/**
	 * Create a Active.
	 * 
	 */
	public Active() { super(); }

    /**
     * @see com.thinkparity.ophelia.model.util.filter.Filter#logVariable(String, org.apache.log4j.Logger)
     * 
     */
    public void debug(final Logger logger) {
        logger.debug("[LMODEL] [FILTER] [ARTIFACT] [IS ACTIVE]");
    }

    /**
	 * @see com.thinkparity.ophelia.model.util.filter.Filter#doFilter(T)
	 * 
	 */
	public Boolean doFilter(final Artifact o) {
		return o.getState() != ArtifactState.ACTIVE;
	}
}
