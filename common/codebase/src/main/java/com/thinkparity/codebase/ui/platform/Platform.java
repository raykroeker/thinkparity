/*
 * Mar 16, 2006
 */
package com.thinkparity.codebase.ui.platform;

import com.thinkparity.codebase.ApplicationId;
import com.thinkparity.codebase.ui.application.ApplicationListener;
import com.thinkparity.codebase.ui.platform.event.LifeCycleListener;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Platform extends ApplicationListener<Platform> {

    /**
     * Add a platform life cycle listener.
     * 
     * @param listener
     *            A <code>LifeCycleListener</code>.
     * @return Whether or not the listener list was modified.
     */
    public boolean addListener(final LifeCycleListener listener);

    /**
     * End the platform.
     *
     */
    public void end();

    /**
	 * Request that the application hibernate.
	 * 
	 * @param applicationId
	 *            The application id.
	 */
	public void hibernate(final ApplicationId applicationId);

	/**
     * Add a platform life cycle listener.
     * 
     * @param listener
     *            A <code>LifeCycleListener</code>.
     * @return Whether or not the listener list was modified.
     */
    public boolean removeListener(final LifeCycleListener listener);

    /**
     * Restart the platform.
     * 
     */
    public void restart();

    /**
     * Restore an application from hibernation.
     * 
     * @param applicationId
     *            An <code>ApplicationId</code>.
     */
	public void restore(final ApplicationId applicationId);

    /**
     * Start the platform.
     *
     */
    public void start();
}
