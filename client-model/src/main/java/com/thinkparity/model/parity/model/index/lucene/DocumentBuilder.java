/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentBuilder {

	/**
	 * List of all fields for a document.
	 * 
	 */
	private Field[] indexFields;

	/**
	 * The length of the index fields.
	 * 
	 */
	private int length;

	/**
	 * Create a DocumentBuilder.
	 * 
	 * @param capacity
	 *            The initial capacity.
	 */
	public DocumentBuilder(final int capacity) {
		super();
		this.indexFields = new Field[capacity];
	}

	/**
	 * Append a field to the document.
	 * 
	 * @param field
	 *            The field.
	 * @return A reference to this object.
	 */
	public DocumentBuilder append(final Field field) {
		final int newLength = length + 1;
		if(newLength > indexFields.length) { expand(newLength); }
		indexFields[length++] = field;
		return this;
	}

	/**
	 * Obtain the document representation of the fields in this builder.
	 * 
	 * @return The lucene document.
	 */
	public Document toDocument() {
		final Document document = new Document();
		for(final Field indexField : indexFields) {
			if(null == indexField) { break; }
			document.add(indexField);
		}
		return document;
	}

	/**
	 * Expand the index fields to ensure a minimum capacity.
	 * 
	 * @param minCapacity
	 *            A minimum capacity.
	 */
	private void expand(final int minCapacity) {
		int newCapacity = (indexFields.length + 1) * 2;
		if(newCapacity < 0) { newCapacity = Integer.MAX_VALUE; }
		else if(minCapacity > newCapacity) { newCapacity = minCapacity; }
		Field[] newIndexFields = new Field[newCapacity];
		System.arraycopy(indexFields, 0, newIndexFields, 0, length);
		indexFields = newIndexFields;
	}
}
