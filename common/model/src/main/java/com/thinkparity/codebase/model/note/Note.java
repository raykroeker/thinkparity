/*
 * Apr 25, 2005
 */
package com.thinkparity.codebase.model.note;

/**
 * A parity note. Is a wrapper around a simple text string.
 * 
 * @author raykroeker@gmail.com
 * @version 1.3
 */
public class Note {

	/**
	 * Note.
	 */
	private String note;

	/**
	 * Create a new Note.
	 */
	public Note() { this(null); }

	/**
	 * Create a new Note.
	 * 
	 * @param note
	 *            The note.
	 */
	public Note(final String note) {
		super();
		this.note = note;
	}

	/**
	 * Obtain the note.
	 * 
	 * @return The note.
	 */
	public String getNote() { return note; }

	/**
	 * Set The note.
	 * 
	 * @param note
	 *            The note.
	 */
	public void setNote(final String note) { this.note = note; }
}
