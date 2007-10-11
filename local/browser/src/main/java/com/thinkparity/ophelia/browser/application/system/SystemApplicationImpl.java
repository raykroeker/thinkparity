/*
 * Mar 18, 2006
 */
package com.thinkparity.ophelia.browser.application.system;

import java.awt.AWTException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.ophelia.browser.application.system.dialog.DisplayInfoFrame;
import com.thinkparity.ophelia.browser.application.system.dialog.Notification;
import com.thinkparity.ophelia.browser.application.system.dialog.NotifyFrame;
import com.thinkparity.ophelia.browser.application.system.dialog.PriorityNotification;
import com.thinkparity.ophelia.browser.application.system.dialog.PriorityNotifyFrame;
import com.thinkparity.ophelia.browser.application.system.tray.Tray;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class SystemApplicationImpl extends Thread {

	/**
	 * Flag indicating the current running state.
	 * 
	 * @see #run()
	 */
	Boolean running = Boolean.FALSE;

	/** Flag indicating the DisplayInfo dialog has been requested. */
    private Boolean displayInfoRequested = Boolean.FALSE;

    /** Queue of notifications not yet displayed. */
	private final List<Notification> notificationQueue;

	/** Queue of priority notifications not yet displayed. */
    private final List<PriorityNotification> priorityNotificationQueue;

	/** The system application. */
	private final SystemApplication sysApp;

	/** The system tray functionality. */
	private Tray sysTray;

	SystemApplicationImpl(final SystemApplication sysApp) {
		super("TPS-OpheliaUI-SystemApplication");
		this.sysApp = sysApp;
        this.notificationQueue = new LinkedList<Notification>();
        this.priorityNotificationQueue = new LinkedList<PriorityNotification>();
	}

	/**
     * Notification that the priority notify window has been closed.
     * This allows regular notifications to be displayed.
     */
    public void firePriorityNotifyWindowClosed() {
        synchronized (this) {
            notifyAll();
        }
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
                    processDisplayInfo();
                    processPriorityNotificationQueue();
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
	 * End the application.
	 */
	void end() {
		running = Boolean.FALSE;

        sysTray.unInstall();
        sysTray = null;

        if (PriorityNotifyFrame.isDisplayed()) {
            PriorityNotifyFrame.close();
        }
        if (NotifyFrame.isDisplayed()) {
            NotifyFrame.close();
        }
        if (DisplayInfoFrame.isDisplayed()) {
            DisplayInfoFrame.close();
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
            // Remove queued notifications that match the id. It is not
            // necessary to do the same for priority notifications because
            // they aren't in the queue long enough to bother.
            if (0 < getNotificationQueueTotal()) {
                Notification notification;
                for (final Iterator<Notification> i = notificationQueue.iterator();
                        i.hasNext();) {
                    notification = i.next();
                    if (notification.isMatchingId(notificationId)) {
                        i.remove();
                    }
                }
            }
            // remove displayed notifications that match the id
            NotifyFrame.close(notificationId);
            PriorityNotifyFrame.close(notificationId);
        }
    }

	/**
     * Fire a connection offline event.
     * 
     */
    void fireConnectionOffline() {
        synchronized (this) {
            sysTray.fireConnectionOffline();
            PriorityNotifyFrame.fireConnectionOffline();
        }
    }

    /**
     * Fire a connection online event.
     * 
     */
    void fireConnectionOnline() {
        synchronized (this) {
            sysTray.fireConnectionOnline();
            PriorityNotifyFrame.fireConnectionOnline();
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

    /**
     * Priority notification received.
	 * 
	 * @param notification
     *            A <code>PriorityNotification</code>.
	 */
	void fireNotification(final PriorityNotification notification) {
		synchronized (this) {
            priorityNotificationQueue.add(notification);
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
            priorityNotificationQueue.clear();
            notificationQueue.clear();
			notifyAll();
		}
	}

    /**
	 * Obtain the total number of queued notification events.
	 * 
	 * @return The total number of queued notification events.
	 */
	private Integer getNotificationQueueTotal() { return notificationQueue.size(); }

	/**
     * Obtain the total number of queued priority notification events.
     * 
     * @return The total number of queued priority notification events.
     */
    private Integer getPriorityNotificationQueueTotal() { return priorityNotificationQueue.size(); }

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
     * Notifications are ignored while a priority notification is displayed.
	 */
	private void processNotificationQueue() {
        if (0 < getNotificationQueueTotal() && !PriorityNotifyFrame.isDisplayed()) {
            synchronized (this) {
                Notification notification;
                for (final Iterator<Notification> i = notificationQueue.iterator();
                        i.hasNext();) {
                    notification = i.next();
                    NotifyFrame.display(notification);
                    i.remove();
                }
            }
        }
	}

	/**
     * Process pending priority notification queue events.
     */
    private void processPriorityNotificationQueue() {
        if (0 < getPriorityNotificationQueueTotal()) {
            synchronized (this) {
                PriorityNotification notification;
                for (final Iterator<PriorityNotification> i = priorityNotificationQueue.iterator();
                        i.hasNext();) {
                    notification = i.next();
                    PriorityNotifyFrame.display(notification, sysApp);
                    i.remove();
                }
            }
        }
    }
}
