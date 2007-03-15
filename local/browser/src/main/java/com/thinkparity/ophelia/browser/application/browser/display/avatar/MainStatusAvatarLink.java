/*
 * Created On:  15-Mar-07 9:26:20 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface MainStatusAvatarLink {

    /**
     * Obtain the link text.
     * 
     * @return A <code>String</code>.
     */
    public String getLinkText();

    /**
     * Obtain the link target.
     * 
     * @return A <code>Runnable</code>.
     */
    public Runnable getTarget();

    /**
     * Obtain the non-link text.
     * 
     * @return A <code>String</code>.
     */
    public String getText();
}
