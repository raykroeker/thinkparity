/*
 * Created On: Mar 28, 2006
 */
package com.thinkparity.codebase.filter;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public interface Filter<T> {
    public Boolean doFilter(final T o);
}
