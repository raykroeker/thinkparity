/*
 * Mar 28, 2006
 */
package com.thinkparity.model.parity.model.filter.container;

import org.apache.log4j.Logger;

import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.filter.AbstractFilter;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DraftDoesNotExist extends AbstractFilter<Container> {

	/** Create DraftExists. */
	public DraftDoesNotExist() { super(); }

	/**
     * @see com.thinkparity.model.parity.model.filter.Filter#debug(org.apache.log4j.Logger)
     * 
     */
    public void debug(final Logger logger) {
        logger.debug("[LMODEL] [FILTER] [CONTAINER] [DRAFT EXISTS]");
    }

    /**
	 * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
	 * 
	 */
	public Boolean doFilter(final Container o) {
		return null != o.getDraft();
	}
}
