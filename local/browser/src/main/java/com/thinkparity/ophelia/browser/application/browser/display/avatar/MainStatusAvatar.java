/*
 * Created on July 29, 2006, 8:58 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.border.TopBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.Constants.Colors.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer.ResizeEdges;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity Browser Status<br>
 * <b>Description:</b>Displays the status bar at the bottom of the browser
 * window.
 * 
 * @author raymond@thinkparity.com
 */
public class MainStatusAvatar extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** The resize offset size in the x direction. */
    private int resizeOffsetX;    
    
    /** The resize offset size in the y direction. */
    private int resizeOffsetY;
    
    /** A thinkParity user's profile. */
    private Profile profile;

    /** Creates new form BrowserStatus */
    MainStatusAvatar() {
        super(AvatarId.MAIN_STATUS.toString());
        initComponents();
        installResizer();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     */
    @Override
    public AvatarId getId() { return AvatarId.MAIN_STATUS; }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()
     */
    @Override
    public State getState() { return null; }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getResizeEdges()
     */
    @Override
    protected ResizeEdges getResizeEdges() {
        return Resizer.ResizeEdges.BOTTOM;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isSupportMouseMove()
     */
    @Override
    protected Boolean isSupportMouseMove() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        this.profile = getInputProfile();
        reloadCustom();
        reloadConnection();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     */
    @Override
    public void setState(final State state) {}

    /**
     * Obtain a localized string for the connection.
     * 
     * @param connection
     *            A platform connection.
     * @return A localized string.
     */
    protected String getString(final Connection connection) {
        return getString(connection.toString());
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics g2 = g.create();
        try {
            GradientPainter.paintVertical(g2, getSize(),
                    Browser.MainStatus.BG_GRAD_START,
                    Browser.MainStatus.BG_GRAD_FINISH);
            
            // These images help to make the rounded corner look good.
            g2.drawImage(Images.BrowserTitle.BROWSER_BOTTOM_LEFT_INNER,
                    0,
                    getSize().height - Images.BrowserTitle.BROWSER_BOTTOM_LEFT_INNER.getHeight(),
                    Images.BrowserTitle.BROWSER_BOTTOM_LEFT_INNER.getWidth(),
                    Images.BrowserTitle.BROWSER_BOTTOM_LEFT_INNER.getHeight(), this);
            g2.drawImage(Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_INNER,
                    getSize().width - Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_INNER.getWidth(),
                    getSize().height - Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_INNER.getHeight(),
                    Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_INNER.getWidth(),
                    Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_INNER.getHeight(), this);
        }
        finally { g2.dispose(); }
    }

    /**
     * Obtain the input connection.
     * 
     * @return A platform connection.
     */
    private Connection getInputConnection() {
        if (null == input) {
            return null;
        } else {
            return (Connection) ((Data) input).get(DataKey.CONNECTION);
        }
    }

    /**
     * Obtain the input custom message.
     * 
     * @return A string.
     */
    private String getInputCustomMessage() {
        if (null == input) {
            return null;
        } else {
            return (String) ((Data) input).get(DataKey.CUSTOM_MESSAGE);
        }
    }

    /**
     * Obtain the custom message localization arguments.
     * 
     * @return An object array.
     */
    private Object[] getInputCustomMessageArguments() {
        if (null == input) {
            return null;
        } else {
            return (Object[]) ((Data) input).get(DataKey.CUSTOM_MESSAGE_ARGUMENTS);
        }
    }
    
    /**
     * Obtain the input profile.
     * 
     * @return The input profile.
     */
    private Profile getInputProfile() {
        if(null == input) {
            return null;
        } else {
            return (Profile) ((Data) input).get(DataKey.PROFILE);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JLabel resizeJLabel;

        customJLabel = new javax.swing.JLabel();
        connectionJLabel = new javax.swing.JLabel();
        resizeJLabel = new javax.swing.JLabel();

        setBorder(new TopBorder(Colors.Browser.MainStatus.TOP_BORDER));

        connectionJLabel.setForeground(Colors.Browser.MainStatus.CONNECTION_FOREGROUND_OFFLINE);

        resizeJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserStatus_Resize.png")));
        resizeJLabel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                resizeJLabelMouseDragged(evt);
            }
        });
        resizeJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resizeJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resizeJLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                resizeJLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                resizeJLabelMouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(customJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 419, Short.MAX_VALUE)
                .add(connectionJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(resizeJLabel))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(connectionJLabel)
                    .add(customJLabel))
                .addContainerGap(57, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .add(resizeJLabel))
        );
    }// </editor-fold>//GEN-END:initComponents


    /**
     * Reload the connection status message.
     * 
     */
    private void reloadConnection() {
        connectionJLabel.setText("");
        final String connectionText;
        switch(getInputConnection()) {
        case ONLINE:
            connectionJLabel.setForeground(Colors.Browser.MainStatus.CONNECTION_FOREGROUND_ONLINE);
            connectionText = profile.getName();
            break;
        case OFFLINE:
            connectionJLabel.setForeground(Colors.Browser.MainStatus.CONNECTION_FOREGROUND_OFFLINE);
            connectionText = getString(Connection.OFFLINE);
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN CONNECTION");
        }
        connectionJLabel.setText(connectionText);
    }

    /**
     * Reload the custom status message.
     *
     */
    private void reloadCustom() {
        customJLabel.setText("");
        final String customMessage = getInputCustomMessage();
        if (null != customMessage) {
            final Object[] customMessageArguments = getInputCustomMessageArguments();
            if (null == customMessageArguments) {
                customJLabel.setText(getString(customMessage));
            } else {
                customJLabel.setText(getString(customMessage, customMessageArguments));
            }
        }
    }

    private void resizeJLabelMouseDragged(java.awt.event.MouseEvent e) {                                          
        getController().resizeBrowserWindow(
                new Dimension(e.getPoint().x - resizeOffsetX,
                        e.getPoint().y - resizeOffsetY));
    }// GEN-LAST:event_resizeJLabelMouseDragged

    private void resizeJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_resizeJLabelMouseEntered
        if (!isResizeDragging()) {
            final Cursor cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
            changeCursor(cursor, e.getComponent());
        }
    }//GEN-LAST:event_resizeJLabelMouseEntered
    
    private void resizeJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_resizeJLabelMouseExited
        if (!isResizeDragging()) {
            final Cursor cursor = null;
            changeCursor(cursor, e.getComponent());
        }
    }//GEN-LAST:event_resizeJLabelMouseExited

    private void resizeJLabelMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_resizeJLabelMousePressed
        resizeOffsetX = e.getPoint().x;
        resizeOffsetY = e.getPoint().y;
        setResizeDragging(Boolean.TRUE);
    }//GEN-LAST:event_resizeJLabelMousePressed
    
    private void resizeJLabelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resizeJLabelMouseReleased
        setResizeDragging(Boolean.FALSE);
    }//GEN-LAST:event_resizeJLabelMouseReleased
    
    private void changeCursor(final Cursor cursor, final Component component) {
        component.setCursor(cursor);
        Window window = SwingUtilities.getWindowAncestor(component);
        if (null!=window) {
            window.setCursor(cursor);
        }
        getController().setCursor(cursor);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel connectionJLabel;
    private javax.swing.JLabel customJLabel;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { PROFILE, CONNECTION, CUSTOM_MESSAGE, CUSTOM_MESSAGE_ARGUMENTS }
}
