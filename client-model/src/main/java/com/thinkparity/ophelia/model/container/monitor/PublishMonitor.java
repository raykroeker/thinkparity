/*
 * Created On:  7-Nov-06 8:37:31 AM
 */
package com.thinkparity.ophelia.model.container.monitor;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface PublishMonitor {
    public void determine(final Integer stages);
    public void processBegin();
    public void processEnd();
    public void stageBegin(final PublishStage stage, final Object data);
    public void stageEnd(final PublishStage stage);
}
