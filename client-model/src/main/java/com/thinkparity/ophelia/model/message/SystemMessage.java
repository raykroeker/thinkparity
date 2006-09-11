/*
 * Jan 31, 2006
 */
package com.thinkparity.ophelia.model.message;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessage {

	private Long id;

	private SystemMessageType type;

	/**
	 * Create a Message.
	 */
	public SystemMessage() { super(); }

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return Returns the type.
	 */
	public SystemMessageType getType() {
		return type;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(SystemMessageType type) {
		this.type = type;
	}

}
