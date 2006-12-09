/*
 * Created On: Aug 22, 2006 2:22:46 PM
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.profile.Edit;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;
import com.thinkparity.ophelia.browser.util.localization.JFrameLocalization;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class BrowserMenuBar extends JMenuBar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** The browser application. */
    private final Browser browser;
    
    /** Resource bundle based localization. */
    private final JFrameLocalization localization;
    
    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;
    
    /** Close label icon. */
    private static final Icon CLOSE_ICON;
    
    /** Close label rollover icon. */
    private static final Icon CLOSE_ROLLOVER_ICON;

    /** Minimize label icon. */
    private static final Icon MINIMIZE_ICON;
    
    /** Minimize label rollover icon. */
    private static final Icon MINIMIZE_ROLLOVER_ICON;
    
    /** Maximize label icon. */
    private static final Icon MAXIMIZE_ICON;
    
    /** Maximize label rollover icon. */
    private static final Icon MAXIMIZE_ROLLOVER_ICON;
    
    /** Un-Maximize label icon. */
    private static final Icon UNMAXIMIZE_ICON;
    
    /** Un-Maximize label rollover icon. */
    private static final Icon UNMAXIMIZE_ROLLOVER_ICON;
    
    /** Sign-Up label icon. */
    private static final Icon SIGNUP_ICON;
    
    /** Sign-Up label rollover icon. */
    private static final Icon SIGNUP_ROLLOVER_ICON;
    
    
    static {
        CLOSE_ICON = ImageIOUtil.readIcon("BrowserTitle_Close.png");
        CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_CloseRollover.png");

        MINIMIZE_ICON = ImageIOUtil.readIcon("BrowserTitle_Minimize.png");
        MINIMIZE_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_MinimizeRollover.png");
        
        MAXIMIZE_ICON = ImageIOUtil.readIcon("BrowserTitle_Maximize.png");
        MAXIMIZE_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_MaximizeRollover.png");
        
        UNMAXIMIZE_ICON = ImageIOUtil.readIcon("BrowserTitle_UnMaximize.png");
        UNMAXIMIZE_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_UnMaximizeRollover.png");
        
        SIGNUP_ICON = ImageIOUtil.readIcon("BrowserTitle_SignUp.png");
        SIGNUP_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_SignUpRollover.png");
    }

    /**
     * @see javax.swing.JMenuBar#paintBorder(java.awt.Graphics)
     */
    @Override
    protected void paintBorder(Graphics g) {
        // Prevent the drawing of the border.
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * 
     */
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g.create();
        try {
            GradientPainter.paintVertical(g2, getSize(),
                    com.thinkparity.ophelia.browser.Constants.Colors.Browser.MainTitleTop.BG_GRAD_START,
                    com.thinkparity.ophelia.browser.Constants.Colors.Browser.MainTitleTop.BG_GRAD_FINISH);
            
            // These images help to make the rounded corner look good.
            g2.drawImage(Images.BrowserTitle.BROWSER_TOP_LEFT_INNER,
                    0,
                    0,
                    Images.BrowserTitle.BROWSER_TOP_LEFT_INNER.getWidth(),
                    Images.BrowserTitle.BROWSER_TOP_LEFT_INNER.getHeight(), this);
            g2.drawImage(Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER,
                    getSize().width - Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER.getWidth(),
                    0,
                    Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER.getWidth(),
                    Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER.getHeight(), this);
        }
        finally { g2.dispose(); }
    }

    /**
     * Create a Browser menu bar.
     * @param browser
     *          The Browser.
     */
    public BrowserMenuBar(final Browser browser, final Boolean maximized) {
        super();
        this.browser = browser;
        this.localization = new JFrameLocalization("BrowserWindow.Menu");   
        this.popupItemFactory = PopupItemFactory.getInstance();
        new Resizer(browser, this, Boolean.FALSE, Resizer.ResizeEdges.TOP);
        
        // Double click to maximize the browser window
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON1) {
                    if (e.getClickCount() % 2 == 0) {
                        browser.maximize();
                    }
                }
            }
        });

        // Create JMenus
        final JMenu newMenu = MenuFactory.create(localization.getString("New"), localization.getString("NewMnemonic"));
        this.add(Box.createRigidArea(new Dimension(6,0))); 
        this.add(newMenu);        
        final JMenu profileMenu = MenuFactory.create(localization.getString("Profile"), localization.getString("ProfileMnemonic"));
        this.add(Box.createRigidArea(new Dimension(2,0)));  
        this.add(profileMenu);
        final JMenu helpMenu = MenuFactory.create(localization.getString("Help"), localization.getString("HelpMnemonic"));
        this.add(Box.createRigidArea(new Dimension(2,0)));  
        this.add(helpMenu);

        // Create the New popup menu
        newMenu.add(popupItemFactory.createMenuPopupItem(ActionId.CONTAINER_CREATE, Data.emptyData()));
        newMenu.add(popupItemFactory.createMenuPopupItem(ActionId.CONTACT_CREATE_INCOMING_INVITATION, Data.emptyData()));

        // Create the Profile menu
/*        final Data updateProfileData = new Data(1);
        updateProfileData.set(Update.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
        profileMenu.add(popupItemFactory.createMenuPopupItem(ActionId.PROFILE_UPDATE, updateProfileData));*/

        final Data editProfileData = new Data(1);
        editProfileData.set(Edit.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
        profileMenu.add(popupItemFactory.createMenuPopupItem(ActionId.PROFILE_EDIT, editProfileData));
        
        // Create the OpenHelp menu
        helpMenu.add(popupItemFactory.createMenuPopupItem(ActionId.PLATFORM_BROWSER_OPEN_HELP, Data.emptyData()));
        helpMenu.add(popupItemFactory.createMenuPopupItem(ActionId.PLATFORM_BROWSER_DISPLAY_INFO, Data.emptyData()));
        
        // Create the Sign-Up button
        // TODO Add this button back when the user is a guest
        this.add(Box.createHorizontalGlue());
/*        this.add(getSignUpButton());
        this.add(Box.createRigidArea(new Dimension(2,0)));*/
        
        // Add minimize, maximize and close buttons
        this.add(getMinimizeButton());
        this.add(Box.createRigidArea(new Dimension(2,0)));
        this.add(getMaximizeButton(maximized));
        this.add(Box.createRigidArea(new Dimension(2,0)));
        this.add(getCloseButton());
        this.add(Box.createRigidArea(new Dimension(4,0)));
    }
    
    private JLabel getSignUpButton() {
        final javax.swing.JLabel signUpJLabel = new JLabel(SIGNUP_ICON);
        signUpJLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), Cursor.HAND_CURSOR);
                ((JLabel) e.getSource()).setIcon(SIGNUP_ROLLOVER_ICON);
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), Cursor.DEFAULT_CURSOR);
                ((JLabel) e.getSource()).setIcon(SIGNUP_ICON);
            }
            @Override
            public void mouseClicked(final MouseEvent e) {
                browser.runProfileSignUp();                
            }            
        });
        return signUpJLabel;
    }
    
    private JLabel getMinimizeButton() {
        javax.swing.JLabel minimizeJLabel = new JLabel(MINIMIZE_ICON);
        minimizeJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimizeJLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizeJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizeJLabelMouseExited(evt);
            }
        });
        
        return minimizeJLabel;        
    }
    
    private JLabel getMaximizeButton(final Boolean maximized) {
        javax.swing.JLabel maximizeJLabel = new JLabel();
        if (maximized) {
            maximizeJLabel.setIcon(UNMAXIMIZE_ICON);
        } else {
            maximizeJLabel.setIcon(MAXIMIZE_ICON);
        }
        maximizeJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                maximizeJLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                maximizeJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                maximizeJLabelMouseExited(evt);
            }
        });
        
        return maximizeJLabel;        
    }
    
    private JLabel getCloseButton() {
        javax.swing.JLabel closeJLabel = new JLabel(CLOSE_ICON);
        closeJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeJLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeJLabelMouseExited(evt);
            }
        });
        
        return closeJLabel;        
    }
    
    private void minimizeJLabelMouseClicked(java.awt.event.MouseEvent evt) {
        browser.minimize();
        minimizeJLabelMouseExited(evt);
    }

    private void minimizeJLabelMouseEntered(java.awt.event.MouseEvent evt) {
        ((JLabel) evt.getSource()).setIcon(MINIMIZE_ROLLOVER_ICON);
    }

    private void minimizeJLabelMouseExited(java.awt.event.MouseEvent evt) {
        ((JLabel) evt.getSource()).setIcon(MINIMIZE_ICON);
    }
    
    private void maximizeJLabelMouseClicked(java.awt.event.MouseEvent evt) {
        browser.maximize();
        maximizeJLabelMouseExited(evt);
    }

    private void maximizeJLabelMouseEntered(java.awt.event.MouseEvent evt) {
        if (browser.isBrowserWindowMaximized()) {
            ((JLabel) evt.getSource()).setIcon(UNMAXIMIZE_ROLLOVER_ICON);
        } else {
            ((JLabel) evt.getSource()).setIcon(MAXIMIZE_ROLLOVER_ICON);
        }
    }

    private void maximizeJLabelMouseExited(java.awt.event.MouseEvent evt) {
        if (browser.isBrowserWindowMaximized()) {
            ((JLabel) evt.getSource()).setIcon(UNMAXIMIZE_ICON);
        } else {
            ((JLabel) evt.getSource()).setIcon(MAXIMIZE_ICON);
        }
    }
       
    private void closeJLabelMouseClicked(java.awt.event.MouseEvent evt) {
        browser.closeBrowserWindow();
        closeJLabelMouseExited(evt);
    }

    private void closeJLabelMouseEntered(java.awt.event.MouseEvent evt) {
        ((JLabel) evt.getSource()).setIcon(CLOSE_ROLLOVER_ICON);
    }

    private void closeJLabelMouseExited(java.awt.event.MouseEvent evt) {
        ((JLabel) evt.getSource()).setIcon(CLOSE_ICON);
    }
}
