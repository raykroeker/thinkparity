/*
 * Created On: Sep 20, 2006 10:59:53 AM
 */
package com.thinkparity.ophelia.browser.platform.event;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class LifeCycleAdapter implements LifeCycleListener {

    /** Create LifeCycleAdapter. */
    public LifeCycleAdapter() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.event.LifeCycleListener#started(com.thinkparity.ophelia.browser.platform.event.LifeCycleEvent)
     */
    public void started(final LifeCycleEvent e) {}

    /**
     * @see com.thinkparity.ophelia.browser.platform.event.LifeCycleListener#starting(com.thinkparity.ophelia.browser.platform.event.LifeCycleEvent)
     */
    public void starting(final LifeCycleEvent e) {}

    /**
     * @see com.thinkparity.ophelia.browser.platform.event.LifeCycleListener#ended(com.thinkparity.ophelia.browser.platform.event.LifeCycleEvent)
     */
    public void ended(final LifeCycleEvent e) {}

    /**
     * @see com.thinkparity.ophelia.browser.platform.event.LifeCycleListener#ending(com.thinkparity.ophelia.browser.platform.event.LifeCycleEvent)
     */
    public void ending(final LifeCycleEvent e) {}
}
