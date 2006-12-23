/*
 * Created on July 29, 2006, 8:58 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.io.File;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.SwingUtil;
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
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        if (null != input) {
            this.profile = getInputProfile();
            reloadMessage();
            reloadUser();
        }
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
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isAvatarBackgroundImage()
     */
    @Override
    public Boolean isAvatarBackgroundImage() {
        // Default avatar background image is not required for this avatar.
        return Boolean.FALSE;
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
     * Obtain the input file.
     * 
     * @return The input file.
     */
    private File getInputFile() {
        if(null == input) {
            return null;
        } else {
            return (File) ((Data) input).get(DataKey.FILE);
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
        fileJLabel = new javax.swing.JLabel();
        userJLabel = new javax.swing.JLabel();
        resizeJLabel = new javax.swing.JLabel();

        setBorder(new TopBorder(Colors.Browser.MainStatus.TOP_BORDER));
        customJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        fileJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        fileJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fileJLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fileJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fileJLabelMouseExited(evt);
            }
        });

        userJLabel.setForeground(Colors.Browser.MainStatus.CONNECTION_FOREGROUND_OFFLINE);

        resizeJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserStatus_Resize.png")));
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
        resizeJLabel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                resizeJLabelMouseDragged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(customJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(fileJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 413, Short.MAX_VALUE)
                .add(userJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(resizeJLabel))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(userJLabel)
                            .add(customJLabel)
                            .add(fileJLabel))
                        .addContainerGap(57, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, resizeJLabel)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fileJLabelMouseExited(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_fileJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_fileJLabelMouseExited

    private void fileJLabelMouseEntered(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_fileJLabelMouseEntered
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }//GEN-LAST:event_fileJLabelMouseEntered

    private void fileJLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileJLabelMouseClicked
    // TODO open the file.
    }//GEN-LAST:event_fileJLabelMouseClicked

    /**
     * Reload the connection status message.
     * 
     */
    private void reloadConnection() {
        customJLabel.setText("");
        final String connectionText;
        switch(getInputConnection()) {
        case ONLINE:
            customJLabel.setForeground(Colors.Browser.MainStatus.CONNECTION_FOREGROUND_ONLINE);
            connectionText = getString(Connection.ONLINE);
            break;
        case OFFLINE:
            customJLabel.setForeground(Colors.Browser.MainStatus.CONNECTION_FOREGROUND_OFFLINE);
            connectionText = getString(Connection.OFFLINE);
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN CONNECTION");
        }
        customJLabel.setText(connectionText);
    }
    
    /**
     * Reload the custom status message.
     *
     */
    private void reloadCustom() {
        customJLabel.setText("");
        customJLabel.setForeground(Colors.Browser.MainStatus.CUSTOM_MESSAGE_FOREGROUND);
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
    
    /**
     * Reload the file message.
     */
    private void reloadFile() {
        fileJLabel.setText("");
        final File file = getInputFile();
        if (null != file) {
            fileJLabel.setText(file.getAbsolutePath());
        }
    }
    
    /**
     * Reload the message.
     */
    private void reloadMessage() {
        if (Connection.OFFLINE == getInputConnection()) {
            reloadConnection();
        } else {        
            reloadCustom();
            reloadFile();  
        }
    }
    
    /**
     * Reload the user name.
     */
    private void reloadUser() {
        userJLabel.setText("");
        if (isDebugMode() && (null != profile)) {
            userJLabel.setForeground(Colors.Browser.MainStatus.USER_NAME_FOREGROUND);
            userJLabel.setText(profile.getName());
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
    private javax.swing.JLabel customJLabel;
    private javax.swing.JLabel fileJLabel;
    private javax.swing.JLabel userJLabel;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { PROFILE, CONNECTION, CUSTOM_MESSAGE, CUSTOM_MESSAGE_ARGUMENTS, FILE }
}
