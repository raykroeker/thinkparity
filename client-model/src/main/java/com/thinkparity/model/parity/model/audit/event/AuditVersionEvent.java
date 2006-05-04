/*
 * Created On: Wed May 03 2006 13:33 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;

/**
 * An abstraction of an event with a version.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see AuditEvent
 */
public abstract class AuditVersionEvent extends AuditEvent {

    /** The artifact version id. */
    protected Long artifactVersionId;

	/**
     * Create AuditVersionEvent.
     *
     * @param type
     *      The event type.
     */
	protected AuditVersionEvent(final AuditEventType type) { super(type); }

	/**
     * Obtain the version id.
     *
	 * @return The artifact version id.
	 */
	public Long getArtifactVersionId() { return artifactVersionId; }

	/**
     * Set the artifact version id.
     *
	 * @param artifactVersionId
     *      The artifact version id.
	 */
	public void setArtifactVersionId(final Long artifactVersionId) {
		this.artifactVersionId = artifactVersionId;
	}
}
