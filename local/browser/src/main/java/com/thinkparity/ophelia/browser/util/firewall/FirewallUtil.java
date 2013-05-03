/*
 * Created On:  Apr 18, 2007 8:20:25 PM
 */
package com.thinkparity.ophelia.browser.util.firewall;

import java.io.File;

/**
 * <b>Title:</b>thinkParity OpheliaUI Firewall Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface FirewallUtil {

    /**
     * Add an executable to the firewall.
     * 
     * @param executable
     *            An executable <code>File</code>.
     */
    public void addExecutable(final File executable)
            throws FirewallAccessException;

    /**
     * Determine whether or not the firewall has been enabled.
     * 
     * @return True if the firewall has been enabled.
     */
    public Boolean isEnabled();

    /**
     * Remove an executable from the firewall.
     * 
     * @param executable
     *            An executable <code>File</code>.
     */
    public void removeExecutable(final File executable);
}
