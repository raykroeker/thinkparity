/*
 * Created On:  17-Nov-06 2:11:16 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;

/**
 * <b>Title:</b>thinkParity Ophelia UI Publish Swing Monitor<br>
 * <b>Description:</b>A bridge pattern between the publish action and the
 * publish progress display.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PublishContainerSwingMonitor implements ThinkParitySwingMonitor {

    /** A container id. */
    private final Long containerId;

    /** The progress display. */
    private final PublishContainerSwingDisplay display;

    /** The progress note. */
    private String note;

    /** The current step. */
    private int step = 0;

    /**
     * Create PublishContainerSwingMonitor.
     * 
     * @param display
     *            The <code>PublishContainerSwingDisplay</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     */
    PublishContainerSwingMonitor(final PublishContainerSwingDisplay display,
            final Long containerId) {
        super();
        this.containerId = containerId;
        this.display = display;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#monitor()
     *
     */
    public void monitor() {
        // start the monitoring process display
        display.installProgressBar(containerId);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#reset()
     *
     */
    public void reset() {
        // reset the monitor and update the display
        this.step = 0;
        this.note = "";
        display.resetProgressBar(containerId);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#setStep(int)
     *
     */
    public void setStep(final int step) {
        // set the progress and update the display
        this.step = step;
        updateProgressBar();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#setStep(int, java.lang.String)
     *
     */
    public void setStep(final int step, final String note) {
        // set the progress and update the display
        this.step = step;
        this.note = note;
        updateProgressBar();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#complete()
     *
     */
    public void complete() {
        // finish the progress display
        display.dispose();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#setSteps(int)
     *
     */
    public void setSteps(final int steps) {
        // set the scope of the progress display
        display.setDetermination(containerId, steps);
    }

    /**
     * Update the progress bar.
     *
     */
    private void updateProgressBar() {
        // refresh the progress display
        display.updateProgress(containerId, step, note);
    }
}
