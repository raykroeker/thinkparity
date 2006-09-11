/*
 * Created On:  Wed May 03 13:02:07 PDT 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.audit.event;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * The audit event created when a document is published.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PublishEvent extends AuditVersionEvent {

	/** Create PublishEvent. */
	public PublishEvent() { super(AuditEventType.PUBLISH); }
}
