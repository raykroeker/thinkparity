/*
 * Dec 1, 2005
 */
package com.thinkparity.server.model.queue;

import java.util.Calendar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QueueItem {

	private final Calendar createdOn;

	private final Integer queueId;

	private final String queueMessage;

	private final Integer queueMessageSize;

	private final Calendar updatedOn;

	private final String username;

	/**
	 * Create a QueueItem.
	 */
	public QueueItem(final Calendar createdOn, final Integer queueId,
			final String queueMessage, final Integer queueMessageSize,
			final Calendar updatedOn, final String username) {
		super();
		this.createdOn = createdOn;
		this.queueId = queueId;
		this.queueMessage = queueMessage;
		this.queueMessageSize = queueMessageSize;
		this.updatedOn = updatedOn;
		this.username = username;
	}

	/**
	 * Obtain the creation date.
	 * 
	 * @return The creation date.
	 */
	public Calendar getCreatedOn() { return createdOn; }

	/**
	 * Obtain the queue id.
	 * 
	 * @return The queue id.
	 */
	public Integer getQueueId() { return queueId; }

	/**
	 * Obtain the message
	 * 
	 * @return Returns the message.
	 */
	public String getQueueMessage() { return queueMessage; }

	/**
	 * Obtain the message size.
	 * 
	 * @return The message size.
	 */
	public Integer getQueueMessageSize() { return queueMessageSize; }

	/**
	 * Obtain the update date.
	 * 
	 * @return The updated date.
	 */
	public Calendar getUpdatedOn() { return updatedOn; }

	/**
	 * Obtain the username.
	 * 
	 * @return The username.
	 */
	public String getUsername() { return username; }
}
