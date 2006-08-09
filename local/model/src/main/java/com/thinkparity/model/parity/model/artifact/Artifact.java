/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.*;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.note.Note;

/**
 * The parity object is the quintessential parity model object. It represents
 * the main object stored in the parity model's meta data store. Additional
 * objects are stored in the meta data; however they are all tied to a parity
 * object.
 * 
 * The parity object is uniquely identifiable; as well as "shareable" across the
 * xmpp network.
 * 
 * @author raykroeker@gmail.com
 * @version 1.5.2.11
 */
public abstract class Artifact {

	/**
	 * The creator of the object.
	 * 
	 */
	private String createdBy;

	/**
	 * The creation date of the object.
	 */
	private Calendar createdOn;

	/**
	 * The custom properties of the parity object.
	 */
	private Properties customProperties;

	/**
	 * The description of object.
	 */
	private String description;

	/**
	 * The current state of the object.
	 */
	private final Collection<ArtifactFlag> flags;

	/**
	 * The local artifact id.
	 * 
	 */
	private Long id;

	/**
	 * The name of the object.
	 */
	private String name;

	/**
	 * List of notes.
	 */
	private Collection<Note> notes;

	/**
	 * The artifact's remote info.
	 * 
	 */
	private ArtifactRemoteInfo remoteInfo;

	/**
	 * The artifact state.
	 * 
	 */
	private ArtifactState state;

	/**
	 * Unique id of the object.
	 */
	private UUID uniqueId;

	/**
	 * Updator of the object.
	 */
	private String updatedBy;

	/**
	 * Update date of the object.
	 */
	private Calendar updatedOn;

	/**
	 * Create an Artifact.
	 * 
	 */
	protected Artifact() {
		super();
		this.flags = new Vector<ArtifactFlag>(0);
	}

	/**
	 * Create a Artifact.
	 * 
	 * @param createdBy
	 *            The object creator.
	 * @param createdOn
	 *            The object creation date.
	 * @param description
	 *            The object description.
	 * @param flags
	 *            The flags to apply to this parity object.
	 * @param id
	 *            The object unique id.
	 * @param name
	 *            The object name.
	 * @param parentId
	 *            The object parent id.
	 * @param updatedBy
	 *            The object updator.
	 * @param updatedOn
	 *            The object update date.
	 */
	protected Artifact(final String createdBy, final Calendar createdOn,
			final String description, final Collection<ArtifactFlag> flags,
			final UUID uniqueId, final String name, final UUID parentId,
			final String updatedBy, final Calendar updatedOn) {
		super();
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.customProperties = new Properties(createDefaultCustomProperties(name, description));
		this.description = description;
		this.uniqueId = uniqueId;
		this.name = name;
		this.notes = new Vector<Note>(7);
		this.flags = new Vector<ArtifactFlag>(flags.size());
		add(flags);
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
	}

	/**
	 * Add a flag to this parity object.
	 * 
	 * @param flag
	 *            The flag to add to the object.
	 */
	public void add(final ArtifactFlag flag) {
		Assert.assertNotTrue("add(ArtifactFlag)", flags.contains(flag));
		flags.add(flag);
	}

	/**
	 * Add a list of flags to the object.
	 * 
	 * @param flags
	 *            The list of flags to add.
	 */
	public void add(final Collection<ArtifactFlag> flags) {
		for(ArtifactFlag flag : flags) { add(flag); }
	}

	/**
	 * Add a note to the object.
	 * 
	 * @param note
	 *            The note to add.
	 */
	public void add(final Note note) {
		Assert.assertNotTrue(
				"Cannot add the same note more than once.",
				notes.contains(note));
		notes.add(note);
	}

	/**
	 * Determine whether or not the object has been flagged with flag.
	 * 
	 * @param flag
	 *            The target flag.
	 * @return True if the object has been flagged with flag; false otherwise.
	 */
	public Boolean contains(final ArtifactFlag flag) {
		return flags.contains(flag);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(null != obj && obj instanceof Artifact) {
			return id.equals(((Artifact) obj).id);
		}
		return false;
	}

	/**
	 * Get the object creator.
	 * 
	 * @return The object creator.
	 */
	public String getCreatedBy() { return createdBy; }

	/**
	 * Get the object creation date.
	 * 
	 * @return The object creation date.
	 */
	public Calendar getCreatedOn() { return createdOn; }

	/**
	 * Obtain the description for the parity object.
	 * 
	 * @return The object description.
	 */
	public String getDescription() { return description; }

	/**
	 * Obtain the current object state.
	 * 
	 * @return The current object state.
	 */
	public Collection<ArtifactFlag> getFlags() {
		return Collections.unmodifiableCollection(flags);
	}

	/**
	 * Obtain the artifact id.
	 * 
	 * @return The artifact id.
	 */
	public Long getId() { return id; }

	/**
	 * Obtain the name of the object.
	 * 
	 * @return The name of the object.
	 */
	public String getName() { return name; }

	/**
	 * Obtain the notes for the object.
	 * 
	 * @return The notes for the object.
	 */
	public Collection<Note> getNotes() {
		return Collections.unmodifiableCollection(notes);
	}

	/**
     * Obtain the artifact's remote info.
     * 
     * @return The remote info.
     */
	public ArtifactRemoteInfo getRemoteInfo() { return remoteInfo; }

	/**
	 * Obtain the artifact state.
	 * 
	 * @return The artifact state.
	 */
	public ArtifactState getState() { return state; }

	/**
	 * Obtain the type of object.
	 * 
	 * @return The type of object.
	 */
	public abstract ArtifactType getType();

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public UUID getUniqueId() { return uniqueId; }

	/**
	 * Obtain the update date.
	 * 
	 * @return The updated date.
	 */
	public String getUpdatedBy() { return updatedBy; }

	/**
	 * Obtain the updator.
	 * 
	 * @return The updator.
	 */
	public Calendar getUpdatedOn() { return updatedOn;}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() { return uniqueId.hashCode(); }

	/**
	 * Remove a flag from the object.
	 * 
	 * @param flag
	 *            The flag to remove.
	 */
	public void remove(final ArtifactFlag flag) {
		Assert.assertTrue("remove(ArtifactFlag)", flags.contains(flag));
		flags.remove(flag);
	}

	/**
	 * Remove a note from the object.
	 * 
	 * @param note
	 *            The note to remove.
	 */
	public void remove(final Note note) {
		if(notes.contains(note))
			notes.remove(note);
	}

	/**
	 * Set the creator.
	 * 
	 * @param createdBy
	 *            The creator.
	 */
	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Set the creation date.
	 * 
	 * @param createdOn
	 *            The creation date.
	 */
	public void setCreatedOn(final Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Set the custom description for the parity object. The description is set
	 * on a per-user basis.
	 * 
	 * @param description
	 *            The custom description.
	 */
	public void setCustomDescription(final String description) {
		customProperties.setProperty("description", description);
	}
	
	/**
	 * Set the custom name for the parity object. The name is set on a per-user
	 * basis.
	 * 
	 * @param name
	 *            The custom name.
	 */
	public void setCustomName(final String name) {
		customProperties.setProperty("name", name);
	}
	/**
	 * Set a named custom property. The custom property is set on a per-user
	 * basis.
	 * 
	 * @param customPropertyName
	 *            The custom property name.
	 * @param customPropertyValue
	 *            The custom property value.
	 */
	public void setCustomProperty(final String customPropertyName,
			final String customPropertyValue) {
		customProperties.setProperty(customPropertyName, customPropertyValue);
	}

	/**
	 * Set the artifact flags.
	 * 
	 * @param flags
	 *            The artifact flags.
	 */
	public void setFlags(final List<ArtifactFlag> flags) {
		this.flags.clear();
		this.flags.addAll(flags);
	}

	/**
	 * Set the artifact id.
	 * 
	 * @param id
	 *            The artifact id.
	 */
	public void setId(final Long id) { this.id = id; }

	/**
	 * Set the artifact name.
	 * 
	 * @param name
	 *            The name.
	 */
	public void setName(final String name) { this.name = name; }

	/**
     * Set the artifact's remote info.
     * 
     * @param remoteInfo
     *            The remote info.
     */
	public void setRemoteInfo(final ArtifactRemoteInfo remoteInfo) {
		this.remoteInfo = remoteInfo;
	}

	/**
	 * Set the artifact state.
	 * 
	 * @param state
	 *            The artifact state.
	 */
	public void setState(final ArtifactState state) { this.state = state; }

	/**
	 * Set the artifact type.
	 * 
	 * @param type
	 *            The artifact type.
	 */
	public void setType(final ArtifactType type) {}

	/**
	 * Set the artifact unique id.
	 * 
	 * @param uniqueId
	 *            The artifact unique id.
	 */
	public void setUniqueId(final UUID uniqueId) { this.uniqueId = uniqueId; }

	/**
	 * Set the updator.
	 * 
	 * @param updatedBy
	 *            The new updator.
	 */
	public void setUpdatedBy(final String updatedBy) {
		this.updatedBy = updatedBy;
	}

    /**
	 * Set the update date.
	 * 
	 * @param updatedOn
	 *            The new update date.
	 */
	public void setUpdatedOn(final Calendar updatedOn) {
		this.updatedOn = updatedOn;
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
}
