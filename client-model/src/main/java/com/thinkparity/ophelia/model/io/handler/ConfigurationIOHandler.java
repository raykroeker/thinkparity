/*
 * Feb 22, 2006
 */
package com.thinkparity.ophelia.model.io.handler;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;

/**
 * The configuration io handler is a simple string configuration storage
 * strategy.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public interface ConfigurationIOHandler {

    /**
     * Delete a configuration entry.
     * 
     * @param key
     *            The configuration key.
     * @throws HypersonicException
     */
    public void delete(final String key) throws HypersonicException;

    /**
     * Read a configuration entry.
     * 
     * @param key
     *            The configuration key.
     * @return The configuration value.
     * @throws HypersonicException
     */
    public String read(final String key) throws HypersonicException;

    /**
     * Create a configuration entry.
     * 
     * @param key
     *            The key <code>String</code>.
     * @param value
     *            An <code>Integer</code> value.
     */
    void create(String key, Integer value);

    /**
     * Create a configuration entry.
     * 
     * @param key
     *            The key <code>String</code>.
     * @param value
     *            A <code>String</code> value.
     */
    void create(String key, String value);

    /**
     * Determine if a configuration value is set.
     * 
     * @param key
     *            A <code>String</code>.
     * @return A <code>Boolean</code>.
     */
    Boolean isSet(String key);

    /**
     * Read an integer configuration value.
     * 
     * @param key
     *            A <code>String</code>.
     * @return An <code>Integer</code>.
     */
    Integer readInteger(String key);

    /**
     * Update an integer configuration entry.
     * 
     * @param key
     *            The key <code>String</code>.
     * @param value
     *            An <code>Integer</code> value.
     */
    void update(String key, Integer value);

	/**
     * Update a string configuration entry.
     * 
     * @param key
     *            The key <code>String</code>.
     * @param value
     *            An <code>Integer</code> value.
     */
	void update(String key, String value);
}
