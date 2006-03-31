/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.platform.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JList;
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

	public static Boolean regionContains(final Rectangle region,
			final Point point) {
		synchronized(singletonLock) { return singleton.doesRegionContain(region, point); }
	}

	public static Boolean extract(final JCheckBox jCheckBox) {
		synchronized(singletonLock) { return singleton.doExtract(jCheckBox); }
	}
	public static String extract(final JTextField jTextField) {
		synchronized(singletonLock) { return singleton.doExtract(jTextField); }
	}

	public static <T extends Object> List<T> extract(final JList jList) {
		synchronized(singletonLock) { return singleton.doExtract(jList); }
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

	private Boolean doesRegionContain(final Rectangle region, final Point point) {
		if(null == region) { return Boolean.FALSE; }
		if(null == point) { return Boolean.FALSE; }
        return (point.x >= region.x)
        	&& (point.x < region.x + region.width)
        	&& (point.y >= region.y)
        	&& (point.y < region.y + region.height);
	}

	private <T extends Object> List<T> doExtract(final JList jList) {
		final List<T> l = new LinkedList<T>();
		final Object[] a = jList.getSelectedValues();
		for(final Object o : a) { l.add((T) o); }
		return l;
	}

	private Boolean doExtract(final JCheckBox jCheckBox) {
		return jCheckBox.isSelected();
	}
}
