/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity.api;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

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
public abstract class ParityObject {

	/**
	 * The creator of the object.
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
	private Collection<ParityObjectFlag> flags;

	/**
	 * Unique id of the object.
	 */
	private UUID id;

	/**
	 * The name of the object.
	 */
	private String name;

	/**
	 * List of notes.
	 */
	private Collection<Note> notes;

	/**
	 * Object's parent's unique id.
	 */
	private UUID parentId;

	/**
	 * Updator of the object.
	 */
	private String updatedBy;

	/**
	 * Update date of the object.
	 */
	private Calendar updatedOn;

	/**
	 * Create a ParityObject.
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
	protected ParityObject(final String createdBy, final Calendar createdOn,
			final String description, final Collection<ParityObjectFlag> flags,
			final UUID id, final String name, final UUID parentId,
			final String updatedBy, final Calendar updatedOn) {
		super();
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.customProperties = new Properties(createDefaultCustomProperties(name, description));
		this.description = description;
		this.id = id;
		this.name = name;
		this.notes = new Vector<Note>(7);
		this.parentId = parentId;
		this.flags = new Vector<ParityObjectFlag>(flags.size());
		add(flags);
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
	}

	/**
	 * Add a list of flags to the object.
	 * 
	 * @param flags
	 *            The list of flags to add.
	 */
	public void add(final Collection<ParityObjectFlag> flags) {
		for(ParityObjectFlag flag : flags) { add(flag); }
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
	 * Add a flag to this parity object.
	 * 
	 * @param flag
	 *            The flag to add to the object.
	 */
	public void add(final ParityObjectFlag flag) {
		Assert.assertNotTrue("add(ParityObjectFlag)", flags.contains(flag));
		flags.add(flag);
	}

	/**
	 * Determine whether or not the object has been flagged with flag.
	 * 
	 * @param flag
	 *            The target flag.
	 * @return True if the object has been flagged with flag; false otherwise.
	 */
	public Boolean contains(final ParityObjectFlag flag) {
		return flags.contains(flag);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof ParityObject) {
			return id.equals(((ParityObject) obj).getId());
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
	 * Obtain the custom description for the parity object. The custom
	 * description is set on a per-user basis.
	 * 
	 * @return The custom description.
	 */
	public String getCustomDescription() {
		return getCustomProperty("description");
	}

	/**
	 * Obtain the custom name for the parity object. The custom name is set on a
	 * per-user basis.
	 * 
	 * @return The custom name.
	 */
	public String getCustomName() { return getCustomProperty("name"); }

	/**
	 * Obtain all of the custom properties for the parity object. Custom
	 * properties are set on a per-user basis.
	 * 
	 * @return The custom properties.
	 */
	public Properties getCustomProperties() { return customProperties; }

	/**
	 * Obtain a named custom property for the parity object. The custom property
	 * is set on a per-user basis.
	 * 
	 * @param customPropertyName
	 *            The custom property name.
	 * @return The custom property value.
	 */
	public String getCustomProperty(final String customPropertyName) {
		return customProperties.getProperty(customPropertyName);
	}

	/**
	 * Obtain a named custom property for the parity object, and if the property
	 * is not set, return the default value. The custom property is set on a
	 * per-user basis.
	 * 
	 * @param customPropertyName
	 *            The custom property name.
	 * @param customPropertyDefaultValue
	 *            A default value for the custom property.
	 * @return The custom property.
	 */
	public String getCustomProperty(final String customPropertyName,
			final String customPropertyDefaultValue) {
		return customProperties.getProperty(customPropertyName,
				customPropertyDefaultValue);
	}

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
	public Collection<ParityObjectFlag> getFlags() {
		return Collections.unmodifiableCollection(flags);
	}

	/**
	 * Obtain id of the object.
	 * 
	 * @return The object id.
	 */
	public UUID getId() { return id; }

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
	 * Obtain the parent id.
	 * 
	 * @return The parent id.
	 */
	public UUID getParentId() { return parentId; }

	/**
	 * Obtain the type of object.
	 * 
	 * @return The type of object.
	 */
	public abstract ParityObjectType getType();

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
	public int hashCode() { return id.hashCode(); }

	/**
	 * Determine whether or not the parent id is set.
	 * 
	 * @return True if the parent id is set; false otherwise.
	 */
	public final Boolean isSetParentId() { return null != parentId; }

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
	 * Remove a flag from the object.
	 * 
	 * @param flag
	 *            The flag to remove.
	 */
	public void remove(final ParityObjectFlag flag) {
		Assert.assertTrue("remove(ParityObjectFlag)", flags.contains(flag));
		flags.remove(flag);
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
	 * Set the id of the parent.
	 * 
	 * @param parentId
	 *            The parent id.
	 */
	public void setParentId(final UUID parentId) { this.parentId = parentId; }

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
