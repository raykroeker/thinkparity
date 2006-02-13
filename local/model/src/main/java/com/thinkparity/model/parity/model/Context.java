/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Context {

	/**
	 * The context source.
	 */
	private final Class source;

	/**
	 * Create a Context.
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
		Assert.assertNotNull("Model context contains no source.", source);
		Assert.assertTrue("Model context contains illegal source:  " + "<" + source.getPackage().getName() + ">",
				source.getPackage().getName()
					.startsWith("com.thinkparity.model.parity.model"));
	}
}
