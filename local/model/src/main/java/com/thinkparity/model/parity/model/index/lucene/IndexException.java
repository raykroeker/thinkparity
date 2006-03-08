/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index.lucene;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IndexException extends RuntimeException {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a IndexException.
	 * 
	 * @param message
	 *            The error message.
	 * @param iox
	 *            The error.
	 */
	IndexException(final String message, final IOException iox) {
		super(message, iox);
	}

	/**
	 * Create a IndexException.
	 * 
	 * @param message
	 *            The error message.
	 * @param px
	 *            The error.
	 */
	IndexException(final String message, final ParseException px) {
		super(message, px);
	}
}
