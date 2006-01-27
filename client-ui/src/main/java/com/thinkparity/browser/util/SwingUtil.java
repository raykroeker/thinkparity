/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.util;

import javax.swing.JTextField;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SwingUtil {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final SwingUtil singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new SwingUtil();
		singletonLock = new Object();
	}

	public static String extract(final JTextField jTextField) {
		synchronized(singletonLock) { return singleton.doExtract(jTextField); }
	}

	public static void set(final JTextField jTextField, final String text) {
		synchronized(singletonLock) { singleton.doSet(jTextField, text); }
	}
	public static void setToolTip(final JTextField jTextField, final String text) {
		synchronized(singletonLock) { singleton.doSetToolTip(jTextField, text); }
	}

	/**
	 * Create a SwingUtil [Singleton]
	 * 
	 */
	private SwingUtil() { super(); }

	private String doExtract(final JTextField jTextField) {
		return doExtract(jTextField.getText());
	}

	private String doExtract(final String string) {
		if(null == string) { return null; }
		if(0 == string.length()) { return null; }
		return string;
	}

	private void doSet(final JTextField jTextField, final String string) {
		if(null == string) { return; }
		if(0 == string.length()) { return; }
		jTextField.setText(string);
	}

	private void doSetToolTip(final JTextField jTextField, final String string) {
		if(null == string) { return; }
		if(0 == string.length()) { return; }
		jTextField.setToolTipText(string);
	}
}
