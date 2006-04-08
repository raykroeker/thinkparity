/*
 * Apr 16, 2005
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

/**
 * ArtifactVersion
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public abstract class ArtifactVersion {

	/**
	 * The artifact id.
	 * 
	 */
	private Long artifactId;

	/**
	 * The artifact type.
	 * 
	 */
	private ArtifactType artifactType;

	/**
	 * The artifact unique id.
	 * 
	 */
	private UUID artifactUniqueId;

	/**
	 * The artifact creator.
	 * 
	 */
	private String createdBy;

	/**
	 * The artifact creation date.
	 * 
	 */
	private Calendar createdOn;

	/**
	 * The artifact version meta data.
	 * 
	 */
	private final Properties metaData;

	/**
	 * The artifact name.
	 * 
	 */
	private String name;

	/**
	 * The artifact updator.
	 * 
	 */
	private String updatedBy;

	/**
	 * The artifact version update date.
	 * 
	 */
	private Calendar updatedOn;

	/**
	 * The artifact version id.
	 * 
	 */
	private Long versionId;

	/**
	 * Create a ArtifactVersion
	 * 
	 * @param artifactId
	 *            The artifact unique id.
	 * @param artifactType
	 *            The artifact type.
	 */
	protected ArtifactVersion() {
		super();
		this.metaData = new Properties();
	}

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public Long getArtifactId() { return artifactId; }

	/**
	 * Obtain the artifact type.
	 * 
	 * @return The artifactType.
	 */
	public ArtifactType getArtifactType() { return artifactType; }

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public UUID getArtifactUniqueId() { return artifactUniqueId; }

	/**
	 * Obtain the aritfact creator.
	 * 
	 * @return The aritfact creator.
	 */
	public String getCreatedBy() { return createdBy; }

	/**
	 * Obtain the artifact creation date.
	 * 
	 * @return The aritfact creation date.
	 */
	public Calendar getCreatedOn() { return createdOn; }

	/**
	 * Obtain the artifact version meta data.
	 * 
	 * @return The artifact version meta data.
	 */
	public Properties getMetaData() { return metaData; }
	
	/**
	 * Obtain a meta data item.
	 * 
	 * @param key
	 *            The key.
	 * @return The meta data item value.
	 */
	public String getMetaDataItem(final String key) {
		return metaData.getProperty(key);
	}

	/**
	 * Obtain a meta data item.
	 * 
	 * @param key
	 *            The key.
	 * @param defaultValue
	 *            The default value.
	 * @return The meta data item value.
	 */
	public String getMetaDataItem(final String key, final String defaultValue) {
		return metaData.getProperty(key, defaultValue);
	}

	/**
	 * Obtain the artifact name.
	 * @return The artifact name.
	 */
	public String getName() { return name; }

	/**
	 * Obtain the artifact updator.
	 * 
	 * @return The artifact updator.
	 */
	public String getUpdatedBy() { return updatedBy; }

	/**
	 * Obtain the artifact update date.
	 * 
	 * @return The artifact update date.
	 */
	public Calendar getUpdatedOn() { return updatedOn; }

	/**
	 * Obtain the version id.
	 * 
	 * @return The version id.
	 */
	public Long getVersionId() { return versionId; }

    /**
	 * Set the artifact id.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	public void setArtifactId(final Long artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * Set the artifact type.
	 * @param artifactType The artifact type.
	 */
	public void setArtifactType(final ArtifactType artifactType) {
		this.artifactType = artifactType;
	}

	/**
	 * Set the artifact unique id.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 */
	public void setArtifactUniqueId(final UUID artifactUniqueId) {
		this.artifactUniqueId = artifactUniqueId;
	}

	/**
	 * Obtain the aritfact creator.
	 * 
	 * @param createdBy
	 *            The aritfact creator.
	 */
	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Set the aritfact creation date.
	 * 
	 * @param createdOn
	 *            The aritfact creation date.
	 */
	public void setCreatedOn(final Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Set the artifact version meta data.
	 * 
	 * @param metaData
	 *            The artifact version meta data.
	 */
	public void setMetaData(final Properties metaData) {
		this.metaData.clear();
		this.metaData.putAll(metaData);
	}

	/**
	 * Add a meta data key\value pair.
	 * 
	 * @param key
	 *            The meta data key.
	 * @param value
	 *            The meta data value.
	 * @return The meta data original value.
	 */
	public String setMetaDataItem(final String key, final String value) {
		return (String) metaData.setProperty(key, value);
	}

	/**
	 * Set the artifact name.
	 * 
	 * @param name
	 *            The artifact name.
	 */
	public void setName(final String name) { this.name = name; }

	/**
	 * Set the artifact updator.
	 * 
	 * @param updatedBy
	 *            The artifact updator.
	 */
	public void setUpdatedBy(final String updatedBy) {
		this.updatedBy = updatedBy;
	}

    /**
	 * Set the update date.
	 * 
	 * @param updatedOn
	 *            The updated date.
	 */
	public void setUpdatedOn(final Calendar updatedOn) {
		this.updatedOn = updatedOn;
	}

    /**
	 * Set the version id.
	 * 
	 * @param versionId
	 *            The version id.
	 */
	public void setVersionId(final Long versionId) {
		this.versionId = versionId;
	}
}
