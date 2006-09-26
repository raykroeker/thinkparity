/*
 * Created On: Jul 6, 2006 3:07:07 PM
 */
package com.thinkparity.ophelia.model.util.filter.history;

import org.apache.log4j.Logger;


import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.util.filter.Filter;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class DefaultFilter implements Filter<HistoryItem> {

    /** Create DefaultFilter. */
    public DefaultFilter() { super(); }

    /**
     * @see com.thinkparity.ophelia.model.util.filter.Filter#logVariable(String, org.apache.log4j.Logger)
     */
    public void debug(Logger logger) {
        logger.debug("[LMODEL] [FILTER] [HISTORY ITEM] [DEFAULT]");
    }

    /**
     * @see com.thinkparity.ophelia.model.util.filter.Filter#doFilter(T)
     */
    public Boolean doFilter(final HistoryItem o) { return Boolean.FALSE; }
}
