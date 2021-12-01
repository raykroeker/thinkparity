/*
 * Mar 3, 2006
 */
package com.thinkparity.ophelia.model.util;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class ProgressIndicator {

	private static ProgressIndicator emptyIndicator;

	static { emptyIndicator = new ProgressIndicator() {}; }

	public static ProgressIndicator emptyIndicator() { return emptyIndicator; }

	/**
	 * Create a ProgressIndicator.
	 * 
	 */
	public ProgressIndicator() { super(); }
}
