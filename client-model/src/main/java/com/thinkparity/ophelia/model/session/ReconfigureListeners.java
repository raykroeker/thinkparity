/*
 * Created On:  12-Nov-07 10:04:38 AM
 */
package com.thinkparity.ophelia.model.session;

import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.thinkparity.codebase.event.EventNotifier;

import com.thinkparity.ophelia.model.util.configuration.ReconfigureListener;

/**
 * <b>Title:</b>thinkParity Ophelia Model Session Reconfigure Listeners<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ReconfigureListeners {

    /** A list of reconfigure listeners. */
    private final List<ReconfigureListener<Properties>> listenerList;

    /**
     * Create ReconfigureListeners.
     *
     */
    ReconfigureListeners() {
        super();
        this.listenerList = new Vector<ReconfigureListener<Properties>>();
    }

    /**
     * Add a listener.
     * 
     * @param listener
     *            A <code>ReconfigureListener<Properties></code>.
     */
    Boolean addListener(final ReconfigureListener<Properties> listener) {
        if (listenerList.contains(listener)) {
            return Boolean.FALSE;
        } else {
            return Boolean.valueOf(listenerList.add(listener));
        }
    }

    /**
     * Notify all listeners.
     * 
     * @param notifier
     *            An <code>EventNotifier<ReconfigureListener<Properties>></code>.
     */
    void notifyListeners(final EventNotifier<ReconfigureListener<Properties>> notifier) {
        for (final ReconfigureListener<Properties> listener : listenerList) {
            notifier.notifyListener(listener);
        }
    }

    /**
     * Remove a listener.
     * 
     * @param listener
     *            A <code>ReconfigureListener<Properties></code>.
     */
    Boolean removeListener(final ReconfigureListener<Properties> listener) {
        if (listenerList.contains(listener)) {
            return Boolean.valueOf(listenerList.remove(listener));
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Obtain the listener list size.
     * 
     * @return An <code>Integer</code>.
     */
    Integer size() {
        return Integer.valueOf(listenerList.size());
    }
}
