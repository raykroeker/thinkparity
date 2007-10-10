/*
 * Created On:  29-Sep-07 3:34:03 PM
 */
package com.thinkparity.adriana.backup.ui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.thinkparity.adriana.backup.model.util.ModelFactory;
import com.thinkparity.adriana.backup.model.util.Session;

/**
 * <b>Title:</b>thinkParity Adriana Backup Command Context<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BackupCommandContext {

    /** The backup properties. */
    private static final Properties properties;

    /** The backup properties synchronization lock. */
    private static final Object propertiesLock;

    static {
        properties = new Properties();
        propertiesLock = new Object();
    }

    /** A backup session. */
    private Session session;

    /**
     * Create BackupCommandContext.
     *
     */
    BackupCommandContext() {
        super();
    }

    /**
     * Obtain an instance of a model factory.
     * 
     * @return A <code>ModelFactory</code>.
     */
    public ModelFactory getModelFactory() {
        return ModelFactory.getInstance();
    }

    /**
     * Obtain the properties.
     * 
     * @return A set of <code>Properties</code>.
     */
    public Properties getProperties() {
        synchronized (propertiesLock) {
            return properties;
        }
    }

    /**
     * Obtain the backup session.
     * 
     * @return A <code>Session</code>.
     */
    public Session getSession() {
        return session;
    }

    /**
     * Load the properties.
     * 
     * @throws IOException
     */
    void loadProperties() throws IOException {
        final InputStream inputStream = new FileInputStream("");
        synchronized (propertiesLock) {
            properties.load(inputStream);
        }
    }

    /**
     * Set the backup session.
     * 
     * @param session
     *            A <code>Session</code>.
     */
    void setSession(final Session session) {
        this.session = session;
    }
}
