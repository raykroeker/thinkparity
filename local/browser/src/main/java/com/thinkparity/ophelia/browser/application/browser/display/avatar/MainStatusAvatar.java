/*
 * Created on July 29, 2006, 8:58 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Stack;

import com.thinkparity.codebase.assertion.Assert;
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

        connectionJLabel.setForeground(Colors.Browser.MainStatus.CONNECTION_FOREGROUND);
        connectionJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                connectionJLabelMouseClicked(evt);
            }
        });

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

    private void connectionJLabelMouseClicked(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_connectionJLabelMouseClicked
    }//GEN-LAST:event_connectionJLabelMouseClicked

    /**
     * Reload the connection status message.
     * 
     */
    private void reloadConnection() {
        connectionJLabel.setText("");
        final Connection connection = getInputConnection();
        if (null != connection) {
            final String connectionText;
            switch(getInputConnection()) {
            case ONLINE:
                connectionText = getString(Connection.ONLINE);
                break;
            case OFFLINE:
                connectionText = getString(Connection.OFFLINE);
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN CONNECTION");
            }
            connectionJLabel.setText(connectionText);
        }
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
            getController().setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
        }
    }//GEN-LAST:event_resizeJLabelMouseEntered

    private void resizeJLabelMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_resizeJLabelMousePressed
        resizeOffsetX = e.getPoint().x;
        resizeOffsetY = e.getPoint().y;
        setResizeDragging(Boolean.TRUE);
    }//GEN-LAST:event_resizeJLabelMousePressed
    
    private void resizeJLabelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resizeJLabelMouseReleased
        setResizeDragging(Boolean.FALSE);
    }//GEN-LAST:event_resizeJLabelMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel connectionJLabel;
    private javax.swing.JLabel customJLabel;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { CONNECTION, CUSTOM_MESSAGE, CUSTOM_MESSAGE_ARGUMENTS }
}
