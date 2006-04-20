/*
 * Apr 12, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.message.system.SystemMessage;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainCellSystemMessage extends SystemMessage implements MainCell {

    /**
     * Create a MainCellSystemMessage.
     */
    public MainCellSystemMessage() {
        throw Assert.createNotYetImplemented("MainCellSystemMessage#<init>");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#canImport()
     */
    public boolean canImport() {
        throw Assert.createNotYetImplemented("MainCellSystemMessage#canImport");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#fireSelection()
     */
    public void fireSelection() {
        throw Assert.createNotYetImplemented("MainCellSystemMessage#fireSelection");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackground()
     */
    public BufferedImage getBackground() {
        throw Assert
                .createNotYetImplemented("MainCellSystemMessage#getBackground");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackgroundSelected()
     */
    public BufferedImage getBackgroundSelected() {
        throw Assert
                .createNotYetImplemented("MainCellSystemMessage#getBackgroundSelected");
    }

    public Border getBorder() { return BorderFactory.createEmptyBorder(); }
    public Border getBorderSelected() { return BorderFactory.createEmptyBorder(); }

    public ImageIcon getIcon() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getInfoIcon()
     */
    public ImageIcon getInfoIcon() {
        throw Assert
                .createNotYetImplemented("MainCellSystemMessage#getInfoIcon");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIcon()
     */
    public ImageIcon getNodeIcon() {
        throw Assert.createNotYetImplemented("MainCellSystemMessage#getNodeIcon");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIconSelected()
     */
    public ImageIcon getNodeIconSelected() {
        throw Assert.createNotYetImplemented("MainCellSystemMessage#getNodeIconSelected");
    }

    public ImageIcon getSelectedIcon() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getText()
     */
    public String getText() {
        throw Assert.createNotYetImplemented("MainCellSystemMessage#getText");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextFont()
     */
    public Font getTextFont() {
        throw Assert
                .createNotYetImplemented("MainCellSystemMessage#getTextFont");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextForeground()
     */
    public Color getTextForeground() {
        throw Assert
                .createNotYetImplemented("MainCellSystemMessage#getTextForeground");
    }

    public Float getTextInsetFactor() { return 1.0F; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getToolTip()
     */
    public String getToolTip() {
        throw Assert
                .createNotYetImplemented("MainCellSystemMessage#getToolTip");
    }
    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#isGroupSelected()
     */
    public Boolean isGroupSelected() {
        throw Assert.createNotYetImplemented("MainCellSystemMessage#isGroupSelected");
    }
    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#populatePopupMenu(java.awt.event.MouseEvent, javax.swing.JPopupMenu)
     */
    public void populatePopupMenu(MouseEvent e, JPopupMenu jPopupMenu) {
        throw Assert.createNotYetImplemented("MainCellSystemMessage#populatePopupMenu");
    }
    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#setGroupSelected(java.lang.Boolean)
     */
    public void setGroupSelected(Boolean groupSelected) {
        throw Assert.createNotYetImplemented("MainCellSystemMessage#setGroupSelected");
    }
}
