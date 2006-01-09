/*
 * Jan 6, 2006
 */
package com.thinkparity.browser;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserBootstrap {

	static void bootstrap() {
		System.setProperty("smack.debugEnabled", "true");
		System.setProperty("parity.serverhost", "rk-mobile.raykroeker.com");
		System.setProperty("parity.workspace", "C:\\Documents and Settings\\raymond\\My Documents\\thinkparity.com\\alan");

		System.setProperty("parity.projectId", "dd789340-b833-4a5f-a3fc-8d4084ab6245");
	}

	/**
	 * Create a BrowserBootstrap.
	 */
	BrowserBootstrap() { super(); }
}
