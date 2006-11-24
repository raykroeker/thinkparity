/*
 * NotifyFrame.java
 *
 * Created on November 22, 2006, 4:11 PM
 */

package com.thinkparity.ophelia.browser.application.system.notify;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.thinkparity.codebase.swing.AbstractJDialog;

import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;
import com.thinkparity.ophelia.browser.util.l2fprod.NativeSkin;

/**
 *
 * @author  raymond
 */
public class NotifyFrame extends AbstractJDialog {

    /** The <code>NotifyFrame</code> singleton instance. */
    private static NotifyFrame frame;

    /** The notification frame <code>Persistence</code>. */
    private static Persistence framePersistence;

    /**
     * Display a notification. The notification is added to an internal list and
     * if the notify panel is not already visible it is set to visible.
     * 
     * @param notification The notification to display.
     */
    public static void display(final Notification notification) {
        if (null == frame) {
            framePersistence = PersistenceFactory.getPersistence(NotifyFrame.class);
            frame = new NotifyFrame();
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(final WindowEvent e) {
                    framePersistence.set("location", frame.getLocation());
                }
            });
            frame.setLocation(framePersistence.get("location", new Point(0, 0)));
        }
        synchronized (notification) {
            frame.doDisplay(notification);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (frame.isVisible())
                    frame.toFront();
                else
                    frame.setVisible(true);
            }
        });
    }

    /**
     * Close the notification.
     *
     */
    public static void close() {
        if (isDisplayed()) {
            frame.dispose();
            frame = null;
        }
    }

    /**
     * Determine whether or not the notification frame is being displayed.
     * 
     * @return True if the notification frame is currently being displayed.
     */
    public static Boolean isDisplayed() {
        return null != frame && frame.isVisible();
    }

    /**
     * Create NotifyFrame.
     * 
     */
    private NotifyFrame() {
        super(null, Boolean.FALSE, "");
        initComponents();
        new NativeSkin().roundCorners(this);
    }

    /**
     * Reload the notifications.
     *
     */
    private void doDisplay(final Notification notification) {
        notifyPanel.display(notification);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        notifyPanel = new com.thinkparity.ophelia.browser.application.system.notify.NotifyPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(notifyPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(notifyPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.thinkparity.ophelia.browser.application.system.notify.NotifyPanel notifyPanel;
    // End of variables declaration//GEN-END:variables
}
