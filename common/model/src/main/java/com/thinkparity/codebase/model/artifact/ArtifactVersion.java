/*
 * Created On: Apr 16, 2005
 */
package com.thinkparity.codebase.model.artifact;

import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * <b>Title:</b>thinkParity Artifact Version<br>
 * <b>Description:</b>An artifact version represents an <code>Artifact</code>
 * in its entirety; as well as the revision information including an id; a
 * timestamp and some custom meta data.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
 * @see Artifact
 */
public abstract class ArtifactVersion {

    /** A comment applied to a version. */
	private String comment;

	/** The created by user id <code>JabberId</code>. */
	private JabberId createdBy;

	/** The creation date <code>Calendar</code>. */
	private Calendar createdOn;

	/** The id <code>Long</code>. */
	private Long id;

	/** The meta data <code>Properties</code>. */
	private final Properties metaData;

	/** The name <code>String</code>. */
	private String name;

	/** The type <code>ArtifactType</code>. */
	private ArtifactType type;

	/** The unique id <code>UUID</code>. */
	private UUID uniqueId;

	/** The updated by user id <code>JabberId</code>. */
	private JabberId updatedBy;

	/** The update date <code>Calendar</code>. */
	private Calendar updatedOn;

	/** The version id <code>Long</code>. */
	private Long versionId;

	/**
	 * Create ArtifactVersion.
	 * 
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
	public Long getArtifactId() {
        return id;
	}

	/**
	 * Obtain the artifact type.
	 * 
	 * @return The artifactType.
	 */
	public ArtifactType getArtifactType() {
        return type;
	}

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public UUID getArtifactUniqueId() {
        return uniqueId;
	}

	/**
     * Obtain the comment applient to the version.
     * 
     * @return A version comment <code>String</code>.
     */
    public String getComment() {
        return comment;
    }

	/**
	 * Obtain the aritfact creator.
	 * 
	 * @return The aritfact creator.
	 */
	public JabberId getCreatedBy() {
        return createdBy;
    }

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
	public JabberId getUpdatedBy() {
        return updatedBy;
	}

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
	public Long getVersionId() {
        return versionId;
	}

    /**
     * Determine whether or not the comment is set for the version.
     * 
     * @return True if the comment is set.
     */
	public Boolean isSetComment() {
        return null != comment;
    }

	/**
	 * Set the artifact id.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	public void setArtifactId(final Long id) {
		this.id = id;
	}

    /**
	 * Set the artifact type.
	 * @param artifactType The artifact type.
	 */
	public void setArtifactType(final ArtifactType type) {
		this.type = type;
	}

	/**
     * Set the unique id.
     * 
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
	public void setArtifactUniqueId(final UUID uniqueId) {
		this.uniqueId = uniqueId;
	}

    /**
     * Set the comment for the artifact version.
     * 
     * @param comment
     *            A comment <code>String</code>.
     */
	public void setComment(final String comment) {
        this.comment = comment;
    }

	/**
	 * Obtain the aritfact creator.
	 * 
	 * @param createdBy
	 *            The aritfact creator.
	 */
	public void setCreatedBy(final JabberId createdBy) {
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
	public void setUpdatedBy(final JabberId updatedBy) {
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
