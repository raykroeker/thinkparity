/*
 * Created On:  22-Nov-06 4:07:49 PM
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import com.thinkparity.ophelia.browser.util.Swing;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DisplayStub {

    static {
        Swing.init();
    }

    public static void main(final String[] args) {
        final DisplayStub stub = new DisplayStub();
        try {
            stub.setUp();
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
            System.exit(1);
        }
        try {
            stub.display();
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }
        try {
            stub.tearDown();
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }

    }

    private Fixture datum;

    /**
     * Create DisplayStub.
     *
     * @param name
     */
    public DisplayStub() {
        super();
    }

    public void display() {
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

    protected void setUp() {
        final Notification[] notifications = new Notification[4];
        notifications[0] = new Notification() {
            public String getMessage() {
                return "A new something or other has arrived from elsewhere.";
            }
            public String getTitle() {
                return "New Something";
            }
            public void invokeAction() {}
        };
        notifications[1] = new Notification() {
            public String getMessage() {
                return "A second new something or other has arrived from elsewhere.";
            }
            public String getTitle() {
                return "Second New Something";
            }
            public void invokeAction() {}
        };
        notifications[2] = new Notification() {
            public String getMessage() {
                return "A third new something or other has arrived from even farther away.";
            }
            public String getTitle() {
                return "Third New Something";
            }
            public void invokeAction() {}
        };
        notifications[3] = new Notification() {
            public String getMessage() {
                return "This is just getting boring.";
            }
            public String getTitle() {
                return "Boring";
            }
            public void invokeAction() {}
        };
        datum = new Fixture(notifications);
    }

    protected void tearDown() {
    }

    private static final class Fixture {
        private final Notification[] notifications;
        private Fixture(final Notification[] notifications) {
            this.notifications = notifications;
        }
    }
}
