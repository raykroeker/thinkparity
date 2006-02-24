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
		case FORM:
			return SINGLETON.createForm();
		case INFO:
			return SINGLETON.createInfo();
		case LOGO:
			return SINGLETON.createLogo();
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
	 * The form display.
	 * 
	 */
	private Display form;

	/**
	 * Info display.
	 * 
	 */
	private Display info;

	/**
	 * Logo display.
	 * 
	 */
	private Display logo;

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
			applySize(content, 300);
		}
		return content;
	}

	/**
	 * Create the form display.
	 * 
	 * @return The form display.
	 */
	private Display createForm() {
		if(null == form) {
			form = new FormDisplay();
			applySize(form, 394);
		}
		return form;
	}

	/**
	 * Create the info display.
	 * 
	 * @return The info display.
	 */
	private Display createInfo() {
		if(null == info) {
			info = new InfoDisplay();
			applySize(info, 135);
		}
		return info;
	}

	/**
	 * Create the logo display.
	 * 
	 * @return The logo display.
	 */
	private Display createLogo() {
		if(null == logo) {
			logo = new LogoDisplay();
			applySize(logo, 94);
		}
		return logo;
	}

	/**
	 * Create the title display.
	 * 
	 * @return The title display.
	 */
	private Display createTitle() {
		if(null == title) {
			title = new TitleDisplay();
			applySize(title, 21);
		}
		return title;
	}
}
