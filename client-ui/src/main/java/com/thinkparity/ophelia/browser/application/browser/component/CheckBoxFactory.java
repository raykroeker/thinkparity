/*
 * Jan 13, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.Font;

import javax.swing.JCheckBox;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CheckBoxFactory extends ComponentFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final CheckBoxFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new CheckBoxFactory();
		singletonLock = new Object();
	}

	/**
	 * Create a JChceckBox.
	 * 
	 * @return The JCheckBox.
	 */
	public static JCheckBox create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}

	/**
     * Create a JCheckBox.
     * 
     * @param font
     *            A <code>Font</code>.
     * @return A <code>JCheckBox</code>.
     */
	public static JCheckBox create(final Font font) {
	    return singleton.doCreate(font);
	}

	/**
	 * Create a JCheckBox with the specified text.
	 * 
	 * @param text
	 *            The checkbox text.
	 * @return The JCheckBox.
	 */
	public static JCheckBox create(final String text) {
		synchronized(singletonLock) { return singleton.doCreate(text); }
	}

	/**
	 * Create a ButtonFactory.
	 */
	private CheckBoxFactory() { super(); }

    /**
     * Create a JChceckBox.
     * 
     * @return The JCheckBox.
     */
    private JCheckBox doCreate() {
        final JCheckBox jCheckBox = new JCheckBox();
        applyDefaultFont(jCheckBox);
        return jCheckBox;
    }

    /**
     * Create a JChceckBox.
     * 
     * @param font
     *            A <code>Font</code>.
     * @return The JCheckBox.
     */
    private JCheckBox doCreate(final Font font) {
        final JCheckBox jCheckBox = doCreate();
        applyFont(jCheckBox, font);
        return jCheckBox;
    }

	/**
	 * Create a JCheckBox with the specified text.
	 * 
	 * @param text
	 *            The checkbox text.
	 * @return The JCheckBox.
	 */
	private JCheckBox doCreate(final String text) {
		final JCheckBox jCheckBox = doCreate();
		jCheckBox.setText(text);
		return jCheckBox;
	}
}
