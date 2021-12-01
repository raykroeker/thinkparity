/*
 * Created On: Mar 28, 2006
 */
package com.thinkparity.ophelia.model.util.filter;

/**
 * TODO Remove from model and reference codebase.
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public interface Filter<T> {
    public Boolean doFilter(final T o);
}
