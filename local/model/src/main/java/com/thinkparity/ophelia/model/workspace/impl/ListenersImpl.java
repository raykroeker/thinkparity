/*
 * Created On: Sep 12, 2006 9:30:00 AM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.util.EventListener;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ListenersImpl {

    private final Map<Class<? extends AbstractModelImpl>, List<EventListener>> listeners;

    /**
     * Create ListenersImpl.
     * 
     * @param workspace
     *            The <code>WorkspaceImpl</code>.
     */
    ListenersImpl(final WorkspaceImpl workspace) {
        super();
        this.listeners = new HashMap<Class<? extends AbstractModelImpl>, List<EventListener>>();
    }

    /**
     * Add an event listener to the workspace for the model implementation.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @param impl
     *            A thinkParity model implementation.
     * @param listener
     *            A thinkParity event listener.
     * @return Whether or not the listeners list was modified.
     */
    <T extends EventListener> boolean add(final AbstractModelImpl impl,
            final T listener) {
        final List<EventListener> listeners;
        if (this.listeners.containsKey(impl.getClass())) {
            listeners = this.listeners.get(impl.getClass());
        } else {
            listeners = new ArrayList<EventListener>();
        }
        final boolean modified = listeners.add(listener);
        this.listeners.put(impl.getClass(), listeners);
        return modified;
    }

    /**
     * Obtain a list of all of the event listeners for a model implementation.
     * 
     * @param impl
     *            A model implementation.
     * @return A list of event listeners.
     */
    @SuppressWarnings("unchecked")
    <T extends EventListener> List<T> get(final AbstractModelImpl impl) {
        final List<T> listeners;
        if (this.listeners.containsKey(impl.getClass())) {
            listeners = new ArrayList<T>();
            for (final EventListener listener : this.listeners.get(impl.getClass())) {
                listeners.add((T) listener);
            }
        } else {
            listeners = Collections.emptyList();
        }
        return listeners;
    }

    /**
     * Add an event listener to the workspace for the model implementation.
     * 
     * @param impl
     *            A thinkParity model implementation.
     * @param listener
     *            A thinkParity event listener.
     * @return Whether or not the listeners list was modified.
     */
    <T extends EventListener> boolean remove(final AbstractModelImpl impl,
            final T listener) {
        final List<EventListener> listeners;
        if (this.listeners.containsKey(impl.getClass())) {
            listeners = this.listeners.get(impl.getClass());
        } else {
            return false;
        }
        final boolean modified = listeners.remove(listener);
        this.listeners.put(impl.getClass(), listeners);
        return modified;
    }

    /**
     * Start the listeners implementation.
     *
     */
    void start() {
    }

    /**
     * Stop the listeners implementation.
     *
     */
    void stop() {
    }
}
