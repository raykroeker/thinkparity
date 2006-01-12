/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.error;

import org.apache.log4j.Logger;

import com.thinkparity.browser.log4j.BrowserLoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ErrorHandler {

	public enum Type { FATAL, NONFATAL }

	private final Type type;

	private final Throwable throwable;

	/**
	 * Create an ErrorHandler.
	 * 
	 * @param type
	 *            The error type.
	 * @param throwable
	 *            The error.
	 */
	public ErrorHandler(final Type type, final Throwable throwable) {
		super();
		this.type = type;
		this.throwable = throwable;
	}

	/**
	 * Handle the error.
	 *
	 */
	public void handle() {
		switch(type) {
		case FATAL:
			logger.fatal("A fatal error has occured.", throwable);
			break;
		case NONFATAL:
			logger.error("An un-expected error has occured.", throwable);
			break;
		default: Assert.assertUnreachable("");
		}
	}

	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());
}
