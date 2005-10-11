/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity.api;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Loggable;
import com.thinkparity.model.parity.IParityConstants;
import com.thinkparity.model.parity.model.note.Note;
import com.thinkparity.model.parity.model.project.Project;

/**
 * ParityObject
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ParityObject implements IParityConstants, Loggable {

	/**
	 * The parity username of the person who created the parity object.
	 */
	private String createdBy;

	/**
	 * The date\time of creation of the parity object.
	 */
	private Calendar createdOn;

	/**
	 * The custom properties of the parity object.
	 */
	private Properties customProperties;

	/**
	 * The description of the parity object.
	 */
	private String description;

	/**
	 * Universally unique id for the object.
	 */
	private UUID id;

	/**
	 * The parity username of the person in possetion of the key for this
	 * parity object.
	 */
	private String keyHolder;

	/**
	 * The name of the parity object.
	 */
	private String name;

	/**
	 * List of notes associated with the parity object.
	 */
	private Collection<Note> notes;

	/**
	 * Reference to the parent parity object.
	 */
	private Project parent;

	/**
	 * List of versions associated with this document.
	 */
	private Collection<ParityObjectVersion> versions;

	/**
	 * Create a ParityObject
	 */
	protected ParityObject(final Project parent, final String name, final String description,
			final Calendar createdOn, final String createdBy,
			final String keyHolder, final UUID id) {
		super();
		this.parent = parent;
		this.name = name;
		this.description = description;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.keyHolder = keyHolder;
		this.notes = new Vector<Note>(7);
		this.customProperties = new Properties(createDefaultCustomProperties(name, description));
		this.versions = new Vector<ParityObjectVersion>(10);
		this.id = id;
	}

	/**
	 * Create a default instance of the custom properties.
	 * 
	 * @return The default custom properties.
	 */
	private Properties createDefaultCustomProperties(final String name,
			final String description) {
		if(null == name) { throw new NullPointerException(); }
		final Properties customProperties = new Properties();
		customProperties.setProperty("name", name);
		if(null != description)
			customProperties.setProperty("description", description);
		return customProperties;
	}

	/**
	 * Add a new revision to this parity object.
	 * @param version <code>ParityObjectVersion</code>
	 */
	protected final void add(final ParityObjectVersion version) {
		Assert.assertTrue("Cannot add the same version more than once.",
				!versions.contains(version));
		versions.add(version);
	}

	protected void setParent(final Project parent) { this.parent = parent; }

	public final void add(final Note note) {
		Assert.assertTrue("Cannot add the same note more than once.", !notes
				.contains(note));
		notes.add(note);
	}

	public final String getCreatedBy() { return createdBy; }

	public final Calendar getCreatedOn() { return createdOn; }

	/**
	 * Obtain the custom description for the parity object.  The custom
	 * description is set on a per-user basis.
	 * @return <code>java.lang.String</code>
	 */
	public final String getCustomDescription() {
		return getCustomProperty("description");
	}

	/**
	 * Obtain the custom name for the parity object.  The custom name is set on
	 * a per-user basis.
	 * @return <code>java.lang.String</code>
	 */
	public final String getCustomName() { return getCustomProperty("name"); }

	/**
	 * Obtain all of the custom properties for the parity object.  Custom
	 * properties are set on a per-user basis.
	 * @return <code>java.util.Properties</code>
	 */
	public final Properties getCustomProperties() { return customProperties; }

	/**
	 * Obtain a named custom property for the parity object.  The custom
	 * property is set on a per-user basis.
	 * @param customPropertyName <code>java.lang.String</code>
	 * @return <code>java.lang.String</code>
	 */
	public final String getCustomProperty(final String customPropertyName) {
		return customProperties.getProperty(customPropertyName);
	}

	/**
	 * Obtain a named custom property for the parity object, and if the property
	 * is not set, return the default value.  The custom property is set on a
	 * per-user basis.
	 * @param customPropertyName <code>java.lang.String</code>
	 * @param customPropertyDefaultValue <code>java.lang.String</code>
	 * @return <code>java.lang.String</code>
	 */
	public final String getCustomProperty(final String customPropertyName,
			final String customPropertyDefaultValue) {
		return customProperties.getProperty(customPropertyName,
				customPropertyDefaultValue);
	}

	/**
	 * Obtain the description for the parity object.
	 * @return <code>java.lang.String</code>
	 */
	public final String getDescription() { return description; }

	public abstract File getDirectory();

	/**
	 * Obtain the value of id.
	 * @return <code>UUID</code>
	 */
	public UUID getId() { return id; }

	/**
	 * Obtain keyHolder.
	 * @return <code>String</code>
	 */
	public String getKeyHolder() { return keyHolder; }

	/**
	 * Obtain the File identifing the directory within which the meta-data for
	 * the parity object resides.
	 * @return <code>java.io.File</code>
	 */
	public final File getMetaDataDirectory() {
		final File directory = getDirectory();
		return new File(directory, META_DATA_DIRECTORY_NAME);
	}

	/**
	 * Obatin the File identifying the meta-data file for the parity object.
	 * @return <code>java.io.File</code>
	 */
	public final File getMetaDataFile() {
		final File metaDataDirectory = getMetaDataDirectory();
		final String metaDataFileName = new StringBuffer(getName())
				.append(".").append(getClass().getSimpleName().toLowerCase())
				.toString();
		return new File(metaDataDirectory, metaDataFileName);
	}

	/**
	 * Obtain the name for the parity object.
	 * @return <code>java.lang.String</code>
	 */
	public final String getName() { return name; }

	public final Collection<Note> getNotes() { return notes; }

	/**
	 * Obtain parent.
	 * @return Project
	 */
	public final Project getParent() { return parent; }

	/**
	 * Obtain the path for the object.
	 * @return StringBuffer
	 */
	public abstract StringBuffer getPath();

	/**
	 * Obtain the type of parity object.
	 * 
	 * @return <code>com.thinkparity.model.parity.api.ParityObjectType</code>
	 */
	public abstract ParityObjectType getType();

	/**
	 * Obtain the versions for this parity object.
	 * 
	 * @return <code>java.util.Collection&lt;com.thinkparity.model.parity.api.ParityObjectVersion&gt;</code>
	 */
	public final Collection<ParityObjectVersion> getVersions() {
		return versions;
	}

	/**
	 * Determine whether or not the parent is set.
	 * @return Boolean</code>
	 */
	public final Boolean isSetParent() { return null != parent; }

	public final void remove(final Note note) {
		if(notes.contains(note))
			notes.remove(note);
	}

	/**
	 * Set the custom description for the parity object.  The description is set
	 * on a per-user basis.
	 * @param description <code>java.lang.String</code>
	 */
	public final void setCustomDescription(final String description) {
		customProperties.setProperty("description", description);
	}

	/**
	 * Set the custom name for the parity object.  The name is set on a per-user
	 * basis.
	 * @param name <code>java.lang.String</code>
	 */
	public final void setCustomName(final String name) {
		customProperties.setProperty("name", name);
	}

	/**
	 * Set a named custom property.  The custom property is set on a per-user
	 * basis.
	 * @param customPropertyName <code>java.lang.String</code>
	 * @param customPropertyValue <code>java.lang.String</code>
	 */
	public final void setCustomProperty(final String customPropertyName,
			final String customPropertyValue) {
		customProperties.setProperty(customPropertyName, customPropertyValue);
	}
}
