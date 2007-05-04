/**
 * Created On: 15-Dec-06 6:03:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Colors;
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
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                    if (cell.isActionAvailable()) {
                        cell.invokeAction();
                    }
                }
                if (!cell.isActionAvailable()) {
                    maybeShowPopup(e);
                }
            }
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (!cell.isActionAvailable()) {
                    maybeShowPopup(e);
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
                tabPanel.panelCellMousePressed(cell, Boolean.FALSE, e);
                maybeShowPopup(e);
            }
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                maybeShowPopup(e);
            }
        });
    }

    /**
     * Paint a selection line.
     * 
     * @param g2
     *            A <code>Graphics2D</code> context.
     * @param x
     *            The x location <code>int</code>.
     * @param y
     *            The y location <code>int</code>.
     * @param width
     *            The width <code>int</code>.
     * @param height
     *            The height <code>int</code>.
     */
    protected void paintSelectionLine(final Graphics2D g2, final int x, final int y, final int width, final int height) {
        final int insetX = 5;
        final int insetY = 2;
        final Path2D path = getSelectionPath(x - insetX, y - insetY, width + 2*insetX, height + 2*insetY);
        paintLine(g2, path, Colors.Browser.Panel.PANEL_LIST_SELECTION_LINE[index], Boolean.TRUE);
    }

    /**
     * Get a path for the selection line.
     * 
     * @param x
     *            A x offset <code>int</code>.
     * @param y
     *            A y offset <code>int</code>.
     * @param width
     *            A width <code>int</code>.
     * @param height
     *            A height <code>int</code>.   
     * @return A <code>Path2D</code>.
     */
    private Path2D getSelectionPath(final int x, final int y, final int width, final int height) {
        final Path2D path = new Path2D.Double();           
        final int radius = 2;   // Radius of the curves on the corners
        final int quad = 0;     // Inset to the control point for defining curves
        final int minX = x;
        final int maxX = x + width - 1;
        final int minY = y;
        final int maxY = y + height - 1;
        path.moveTo(minX + radius, minY);
        path.lineTo(maxX - radius, minY);
        path.quadTo(maxX - quad, minY + quad, maxX, minY + radius);
        path.lineTo(maxX, maxY - radius);
        path.quadTo(maxX - quad, maxY - quad, maxX - radius, maxY);
        path.lineTo(minX + radius, maxY);
        path.quadTo(minX + quad, maxY - quad, minX, maxY - radius);
        path.lineTo(minX, minY + radius);
        path.quadTo(minX + quad, minY + quad, minX + radius, minY);
        return path;
    }

    /**
     * Maybe show a popup.
     */
    private void maybeShowPopup(final java.awt.event.MouseEvent e) {
        if (e.isPopupTrigger() && cell.isPopupAvailable()) {
            if (null != tabPanel.getPopupDelegate()) {
                tabPanel.getPopupDelegate().initialize((Component) e.getSource(), e.getX(), e.getY());
                cell.showPopup();
            }
        }
    }

    /**
     * Paint a solid or dashed line.
     * 
     * @param g2
     *            A <code>Graphics2D</code> context.
     * @param path
     *            A <code>Path2D</code>.
     * @param color
     *            A <code>Color</code>.
     * @param dashed
     *            A dashed <code>Boolean</code>. 
     */
    private void paintLine(final Graphics2D g2, final Path2D path, final Color color, final Boolean dashed) {
        final float dashes[] = {1};
        if (dashed) {
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, dashes, 0));
        } else {
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f));
        }
        g2.setColor(color);
        g2.draw(path);
    }
}
