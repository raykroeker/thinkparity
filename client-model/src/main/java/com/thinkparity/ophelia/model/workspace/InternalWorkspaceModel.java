/*
 * Created On: Sep 12, 2006 9:22:19 AM
 */
package com.thinkparity.ophelia.model.workspace;

import java.util.List;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.util.EventListener;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InternalWorkspaceModel extends WorkspaceModel implements
        InternalModel {

    /** Create InternalWorkspaceModel. */
    InternalWorkspaceModel(final Context context) {
        super();
        context.assertContextIsValid();
    }

    /**
     * Add a model's event listener.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @param impl
     *            A thinkParity model impl.
     * @param listener
     *            A thinkParity event listener.
     */
    public <T extends EventListener> boolean addListener(final Workspace workspace,
            final AbstractModelImpl impl, final T listener) {
        synchronized (getImplLock()) {
            return getImpl().addListener(workspace, impl, listener);
        }
    }

    /**
     * Obtain the model's event listeners.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @return A list of typed thinkParity event listeners.
     */
    public <T extends EventListener> List<T> getListeners(final Workspace workspace,
            final AbstractModelImpl<T> impl) {
        synchronized (getImplLock()) {
            return getImpl().getListeners(workspace, impl);
        }
    }

    /**
     * Remove a model's event listener.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @param listener
     *            A thinkParity event listener.
     */
    public <T extends EventListener> boolean removeListener(final Workspace workspace,
            final AbstractModelImpl impl, final T listener) {
        synchronized (getImplLock()) {
            return getImpl().removeListener(workspace, impl, listener);
        }
    }
}
