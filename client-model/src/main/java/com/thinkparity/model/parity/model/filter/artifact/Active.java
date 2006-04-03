/*
 * Mar 28, 2006
 */
package com.thinkparity.model.parity.model.filter.artifact;

import org.apache.log4j.Logger;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.filter.AbstractFilter;

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
     * @see com.thinkparity.model.parity.model.filter.Filter#debug(org.apache.log4j.Logger)
     * 
     */
    public void debug(final Logger logger) {
        logger.debug("[LMODEL] [FILTER] [ARTIFACT] [IS ACTIVE]");
    }

    /**
	 * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
	 * 
	 */
	public Boolean doFilter(final Artifact o) {
		return o.getState() != ArtifactState.ACTIVE;
	}
}
