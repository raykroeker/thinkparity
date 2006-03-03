/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum AuditEventType {

	CLOSE(0), CREATE(1), RECEIVE(2), RECEIVE_KEY(3), REQUEST_KEY(4), SEND(5), SEND_KEY(6);

	public static AuditEventType fromId(final Integer id) {
		switch(id) {
		case 0: return CLOSE;
		case 1: return CREATE;
		case 2: return RECEIVE;
		case 3: return RECEIVE_KEY;
		case 4: return REQUEST_KEY;
		case 5: return SEND;
		case 6: return SEND_KEY;
		default:
			throw Assert.createUnreachable("Could not determine audit type:  " + id);
		}
	}

	private final Integer id;

	private AuditEventType(final Integer id) {
		this.id = id;
	}

	public Integer getId() { return id; }
}
