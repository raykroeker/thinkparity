/*
 * Created On: Mar 3, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;

/**
 * This event is recorded when an artifact is archived.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArchiveEvent extends AuditEvent {

	/**
	 * Create a ArchiveEvent.
	 */
	public ArchiveEvent() { super(AuditEventType.ARCHIVE); }
}
