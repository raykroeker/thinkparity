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
     * 
     * @param message
     *            A custom message <code>String</code>.
     */
    void installProgressBar(final String message);

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
     * Set the error.
     * 
     * @param errorMessageKey
     *            An error message key <code>String</code>.
     */
    void setError(final String errorMessageKey);

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
