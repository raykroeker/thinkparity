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
     * Create a configuration entry.
     * 
     * @param key
     *            The configuration key.
     * @param value
     *            The configuration value.
     * @throws HypersonicException
     */
	public void create(final String key, final String value)
            throws HypersonicException;

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
     * Update a configuration entry.
     * 
     * @param key
     *            The configuration key.
     * @param value
     *            The new configuration value.
     * @throws HypersonicException
     */
	public void update(final String key, final String value)
            throws HypersonicException;
}
