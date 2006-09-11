/*
 * Created On: Jun 27, 2006 12:32:30 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.util.filter.container;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;

import com.thinkparity.ophelia.model.util.filter.AbstractFilter;

/**
 * <b>Title:</b>thinkParity DefaultFilter Container Filter<br>
 * <b>Description:</b>A default container filter that doesn't filter out
 * anything.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class DefaultVersionFilter extends AbstractFilter<ArtifactVersion> {

    /** Create DefaultFilter. */
    public DefaultVersionFilter() { super(); }

    /**
     * @see com.thinkparity.ophelia.model.util.filter.Filter#debug(org.apache.log4j.Logger)
     * 
     */
    public void debug(final Logger logger) {
        logger.debug("[LMODEL] [FILTER] [CONTAINER VERSION] [DEFAULT]");
    }

    /**
     * @see com.thinkparity.ophelia.model.util.filter.Filter#doFilter(T)
     * 
     */
    public Boolean doFilter(final ArtifactVersion o) { return Boolean.FALSE; }
}
