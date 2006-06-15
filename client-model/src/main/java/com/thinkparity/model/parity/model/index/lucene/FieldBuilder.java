/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index.lucene;

import java.util.Calendar;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Field;

import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FieldBuilder {

	/**
	 * The field index.
	 * 
	 */
	private Field.Index index;

	/**
	 * The field name.
	 * 
	 */
	private String name;

	/**
	 * The field store.
	 * 
	 */
	private Field.Store store;

	/**
	 * The field term vector.
	 * 
	 */
	private Field.TermVector termVector;

	/**
	 * The field value.
	 * 
	 */
	private String value;

	/**
	 * Create a FieldBuilder.
	 * 
	 */
	public FieldBuilder() { super(); }

	/**
	 * Set the index.
	 * 
	 * @param index
	 *            The index.
	 * @return A reference to this object.
	 */
	public FieldBuilder setIndex(final Field.Index index) {
		this.index = index;
		return this;
	}

	/**
	 * Set the field name.
	 * 
	 * @param name
	 *            The field name.
	 * @return A reference to this object.
	 */
	public FieldBuilder setName(final String name) {
		this.name = name;
		return this;
	}

	/**
	 * Set the store.
	 * 
	 * @param store
	 *            The store.
	 * @return A reference to this object.
	 */
	public FieldBuilder setStore(final Field.Store store) {
		this.store = store;
		return this;
	}

	/**
	 * Set the term vector.
	 * 
	 * @param termVector
	 *            The term vector.
	 * @return A reference to this object.
	 */
	public FieldBuilder setTermVector(final Field.TermVector termVector) {
		this.termVector = termVector;
		return this;
	}

	/**
	 * Set the field value.
	 * 
	 * @param c
	 *            The field value.
	 * @param r
	 *            The field value resolution.
	 * @return A reference to this object.
	 */
	public FieldBuilder setValue(final Calendar c, final DateTools.Resolution r) {
		this.value = DateTools.dateToString(c.getTime(), r);
		return this;
	}

	/**
	 * Set the field value.
	 * 
	 * @param users
	 *            The field value.
	 * @return A reference to this object.
	 */
	public FieldBuilder setValue(final Set<User> users) {
		final StringBuffer buffer = new StringBuffer();
		for(final User user : users) {
			if(0 < buffer.length()) { buffer.append(Separator.SemiColon); }
			buffer.append(tokenize(user));
		}
		this.value = buffer.toString();
		return this;
	}

	/**
	 * Set the field value.
	 * 
	 * @param l
	 *            The field value.
	 * @return A reference to this object.
	 */
	public FieldBuilder setValue(final Long l) {
		this.value = l.toString();
		return this;
	}

	/**
	 * Set the field value.
	 * 
	 * @param s
	 *            The field value.
	 * @return A reference to this object.
	 */
	public FieldBuilder setValue(final String s) {
		this.value = s;
		return this;
	}

	/**
     * Set the field value.
     * 
     * @param b
     *            The field value.
     * @return A reference to this object.
     */
	public FieldBuilder setValue(final StringBuffer b) {
		this.value = b.toString();
		return this;
	}

	/**
	 * Set the field value.
	 * 
	 * @param u
	 *            The field value.
	 * @return A reference to this object.
	 */
	public FieldBuilder setValue(final User user) {
		this.value = tokenize(user);
		return this;
	}

	/**
	 * Obtain the lucene field representation.
	 * 
	 * @return The lucene field.
	 */
	public Field toField() {
		return new Field(name, value, store, index, termVector);
	}

	/**
     * Obtain a lucene field representation for search only. Note the value will
     * be an empty string.
     * 
     * @return The lucene field.
     */
	public Field toSearchField() {
		return new Field(name, "", store, index, termVector);
	}

	/**
	 * Tokenize a user's name\organization.
	 * 
	 * @param user
	 *            The user.
	 * @return A comma separated list.
	 */
	private String tokenize(final User user) {
        final StringBuffer buffer = new StringBuffer();
        final StringTokenizer nameTokenizer = new StringTokenizer(user.getName(), " ");
        while(nameTokenizer.hasMoreTokens()) {
            if(0 < buffer.length()) { buffer.append(Separator.Comma); }
            buffer.append(nameTokenizer.nextToken());
        }
		if(user.isSetOrganization()) {
			buffer.append(Separator.Comma).append(user.getOrganization());
		}
		return buffer.toString();
	}
}
