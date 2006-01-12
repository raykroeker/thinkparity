/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.javax.swing.action;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Data {

	/**
	 * The underlying data container.  It contains a map of enumerated types
	 * pointing to object values.
	 * 
	 */
	private final Map<Enum<?>,Object> data;

	/**
	 * Create a Data.
	 * 
	 */
	public Data() { this(7); }

	/**
	 * Create a Data.
	 * 
	 * @param initialCapacity
	 *            The initial capacity of the data container.
	 */
	public Data(final Integer initialCapacity) {
		super();
		this.data = new Hashtable<Enum<?>,Object>(initialCapacity, 0.75F);
	}

	/**
	 * Obtain a data item.
	 * 
	 * @param key
	 *            The data item key.
	 * @return The data item; or null if the container has no map for this key.
	 */
	public Object get(final Enum<?> key) { return data.get(key); }

	/**
	 * Set a data item.
	 * 
	 * @param key
	 *            The data item key.
	 * @param value
	 *            The data item value.
	 * @return The previous value for the data item key.
	 */
	public Object set(final Enum<?> key, final Object value) {
		return data.put(key, value);
	}
}
