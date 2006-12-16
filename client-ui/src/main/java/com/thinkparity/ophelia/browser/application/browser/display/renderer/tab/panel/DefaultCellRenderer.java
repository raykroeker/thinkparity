/**
 * Created On: 15-Dec-06 6:03:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public abstract class DefaultCellRenderer extends AbstractJPanel implements PanelCellRenderer, ListCellRenderer {
    
    /** The tab panel */
    protected final DefaultTabPanel tabPanel;
    
    /** The Cell */
    protected Cell cell;
    
    /**
     * Create DefaultCellRenderer.
     * 
     */
    public DefaultCellRenderer() {
        super();
        this.tabPanel = null;
    }
    
    /**
     * Create DefaultCellRenderer.
     * 
     */
    public DefaultCellRenderer(final DefaultTabPanel tabPanel) {
        super();
        this.tabPanel = tabPanel;
    }
    
    /**
     * This method is always called before the panel is added and painted.
     */
    public void renderComponent(final Cell cell, final int index) {
        this.cell = cell;
        if (null != getIconJLabel()) {
            getIconJLabel().setIcon(cell.getIcon());
        }
        if (null != getTextJLabel()) {
            getTextJLabel().setText(cell.getText());
            if (cell.isEnabled()) {
                getTextJLabel().setForeground(Colors.Browser.List.LIST_FG);
            } else {
                getTextJLabel().setForeground(Colors.Browser.List.INNER_LIST_SELECTION_BORDER);
            }
        }
        if (null != getAdditionalTextJLabel()) {
            if (cell.isSetAdditionalText()) {
                getAdditionalTextJLabel().setText(cell.getAdditionalText());
            } else {
                getAdditionalTextJLabel().setText("");
            }
        }
    }
    
    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
     *      java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(final JList list,
            final Object value, final int index, final boolean isSelected,
            final boolean cellHasFocus) {
        final Cell cell = (Cell) value;
        this.cell = cell;
        renderComponent(cell, index);
        return this;
    }
    
    /**
     * Get the icon JLabel
     */
    protected javax.swing.JLabel getIconJLabel() {
        return null;
    }
    
    /**
     * Get the text JLabel
     */
    protected javax.swing.JLabel getTextJLabel() {
        return null;
    }
    
    /**
     * Get the additional text JLabel
     */
    protected javax.swing.JLabel getAdditionalTextJLabel() {
        return null;
    }
    
    /**
     * Install mouse listeners.
     */
    protected void installListeners() {
        if (null != tabPanel) {
            if (null != getIconJLabel()) {
                getIconJLabel().addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(final java.awt.event.MouseEvent e) {
                        tabPanel.panelCellMouseClicked(cell, e);
                        if (cell.isActionAvailable()) {
                            cell.invokeAction();
                        }
                    }
                    public void mouseEntered(final java.awt.event.MouseEvent e) {
                        if (cell.isActionAvailable()) {
                            SwingUtil.setCursor(DefaultCellRenderer.this, java.awt.Cursor.HAND_CURSOR);
                        }
                    }
                    public void mouseExited(final java.awt.event.MouseEvent e) {
                        if (cell.isActionAvailable()) {
                            SwingUtil.setCursor(DefaultCellRenderer.this, java.awt.Cursor.DEFAULT_CURSOR);
                        }
                    }
                });
            }
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(final java.awt.event.MouseEvent e) {
                    tabPanel.panelCellMouseClicked(cell, e);
                }
                public void mousePressed(final java.awt.event.MouseEvent e) {
                    maybeShowPopup(e);
                }
                public void mouseReleased(final java.awt.event.MouseEvent e) {
                    maybeShowPopup(e);
                }
            });
        }
    }
    
    /**
     * Maybe show a popup.
     */
    private void maybeShowPopup(final java.awt.event.MouseEvent e) {
        tabPanel.panelCellMouseClicked(cell, e);
        if (e.isPopupTrigger() && (null != tabPanel.getPopupDelegate())) {
            tabPanel.getPopupDelegate().initialize((Component) e.getSource(), e.getX(), e.getY());
            cell.showPopup();
        }
    }
}
