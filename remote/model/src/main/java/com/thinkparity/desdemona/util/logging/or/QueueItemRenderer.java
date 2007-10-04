/*
 * Dec 6, 2005
 */
package com.thinkparity.desdemona.util.logging.or;

import java.util.Calendar;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.log4j.or.CalendarRenderer;

import com.thinkparity.desdemona.model.queue.QueueItem;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b>thinkParity Desdemona Model Queue Item Log4J Renderer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class QueueItemRenderer implements ObjectRenderer {

    /** A calendar renderer. */
    private final ObjectRenderer calendarRenderer;

	/**
	 * Create QueueItemRenderer.
	 * 
	 */
	public QueueItemRenderer() {
	    super();
	    this.calendarRenderer = new CalendarRenderer();
	}

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 * 
	 */
	public String doRender(final Object o) {
        if (null == o) {
            return Separator.Null.toString();
        }
        else {
            final QueueItem o2 = (QueueItem) o;
            return StringUtil.toString(o2.getClass(),
                    "getCreatedOn()", doRender(o2.getCreatedOn()),
                    "getQueueId()", o2.getQueueId(),
                    "getQueueMesssage(250)", doRender(o2.getQueueMessage(), 250),
                    "getQueueMessageSize()", o2.getQueueMessageSize(),
                    "getUpdatedOn()", doRender(o2.getUpdatedOn()),
                    "getUsername", o2.getUsername());
        }
	}

    /**
     * Render a calendar.
     * 
     * @param o
     *            A <code>Calendar</code>.
     * @return A <code>String</code>.
     */
	private String doRender(final Calendar o) {
	    return calendarRenderer.doRender(o);
	}

	/**
     * Render a string to a maximum length.
     * 
     * @param o
     *            A <code>String</code>.
     * @param maxLength
     *            An <code>Integer</code>.
     * @return A <code>String</code>.
     */
	private String doRender(final String o, final int maxLength) {
	    if (null == o) {
	        return Separator.Null.toString();
	    } else {
	        final String o2 = (String) o;
	        return maxLength > o2.length() ? o2 : o2.substring(0, maxLength - 1);
	    }
    }
}
