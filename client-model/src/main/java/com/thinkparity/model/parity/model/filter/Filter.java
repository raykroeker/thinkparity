/*
 * Mar 28, 2006
 */
package com.thinkparity.model.parity.model.filter;

import org.apache.log4j.Logger;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Filter<T> {
	public void debug(final Logger logger);
    public Boolean doFilter(final T o);
}
