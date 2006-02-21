/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum ArtifactAuditType {

	CLOSE(0), CREATE(1), RECEIVE(2), RECEIVE_KEY(3), SEND(4), SEND_KEY(5);

	public static ArtifactAuditType fromId(final Integer id) {
		switch(id) {
		case 0: return CLOSE;
		case 1: return CREATE;
		case 2: return RECEIVE;
		case 3: return RECEIVE_KEY;
		case 4: return SEND;
		case 5: return SEND_KEY;
		default:
			throw Assert.createUnreachable("Could not determine audit type:  " + id);
		}
	}

	private final Integer id;

	private ArtifactAuditType(final Integer id) {
		this.id = id;
	}

	public Integer getId() { return id; }
}
