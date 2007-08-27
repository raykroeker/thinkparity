/**
 * Created On: 15-Dec-06 6:03:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.awt.Component;
import java.awt.event.MouseEvent;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public abstract class DefaultCellRenderer extends AbstractJPanel implements PanelCellRenderer {

    /** The tab panel */
    protected final DefaultTabPanel tabPanel;

    /** The Cell */
    protected Cell cell;

    /** The index */
    protected int index;

    /** The <code>PanelSelectionManager</code>. */
    private PanelSelectionManager panelSelectionManager;

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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelCellRenderer#setPanelSelectionManager(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelSelectionManager)
     */
    public void setPanelSelectionManager(final PanelSelectionManager panelSelectionManager) {
        this.panelSelectionManager = panelSelectionManager;
    }

    /**
     * Determine whether or not a cell is selected.
     * 
     * @param cell
     *            The <code>Cell</code>.
     * @return True if it is selected.
     */
    protected Boolean isSelected() {
        Assert.assertNotNull("Null panel selection manager in cell renderer.", panelSelectionManager);
        return panelSelectionManager.isSelected(cell);
    }

    /**
     * This method is always called before the panel is added and painted.
     */
    protected void renderComponent(final Cell cell, final int index,
            final javax.swing.JLabel iconJLabel,
            final javax.swing.JLabel textJLabel,
            final javax.swing.JLabel additionalTextJLabel) {
        this.cell = cell;
        this.index = index;
        iconJLabel.setIcon(cell.getIcon());
        textJLabel.setText(cell.getText());
        if (cell.isEnabled()) {
            textJLabel.setForeground(Colors.Browser.Panel.PANEL_CONTAINER_TEXT_FG);
        } else {
            textJLabel.setForeground(Colors.Browser.Panel.PANEL_DISABLED_TEXT_FG);
        }
        if (cell.isEmphasized()) {
            textJLabel.setFont(Fonts.DefaultFontBold);
        } else {
            textJLabel.setFont(Fonts.DefaultFont);
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
                tabPanel.panelCellMousePressed(cell, Boolean.TRUE, e);
                if (e.getButton() == MouseEvent.BUTTON1
                        && e.getClickCount() == 1 && cell.isActionAvailable()) {
                    cell.invokeAction();
                } else {
                    maybeShowPopup(e);
                }
            }
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                maybeShowPopup(e);
            }
            public void mouseEntered(final java.awt.event.MouseEvent e) {
                if (cell.isActionAvailable()) {
                    SwingUtil.setCursor(DefaultCellRenderer.this, java.awt.Cursor.HAND_CURSOR);
                }
            }
            public void mouseExited(final java.awt.event.MouseEvent e) {
                if (cell.isActionAvailable()) {
                    SwingUtil.setCursor(DefaultCellRenderer.this, null);
                }
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(final java.awt.event.MouseEvent e) {
                tabPanel.panelCellMousePressed(cell, Boolean.FALSE, e);
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
    protected void maybeShowPopup(final java.awt.event.MouseEvent e) {
        if (e.isPopupTrigger() && cell.isPopupAvailable()) {
            if (null != tabPanel.getPopupDelegate()) {
                tabPanel.getPopupDelegate().initialize((Component) e.getSource(), e.getX(), e.getY());
                cell.showPopup();
            }
        }
    }
}
