/*
 * Created On: Sep 12, 2006 9:22:19 AM
 */
package com.thinkparity.ophelia.model.workspace;

import java.util.List;

import com.thinkparity.codebase.event.EventListener;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModelImpl;

/**
 * <b>Title:</b>thinkParity Internal Workspace Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
public final class InternalWorkspaceModel extends WorkspaceModel {

    /**
     * Obtain an internal workspace model.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @return A <code>InternalWorkspaceModel</code>.
     */
    public static InternalWorkspaceModel getInstance(final Context context,
            final Environment environment) {
        return new InternalWorkspaceModel(environment);
    }

    /**
     * Create InternalWorkspaceModel.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     */
    private InternalWorkspaceModel(final Environment environment) {
        super(environment);
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
    public <T extends EventListener> boolean addListener(
            final Workspace workspace, final AbstractModelImpl impl,
            final T listener) {
        return findImpl(workspace).addListener(impl, listener);
    }

    /**
     * Obtain the model's event listeners.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @return A list of typed thinkParity event listeners.
     */
    public <T extends EventListener> List<T> getListeners(
            final Workspace workspace, final AbstractModelImpl<T> impl) {
        return findImpl(workspace).getListeners(workspace, impl);
    }

    /**
     * Remove a model's event listener.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @param listener
     *            A thinkParity event listener.
     */
    public <T extends EventListener> boolean removeListener(
            final Workspace workspace, final AbstractModelImpl impl,
            final T listener) {
        return findImpl(workspace).removeListener(impl, listener);
    }
}
