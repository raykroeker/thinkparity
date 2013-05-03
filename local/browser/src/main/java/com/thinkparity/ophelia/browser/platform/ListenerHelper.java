/*
 * Created On: Sep 20, 2006 11:05:10 AM
 */
package com.thinkparity.ophelia.browser.platform;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.event.EventNotifier;

import com.thinkparity.ophelia.browser.platform.event.LifeCycleEvent;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleListener;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ListenerHelper {

    private final List<LifeCycleListener> listeners;

    /** The browser platform. */
    private final BrowserPlatform platform;

    ListenerHelper(final BrowserPlatform platform) {
        super();
        this.listeners = new ArrayList<LifeCycleListener>();
        this.platform = platform;
    }

    boolean addListener(final LifeCycleListener listener) {
        synchronized (listeners) {
            if (listeners.contains(listener)) {
                return false;
            } else {
                return listeners.add(listener);
            }
        }
    }

    LifeCycleEvent createEvent() {
        return new LifeCycleEvent((Platform) platform);
    }

    void notifyListeners(final EventNotifier<LifeCycleListener> notifier) {
        synchronized (listeners) {
            for (final LifeCycleListener listener : listeners) {
                notifier.notifyListener(listener);
            }
        }
    }

    boolean removeListener(final LifeCycleListener listener) {
        synchronized (listeners) {
            if (!listeners.contains(listener)) {
                return false;
            } else {
                return listeners.remove(listener);
            }
        }
    }
}
