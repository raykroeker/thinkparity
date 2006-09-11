/*
 * Created On: Jul 6, 2006 9:38:59 PM
 */
package com.thinkparity.ophelia.model.util.logging;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.ophelia.model.container.ContainerHistoryItem;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class ContainerHistoryItemRenderer implements ObjectRenderer {

    /** Create DocumentHistoryItemRenderer. */
    public ContainerHistoryItemRenderer() { super(); }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     */
    public String doRender(final Object o) {
        if(null == o ) {
            return new StringBuffer(getClass().getName())
                    .append("//null")
                    .toString();
        }
        else {
            final ContainerHistoryItem item = (ContainerHistoryItem) o;
            final StringBuffer buffer = new StringBuffer(getClass().getName())
                .append("//id=").append(item.getId())
                .append(",containerId=").append(item.getContainerId());
            if(item.isSetVersionId())
                buffer.append(",versionId=").append(item.getVersionId());
            return buffer.append(",date=").append(item.getDate().getTimeInMillis())
                    .append(",event=").append(item.getEvent())
                    .toString();
        }
    }

}
