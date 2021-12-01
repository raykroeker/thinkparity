/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public enum AuditEventType {

	ADD_TEAM_MEMBER(11),
    ADD_TEAM_MEMBER_CONFIRM(13),
    ARCHIVE(0),
    CLOSE(1),
    CREATE(2),
    CREATE_REMOTE(14),
    KEY_REQUEST_DENIED(8),
    KEY_RESPONSE_DENIED(9),
    PUBLISH(12),
    REACTIVATE(16),
    RECEIVE(3),
    RECEIVE_KEY(4),
    RENAME(15),
    REQUEST_KEY(5),
    SEND(6),
    SEND_CONFIRM(10),
    SEND_KEY(7);

	public static AuditEventType fromId(final Integer id) {
		switch(id) {
        case 11: return ADD_TEAM_MEMBER;
        case 13: return ADD_TEAM_MEMBER_CONFIRM;
		case  0: return ARCHIVE;
		case  1: return CLOSE;
		case  2: return CREATE;
        case 14: return CREATE_REMOTE;
		case  8: return KEY_REQUEST_DENIED;
		case  9: return KEY_RESPONSE_DENIED;
        case 12: return PUBLISH;
        case 16: return REACTIVATE;
		case  3: return RECEIVE;
		case  4: return RECEIVE_KEY;
        case 15: return RENAME;
		case  5: return REQUEST_KEY;
		case  6: return SEND;
		case 10: return SEND_CONFIRM;
		case  7: return SEND_KEY;
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
