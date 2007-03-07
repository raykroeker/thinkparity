/*
 * Created On:  2-Mar-07 1:16:44 PM
 */
package com.thinkparity.ophelia.model.util;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProcessAdapter implements ProcessMonitor {

    /* (non-Javadoc)
     * @see com.thinkparity.ophelia.model.util.ProcessMonitor#beginProcess()
     */
    public void beginProcess() {
    }

    /* (non-Javadoc)
     * @see com.thinkparity.ophelia.model.util.ProcessMonitor#beginStep(com.thinkparity.ophelia.model.util.Step, java.lang.Object)
     */
    public void beginStep(Step step, Object data) {
    }

    /* (non-Javadoc)
     * @see com.thinkparity.ophelia.model.util.ProcessMonitor#determineSteps(java.lang.Integer)
     */
    public void determineSteps(Integer steps) {
    }

    /* (non-Javadoc)
     * @see com.thinkparity.ophelia.model.util.ProcessMonitor#endProcess()
     */
    public void endProcess() {
    }

    /* (non-Javadoc)
     * @see com.thinkparity.ophelia.model.util.ProcessMonitor#endStep(com.thinkparity.ophelia.model.util.Step)
     */
    public void endStep(Step step) {
    }
}
