/**
 * Created On: 15-Dec-06 6:03:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel.PanelLocation;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public abstract class DefaultCellRenderer extends AbstractJPanel implements PanelCellRenderer {

    /** The space between text and additional text. */
    private static final int TEXT_SPACE_BETWEEN;

    /** The space to leave at the end of the text. */
    private static final int TEXT_SPACE_END;

    static {
        TEXT_SPACE_BETWEEN = 5;
        TEXT_SPACE_END = 15;
    }

    /** The Cell */
    protected Cell cell;

    /** The index */
    protected int index;

    /** The tab panel */
    protected final DefaultTabPanel tabPanel;

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
     * Install mouse listeners.
     */
    protected void installListeners(final DefaultTabPanel tabPanel,
            final javax.swing.JLabel iconJLabel) {
        iconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
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
            public void mousePressed(final java.awt.event.MouseEvent e) {
                if (cell.isActionAvailable() && cell.isActionDelayRequired()) {
                    SwingUtil.setCursor(DefaultCellRenderer.this, null);
                }
                tabPanel.panelCellMousePressed(cell, PanelLocation.ICON, e);
                if (e.getButton() == MouseEvent.BUTTON1
                        && cell.isActionAvailable()
                        && e.getClickCount() == 1) {
                    cell.invokeAction();
                } else if (e.getButton() == MouseEvent.BUTTON1
                        && cell.isActionAvailable()
                        && !cell.isActionDelayRequired()
                        && e.getClickCount() > 1) {
                    cell.invokeAction();
                } else {
                    maybeShowPopup(e);
                }
            }
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                maybeShowPopup(e);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(final java.awt.event.MouseEvent e) {
                tabPanel.panelCellMousePressed(cell, PanelLocation.BODY, e);
                maybeShowPopup(e);
            }
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                maybeShowPopup(e);
            }
        });
    }

    /**
     * Determine whether or not a cell is selected.
     * 
     * @return True if it is selected.
     */
    protected Boolean isSelected() {
        Assert.assertNotNull("Null panel selection manager in cell renderer.", panelSelectionManager);
        return panelSelectionManager.isSelected(cell);
    }

    /**
     * Maybe show a popup.
     */
    protected void maybeShowPopup(final java.awt.event.MouseEvent e) {
        if (e.isPopupTrigger() && cell.isPopupAvailable()) {
            if (null != tabPanel.getPopupDelegate()) {
                tabPanel.getPopupDelegate().initialize((Component) e.getSource(), e.getX(), e.getY());
                cell.showPopup(Boolean.FALSE);
            }
        }
    }

    /**
     * Paint the text manually.
     * This avoids layout problems when the textJLabel text is too long.
     * 
     * @param g
     *            The <code>Graphics</code>.
     * @param textJLabel
     *            The text <code>JLabel</code>.
     * @param additionalTextJLabel
     *            The additional text <code>JLabel</code>.
     */
    protected void paintText(final Graphics g,
            final javax.swing.JLabel textJLabel,
            final javax.swing.JLabel additionalTextJLabel) {
        final Graphics2D g2 = (Graphics2D)g.create();
        try {
            g2.setFont(textJLabel.getFont());
            Point location = SwingUtilities.convertPoint(textJLabel,
                    new Point(0, g2.getFontMetrics().getMaxAscent()), this);
            if (cell.isSetText()) {
                // paint the text
                final String clippedText = clipText(g2, location, cell.getText());
                paintText(g2, location, textJLabel.getForeground(), clippedText);
                String clippedAdditionalText = null;
                if (!isClipped(cell.getText(), clippedText) && cell.isSetAdditionalText()) {
                    location.x += TEXT_SPACE_BETWEEN + SwingUtil.getStringWidth(clippedText, g2);
                    clippedAdditionalText = clipText(g2, location, cell.getAdditionalText());
                    if (null != clippedAdditionalText) {
                        paintText(g2, location, additionalTextJLabel.getForeground(), clippedAdditionalText);
                    }
                }
            }
        }
        finally { g2.dispose(); }
    }

    /**
     * This method is always called before the panel is added and painted.
     * 
     * @param cell
     *            The <code>Cell</code>.
     * @param index
     *            The <code>index</code>.
     * @param iconJLabel
     *            The icon <code>JLabel</code>.
     * @param textJLabel
     *            The text <code>JLabel</code>.
     * @param additionalTextJLabel
     *            The additional text <code>JLabel</code>.
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
     * Clip text.
     * 
     * @param g
     *            The <code>Graphics2D</code>.
     * @param location
     *            The text location <code>Point</code>.
     * @param text
     *            The text <code>String</code>.
     * @return The text <code>String</code>, which may or may not be clipped.
     */
    private String clipText(final Graphics2D g, final Point location, final String text) {
        final int availableWidth = getWidth() - location.x - TEXT_SPACE_END;
        return SwingUtil.limitWidthWithEllipsis(text, availableWidth, g);
    }

    /**
     * Determine if the string was clipped.
     * 
     * @return true if the string was clipped.
     */
    private Boolean isClipped(final String originalText, final String clippedText) {
        return (null == clippedText || !clippedText.equals(originalText));
    }

    /**
     * Paint text.
     * 
     * @param g
     *            The <code>Graphics2D</code>.
     * @param location
     *            The text location <code>Point</code>.
     * @param color
     *            The text <code>Color</code>.
     * @param text
     *            The text <code>String</code>.
     */
    private void paintText(final Graphics2D g, final Point location, final Color color, final String text) {
        g.setPaint(color);
        g.drawString(text, location.x, location.y);
    }
}
