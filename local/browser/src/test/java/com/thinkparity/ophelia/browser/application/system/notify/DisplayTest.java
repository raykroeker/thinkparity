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
            NotifyFrame.testDisplay(notification);
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
        notifications[0] = new Notification() {
            public String getMessage() {
                return "A new something or other has arrived from elsewhere.";
            }
            public void invokeAction() {}
        };
        notifications[1] = new Notification() {
            public String getMessage() {
                return "A second new something or other has arrived from elsewhere.";
            }
            public void invokeAction() {}
        };
        notifications[2] = new Notification() {
            public String getMessage() {
                return "A third new something or other has arrived from even farther away.";
            }
            public void invokeAction() {}
        };
        notifications[3] = new Notification() {
            public String getMessage() {
                return "This is just getting boring.";
            }
            public void invokeAction() {}
        };
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
