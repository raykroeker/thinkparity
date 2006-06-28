/*
 * Created On: Jun 25, 2006 11:26:30 AM
 * $Id$
 */
package com.thinkparity.migrator.application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.l10n.L18nContext;

import com.thinkparity.migrator.platform.Platform;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractApplication implements Application {

    /** Application listeners. */
    private static final Map<Class, Set<ApplicationListener>> APPLICATION_LISTENERS;

    static {
        APPLICATION_LISTENERS = new HashMap<Class, Set<ApplicationListener>>();
    }

    /** An apache logger. */
    protected final Logger logger;

    /** The browser platform. */
    private final Platform platform;

    /**
     * The current application status.
     * 
     * @see #AbstractApplication(Platform, L18nContext)
     * @see #getStatus()
     * @see #setStatus(ApplicationStatus)
     */
    private ApplicationStatus status;

    /**
     * Create an AbstractApplication.
     * 
     */
    protected AbstractApplication(final Platform platform) {
        super();
        this.logger = Logger.getLogger(getClass());
        this.platform = platform;
        this.status = ApplicationStatus.NEW;
    }

    /**
     * @see com.thinkparity.browser.platform.application.Application#addListener(com.thinkparity.browser.platform.application.ApplicationListener)
     * 
     */
    public void addListener(final ApplicationListener l) {
        Assert.assertNotNull(
                "[LBROWSER] [PLATFORM] [APPLICATION] [ADD LISTENER] [CANNOT ADD NULL LISTENER]", l);
        synchronized(APPLICATION_LISTENERS) {
            final Set<ApplicationListener> listeners = getListeners();
            Assert.assertNotTrue(
                    "[LBROWSER] [PLATFORM] [APPLICATION] [ADD LISTENER] [CANNOT RE-ADD LISTENER]",
                    listeners.contains(l));
            listeners.add(l);
            APPLICATION_LISTENERS.put(getClass(), listeners);
        }
    }

    /**
     * @see com.thinkparity.browser.platform.application.Application#getStatus()
     * 
     */
    public ApplicationStatus getStatus() { return status; }

    /**
     * @see com.thinkparity.browser.platform.application.Application#removeListener(com.thinkparity.browser.platform.application.ApplicationListener)
     * 
     */
    public void removeListener(ApplicationListener l) {
        synchronized(APPLICATION_LISTENERS) {
            final Set<ApplicationListener> listeners = getListeners();
            Assert.assertTrue(
                    "Cannot re-remove a listener.",
                    listeners.contains(l));
            listeners.remove(l);
            APPLICATION_LISTENERS.put(getClass(), listeners);
        }
    }

    /**
     * Assert the status change is valid.
     * 
     * @param status
     *            The status to change to.
     */
    protected void assertStatusChange(final ApplicationStatus status) {
        final ApplicationStatus[] acceptableStatus;
        switch(this.status) {
        case NEW:
            acceptableStatus = new ApplicationStatus[] {
                ApplicationStatus.STARTING
            };
            break;
        case STARTING:
            acceptableStatus = new ApplicationStatus[] {
                ApplicationStatus.RUNNING
            };
            break;
        case RUNNING:
            acceptableStatus = new ApplicationStatus[] {
                ApplicationStatus.ENDING, ApplicationStatus.HIBERNATING
            };
            break;
        case HIBERNATING:
            acceptableStatus = new ApplicationStatus[] {
                ApplicationStatus.ENDING, ApplicationStatus.RESTORING
            };
            break;
        case RESTORING:
            acceptableStatus = new ApplicationStatus[] {
                ApplicationStatus.RUNNING
            };
            break;
        case ENDING:
            acceptableStatus = new ApplicationStatus[] {};
            break;
        default:
            throw Assert.createUnreachable(
                    "Unknown application status:  " + status);
        }
        assertIsOneOf(acceptableStatus, status);
    }

    /**
     * Obtain the platform.
     * 
     * @return The platform.
     */
    protected Platform getPlatform() { return platform; }

    /**
     * Notify all application listeners that the application has ended.
     *
     */
    protected void notifyEnd() {
        synchronized(APPLICATION_LISTENERS) {
            final Set<ApplicationListener> listeners = getListeners();
            for(final ApplicationListener l : listeners) {
                l.notifyEnd(this);
            }
        }
    }

    /**
     * Notify all application listeners that the application has hibernated.
     *
     */
    protected void notifyHibernate() {
        synchronized(APPLICATION_LISTENERS) {
            final Set<ApplicationListener> listeners = getListeners();
            for(final ApplicationListener l : listeners) {
                l.notifyHibernate(this);
            }
        }
    }

    /**
     * Notify all application listeners that the application has restored.
     *
     */
    protected void notifyRestore() {
        synchronized(APPLICATION_LISTENERS) {
            final Set<ApplicationListener> listeners = getListeners();
            for(final ApplicationListener l : listeners) {
                l.notifyRestore(this);
            }
        }
    }

    /**
     * Notify all application listeners that the application has started.
     *
     */
    protected void notifyStart() {
        synchronized(APPLICATION_LISTENERS) {
            final Set<ApplicationListener> listeners = getListeners();
            for(final ApplicationListener l : listeners) {
                l.notifyStart(this);
            }
        }
    }

    /**
     * Set the status.
     * 
     * @param status
     *            The new status.
     */
    protected void setStatus(final ApplicationStatus status) {
        assertStatusChange(status);
        this.status = status;
    }

    private void assertIsOneOf(final ApplicationStatus[] acceptableStatus,
            final ApplicationStatus targetStatus) {
        for(final ApplicationStatus status : acceptableStatus) {
            if(status == targetStatus) { return; }
        }
        throw new IllegalArgumentException(
                "Cannot move application status from:  " +
                this.status + " to:  " + targetStatus + ".");
    }

    /**
     * Obtain the listeners for this class from the static list. Note that the
     * execution of this api must exist from within a synchronized code-block.
     * 
     * @return The set of application listeners for this class.
     */
    private Set<ApplicationListener> getListeners() {
        final Set<ApplicationListener> listeners;
        if(APPLICATION_LISTENERS.containsKey(getClass())) {
            listeners = APPLICATION_LISTENERS.get(getClass());
        }
        else { listeners = new HashSet<ApplicationListener>(); }
        return listeners;
    }
}
