/*
 * Created On:  9-Jan-07 1:03:56 PM
 */
package com.thinkparity.desdemona.model;

import java.lang.reflect.Proxy;

import com.thinkparity.codebase.model.ThinkParityException;

import com.thinkparity.desdemona.model.backup.BackupModel;
import com.thinkparity.desdemona.model.backup.BackupModelImpl;
import com.thinkparity.desdemona.model.migrator.MigratorModel;
import com.thinkparity.desdemona.model.migrator.MigratorModelImpl;
import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ModelFactory {

    /**
     * Obtain an instance of <code>ModelFactory</code>.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @return A <code>ModelFactory</code>.
     */
    public static ModelFactory getInstance(final Session session) {
        return new ModelFactory(session);
    }

    /**
     * Create a proxy for a thinkParity model.
     * 
     * @param session
     *            A user <code>Session</code>.
     * @param classLoader
     *            A <code>ClassLoader</code>.
     * @param modelInterface
     *            A model interface <code>Class</code>.
     * @param modelImplementation
     *            A model implementation <code>Class</code>.
     * @return An instance of the model.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    static Object newModelProxy(final Session session, final ClassLoader classLoader,
            final Class<?> modelInterface, final Class<?> modelImplementation) {
        try {
            final AbstractModelImpl modelInstance = (AbstractModelImpl) modelImplementation.newInstance();
            modelInstance.initialize(session);
            modelInstance.initializeModel(session);
            return Proxy.newProxyInstance(classLoader,
                    new Class<?>[] { modelInterface },
                    new ModelInvocationHandler(session, modelInstance));
        } catch (final Exception x) {
            throw new ThinkParityException("Cannot instantiate model.", x);
        }
    }

    /** The proxy <code>ClassLoader</code>. */
    private final ClassLoader classLoader;

    /** A user <code>Session</code>. */
    private final Session session;

    /**
     * Create ModelFactory.
     *
     */
    private ModelFactory(final Session session) {
        super();
        this.session = session;
        this.classLoader = session.getClass().getClassLoader();
    }

    /**
     * Obtain a backup model.
     * 
     * @return An instance of <code>BackupModel</code>.
     */
    public final BackupModel getBackupModel() {
        return (BackupModel) newModelProxy(
                BackupModel.class, BackupModelImpl.class);
    }

    /**
     * Obtain a migrator model.
     * 
     * @return An instance of <code>MigratorModel</code>.
     */
    public final MigratorModel getMigratorModel() {
        return (MigratorModel) newModelProxy(
                MigratorModel.class, MigratorModelImpl.class);
    }

    /**
     * Create a proxy for a thinkParity model.
     * 
     * @param modelInterface
     *            A thinkParity model interface.
     * @param modelImplementation
     *            A thinkParity model implementation.
     * @return A new proxy instance <code>Object</code>.
     */
    private Object newModelProxy(final Class<?> modelInterface,
            final Class<?> modelImplementation) {
        return newModelProxy(session, classLoader, modelInterface,
                modelImplementation);
    }
}
