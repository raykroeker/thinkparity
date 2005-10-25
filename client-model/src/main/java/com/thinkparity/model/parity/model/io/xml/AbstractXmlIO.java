/*
 * 25-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import org.apache.log4j.Logger;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * This is an abstraction of the xml input\output routines used by the parity
 * xml input\output classes.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public abstract class AbstractXmlIO {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(getClass());

	/**
	 * Parity workspace to work within.
	 */
	private final Workspace workspace;

	/**
	 * Create a AbstractXmlIO.
	 */
	protected AbstractXmlIO(final Workspace workspace) {
		super();
		this.workspace = workspace;
	}
}
