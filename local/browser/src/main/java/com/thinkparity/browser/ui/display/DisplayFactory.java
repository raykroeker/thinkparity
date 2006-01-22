/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Dimension;

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
		case LOGO:
			return SINGLETON.createLogo();
		case CONTENT:
			return SINGLETON.createContent();
		case INFO:
			return SINGLETON.createInfo();
		default: throw Assert.createUnreachable("Unknown display id:  " + id);
		}
	}

	/**
	 * Content display.
	 * 
	 */
	private Display content;

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
	 * Create a DisplayFactory [Singleton, Factory]
	 * 
	 */
	private DisplayFactory() { super(); }

	/**
	 * Create the content display.
	 * 
	 * @return The content display.
	 */
	private Display createContent() {
		if(null == content) {
			content = new ContentDisplay();
			final Dimension s = new Dimension(350, 275);
			content.setPreferredSize(s);
			content.setMinimumSize(s);
			content.setMaximumSize(s);
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
			final Dimension s = new Dimension(350, 160);
			info.setPreferredSize(s);
			info.setMinimumSize(s);
			info.setMaximumSize(s);
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
			final Dimension s = new Dimension(350, 85);
			logo.setPreferredSize(s);
			logo.setMinimumSize(s);
			logo.setMaximumSize(s);
		}
		return logo;
	}
}
