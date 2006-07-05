/*
 * Created On: Jun 27, 2006 12:32:30 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.filter.artifact;

import org.apache.log4j.Logger;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.filter.AbstractFilter;

/**
 * <b>Title:</b>thinkParity Default Artifact Filter<br>
 * <b>Description:</b>A default container filter that doesn't filter out
 * anything.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class DefaultFilter extends AbstractFilter<Artifact> {

    /** Create DefaultFilter. */
    public DefaultFilter() { super(); }

    /**
     * @see com.thinkparity.model.parity.model.filter.Filter#debug(org.apache.log4j.Logger)
     * 
     */
    public void debug(final Logger logger) {
        logger.debug("[LMODEL] [FILTER] [ARTIFACT] [DEFAULT]");
    }

    /**
     * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
     * 
     */
    public Boolean doFilter(final Artifact o) { return Boolean.FALSE; }
}
