/*
 * Created On: Dec 30, 2005
 * $Id$
 */
package com.thinkparity.browser;

import com.thinkparity.browser.platform.BrowserPlatform;

/**
 * The browser entry point.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Browser {

	/**
	 * Run Browser
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) { BrowserPlatform.start(args); }

	/**
	 * Create a Browser.
	 * 
	 */
	private Browser() { super(); }
}
