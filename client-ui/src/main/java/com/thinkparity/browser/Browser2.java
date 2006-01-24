/*
 * Dec 30, 2005
 */
package com.thinkparity.browser;


import com.thinkparity.codebase.PropertiesUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Browser2 {

	static {
		final StringBuffer buffer = new StringBuffer();
		PropertiesUtil.print(
				buffer, "--- Parity Browser2 ---", System.getProperties());
		Initializer.getInstance().initialize();
	}

	/**
	 * Run Browser2
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		try { new Browser2().run(); }
		catch(Exception x) {
			x.printStackTrace(System.err);
			System.exit(1);
		}
	}

	/**
	 * Handle to the parity browser controller.
	 * 
	 */
	private final Controller controller;

	/**
	 * Create a Browser2.
	 * 
	 */
	private Browser2() {
		super();
		this.controller = Controller.getInstance();
	}

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
	private void run() { controller.openMainWindow(); }
}
