/*
 * Mar 28, 2006
 */
package com.thinkparity.ophelia.model.util.filter.artifact;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactState;

import com.thinkparity.ophelia.model.util.filter.AbstractFilter;

/**
 * A parity filter that will catch only closed documents.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * @see Artifact#getState()
 * @see ArtifactState#CLOSED
 */
public class Closed extends AbstractFilter<Artifact> {

	/**
	 * Create a Closed.
	 * 
	 */
	public Closed() { super(); }

    /**
     * @see com.thinkparity.ophelia.model.util.filter.Filter#debug(org.apache.log4j.Logger)
     * 
     */
    public void debug(final Logger logger) {
        logger.debug("[LMODEL] [FILTER] [ARTIFACT] [IS CLOSED]");
    }

	/**
	 * @see com.thinkparity.ophelia.model.util.filter.Filter#doFilter(T)
	 */
	public Boolean doFilter(final Artifact o) {
		return o.getState() != ArtifactState.CLOSED;
	}
}
