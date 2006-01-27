/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.model.tmp.system.message;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Message {

	private String body;

	private String header;

	private MessageId id;

	private Sender sender;

	/**
	 * Create a Message.
	 */
	public Message() {
		super();
		this.id = new MessageId();
	}

	/**
	 * @return Returns the body.
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @return Returns the header.
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @return Returns the id.
	 */
	public MessageId getId() {
		return id;
	}

	/**
	 * @return Returns the sender.
	 */
	public Sender getSender() {
		return sender;
	}

	/**
	 * @param body The body to set.
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @param header The header to set.
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(MessageId id) {
		this.id = id;
	}

	/**
	 * @param sender The sender to set.
	 */
	public void setSender(Sender sender) {
		this.sender = sender;
	}
}
