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
	 * Create a Project
	 */
	public Project(final Project parent, final String name,
			final Calendar createdOn, final String createdBy,
			final String description, final UUID id) {
		super(parent, name, description, createdOn, createdBy, id);
	}

	/**
	 * Create a Project
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
