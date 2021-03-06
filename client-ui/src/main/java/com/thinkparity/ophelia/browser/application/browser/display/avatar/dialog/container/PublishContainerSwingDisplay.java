/*
 * Created On:  17-Nov-06 2:14:05 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
interface PublishContainerSwingDisplay {

    /**
     * Dispose of the display.
     *
     */
    void dispose();

    /**
     * Install a progress bar.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param message
     *            A custom message <code>String</code>.
     */
    void installProgressBar(final Long containerId, final String message);

    /**
     * Reset the progress bar.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    void resetProgressBar(final Long containerId);

    /**
     * Set the number of steps to complete.
     * 
     * @param steps
     *            The number of steps to complete.
     */
    void setDetermination(final Long containerId, final Integer steps);

    /**
     * Set the error.
     */
    void setError(final String errorMessageKey);

    /**
     * Update the progress for a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param completion
     *            The progress completion <code>Float</code>.
     * @param status
     *            A custom status message <code>String</code>.
     */
    void updateProgress(final Long containerId, final Integer step,
            final String status);
}
