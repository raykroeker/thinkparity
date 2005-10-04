/*
 * Apr 25, 2005
 */
package com.thinkparity.model.parity.model.note;

import com.thinkparity.codebase.log4j.Loggable;

/**
 * Note
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Note implements Loggable {

	/**
	 * Content.
	 */
	private String content;

	/**
	 * Subject.
	 */
	private String subject;

	/**
	 * Create a new Note.
	 */
	public Note() { this(null, null); }

	/**
	 * Create a new Note.
	 * 
	 * @param subject
	 *            The subject of the new note.
	 * @param content
	 *            The content of the new note.
	 */
	public Note(final String subject, final String content) {
		super();
		this.subject = subject;
		this.content = content;
	}

	/**
	 * Obtain the value of content.
	 * @return <code>String</code>
	 */
	public String getContent() { return content; }

	/**
	 * Obtain the value of subject.
	 * @return <code>String</code>
	 */
	public String getSubject() { return subject; }

	/**
	 * @see com.thinkparity.codebase.log4j.Loggable#logMe()
	 */
	public StringBuffer logMe() {
		return new StringBuffer("<note subject=\"")
			.append(getSubject()).append("\">")
			.append(getContent())
			.append("</note>");
	}

	/**
	 * Set a value for content.
	 * @param content <code>String</code>
	 */
	public void setContent(String content) { this.content = content; }

	/**
	 * Set a value for subject.
	 * @param subject <code>String</code>
	 */
	public void setSubject(String subject) { this.subject = subject; }
}
