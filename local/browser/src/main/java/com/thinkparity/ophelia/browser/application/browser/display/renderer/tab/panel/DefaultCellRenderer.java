/**
 * Created On: 15-Dec-06 6:03:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.awt.Component;
import java.awt.event.MouseEvent;

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
    public abstract void renderComponent(final Cell cell, final int index);
    
    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
     *      java.lang.Object, int, boolean, boolean)
     */
    public abstract Component getListCellRendererComponent(final JList list,
            final Object value, final int index, final boolean isSelected,
            final boolean cellHasFocus);
    
    /**
     * This method is always called before the panel is added and painted.
     */
    protected void renderComponent(final Cell cell, final int index,
            final javax.swing.JLabel iconJLabel,
            final javax.swing.JLabel textJLabel,
            final javax.swing.JLabel additionalTextJLabel) {
        this.cell = cell;
        iconJLabel.setIcon(cell.getIcon());
        textJLabel.setText(cell.getText());
        if (cell.isEnabled()) {
            textJLabel.setForeground(Colors.Browser.List.LIST_FG);
        } else {
            textJLabel.setForeground(Colors.Browser.List.INNER_LIST_SELECTION_BORDER);
        }
        if (cell.isSetAdditionalText()) {
            additionalTextJLabel.setText(cell.getAdditionalText());
        } else {
            additionalTextJLabel.setText("");
        }
    }
    
    /**
     * Install mouse listeners.
     */
    protected void installListeners(final DefaultTabPanel tabPanel,
            final javax.swing.JLabel iconJLabel) {
        iconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(final java.awt.event.MouseEvent e) {
                tabPanel.panelCellMousePressed(cell, e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (cell.isActionAvailable()) {
                        cell.invokeAction();
                    }
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
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(final java.awt.event.MouseEvent e) {
                tabPanel.panelCellMousePressed(cell, e);
                maybeShowPopup(e);
            }
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                maybeShowPopup(e);
            }
        });
    }
    
    /**
     * Maybe show a popup.
     */
    private void maybeShowPopup(final java.awt.event.MouseEvent e) {
        if (e.isPopupTrigger()) {
            if (null != tabPanel.getPopupDelegate()) {
                tabPanel.getPopupDelegate().initialize((Component) e.getSource(), e.getX(), e.getY());
                cell.showPopup();
            }
        }
    }
}
