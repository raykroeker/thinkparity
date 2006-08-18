/*
 * Mar 18, 2006
 */
package com.thinkparity.browser.application.system;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.browser.application.system.tray.Tray;
import com.thinkparity.browser.application.system.tray.TrayNotification;
import com.thinkparity.browser.platform.Platform;

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

	/** Queue of pending new/updated artifacts\versions\system messages. */
	private final List<TrayNotification> queue;

	/** The system application. */
	private final SystemApplication sysApp;

	/** The system tray functionality. */
	private Tray sysTray;

	SystemApplicationImpl(final SystemApplication sysApp) {
		super("[BROWSER2] [APP] [SYS] [THREAD]");
		this.sysApp = sysApp;
        this.queue = new LinkedList<TrayNotification>();
	}

	/**
	 * @see java.lang.Thread#run()
	 * 
	 */
	public void run() {
		while(running) {
			sysApp.logApiId();
			try { synchronized(this) { wait(); } }
			catch(final InterruptedException ix) {
				sysApp.logApiId("INTERRUPTED");
			}
            // do not try to process the queue if after waking up
            // we are no longer running
            if(running) {
    			try { processQueue(); }
    			catch(final RuntimeException rx) {
    				sysApp.translateError(rx);
    				throw rx;
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

		sysTray = new Tray(sysApp, sysApp.getProfile());
        sysTray.install();

		super.start();
	}

	/**
	 * End the application.
	 *
	 */
	void end() {
		running = Boolean.FALSE;

        sysTray.unInstall();
        sysTray = null;

        synchronized(this) { notifyAll(); }
	}

	/**
	 * Notification that an artifact has been received.
	 * 
	 * @param artifact
	 *            The artifact.
	 */
	void fireNotification(final TrayNotification notification) {
		synchronized(this) {
			queue.add(notification);
			notifyAll();
		}
	}

	String getString(final String localKey) {
		return sysApp.getString(localKey);
	}

	String getString(final String localKey, final Object[] arguments) {
		return sysApp.getString(localKey, arguments);
	}

	/** Set the menu for the system tray. */
    void reloadConnectionStatus(final Platform.Connection cx) {
        synchronized(this) { sysTray.reloadConnection(cx); }
    }

    /**
	 * Reset the number of queue items.
	 *
	 */
	void resetQueue() {
		synchronized(this) {
			queue.clear();
			notifyAll();
		}
	}

	/**
	 * Obtain the total number of queued events.
	 * 
	 * @return The total number of queued artifacts and artifact versions.
	 */
	private Integer getQueueTotal() { return queue.size(); }

	/**
	 * Process pending queue events.
	 *
	 */
	private void processQueue() {
        sysApp.logApiId(MessageFormat.format("{0}", getQueueTotal()));
        if(0 < getQueueTotal()) {
            TrayNotification notification;
            synchronized(this) {
                for(final Iterator<TrayNotification> i = queue.iterator(); i.hasNext();) {
                    notification = i.next();
                    sysTray.display(notification);
                    i.remove();
                }
            }
        }
	}
}
