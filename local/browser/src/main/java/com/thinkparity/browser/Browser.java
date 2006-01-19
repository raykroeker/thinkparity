/*
 * Dec 30, 2005
 */
package com.thinkparity.browser;

import com.thinkparity.browser.javax.swing.BrowserJFrame;

import com.thinkparity.codebase.PropertiesUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Browser {

	static {
		Initializer.getInstance().initialize();
		final StringBuffer buffer = new StringBuffer();
		PropertiesUtil.print(
				buffer, "--- Parity Browser ---", System.getProperties());
		System.out.println(buffer);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try { new Browser().run(); }
		catch(Exception x) {
			x.printStackTrace(System.err);
			System.exit(1);
		}
	}

	/**
	 * Create a Browser.
	 */
	private Browser() { super(); }

	/**
	 * Exit the browser.
	 * 
	 * @param status
	 *            exit status.
	 */
	public void exit(int status) { System.exit(status); }

	/**
	 * Run the browser.
	 *
	 */
	private void run() { BrowserJFrame.open(this); }
}
