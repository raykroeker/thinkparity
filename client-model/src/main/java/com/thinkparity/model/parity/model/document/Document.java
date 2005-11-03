/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.parity.model.project.Project;

/**
 * Document
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Document extends ParityObject {

	/**
	 * Create a Document.
	 * 
	 * @param name
	 *            The document name.
	 * @param createdOn
	 *            The document creation date.
	 * @param createdBy
	 *            The document creator.
	 * @param description
	 *            The document description.
	 * @param id
	 *            The document id.
	 */
	public Document(final String name, final Calendar createdOn,
			final String createdBy, final String description, final UUID id) {
		this(null, name, createdOn,createdBy, description, id);
	}

	/**
	 * Create a Document.
	 * 
	 * @param parent
	 *            The parent project.
	 * @param name
	 *            The document name.
	 * @param createdOn
	 *            The document creation date.
	 * @param createdBy
	 *            The document creator.
	 * @param description
	 *            The document description.
	 * @param id
	 *            The document id.
	 */
	Document(final Project parent, final String name, final Calendar createdOn,
			final String createdBy, final String description, final UUID id) {
		super(parent, name, description, createdOn, createdBy, id);
	}

	/**
	 * Obtain the path of the document.
	 * 
	 * @return The path of the document.
	 * @see com.thinkparity.model.parity.api.ParityObject#getPath()
	 */
	public StringBuffer getPath() {
		return getParent().getPath().append("/").append(getCustomName());
	}

	/**
	 * Obtain the type of parity object.
	 * 
	 * @return The type of parity object.
	 * @see ParityObject#getType()
	 * @see ParityObjectType#DOCUMENT
	 */
	public ParityObjectType getType() { return ParityObjectType.DOCUMENT; }
}
