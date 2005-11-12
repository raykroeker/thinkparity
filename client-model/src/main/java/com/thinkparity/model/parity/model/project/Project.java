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
	 * @param createdBy
	 *            The creation date.
	 * @param createdOn
	 *            The creator.
	 * @param description
	 *            The description.
	 * @param id
	 *            The unique id.
	 * @param name
	 *            The name.
	 * @param parentId
	 *            The parent id.
	 * @param updatedBy
	 *            The updator.
	 * @param updatedOn
	 *            The update date.
	 */
	public Project(final String createdBy, final Calendar createdOn,
			final String description, final UUID id, final String name,
			final UUID parentId, final String updatedBy,
			final Calendar updatedOn) {
		super(createdBy, createdOn, description, id, name, parentId, updatedBy,
				updatedOn);
	}

	/**
	 * Obtain the type of parity object; in this case a project.
	 * 
	 * @see ParityObject#getType()
	 * @see ParityObjectType#PROJECT
	 */
	public ParityObjectType getType() { return ParityObjectType.PROJECT; }
}
