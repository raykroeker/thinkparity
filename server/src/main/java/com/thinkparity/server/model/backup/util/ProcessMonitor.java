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
public interface ProcessMonitor {

    /**
     * Notify the beginning of the process.
     * 
     */
    public void beginProcess();

    /**
     * Signify the beginning of a stage.
     * 
     * @param stage
     *            A <code>Step</code>.
     */
    public void beginStep(final Step step, final Object data);

    /**
     * Notify the determination of the process.
     * 
     * @param stages
     *            An <code>Integer</code> indicating the number of stages.
     */
    public void determineSteps(final Integer steps);

    /**
     * Signify the end of the process.
     * 
     */
    public void endProcess();

    /**
     * Signify the end of a stage.
     * 
     * @param step
     *            A <code>Step</code>.
     */
    public void endStep(final Step step);
}
