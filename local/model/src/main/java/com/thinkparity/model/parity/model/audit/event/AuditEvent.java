/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.audit.event;

import java.util.Calendar;

import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.xmpp.user.User;

/**
 * An abstraction of an audit event.  It contains an artifact id; the creator
 * the creation date; the audit id and the event type.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AuditEvent {

    /** The artifact id. */
    protected Long artifactId;

    /** The creator. */
	protected User createdBy;

    /** The creation date. */
	protected Calendar createdOn;

    /** The audit id. */
	protected Long id;

    /** The event type. */
	protected AuditEventType type;

	/**
     * Create AuditEvent.
     *
     * @param type
     *      The event type.
     */
	protected AuditEvent(final AuditEventType type) {
        super();
        setType(type);
    }

	/**
     * Obtain the artifact id.
     *
	 * @return The artifact id.
	 */
	public Long getArtifactId() { return artifactId; }

	/**
     * Obtain the creator.
     *
	 * @return The createor.
	 */
	public User getCreatedBy() { return createdBy; }

	/**
     * Obtain the creation date.
     *
	 * @return The creation date.
	 */
	public Calendar getCreatedOn() { return createdOn; }

	/**
     * Obtain the event id.
     *
	 * @return The event id.
	 */
	public Long getId() { return id; }

	/**
     * Obtain the event type.
     *
	 * @return The type.
	 */
	public AuditEventType getType() { return type; }

	/**
     * Set the artifact id.
     *
	 * @param artifact
     *      The artifact id.
	 */
	public void setArtifactId(final Long artifactId) {
        this.artifactId = artifactId;
	}

	/**
     * Set the creator.
     *
	 * @param createdBy
     *      The creator.
	 */
	public void setCreatedBy(final User createdBy) {
		this.createdBy = createdBy;
	}

	/**
     * Set the creation date.
     *
	 * @param createdOn
     *      The creation date.
	 */
	public void setCreatedOn(final Calendar createdOn) {
        this.createdOn = createdOn;
	}

    /**
     *
	 * @param id The id to set.
	 */
	public void setId(final Long id) { this.id = id; }

    /**
     * Set the event type.
     *
	 * @param type
     *      The event type.
	 */
	private void setType(final AuditEventType type) { this.type = type; }
}
