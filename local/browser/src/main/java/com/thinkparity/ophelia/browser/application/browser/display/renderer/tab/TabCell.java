/*
 * Created On: Apr 12, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.ophelia.browser.platform.Platform.Connection;

/**
 * <b>Title:</b>thinkParity Tab Cell<br>
 * <b>Description:</b>A tab cell represents a single line item in either of the
 * browser's tabbed lists. To add a disparate type to the main list; simply
 * implement this interface; and update the model for the appropriate tab.<br>
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public interface TabCell {

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
     * @param index
     *          The index of this cell in the list model.
     * @param lastCell
     *          TRUE if this is the last cell in the list model.
     * 
     * @return A border.
     */
    public Border getBorder(int index, Boolean lastCell);

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
     * Obtain the parent cell; if applicable.
     * 
     * @return The parent cell; or null if the cell has no parent.
     */
    public TabCell getParent();

    /**
     * Obtain the text to display.
     * 
     * @return A string.
     */
    public String getText();
    
    /**
     * Obtain the secondary text to display.
     * 
     * @return A string.
     */
    public String getSecondaryText();

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

    /**
     * Trigger a connection sensitive popup menu for the cell.
     * @param invoker
     *            The invoker component.
     * @param e
     *            The x position.
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e);
}
