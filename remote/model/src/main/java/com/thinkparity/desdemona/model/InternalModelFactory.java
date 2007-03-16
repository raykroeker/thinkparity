/*
 * Created On: Sep 11, 2006 4:06:57 PM
 */
package com.thinkparity.desdemona.model;

import com.thinkparity.codebase.model.Context;

import com.thinkparity.ophelia.model.migrator.InternalMigratorModel;
import com.thinkparity.ophelia.model.migrator.MigratorModelImpl;

import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Model Factory<br>
 * <b>Description:</b>An internal model factory is used by the model to
 * generate references to interal models outside the scope of the model impl
 * classes; usually to pass off to helper command pattern objects.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class InternalModelFactory {

    /**
     * Obtain an instance of <code>InternalModelFactory</code>.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @return A <code>InternalModelFactory</code>.
     */
    public static InternalModelFactory getInstance(final Context context,
            final Session session) {
        return new InternalModelFactory(context, session);
    }

    /** A <code>ClassLoader</code>. */
    private final ClassLoader classLoader;

    /** A user <code>Session</code>. */
    private final Session session;

    /**
     * Create InternalModelFactory.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @param session
     *            A user <code>Session</code>.
     */
    private InternalModelFactory(final Context context, final Session session) {
        super();
        this.session = session;
        this.classLoader = session.getClass().getClassLoader();
    }

    /**
     * Obtain an internal migrator model.
     * 
     * @return An instance of <code>InternalMigratorModel</code>.
     */
    public final InternalMigratorModel getMigratorModel() {
        return (InternalMigratorModel) newModelProxy(
                InternalMigratorModel.class, MigratorModelImpl.class);
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
        return ModelFactory.newModelProxy(session, classLoader, modelInterface,
                modelImplementation);
    }
}
