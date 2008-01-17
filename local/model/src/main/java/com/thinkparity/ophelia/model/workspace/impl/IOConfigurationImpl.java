/*
 * Created On:  21-Dec-07 11:54:05 AM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.configuration.Configuration;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace IO Configuration
 * Implementation<br>
 * <b>Description:</b>A database backed implementation of the workspace io
 * configuration.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class IOConfigurationImpl extends ConfigurationImpl implements Configuration {

    /** A configuration io handler. */
    private final ConfigurationIOHandler configurationIO;

    /**
     * Create IOConfigurationImpl.
     * 
     * @param workspace
     *            A <code>Workspace</code>.
     */
    IOConfigurationImpl(final Workspace workspace) {
        super(workspace);
        this.configurationIO = IOFactory.getDefault(workspace).createConfigurationHandler();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#getInteger(java.lang.String)
     *
     */
    @Override
    protected final Integer getInteger(final String key) {
        return configurationIO.readInteger(key);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#getString(java.lang.String)
     *
     */
    @Override
    protected final String getString(final String key) {
        return configurationIO.read(key);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#isSet(java.lang.String)
     * 
     */
    @Override
    protected final Boolean isSet(final String key) {
        return configurationIO.isSet(key);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#set(java.lang.String, java.lang.Integer)
     *
     */
    @Override
    protected final void set(final String key, final Integer value) {
        configurationIO.create(key, value);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#set(java.lang.String, java.lang.String)
     *
     */
    @Override
    protected final void set(final String key, final String value) {
        configurationIO.create(key, value);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.impl.ConfigurationImpl#unset(java.lang.String)
     *
     */
    @Override
    protected final void unset(final String key) {
        if (isSet(key)) {
            configurationIO.delete(key);
        }
    }
}
