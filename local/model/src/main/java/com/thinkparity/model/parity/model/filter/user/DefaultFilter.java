/*
 * Created On: Jun 27, 2006 4:37:33 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.filter.user;

import org.apache.log4j.Logger;

import com.thinkparity.model.parity.model.filter.AbstractFilter;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class DefaultFilter extends AbstractFilter<User> {

    /** Create DefaultFilter. */
    public DefaultFilter() { super(); }

    /**
     * @see com.thinkparity.model.parity.model.filter.Filter#debug(org.apache.log4j.Logger)
     */
    public void debug(final Logger logger) {
        logger.debug("[LMODEL] [FILTER] [USER]");
    }

    /**
     * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
     * 
     */
    public Boolean doFilter(final User o) { return Boolean.FALSE; }
}
