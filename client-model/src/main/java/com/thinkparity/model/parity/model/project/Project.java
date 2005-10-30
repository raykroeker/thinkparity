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
 * @version 1.3
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
	 * @see com.thinkparity.model.parity.api.ParityObject#getPath()
	 */
	@Override
	public StringBuffer getPath() {
		if(isSetParent()) {
			return new StringBuffer(getParent().getPath())
				.append("/")
				.append(getCustomName());
		}
		else { return new StringBuffer(getCustomName()); }
	}

	/**
	 * @see com.thinkparity.model.parity.api.ParityObject#getType()
	 */
	public ParityObjectType getType() { return ParityObjectType.PROJECT; }
}
