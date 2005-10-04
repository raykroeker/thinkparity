/*
 * Oct 3, 2005
 */
package com.thinkparity.codebase.log4j.or;

import org.apache.log4j.or.ObjectRenderer;

/**
 * AbstractObjectRenderer
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public abstract class AbstractObjectRenderer implements ObjectRenderer {

	protected final String NULL_RENDERING = "null";

	/**
	 * Create a new AbstractObjectRenderer.
	 */
	protected AbstractObjectRenderer() { super(); }
}
