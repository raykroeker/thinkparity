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

	ARCHIVE(0), CLOSE(1), CONFIRM_RECEIPT(10), CREATE(2), RECEIVE(3),
    RECEIVE_KEY(4), REQUEST_KEY(5), SEND(6), SEND_KEY(7), KEY_REQUEST_DENIED(8),
    KEY_RESPONSE_DENIED(9);

	public static AuditEventType fromId(final Integer id) {
		switch(id) {
		case 0: return ARCHIVE;
		case 1: return CLOSE;
        case 10: return CONFIRM_RECEIPT;
		case 2: return CREATE;
		case 3: return RECEIVE;
		case 4: return RECEIVE_KEY;
		case 5: return REQUEST_KEY;
		case 6: return SEND;
		case 7: return SEND_KEY;
		case 8: return KEY_REQUEST_DENIED;
		case 9: return KEY_RESPONSE_DENIED;
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
