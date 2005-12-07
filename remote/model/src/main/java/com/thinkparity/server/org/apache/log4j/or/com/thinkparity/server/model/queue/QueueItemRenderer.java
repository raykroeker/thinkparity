/*
 * Dec 6, 2005
 */
package com.thinkparity.server.org.apache.log4j.or.com.thinkparity.server.model.queue;

import java.util.Calendar;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.server.model.queue.QueueItem;
import com.thinkparity.server.org.apache.log4j.or.IRendererConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QueueItemRenderer implements ObjectRenderer {

	private static final String CREATED_ON = ",createdOn;";

	private static final String MESSAGE = ",message:";

	private static final String MESSAGE_SIZE = ",messageSize:";

	private static final String PREFIX =
		QueueItem.class + IRendererConstants.PREFIX_SUFFIX;

	private static final String UPDATED_ON = ",updatedOn:";

	private static final String USERNAME = ",username:";

	/**
	 * Create a QueueItemRenderer.
	 */
	public QueueItemRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) {
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.NULL)
				.append(IRendererConstants.SUFFIX).toString();
		}
		else {
			final QueueItem qi = (QueueItem) o;
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.ID).append(qi.getQueueId())
				.append(USERNAME).append(qi.getUsername())
				.append(MESSAGE).append(qi.getQueueMessage())
				.append(MESSAGE_SIZE).append(qi.getQueueMessageSize())
				.append(CREATED_ON).append(doRender(qi.getCreatedOn()))
				.append(UPDATED_ON).append(doRender(qi.getUpdatedOn()))
				.append(IRendererConstants.SUFFIX).toString();
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
}
