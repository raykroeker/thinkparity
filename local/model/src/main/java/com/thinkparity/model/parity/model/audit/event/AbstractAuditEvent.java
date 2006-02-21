/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import java.util.Calendar;

import com.thinkparity.model.parity.model.audit.ArtifactAuditType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractAuditEvent {

	protected Long artifactId;

	protected String createdBy;

	protected Calendar createdOn;

	protected Long id;

	protected ArtifactAuditType type;

	/**
	 * Create an AbstractAuditEvent.
	 * 
	 */
	protected AbstractAuditEvent() { super(); }

	/**
	 * @return Returns the artifact.
	 */
	public Long getArtifactId() { return artifactId; }

	/**
	 * @return Returns the createdBy.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return Returns the createdOn.
	 */
	public Calendar getCreatedOn() {
		return createdOn;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return Returns the type.
	 */
	public ArtifactAuditType getType() {
		return type;
	}

	/**
	 * @param artifact The artifact to set.
	 */
	public void setArtifactId(Long artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @param createdOn The createdOn to set.
	 */
	public void setCreatedOn(Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long auditEventId) {
		this.id = auditEventId;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(ArtifactAuditType type) {
		this.type = type;
	}
}
