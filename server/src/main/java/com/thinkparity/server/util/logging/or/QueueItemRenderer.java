/*
 * Dec 6, 2005
 */
package com.thinkparity.desdemona.util.logging.or;

import java.util.Calendar;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.desdemona.model.queue.QueueItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QueueItemRenderer implements ObjectRenderer {

	/**
	 * Create a QueueItemRenderer.
	 */
	public QueueItemRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(final Object o) {
        if(null == o ) {
            return new StringBuffer(o.getClass().getName()).append("//null")
                    .toString();
        }
        else {
            final QueueItem item = (QueueItem) o;
            return new StringBuffer(o.getClass().getName())
                .append("//").append(item.getQueueId())
                .append("?createdOn=").append(doRender(item.getCreatedOn()))
                .append("&message=").append(doRender(item.getQueueMessage(), 250))
                .append("&messageSize=").append(item.getQueueMessageSize())
                .append("&updatedOn=").append(doRender(item.getUpdatedOn()))
                .append("&username=").append(item.getUsername())
                .toString();
        }
	}

    /**
	 * Render a calendar.
	 * 
	 * @param c
	 *            The calendar.
	 * @return Formatted calendar.
	 */
	private String doRender(final Calendar c) {
		if(null == c) { return IRendererConstants.NULL; }
		else { return IRendererConstants.SDF.format(c.getTime()); }
	}

	private String doRender(final String string, final Integer maxLength) {
        if (maxLength < string.length()) {
            return string.substring(0, maxLength - 1);
        } else {
            return string;
        }
    }
}
