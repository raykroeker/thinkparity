/*
 * Feb 13, 2006
 */
package com.thinkparity.codebase.model;

import com.thinkparity.codebase.assertion.Assert;

/**
 * A thinkParity model context.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public final class Context {

	/** A thinkParity model implementation. */
	private final Object context;

	/**
     * Create Context.
     * 
     * @param The
     *            context source.
     */
	Context(final AbstractModel context) {
		super();
		this.context = context;
	}

	/**
	 * Ensure the context is valid.
	 *
	 */
	public final void assertIsValid() {
		Assert.assertNotNull(context, "Null context.");
		Assert.assertTrue(isValid(),
                "Illegal context.", context.getClass().getPackage().getName());
	}

    /**
     * Determine if the context is valid.
     * 
     * @return True if the context is valid.
     */
    private final Boolean isValid() {
        return context.getClass().getPackage().getName().startsWith("com.thinkparity.ophelia.model") ||
            context.getClass().getPackage().getName().startsWith("com.thinkparity.desdemona.model");
    }
}
