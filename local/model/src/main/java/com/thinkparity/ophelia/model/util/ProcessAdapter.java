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

    /**
     * @see com.thinkparity.ophelia.model.util.ProcessMonitor#beginProcess()
     *
     */
    public void beginProcess() {}

    /**
     * @see com.thinkparity.ophelia.model.util.ProcessMonitor#beginStep(com.thinkparity.ophelia.model.util.Step, java.lang.Object)
     *
     */
    public void beginStep(final Step step, final Object data) {}

    /**
     * @see com.thinkparity.ophelia.model.util.ProcessMonitor#determineSteps(java.lang.Integer)
     *
     */
    public void determineSteps(final Integer steps) {}

    /**
     * @see com.thinkparity.ophelia.model.util.ProcessMonitor#endProcess()
     *
     */
    public void endProcess() {}

    /**
     * @see com.thinkparity.ophelia.model.util.ProcessMonitor#endStep(com.thinkparity.ophelia.model.util.Step)
     *
     */
    public void endStep(final Step step) {}
}
