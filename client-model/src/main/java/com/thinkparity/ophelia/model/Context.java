/*
 * Feb 13, 2006
 */
package com.thinkparity.ophelia.model;

import java.text.MessageFormat;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Context {

	/** The context source. */
	private final Class source;

	/**
     * Create Context.
     * 
     * @param The
     *            context source.
     */
	Context(final Class source) {
		super();
		this.source = source;
	}

	/**
	 * Ensure the context is valid.
	 *
	 */
	public void assertContextIsValid() {
		Assert.assertNotNull("CONTEXT SOURCE IS NULL", source);
		Assert.assertTrue(MessageFormat.format("ILLEGAL CONTEXT SOURCE:  {0}",
                source.getPackage().getName()),
                source.getPackage().getName().startsWith("com.thinkparity.ophelia.model"));
	}
}
