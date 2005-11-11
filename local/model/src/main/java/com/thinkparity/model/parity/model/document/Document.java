/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectType;

/**
 * Document
 * @author raykroeker@gmail.com
 * @version 1.2.2.10
 */
public class Document extends ParityObject {

	/**
	 * Create a Document.
	 * 
	 * @param projectId
	 *            The parent project id.
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
	public Document(final UUID projectId, final String name,
			final Calendar createdOn, final String createdBy,
			final String description, final UUID id) {
		super(projectId, name, description, createdOn, createdBy, id);
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
