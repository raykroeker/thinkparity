/**
 * 
 */
package com.thinkparity.codebase.ui.application;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.l10n.L18nContext;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.ui.platform.Platform;

/**
 * @author raymond
 *
 */
public abstract class DefaultApplication<T extends Platform> implements Application<T> {

    /** An <code>Application</code> <code>ListenerHelper</code>. */
    protected final ListenerHelper listenerHelper;

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /**
     * The current application status.
     * 
     * @see #AbstractApplication(Platform, L18nContext)
     * @see #getStatus()
     * @see #setStatus(ApplicationStatus)
     */
    private ApplicationStatus status;

    /**
     * Create DefaultApplication.
     * 
     */
    protected DefaultApplication() {
        super();
        this.listenerHelper = new ListenerHelper();
        this.logger = new Log4JWrapper();
        this.status = ApplicationStatus.NEW;
    }

    /**
     * @see com.thinkparity.codebase.ui.application.Application#addListener(com.thinkparity.codebase.ui.application.ApplicationListener)
     * 
     */
    public boolean addListener(final ApplicationListener l) {
        return listenerHelper.addListener(l);
    }

    /**
     * Obtain the application status.
     * 
     * @return The <code>ApplicationStatus</code>.
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * @see com.thinkparity.codebase.ui.application.Application#removeListener(com.thinkparity.codebase.ui.application.ApplicationListener)
     * 
     */
    public boolean removeListener(ApplicationListener listener) {
        return listenerHelper.removeListener(listener);
        
    }

    /**
     * Assert the status change is valid.
     * 
     * @param target
     *            The target <code>ApplicationStatus</code>.
     */
    protected final void assertStatusChange(final ApplicationStatus target) {
        final ApplicationStatus[] acceptable;
        switch (this.status) {
        case NEW:
            acceptable = new ApplicationStatus[] {
                ApplicationStatus.STARTING
            };
            break;
        case STARTING:
            acceptable = new ApplicationStatus[] {
                ApplicationStatus.RUNNING
            };
            break;
        case RUNNING:
            acceptable = new ApplicationStatus[] {
                ApplicationStatus.ENDING, ApplicationStatus.HIBERNATING
            };
            break;
        case HIBERNATING:
            acceptable = new ApplicationStatus[] {
                ApplicationStatus.ENDING, ApplicationStatus.RESTORING
            };
            break;
        case RESTORING:
            acceptable = new ApplicationStatus[] {
                ApplicationStatus.RUNNING
            };
            break;
        case ENDING:
            acceptable = new ApplicationStatus[] {};
            break;
        default:
            throw Assert.createUnreachable(
                    "Unknown application status:  " + status);
        }
        assertIsOneOf(target, acceptable);
    }

    /**
     * Set the application status.
     * 
     * @param status
     *            The <code>ApplicationStatus</code>.
     */
    protected void setStatus(final ApplicationStatus status) {
        assertStatusChange(status);
        this.status = status;
    }

    /**
     * Assert that the target application status is acceptable.
     * 
     * @param target
     *            A target <code>ApplicationStatus</code>.
     * @param acceptable
     *            An <code>ApplicationStatus</code> array.
     */
    private void assertIsOneOf(final ApplicationStatus target,
            final ApplicationStatus[] acceptable) {
        for (final ApplicationStatus acceptableStatus : acceptable) {
            if (target == acceptableStatus) {
                return;
            }
        }
        throw new IllegalArgumentException(
                "Cannot move application status from:  " +
                this.status + " to:  " + target + ".");
    }

    /**
     * A listener helper designed to ease application listener registration as
     * well as event notification.
     * 
     */
    public final class ListenerHelper {

        /** A <code>ApplicationListener</code> <code>List</code>. */
        private final List<ApplicationListener> listeners;

        /**
         * Create ListenerHelper.
         * 
         */
        private ListenerHelper() {
            super();
            this.listeners = new ArrayList<ApplicationListener>();
        }

        /**
         * Add a life cycle listener.
         * 
         * @param listener
         *            A <code>LifeCycleListener</code>.
         * @return True if the listeners list was updated.
         */
        public final boolean addListener(final ApplicationListener listener) {
            synchronized (listeners) {
                if (listeners.contains(listener)) {
                    return false;
                } else {
                    return listeners.add(listener);
                }
            }
        }

        /**
         * Notify all life cycle listeners.
         * 
         * @param notifier
         *            A
         *            <code>LifeCycleListener</code> <code>EventNotifier</code>.
         */
        public final void notifyListeners(
                final EventNotifier<ApplicationListener> notifier) {
            synchronized (listeners) {
                for (final ApplicationListener listener : listeners) {
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
        public final boolean removeListener(final ApplicationListener listener) {
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
