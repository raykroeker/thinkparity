/*
 * Created On:  25-Dec-06 10:25:46 AM
 */
package com.thinkparity.ophelia.browser.platform.application.display.avatar;


/**
 * <b>Title:</b>thinkParity Avatar Event Dispatcher<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface EventDispatcher<T extends Avatar> {

    /**
     * Add all model listeners for an avatar.
     * 
     * @param avatar
     *            An <code>Avatar</code>.
     */
    public void addListeners(final T avatar);

    /**
     * Remove all model listeners for an avatar.
     * 
     * @param avatar
     *            An <code>Avatar</code>.
     */
    public void removeListeners(final T avatar);
}
