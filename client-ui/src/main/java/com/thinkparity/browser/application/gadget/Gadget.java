/*
 * Feb 11, 2006
 */
package com.thinkparity.browser.application.gadget;

import java.awt.Point;

import org.apache.log4j.Logger;

import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Gadget implements Application {

	/**
	 * The singleton instance.
	 * 
	 */
	private static Gadget singleton;

	/**
	 * Obtain the gadget instance.
	 * 
	 * @param The
	 *            browser platform.
	 * @return The gadget instance.
	 */
	public static Application createInstance(final Platform platform) {
		singleton = new Gadget(platform);
		return singleton;
	}

	/**
	 * Obtain the gadget instance.
	 * @return The gadget instance.
	 */
	static Gadget getInstance() { return singleton; }

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * The main browser platform.
	 * 
	 */
	private final Platform platform;

	/**
	 * The gadget window.
	 * 
	 */
	private GadgetWindow window;

	/**
	 * Create a Gadget [Singleton]
	 * 
	 */
	private Gadget(final Platform platform) {
		super();
		this.logger = LoggerFactory.getLogger(getClass());
		this.platform = platform;
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#end()
	 * 
	 */
	public void end() {}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#getId()
	 * 
	 */
	public ApplicationId getId() { return ApplicationId.GADGET; }

	/**
	 * @see com.thinkparity.browser.platform.application.Application#hibernate()
	 * 
	 */
	public void hibernate() {}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#launch()
	 * 
	 */
	public void launch() { openWindow(); }

	/**
	 * @see com.thinkparity.browser.platform.Saveable#restore(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void restore(final State state) {}

	/**
	 * @see com.thinkparity.browser.platform.Saveable#save(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void save(final State state) {}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#start()
	 * 
	 */
	public void start() { launch(); }

	/**
	 * Move the main window.
	 * 
	 * @param relativeLocation
	 *            The new relative location of the window.
	 */
	void moveWindow(final Point relativeLocation) {
		logger.info("moveWindow(Point)");
		logger.debug(relativeLocation);
		final Point l = window.getLocation();
		l.x += relativeLocation.x;
		l.y += relativeLocation.y;
		logger.debug(l);
		window.setLocation(l);
	}

	/**
	 * Open the main browser.
	 *
	 */
	void runOpenBrowser() {
		platform.launchApplication(ApplicationId.BROWSER);
	}

	/**
	 * Open the gadget ui.
	 *
	 */
	private void openWindow() {
		Assert.assertIsNull("The window is already open.", window);
		window = GadgetWindow.open();
	}
}
