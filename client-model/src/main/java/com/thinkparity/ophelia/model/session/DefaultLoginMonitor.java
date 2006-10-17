/*
 * Created On: Tue Oct 17, 2006 15:54
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.codebase.model.session.Credentials;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultLoginMonitor implements LoginMonitor {

    /**
     * Create DefaultLoginMonitor.
     */
    public DefaultLoginMonitor() {
        super();
    }

    /**
     * Will not confirm synchronization.
     * 
     * @return <code>FALSE</code>.
     */
    public Boolean confirmSynchronize() {
        return Boolean.FALSE;
    }

    /**
     * Will not do anything.
     * 
     * @param credentials
     *            The invalid <code>Credentials</code>.
     */
    public void notifyInvalidCredentials(final Credentials credentials) {}
}
