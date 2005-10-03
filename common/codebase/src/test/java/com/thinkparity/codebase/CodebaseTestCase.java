/*
 * Nov 5, 2003
 */
package com.thinkparity.codebase;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * <b>Title:</b>  CodebaseTestCase
 * <br><b>Description:</b>  
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class CodebaseTestCase extends TestCase {

	/**
	 * Test logger.
	 */
	protected final Logger logger = Logger.getRootLogger();

	/**
	 * Create a new CodebaseTestCase
	 */
	public CodebaseTestCase() {super();}

	/**
	 * Create a new CodebaseTestCase
	 * @param arg0
	 */
	public CodebaseTestCase(String arg0) {super(arg0);}
}
