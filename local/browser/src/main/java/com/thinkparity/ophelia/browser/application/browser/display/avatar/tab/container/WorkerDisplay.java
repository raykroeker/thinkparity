/*
 * Created On:  17-Nov-06 2:14:05 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
interface WorkerDisplay {

    /**
     * Install a progress bar.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    void installProgressBar(final Long containerId, final Integer steps);

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
