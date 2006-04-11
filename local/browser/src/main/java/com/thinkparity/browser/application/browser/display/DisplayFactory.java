/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display;

import java.awt.Dimension;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.display.Display;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DisplayFactory {

	private static final DisplayFactory SINGLETON;

	static { SINGLETON = new DisplayFactory(); }

	public static Display create(final DisplayId id) {
		switch(id) {
		case CONTENT:
			return SINGLETON.createContent();
		case INFO:
			return SINGLETON.createInfo();
		case TITLE:
			return SINGLETON.createTitle();
		default: throw Assert.createUnreachable("Unknown display id:  " + id);
		}
	}

	/**
	 * Content display.
	 * 
	 */
	private Display content;

	/**
	 * The width to apply to each display.
	 * 
	 * @see #applySize(Display, Integer)
	 */
	private final Integer displayWidth;

	/**
	 * Info display.
	 * 
	 */
	private Display info;

	/**
	 * Title display.
	 * 
	 */
	private Display title;

	/**
	 * Create a DisplayFactory [Singleton, Factory]
	 * 
	 */
	private DisplayFactory() {
		super();
		this.displayWidth = BrowserWindow.getMainWindowSize().width;
	}

	/**
	 * Apply the preferred, minimum and maximum size to the display.
	 * 
	 * @param display
	 *            The display.
	 * @param height
	 *            The height.
	 */
	private void applySize(final Display display, final Integer height) {
		final Dimension s = new Dimension(displayWidth, height);
		display.setPreferredSize(s);
		display.setMinimumSize(s);
		display.setMaximumSize(s);
	}

	/**
	 * Create the content display.
	 * 
	 * @return The content display.
	 */
	private Display createContent() {
		if(null == content) {
			content = new ContentDisplay();
			// HEIGHT ContentDisplay 465
			applySize(content, 465);
		}
		return content;
	}

	/**
	 * Create the info display.
	 * 
	 * @return The info display.
	 */
	private Display createInfo() {
		if(null == info) {
			info = new InfoDisplay();
			// HEIGHT InfoDisplay 27
			applySize(info, 27);
		}
		return info;
	}

	/**
	 * Create the title display.
	 * 
	 * @return The title display.
	 */
	private Display createTitle() {
		if(null == title) {
			title = new TitleDisplay();
			// HEIGHT TitleDisplay 60
			applySize(title, 60);
		}
		return title;
	}
}
