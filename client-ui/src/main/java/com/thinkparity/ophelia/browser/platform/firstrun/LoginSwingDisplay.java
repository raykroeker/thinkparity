/**
 * Created On: Mar 5, 2007 4:56:07 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface LoginSwingDisplay {

    /**
     * Dispose of the display.
     */
    void dispose();

    /**
     * Install a progress bar.
     */
    void installProgressBar();

    /**
     * Reset the progress bar.
     */
    void resetProgressBar();

    /**
     * Set the number of steps to complete.
     * 
     * @param steps
     *            The number of steps to complete.
     */
    void setDetermination(final Integer steps);

    /**
     * Set the credentials valid flag.
     * 
     * @param validCredentials
     *            The credentials valid <code>Boolean</code>.
     */
    void setValidCredentials(final Boolean validCredentials);

    /**
     * Update the progress.
     * 
     * @param step
     *            The step <code>Integer</code>.
     * @param note
     *            A custom message <code>String</code>.
     */
    void updateProgress(final Integer step, final String note);
}
