/*
 * Created On: Sep 20, 2006 10:59:45 AM
 */
package com.thinkparity.ophelia.browser.platform.event;

import com.thinkparity.codebase.event.EventListener;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface LifeCycleListener extends EventListener {

    public void ended(final LifeCycleEvent e);

    public void ending(final LifeCycleEvent e);

    public void started(final LifeCycleEvent e);

    public void starting(final LifeCycleEvent e);
}
