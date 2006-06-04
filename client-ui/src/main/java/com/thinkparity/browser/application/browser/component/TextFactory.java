/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.application.browser.component;

import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TextFactory extends ComponentFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final TextFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new TextFactory();
		singletonLock = new Object();
	}

	public static JTextField create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}

	public static JTextArea createArea() {
		synchronized(singletonLock) { return singleton.doCreateArea(); }
	}

        public static JTextArea createArea(final String text) {
            synchronized(singletonLock) { return singleton.doCreateArea(text); }
        }

	public static JPasswordField createPassword() {
		synchronized(singletonLock) { return singleton.doCreatePassword(); }
	}

	/**
	 * Create a ButtonFactory.
	 */
	private TextFactory() { super(); }

	private JTextArea doCreateArea() {
		final JTextArea jTextArea = new JTextArea();
		applyDefaultFont(jTextArea);
		return jTextArea;
	}

        private JTextArea doCreateArea(final String text) {
            final JTextArea jTextArea = doCreateArea();
            jTextArea.setText(text);
            return jTextArea;
        }

	private JTextField doCreate() {
		final JTextField jTextField = new JTextField();
		applyDefaultFont(jTextField);
		return jTextField;		
	}
	private JPasswordField doCreatePassword() {
		final JPasswordField jPasswordField = new JPasswordField();
		applyDefaultFont(jPasswordField);
		return jPasswordField;
	}
}
