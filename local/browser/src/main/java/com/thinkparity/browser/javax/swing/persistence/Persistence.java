/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.javax.swing.persistence;

import java.awt.Dimension;
import java.awt.Point;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Persistence {
	public Dimension get(final String key, final Dimension defaultValue);
	public int get(final String key, final int defaultValue);
	public Point get(final String key, final Point defaultPoint);
	public String get(final String key, final String defaultValue);
	public void set(final String key, final Dimension value);
	public void set(final String key, final int value);
	public void set(final String key, final Point point);
	public void set(final String key, final String value);
}
