/*
 * Created On: Aug 22, 2006 2:22:46 PM
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.thinkparity.codebase.swing.GradientPainter;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.profile.Update;
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
    
    /** The Resizer */
    @SuppressWarnings("unused")
    private final Resizer resizer;
    
    /** Close label icon. */
    private static final Icon CLOSE_ICON;
    
    /** Close label rollover icon. */
    private static final Icon CLOSE_ROLLOVER_ICON;

    /** Min label icon. */
    private static final Icon MIN_ICON;
    
    /** Min label rollover icon. */
    private static final Icon MIN_ROLLOVER_ICON;
    
    static {
        CLOSE_ICON = ImageIOUtil.readIcon("BrowserTitle_Close.png");
        CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_CloseRollover.png");

        MIN_ICON = ImageIOUtil.readIcon("BrowserTitle_Minimize.png");
        MIN_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_MinimizeRollover.png");
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
    public BrowserMenuBar(final Browser browser) {
        super();
        this.browser = browser;
        this.localization = new JFrameLocalization("BrowserWindow.Menu");        
        this.popupItemFactory = PopupItemFactory.getInstance();
        this.resizer = new Resizer(browser, this, Boolean.TRUE, Resizer.ResizeEdges.TOP);

        // Create JMenus
        final JMenu newMenu = MenuFactory.create(localization.getString("New"),
                new Integer(localization.getString("NewMnemonic").charAt(0)));
        this.add(Box.createRigidArea(new Dimension(6,0)));   
        this.add(newMenu);
        final JMenu profileMenu = MenuFactory.create(localization.getString("Profile"),
                new Integer(localization.getString("ProfileMnemonic").charAt(0)));
        this.add(Box.createRigidArea(new Dimension(2,0)));  
        this.add(profileMenu);
        final JMenu helpMenu = MenuFactory.create(localization.getString("Help"),
                new Integer(localization.getString("HelpMnemonic").charAt(0)));
        this.add(Box.createRigidArea(new Dimension(2,0)));  
        this.add(helpMenu);

        // Create the New popup menu
        newMenu.add(popupItemFactory.createMenuPopupItem(ActionId.CONTAINER_CREATE, Data.emptyData()));
        newMenu.add(popupItemFactory.createMenuPopupItem(ActionId.CONTACT_CREATE_INCOMING_INVITATION, Data.emptyData()));

        // Create the Profile menu
        final Data updateProfileData = new Data(1);
        updateProfileData.set(Update.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
        profileMenu.add(popupItemFactory.createMenuPopupItem(ActionId.PROFILE_UPDATE, updateProfileData));
        
        // Create the OpenHelp menu
        helpMenu.add(popupItemFactory.createMenuPopupItem(ActionId.PLATFORM_BROWSER_OPEN_HELP, Data.emptyData()));
        helpMenu.add(popupItemFactory.createMenuPopupItem(ActionId.PLATFORM_BROWSER_DISPLAY_INFO, Data.emptyData()));
        
        // Create the Sign-Up button
        this.add(Box.createHorizontalGlue());
        this.add(ButtonFactory.create(ActionId.PROFILE_SIGN_UP, Data.emptyData()));
        this.add(Box.createRigidArea(new Dimension(5,0)));

        // Add minimize and close buttons

        this.add(getMinimizeButton());
        this.add(Box.createRigidArea(new Dimension(3,0)));
        this.add(getCloseButton());
        this.add(Box.createRigidArea(new Dimension(3,0)));
    }
    
    private JLabel getMinimizeButton() {
        javax.swing.JLabel minimizeJLabel = new JLabel(ImageIOUtil.readIcon("BrowserTitle_Minimize.png"));
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
    
    private JLabel getCloseButton() {
        javax.swing.JLabel closeJLabel = new JLabel(ImageIOUtil.readIcon("BrowserTitle_Close.png"));
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
    
    private void minimizeJLabelMouseClicked(java.awt.event.MouseEvent evt) {
        browser.minimize();
        minimizeJLabelMouseExited(evt);
    }

    private void minimizeJLabelMouseEntered(java.awt.event.MouseEvent evt) {
        ((JLabel) evt.getSource()).setIcon(MIN_ROLLOVER_ICON);
    }

    private void minimizeJLabelMouseExited(java.awt.event.MouseEvent evt) {
        ((JLabel) evt.getSource()).setIcon(MIN_ICON);
    }
}
