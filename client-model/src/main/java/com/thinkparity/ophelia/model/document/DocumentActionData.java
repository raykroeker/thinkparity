/*
 * Oct 31, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.util.Collection;
import java.util.Properties;
import java.util.Vector;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class DocumentActionData {

	/**
	 * Action data.
	 */
	private final Properties actionData;

	/**
	 * Create a DocumentActionData.
	 */
	public DocumentActionData() {
		super();
		this.actionData = new Properties();
	}

	/**
	 * Determine whether or not there is any action data.
	 * 
	 * @return True if action data contains any data; false otherwise.
	 */
	public Boolean containsData() {
		return !actionData.isEmpty();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof DocumentActionData) {
			return actionData.equals(((DocumentActionData) obj).actionData);
		}
		return false;
	}

	/**
	 * Obtain the value of a data item.
	 * 
	 * @param key
	 *            The key for the data item.
	 * @return The value of the data item; or null if it is not set.
	 */
	public String getDataItem(final String key) {
		return actionData.getProperty(key);
	}

	/**
	 * Obtain the value of a data item.
	 * 
	 * @param key
	 *            The key for the data item.
	 * @param defaultValue
	 *            The default value of the data item.
	 * @return The value of the data item; or default value if the data item is
	 *         null.
	 */
	public String getDataItem(final String key, final String defaultValue) {
		return actionData.getProperty(key, defaultValue);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() { return actionData.hashCode(); }

	/**
	 * Obtain the keys for the action data.
	 * 
	 * @return A collection of strings containing the action data keys.
	 */
	public Collection<String> keys() {
		final Collection<String> keys =
			new Vector<String>(actionData.keySet().size());
		for(Object key : actionData.keySet()) {
			keys.add((String) key);
		}
		return keys;
	}

	/**
	 * Set the value of a data item.
	 * 
	 * @param key
	 *            The key for the data item.
	 * @param value
	 *            The value of the data item.
	 * @return The previous value of the data item; or null if it did not exist.
	 */
	public String setDataItem(final String key, final String value) {
		return (String) actionData.setProperty(key, value);
	}

	/**
	 * Obtain all of the action data properties.
	 * @return
	 */
	public Properties getAll() {
		return new Properties(actionData);
	}
}
