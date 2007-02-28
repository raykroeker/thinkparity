/*
 * Created on July 29, 2006, 8:58 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.border.TopBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.Constants.Colors.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer.ResizeEdges;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.MainStatusAvatarLinks;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.LinkAction;
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

    /** The links */
    private final MainStatusAvatarLinks links;

    /** The resize offset size in the x direction. */
    private int resizeOffsetX;    

    /** The resize offset size in the y direction. */
    private int resizeOffsetY;

    /** A thinkParity user's profile. */
    private Profile profile;

    /** Creates new form MainStatusAvatar */
    MainStatusAvatar() {
        super(AvatarId.MAIN_STATUS.toString());
        initComponents();
        installResizer();
        installAncestorWindowStateListener();
        this.links = new MainStatusAvatarLinks(
                new javax.swing.JLabel[] {linkIntroJLabel, link2IntroJLabel},
                new javax.swing.JLabel[] {linkJLabel, link2JLabel} );
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
            reloadCustom();
            reloadLinkActions();  
            reloadUser();
            reloadConnection();
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
            if (!getController().isBrowserWindowMaximized()) {
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
     * Obtain the input link action.
     * 
     * @return The input link action.
     */
    private LinkAction getInputLinkAction() {
        if (null == input) {
            return null;
        } else {
            return (LinkAction) ((Data) input).get(DataKey.LINK_ACTION);
        }
    }
    
    /**
     * Obtain the input profile.
     * 
     * @return The input profile.
     */
    private Profile getInputProfile() {
        if (null == input) {
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
        javax.swing.JLabel fillJLabel;

        fillJLabel = new javax.swing.JLabel();

        setBorder(new TopBorder(Colors.Browser.MainStatus.TOP_BORDER));
        customJLabel.setFont(Fonts.DefaultFont);

        linkJLabel.setFont(Fonts.DefaultFont);
        linkJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                linkJLabelMousePressed(evt);
            }
        });

        link2JLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                link2JLabelMousePressed(evt);
            }
        });

        userJLabel.setFont(Fonts.DefaultFont);

        connectionJLabel.setFont(Fonts.DefaultFont);
        connectionJLabel.setForeground(Colors.Browser.MainStatus.CONNECTION_FOREGROUND_OFFLINE);

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
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(customJLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(linkIntroJLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(linkJLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(link2IntroJLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(link2JLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(fillJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 280, Short.MAX_VALUE))
                            .add(layout.createSequentialGroup()
                                .add(userJLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                        .add(connectionJLabel)
                        .add(22, 22, 22))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, resizeJLabel)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(connectionJLabel)
                        .add(userJLabel))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(customJLabel)
                        .add(linkJLabel)
                        .add(fillJLabel)
                        .add(linkIntroJLabel)
                        .add(link2IntroJLabel)
                        .add(link2JLabel)))
                .add(57, 57, 57))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .add(resizeJLabel))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Install a window state listener on the ancestor of the component.
     * If the ancestor window is maximized then the resize control will be disabled.
     */
    private void installAncestorWindowStateListener() {
        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(final AncestorEvent event) {
                final Window window = SwingUtilities.getWindowAncestor(MainStatusAvatar.this);
                if (null != window) {
                    installWindowStateListener(window);
                    resizeJLabel.setVisible(!SwingUtil.isInMaximizedWindow(MainStatusAvatar.this));
                }
            }
            public void ancestorMoved(final AncestorEvent event) {}
            public void ancestorRemoved(final AncestorEvent event) {}
        });
    }

    /**
     * Install a window state listener.
     * If the ancestor window is maximized then the resize control will be disabled.
     * 
     * @param window
     *            The <code>Window</code>.
     */
    private void installWindowStateListener(final Window window) {
        window.addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(final WindowEvent e) {
                if (e.getID() == WindowEvent.WINDOW_STATE_CHANGED) {
                    resizeJLabel.setVisible(!isMaximized(e));
                }
            }
        });
    }

    /**
     * Determine if the window event indicates a maximized JFrame window.
     * 
     * @param e
     *            A <code>WindowEvent</code>.
     */
    private Boolean isMaximized(final WindowEvent e) {
        return (e.getNewState() & JFrame.MAXIMIZED_BOTH) > 0;
    }

    private void link2JLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_link2JLabelMousePressed
        links.linkJLabelMousePressed(e, 1);
    }//GEN-LAST:event_link2JLabelMousePressed

    private void linkJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_linkJLabelMousePressed
        links.linkJLabelMousePressed(e, 0);
    }//GEN-LAST:event_linkJLabelMousePressed

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
            connectionText = getString(Connection.ONLINE);
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
     * Reload the link messages.
     */
    private void reloadLinkActions() {
        links.reload(getInputLinkAction());
    }

    /**
     * Reload the user name.
     */
    private void reloadUser() {
        userJLabel.setText("");
        if (isTestMode() && (null != profile)) {
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
            SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.SE_RESIZE_CURSOR);
        }
    }//GEN-LAST:event_resizeJLabelMouseEntered
    
    private void resizeJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_resizeJLabelMouseExited
        if (!isResizeDragging()) {
            SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel connectionJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel customJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel link2IntroJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel link2JLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel linkIntroJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel linkJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel resizeJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel userJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    public enum DataKey { PROFILE, CONNECTION, CUSTOM_MESSAGE, CUSTOM_MESSAGE_ARGUMENTS, LINK_ACTION }
}
