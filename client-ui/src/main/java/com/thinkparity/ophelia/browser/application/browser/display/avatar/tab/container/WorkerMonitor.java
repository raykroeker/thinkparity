/*
 * Created On:  17-Nov-06 2:11:16 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class WorkerMonitor implements ThinkParitySwingMonitor {

    private final Container container;

    private final WorkerDisplay display;

    private String note = "";

    private int step = 0;

    /**
     * Create WorkerMonitor.
     *
     */
    WorkerMonitor(final WorkerDisplay display, final Container container) {
        super();
        this.container = container;
        this.display = display;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#monitor()
     *
     */
    public void monitor() {
        // start the monitoring process
        display.installProgressBar(container.getId());
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
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#setSteps(int)
     *
     */
    public void setSteps(int steps) {
        display.setDetermination(container.getId(), steps);
    }

    /**
     * Update the progress bar.
     *
     */
    private void updateProgressBar() {
        display.updateProgress(container.getId(), step, note);
    }
}
