/**
 * 
 */
package com.thinkparity.codebase.ui.platform;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.ui.platform.event.LifeCycleEvent;
import com.thinkparity.codebase.ui.platform.event.LifeCycleListener;

/**
 * @author raymond@thinkparity.com
 */
public abstract class DefaultPlatform implements Platform {

    /** A <code>DefaultPlatform.ListenerHelper</code>. */
    protected final ListenerHelper listenerHelper;

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /**
     * Create DefaultPlatform.
     * 
     */
    protected DefaultPlatform() {
        super();
        this.listenerHelper = new ListenerHelper(this);
        this.logger = new Log4JWrapper();
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.Platform#addListener(com.thinkparity.codebase.ui.platform.event.LifeCycleListener)
     * 
     */
    public boolean addListener(final LifeCycleListener listener) {
        return listenerHelper.addListener(listener);
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.Platform#removeListener(com.thinkparity.codebase.ui.platform.event.LifeCycleListener)
     * 
     */
    public boolean removeListener(final LifeCycleListener listener) {
        return listenerHelper.removeListener(listener);
    }

    /**
     * Notify all listeners that the platform has ended.
     *
     */
    protected void notifyLifeCycleEnded() {
        final LifeCycleEvent e = listenerHelper.createEvent();
        listenerHelper.notifyListeners(new EventNotifier<LifeCycleListener>() {
            public void notifyListener(final LifeCycleListener listener) {
                listener.ended(e);
            }
        });
    }

    /**
     * Notify all listeners that the platform is ending.
     *
     */
    protected void notifyLifeCycleEnding() {
        final LifeCycleEvent e = listenerHelper.createEvent();
        listenerHelper.notifyListeners(new EventNotifier<LifeCycleListener>() {
            public void notifyListener(final LifeCycleListener listener) {
                listener.ending(e);
            }
        });
    }

    /**
     * Notify all listeners that the platform has started.
     *
     */
    protected void notifyLifeCycleStarted() {
        final LifeCycleEvent e = listenerHelper.createEvent();
        listenerHelper.notifyListeners(new EventNotifier<LifeCycleListener>() {
            public void notifyListener(final LifeCycleListener listener) {
                listener.started(e);
            }
        });
    }

    /**
     * Notify all life cycle listeners that the platform is starting.
     *
     */
    protected void notifyLifeCycleStarting() {
        final LifeCycleEvent e = listenerHelper.createEvent();
        listenerHelper.notifyListeners(new EventNotifier<LifeCycleListener>() {
            public void notifyListener(final LifeCycleListener listener) {
                listener.starting(e);
            }
        });
    }

    /**
     * A listener helper designed to ease life cycle listener registration as
     * well as event notification.
     * 
     */
    public final class ListenerHelper {

        /** A <code>LifeCycleListener</code> <code>List</code>. */
        private final List<LifeCycleListener> listeners;

        /** A <code>Platform</code>. */
        private final Platform platform;

        /**
         * Create ListenerHelper.
         * 
         * @param platform
         *            A <code>Platform</code>.
         */
        private ListenerHelper(final Platform platform) {
            super();
            this.listeners = new ArrayList<LifeCycleListener>();
            this.platform = platform;
        }

        /**
         * Add a life cycle listener.
         * 
         * @param listener
         *            A <code>LifeCycleListener</code>.
         * @return True if the listeners list was updated.
         */
        public final boolean addListener(final LifeCycleListener listener) {
            synchronized (listeners) {
                if (listeners.contains(listener)) {
                    return false;
                } else {
                    return listeners.add(listener);
                }
            }
        }

        /**
         * Create a life cycle event.
         * 
         * @return A <code>LifeCycleEvent</code>.
         */
        public final LifeCycleEvent createEvent() {
            return new LifeCycleEvent(platform);
        }

        /**
         * Notify all life cycle listeners.
         * 
         * @param notifier
         *            A
         *            <code>LifeCycleListener</code> <code>EventNotifier</code>.
         */
        public final void notifyListeners(
                final EventNotifier<LifeCycleListener> notifier) {
            synchronized (listeners) {
                for (final LifeCycleListener listener : listeners) {
                    notifier.notifyListener(listener);
                }
            }
        }

        /**
         * Remove a life cycle listener.
         * 
         * @param listener
         *            A <code>LifeCycleListener</code>.
         * @return True if the listeners list was updated.
         */
        public final boolean removeListener(final LifeCycleListener listener) {
            synchronized (listeners) {
                if (!listeners.contains(listener)) {
                    return false;
                } else {
                    return listeners.remove(listener);
                }
            }
        }
    }
}
