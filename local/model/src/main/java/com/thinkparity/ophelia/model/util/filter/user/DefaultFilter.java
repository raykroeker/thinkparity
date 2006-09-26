/*
 * Created On: Jun 27, 2006 4:37:33 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.util.filter.user;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.util.filter.AbstractFilter;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class DefaultFilter extends AbstractFilter<User> {

    /** Create DefaultFilter. */
    public DefaultFilter() { super(); }

    /**
     * @see com.thinkparity.ophelia.model.util.filter.Filter#logVariable(String, org.apache.log4j.Logger)
     */
    public void debug(final Logger logger) {
        logger.debug("[LMODEL] [FILTER] [USER]");
    }

    /**
     * @see com.thinkparity.ophelia.model.util.filter.Filter#doFilter(T)
     * 
     */
    public Boolean doFilter(final User o) { return Boolean.FALSE; }
}
