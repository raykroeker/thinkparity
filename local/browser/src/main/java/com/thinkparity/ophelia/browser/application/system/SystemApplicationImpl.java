/*
 * Mar 18, 2006
 */
package com.thinkparity.ophelia.browser.application.system;

import java.awt.AWTException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.ophelia.browser.application.system.dialog.DisplayInfoFrame;
import com.thinkparity.ophelia.browser.application.system.dialog.Invitation;
import com.thinkparity.ophelia.browser.application.system.dialog.InvitationFrame;
import com.thinkparity.ophelia.browser.application.system.dialog.Notification;
import com.thinkparity.ophelia.browser.application.system.dialog.NotifyFrame;
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

    /** Queue of invitations not yet displayed. */
    private final List<Invitation> invitationQueue;

	/** Queue of notifications not yet displayed. */
	private final List<Notification> notificationQueue;

	/** The system application. */
	private final SystemApplication sysApp;

	/** The system tray functionality. */
	private Tray sysTray;

	SystemApplicationImpl(final SystemApplication sysApp) {
		super("TPS-OpheliaUI-SystemApplication");
		this.sysApp = sysApp;
        this.invitationQueue = new LinkedList<Invitation>();
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
                    processDisplayInfo();
                    processInvitationQueue();
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

        if (InvitationFrame.isDisplayed()) {
            InvitationFrame.close();
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
     * Fire a clear invitation event.
     * 
     * @param invitationId
     *            An invitation id <code>String</code>.
     */
    void fireClearInvitations(final String invitationId) {
        synchronized (this) {
            InvitationFrame.close(invitationId);
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
            // remove queued notifications that match the id
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
        }
    }

	/**
     * Fire a connection offline event.
     * 
     */
    void fireConnectionOffline() {
        synchronized (this) {
            sysTray.fireConnectionOffline();
            InvitationFrame.fireConnectionOffline();
        }
    }

	/**
     * Fire a connection online event.
     * 
     */
    void fireConnectionOnline() {
        synchronized (this) {
            sysTray.fireConnectionOnline();
            InvitationFrame.fireConnectionOnline();
        }
    }

    /**
	 * Notification that an invitation has been received.
	 * 
	 * @param invitation
     *            An <code>Invitation</code>.
	 */
	void fireInvitationReceived(final Invitation invitation) {
		synchronized (this) {
            invitationQueue.add(invitation);
			notifyAll();
		}
	}

    /**
     * Notification that the invitation window has been closed.
     */
    public void fireInvitationWindowClosed() {
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * Notification that an artifact has been received.
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
            invitationQueue.clear();
            notificationQueue.clear();
			notifyAll();
		}
	}

    /**
     * Obtain the total number of queued invitation events.
     * 
     * @return The total number of queued invitation events.
     */
    private Integer getInvitationQueueTotal() { return invitationQueue.size(); }

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
     * Process pending invitation queue events.
     */
    private void processInvitationQueue() {
        if (0 < getInvitationQueueTotal()) {
            synchronized (this) {
                Invitation invitation;
                for (final Iterator<Invitation> i = invitationQueue.iterator();
                        i.hasNext();) {
                    invitation = i.next();
                    InvitationFrame.display(invitation, sysApp);
                    i.remove();
                }
            }
        }
    }

	/**
	 * Process pending notification queue events.
     * Notifications are ignored while an invitation is displayed.
	 */
	private void processNotificationQueue() {
        if (0 < getNotificationQueueTotal() && !InvitationFrame.isDisplayed()) {
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
}
