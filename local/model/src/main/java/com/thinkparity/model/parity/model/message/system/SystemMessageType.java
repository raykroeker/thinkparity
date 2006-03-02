/*
 * Jan 26, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum SystemMessageType {

	INFO(0), CONTACT_INVITATION(1), CONTACT_INVITATION_RESPONSE(2),
	KEY_REQUEST(3), KEY_RESPONSE(4);

	public static SystemMessageType fromId(final Integer id) {
		switch(id) {
		case 0: return INFO;
		case 1: return CONTACT_INVITATION;
		case 2: return CONTACT_INVITATION_RESPONSE;
		case 3: return KEY_REQUEST;
		case 4: return KEY_RESPONSE;
		default: throw Assert.createUnreachable("");
		}
	}

	private final Integer id;

	private SystemMessageType(final Integer id) { this.id = id; }

	public Integer getId() { return id; }
}
