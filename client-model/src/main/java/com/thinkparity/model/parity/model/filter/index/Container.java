/*
 * Created On: Jul 4, 2006 6:08:01 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.filter.index;

import org.apache.log4j.Logger;

import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.index.IndexHit;

/**
 * <b>Title:</b>thinkParity Index Hit Container Filter<br>
 * <b>Description:</b>Filters index hits for results of type container.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Container implements Filter<IndexHit> {

    /** Create Document. */
    public Container() { super(); }

    /**
     * @see com.thinkparity.model.parity.model.filter.Filter#debug(org.apache.log4j.Logger)
     */
    public void debug(final Logger logger) {
        logger.debug("[FILTER] [INDEX HIT] [DOCUMENT]");
    }

    /**
     * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
     */
    public Boolean doFilter(final IndexHit o) {
        return ArtifactType.CONTAINER != o.getType();
    }
}
