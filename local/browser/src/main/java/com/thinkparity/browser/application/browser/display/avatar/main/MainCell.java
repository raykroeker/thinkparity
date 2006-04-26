/*
 * Apr 12, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

/**
 * MainCell represents a single line item in the browser's main list.  To add
 * a disparate type to the main list; simply implement this interface; and use
 * the {@link MainController} to add your type to the list.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface MainCell {

    /**
     * Determine whether or not the cell supports import.
     *
     * @return True if the cell supports import; false otherwise.
     */
    public boolean canImport();

    /**
     * Obtain the background image for a cell.
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackground();

    /**
     * Obtain the background image for a selected cell.
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackgroundSelected();

    /**
     * Obtain the border to be installed around the edge of the main cell.
     * 
     * @return A border.
     */
    public Border getBorder();

    /**
     * Obtain an info icon.
     * 
     * @return An image icon.
     */
    public ImageIcon getInfoIcon();

    /**
     * Obtain the node icon.
     * 
     * @return The node icon.
     */
    public ImageIcon getNodeIcon();

    /**
     * Obtain the node selected icon.
     * 
     * @return The node selected icon.
     */
    public ImageIcon getNodeIconSelected();

    /**
     * Obtain the text to display.
     * 
     * @return A string.
     */
    public String getText();

    /**
     * Obatin the font for the cell text.
     * 
     * @return A font.
     */
    public Font getTextFont();

    /**
     * Obtain a color for the cell text.
     * 
     * @return A color.
     */
    public Color getTextForeground();

    /**
     * Obtain the text inset factor. This is used to offset text the default
     * amount by the given factor.
     * 
     * @return The text inset factor.
     */
    public Float getTextInsetFactor();
    
    /**
     * Obtain the tool tip text.
     * 
     * @return A string.
     */
    public String getToolTip();
}
