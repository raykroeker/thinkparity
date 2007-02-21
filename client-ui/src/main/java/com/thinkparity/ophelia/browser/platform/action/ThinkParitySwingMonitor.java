/*
 * Created On:  8-Nov-06 11:31:46 AM
 */
package com.thinkparity.ophelia.browser.platform.action;

/**
 * <b>Title:</b>thinkParity Swing Worker<br>
 * <b>Description:</b>An abstraction of a long-running swing task for the
 * thinkParity browser.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ThinkParitySwingMonitor {
    public void complete();
    public void monitor();
    public void reset();
    public void setStep(final int step);
    public void setStep(final int step, final String note);
    public void setSteps(final int steps);
}
