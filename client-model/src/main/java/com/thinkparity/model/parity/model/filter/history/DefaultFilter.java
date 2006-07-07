/*
 * Created On: Jul 6, 2006 3:07:07 PM
 */
package com.thinkparity.model.parity.model.filter.history;

import org.apache.log4j.Logger;

import com.thinkparity.model.parity.model.audit.HistoryItem;
import com.thinkparity.model.parity.model.filter.Filter;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class DefaultFilter implements Filter<HistoryItem> {

    /** Create DefaultFilter. */
    public DefaultFilter() { super(); }

    /**
     * @see com.thinkparity.model.parity.model.filter.Filter#debug(org.apache.log4j.Logger)
     */
    public void debug(Logger logger) {
        logger.debug("[LMODEL] [FILTER] [HISTORY ITEM] [DEFAULT]");
    }

    /**
     * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
     */
    public Boolean doFilter(final HistoryItem o) { return Boolean.FALSE; }
}
