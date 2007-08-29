/**
 * Created On: 28-Aug-07 3:23:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.backup;

import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class RestoreBackupSwingMonitor implements ThinkParitySwingMonitor {

    private final RestoreBackupSwingDisplay display;

    private String note = "";

    private int step = 0;

    /**
     * Create RestoreBackupSwingMonitor.
     *
     * @param display
     * @param container
     */
    RestoreBackupSwingMonitor(final RestoreBackupSwingDisplay display) {
        super();
        this.display = display;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#monitor()
     *
     */
    public void monitor() {
        // start the monitoring process
        display.installProgressBar();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#reset()
     *
     */
    public void reset() {
        this.step = 0;
        this.note = "";
        display.resetProgressBar();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor#setError(java.lang.String)
     */
    public void setError(final String errorMessageKey) {
        display.setError(errorMessageKey);
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
        display.setDetermination(steps);
    }

    /**
     * Update the progress bar.
     *
     */
    private void updateProgressBar() {
        display.updateProgress(step, note);
    }
}
