/*
 * Mar 18, 2006
 */
package com.thinkparity.ophelia.browser.application.system;

import java.awt.AWTException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.ophelia.model.workspace.configuration.ProxyConfiguration;

import com.thinkparity.ophelia.browser.application.system.dialog.DisplayInfoFrame;
import com.thinkparity.ophelia.browser.application.system.dialog.Notification;
import com.thinkparity.ophelia.browser.application.system.dialog.NotifyFrame;
import com.thinkparity.ophelia.browser.application.system.dialog.UpdateConfigurationFrame;
import com.thinkparity.ophelia.browser.application.system.tray.Tray;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
class SystemApplicationImpl extends Thread {

	/**
	 * Flag indicating the current running state.
	 * 
	 * @see #run()
	 */
	Boolean running = Boolean.FALSE;

    /** The proxy configuration. */
    private ProxyConfiguration configuration;

	/** Flag indicating the DisplayInfo dialog has been requested. */
    private Boolean displayInfoRequested = Boolean.FALSE;

    /** Queue of notifications not yet displayed. */
    private final List<Notification> notificationQueue;

	/** The system application. */
	private final SystemApplication sysApp;

	/** The system tray functionality. */
	private Tray sysTray;

	/** Flag indicating the UpdateConfiguration dialog has been requested. */
    private Boolean updateConfigurationRequested = Boolean.FALSE;

	SystemApplicationImpl(final SystemApplication sysApp) {
		super("TPS-OpheliaUI-SystemApplication");
		this.sysApp = sysApp;
        this.notificationQueue = new LinkedList<Notification>();
	}

    /**
	 * @see java.lang.Thread#run()
	 * 
	 */
	public void run() {
		while(running) {
			try {
                synchronized(this) {
                    wait();
                }
			} catch(final InterruptedException ix) {}
            // do not try to process the queue if after waking up
            // we are no longer running
            if (running) {
    			try {
                    processUpdateConfiguration();
                    processDisplayInfo();
                    processNotificationQueue();
    			} catch (final RuntimeException rx) {
    				throw sysApp.translateError(rx);
    			}
            }
		}
	}

	/**
	 * @see java.lang.Thread#start()
	 * 
	 */
	public synchronized void start() {
		running = Boolean.TRUE;

        try {
    		sysTray = new Tray(sysApp, sysApp.getProfile());
            sysTray.install();
        } catch (final AWTException awtx) {
            running = Boolean.FALSE;
        }

		super.start();
	}

    /**
     * Display the "display info" dialog.
     */
    void displayInfo() {
        synchronized (this) {
            displayInfoRequested = Boolean.TRUE;
            notifyAll();
        }
    }

    /**
     * Display the update configuration dialog.
     * 
     * @param configuration
     *            A <code>ProxyConfiguration</code>.
     */
    void displayUpdateConfigurationDialog(final ProxyConfiguration configuration) {
        synchronized (this) {
            this.configuration = configuration;
            updateConfigurationRequested = Boolean.TRUE;
            notifyAll();
        }
    }

	/**
	 * End the application.
	 */
	void end() {
		running = Boolean.FALSE;

        sysTray.unInstall();
        sysTray = null;

        if (NotifyFrame.isDisplayed()) {
            NotifyFrame.close();
        }
        if (DisplayInfoFrame.isDisplayed()) {
            DisplayInfoFrame.close();
        }
        if (UpdateConfigurationFrame.isDisplayed()) {
            UpdateConfigurationFrame.close();
        }

        synchronized (this) {
            notifyAll();
        }
	}

    /**
     * Fire a clear notification event.
     * 
     * @param notificationId
     *            A notification id or group id <code>String</code>.
     */
    void fireClearNotifications(final String notificationId) {
        synchronized (this) {
            // remove displayed notifications that match the id
            NotifyFrame.close(notificationId);
        }
    }

	/**
     * Fire a connection offline event.
     * 
     */
    void fireConnectionOffline() {
        synchronized (this) {
            sysTray.fireConnectionOffline();
            NotifyFrame.fireConnectionOffline();
        }
    }

	/**
     * Fire a connection online event.
     * 
     */
    void fireConnectionOnline() {
        synchronized (this) {
            sysTray.fireConnectionOnline();
            NotifyFrame.fireConnectionOnline();
        }
    }

    /**
     * Notification received.
	 * 
	 * @param notification
     *            A <code>Notification</code>.
	 */
	void fireNotification(final Notification notification) {
		synchronized (this) {
            notificationQueue.add(notification);
			notifyAll();
		}
	}

    String getString(final String localKey) {
		return sysApp.getString(localKey);
	}

    String getString(final String localKey, final Object[] arguments) {
		return sysApp.getString(localKey, arguments);
	}

    /**
	 * Reset the queue items.
	 */
	void resetQueue() {
		synchronized(this) {
            notificationQueue.clear();
			notifyAll();
		}
	}

    /**
     * Set the enabled state of quit.
     * 
     * @param enabled
     *            A <code>Boolean</code>.
     */
    void setQuitEnabled(final Boolean enabled) {
        sysTray.setQuitEnabled(enabled);
    }

	/**
     * Obtain the total number of queued notification events.
     * 
     * @return The total number of queued notification events.
     */
    private Integer getNotificationQueueTotal() { return notificationQueue.size(); }

    /**
     * Process a request for the display info dialog.
     */
    private void processDisplayInfo() {
        if (displayInfoRequested) {
            synchronized (this) {
                DisplayInfoFrame.display(sysApp);
                displayInfoRequested = Boolean.FALSE;
            }
        }
    }

    /**
     * Process pending notification queue events.
     */
    private void processNotificationQueue() {
        if (0 < getNotificationQueueTotal()) {
            synchronized (this) {
                Notification notification;
                for (final Iterator<Notification> i = notificationQueue.iterator();
                        i.hasNext();) {
                    notification = i.next();
                    NotifyFrame.display(notification, sysApp);
                    i.remove();
                }
            }
        }
    }

	/**
     * Process a request for the update configuration dialog.
     */
    private void processUpdateConfiguration() {
        if (updateConfigurationRequested) {
            synchronized (this) {
                UpdateConfigurationFrame.display(configuration);
                updateConfigurationRequested = Boolean.FALSE;
            }
        }
    }
}
