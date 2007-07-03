/*
 * Created On:  12-Jun-07 10:29:11 AM
 */
package com.thinkparity.adriana.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1f
 */
public final class AdrianaWebListener implements ServletContextListener {

    /**
     * Create AdrianaWebListener.
     *
     */
    public AdrianaWebListener() {
        super();
    }

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     *
     */
    public void contextDestroyed(final ServletContextEvent servletContext) {
    }

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     *
     */
    public void contextInitialized(final ServletContextEvent servletContext) {
    }
}
