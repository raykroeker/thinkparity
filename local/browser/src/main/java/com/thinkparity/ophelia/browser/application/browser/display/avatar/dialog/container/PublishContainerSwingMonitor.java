/*
 * Created On:  17-Nov-06 2:11:16 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PublishContainerSwingMonitor implements ThinkParitySwingMonitor {

    private final Long containerId;

    private final PublishContainerSwingDisplay display;

    private String note;

    private int step = 0;

    /**
     * Create PublishContainerSwingMonitor.
     *
     * @param display
     * @param container
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
        // start the monitoring process
        display.installProgressBar(containerId);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#reset()
     *
     */
    public void reset() {
        this.step = 0;
        this.note = "";
        display.resetProgressBar(containerId);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#setStep(int)
     *
     */
    public void setStep(final int step) {
        this.step = step;
        updateProgressBar();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#setStep(int, java.lang.String)
     *
     */
    public void setStep(int step, String note) {
        this.step = step;
        this.note = note;
        updateProgressBar();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#complete()
     *
     */
    public void complete() {
        display.dispose();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#setSteps(int)
     *
     */
    public void setSteps(int steps) {
        display.setDetermination(containerId, steps);
    }

    /**
     * Update the progress bar.
     *
     */
    private void updateProgressBar() {
        display.updateProgress(containerId, step, note);
    }
}
