/*
 * Created On:  7-Nov-06 8:37:31 AM
 */
package com.thinkparity.ophelia.model.archive.monitor;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface OpenMonitor {
    public void determine(final Integer stages);
    public void processBegin();
    public void processEnd();
    public void stageBegin(final OpenStage stage, final Object data);
    public void stageEnd(final OpenStage stage);
}
