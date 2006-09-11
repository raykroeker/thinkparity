/*
 * Created On: Fri Jun 02 2006 11:16 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.audit.event;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * This event is recorded when a document is renamed.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class RenameEvent extends AuditEvent {

    /** The previous name. */
    private String from;

    /** The current name. */
    private String to;

	/** Create RenameEvent. */
	public RenameEvent() { super(AuditEventType.RENAME); }

    public String getFrom() { return from; }

    public String getTo() { return to; }

    public void setFrom(final String from) { this.from = from; }

    public void setTo(final String to) { this.to = to; }
}
