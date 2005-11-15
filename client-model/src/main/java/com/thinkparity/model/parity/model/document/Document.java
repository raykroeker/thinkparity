/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.parity.api.ParityObjectType;

/**
 * The document is the parity object that started the revolution. It is used as
 * the anchor for both the document content and the document versions. One
 * document has a single document content reference (or more aptly a single
 * document content has a single document referenc) and many document versions.
 * 
 * The document content is a wrapper around the document's bytes. The document
 * version contains a snapshot of the document at a single point in time.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2.2.12
 * @see DocumentContent
 * @see DocumentVersion
 */
public class Document extends ParityObject {

	/**
	 * Create a Document.
	 * 
	 * @param createdBy
	 *            The creator.
	 * @param createdOn
	 *            The creation date.
	 * @param description
	 *            The description.
	 * @param flags
	 *            The object flags.
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
	public Document(final String createdBy, final Calendar createdOn,
			final String description, final Collection<ParityObjectFlag> flags,
			final UUID id, final String name, final UUID parentId,
			final String updatedBy, final Calendar updatedOn) {
		super(createdBy, createdOn, description, flags, id, name, parentId,
				updatedBy, updatedOn);
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
