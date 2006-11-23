/*
 * Created On:  22-Nov-06 4:07:49 PM
 */
package com.thinkparity.ophelia.browser.application.system.notify;

import com.thinkparity.ophelia.browser.BrowserTestCase;
import com.thinkparity.ophelia.browser.util.Swing;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DisplayTest extends BrowserTestCase {

    static {
        Swing.init();
    }

    private static final String NAME = "Notify Display Test";

    private Fixture datum;

    /**
     * Create DisplayTest.
     *
     * @param name
     */
    public DisplayTest() {
        super(NAME);
    }

    public void testDisplay() {
        for (final Notification notification : datum.notifications) {
            NotifyFrame.display(notification);
            try {
                Thread.sleep(1 * 1000);
            } catch (final InterruptedException ix) {}
        }

        synchronized (this) {
            while (NotifyFrame.isDisplayed()) {
                try {
                    Thread.sleep(1 * 1000);
                } catch (final InterruptedException ix) {
                    ix.printStackTrace(System.err);
                }
            }
        }
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final Notification[] notifications = new Notification[4];
        Notification notification;
        notification = new Notification();
        notification.setMessage("A new something or other has arrived from elsewhere.");
        notifications[0] = notification;
        notification = new Notification();
        notification.setMessage("A second new something or other has arrived from elsewhere.");
        notifications[1] = notification;
        notification = new Notification();
        notification.setMessage("A third new something or other has arrived from even farther away.");
        notifications[2] = notification;
        notification = new Notification();
        notification.setMessage("This is just getting boring.");
        notifications[3] = notification;
        datum = new Fixture(notifications);
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private static final class Fixture {
        private final Notification[] notifications;
        private Fixture(final Notification[] notifications) {
            this.notifications = notifications;
        }
    }
}
