/*
 * Created On:  2-Jan-08 10:52:53 AM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.util.Properties;

import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.configuration.Configuration;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace Memory Configuration
 * Implementation<br>
 * <b>Description:</b>A properties backed implementation of the workspace
 * configuration.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class MemoryConfigurationImpl extends ConfigurationImpl implements Configuration {

    /** The configuration memory. */
    private final Properties configurationMemory;

    /**
     * Create MemoryConfigurationImpl.
     *
     */
    public MemoryConfigurationImpl(final Workspace workspace) {
        super(workspace);
        this.configurationMemory = new Properties();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#getInteger(java.lang.String)
     *
     */
    @Override
    protected final Integer getInteger(final String key) {
        if (isSet(key)) {
            return Integer.valueOf(configurationMemory.getProperty(key));
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#getString(java.lang.String)
     *
     */
    @Override
    protected final String getString(final String key) {
        if (isSet(key)) {
            return configurationMemory.getProperty(key);
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#isSet(java.lang.String)
     *
     */
    @Override
    protected final Boolean isSet(final String key) {
        return Boolean.valueOf(configurationMemory.containsKey(key));
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#set(java.lang.String, java.lang.Integer)
     *
     */
    @Override
    protected final void set(final String key, final Integer value) {
        configurationMemory.setProperty(key, value.toString());
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#set(java.lang.String, java.lang.String)
     *
     */
    @Override
    protected final void set(final String key, final String value) {
        configurationMemory.setProperty(key, value);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#unset(java.lang.String)
     *
     */
    @Override
    protected final void unset(final String key) {
        if (isSet(key)) {
            configurationMemory.remove(key);
        }
    }
}
