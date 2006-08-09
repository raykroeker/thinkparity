/*
 * Created On: Jan 5, 2006
 */
package com.thinkparity.browser.platform.util.persistence;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public interface Persistence {
    public Boolean get(final String key, final Boolean defaultValue);
	public Dimension get(final String key, final Dimension defaultValue);
	public File get(final String key, final File defaultValue);
	public int get(final String key, final int defaultValue);
	public Point get(final String key, final Point defaultPoint);
	public String get(final String key, final String defaultValue);
	public void set(final String key, final Boolean value);
	public void set(final String key, final Dimension value);
	public void set(final String key, final File value);
	public void set(final String key, final int value);
	public void set(final String key, final Point point);
    public void set(final String key, final String value);
}
