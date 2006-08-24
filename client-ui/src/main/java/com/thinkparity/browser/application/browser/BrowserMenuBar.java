/*
 * Created On: Aug 22, 2006 2:22:46 PM
 */
package com.thinkparity.browser.application.browser;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.thinkparity.browser.Constants.Colors.Browser;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.platform.util.l10n.JFrameLocalization;

import com.thinkparity.codebase.swing.GradientPainter;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class BrowserMenuBar extends JMenuBar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** Resource bundle based localization. */
    private final JFrameLocalization localization;
    
    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;

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
                    Browser.MainTitleTop.BG_GRAD_START,
                    Browser.MainTitleTop.BG_GRAD_FINISH);
        }
        finally { g2.dispose(); }
    }

    /**
     * Create a BrowserMenuBar.
     */
    public BrowserMenuBar() {
        super();
        this.popupItemFactory = PopupItemFactory.getInstance();
        this.localization = new JFrameLocalization("BrowserWindow.Menu");

        // Create JMenus
        final JMenu newMenu = MenuFactory.create(localization.getString("New"),
                new Integer(localization.getString("NewMnemonic").charAt(0)));
        this.add(newMenu);
        final JMenu profileMenu = MenuFactory.create(localization.getString("Profile"),
                new Integer(localization.getString("ProfileMnemonic").charAt(0)));
        this.add(profileMenu);
        final JMenu helpMenu = MenuFactory.create(localization.getString("Help"),
                new Integer(localization.getString("HelpMnemonic").charAt(0)));
        this.add(helpMenu);
        final JMenu signUpMenu = MenuFactory.create(localization.getString("SignUp"),
                new Integer(localization.getString("SignUpMnemonic").charAt(0)));
        this.add(signUpMenu);

        // Create the New popup menu
        newMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_CREATE, new Data(0)));
        newMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_CREATE_INCOMING_INVITATION, new Data(0)));

        // Add minimize and close buttons
        this.add(Box.createHorizontalGlue());
        this.add(new JLabel(ImageIOUtil.readIcon("MinimizeButton.png")));
        this.add(Box.createRigidArea(new Dimension(3,0)));
        this.add(new JLabel(ImageIOUtil.readIcon("CloseButton.png")));
        this.add(Box.createRigidArea(new Dimension(3,0)));
    }
    
    /**
     * @see JFrameLocalization#getString(String)
     * 
     */
    protected String getString(final String localKey) {
        return localization.getString(localKey);
    }    
}
