/*
 * Created On: Sep 12, 2006 10:44:35 AM
 */
package com.thinkparity.codebase.event;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface EventNotifier<T extends EventListener> {
    public void notifyListener(final T listener);
}
