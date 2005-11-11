/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.xstream.XStreamSerializable;

/**
 * Project
 * @author raykroeker@gmail.com
 * @version 1.3.2.7
 */
public class Project extends ParityObject implements XStreamSerializable {

	/**
	 * Create a Project.
	 * 
	 * @param parentId
	 *            The parent id.
	 * @param name
	 *            The name.
	 * @param createdOn
	 *            The creator.
	 * @param createdBy
	 *            The creation date.
	 * @param description
	 *            The description.
	 * @param id
	 *            The id.
	 */
	public Project(final UUID parentId, final String name,
			final Calendar createdOn, final String createdBy,
			final String description, final UUID id) {
		super(parentId, name, description, createdOn, createdBy, id);
	}

	/**
	 * Create a Project.
	 * 
	 * @param name
	 *            The name.
	 * @param createdOn
	 *            The creator.
	 * @param createdBy
	 *            The creation date.
	 * @param description
	 *            The description.
	 * @param id
	 *            The id.
	 */
	public Project(final String name, final Calendar createdOn,
			final String createdBy, final String description, final UUID id) {
		this(null, name, createdOn, createdBy, description, id);
	}

	/**
	 * Obtain the type of parity object; in this case a project.
	 * 
	 * @see ParityObject#getType()
	 * @see ParityObjectType#PROJECT
	 */
	public ParityObjectType getType() { return ParityObjectType.PROJECT; }
}
