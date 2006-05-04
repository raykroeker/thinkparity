/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;

/**
 * This event is recorded when a document is created.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateEvent extends AuditEvent {

	/** Create a CreateEvent. */
	public CreateEvent() { super(AuditEventType.CREATE); }
}
