/**
 * Created On: 28-Aug-07 3:23:16 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.backup;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface RestoreBackupSwingDisplay {

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
