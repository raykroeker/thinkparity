/**
 * Created On: Dec 26, 2006 12:17:16 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.application.Application;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface ActionInvocation {

    /**
     * Invoke an application action.
     * 
     * @param application
     *            An <code>Application</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    public void invokeAction(final Application application, final Data data);

    /**
     * Invoke a platform action.
     * 
     * @param platform
     *            The <code>Platform</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    public void invokeAction(final Platform platform, final Data data);

    /**
     * Retry invoking the action.
     *
     */
    public void retryInvokeAction();
}
