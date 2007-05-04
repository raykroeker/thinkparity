/*
 * Created On:  October 7, 2006, 1:34 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;

/**
 * <b>Title:</b>thinkParity Version Content Cell Renderer<br>
 * <b>Description:</b>A cell renderer for the eastern list within the versions
 * panel.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class EastCellRenderer extends DefaultCellRenderer implements PanelCellRenderer {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel additionalTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel iconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel textJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** The space between text and additional text. */
    private static final int TEXT_SPACE_BETWEEN;

    /** The space to leave at the end of the text. */
    private static final int TEXT_SPACE_END;

    static {
        TEXT_SPACE_BETWEEN = 5;
        TEXT_SPACE_END = 15;
    }

    /**
     * Create a EastCellRenderer
     */
    public EastCellRenderer() {
        super();
        initComponents();
    }
    
    /**
     * Create EastCellRenderer.
     * 
     */
    public EastCellRenderer(final DefaultTabPanel tabPanel) {
        super(tabPanel);
        initComponents();
        installListeners(tabPanel, iconJLabel);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCellRenderer#renderComponent(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell, int)
     */
    @Override
    public void renderComponent(final Cell cell, final int index) {
        // Override behavior of DefaultCellRenderer.
        // The east cell text is painted (see paintComponent). This avoids
        // layout problems when the textJLabel text is too long.
        renderComponent(cell, index, iconJLabel, textJLabel, additionalTextJLabel);
        textJLabel.setText(" ");
        additionalTextJLabel.setText("");
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D)g.create();
        try {
            // Paint text manually. This avoids layout problems when the textJLabel text is too long.
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

                // paint the selection line
                if (isSelected() && PanelFocusHelper.Focus.EAST == PanelFocusHelper.getFocus()) {
                    location = SwingUtilities.convertPoint(textJLabel, new Point(0,0), this);
                    final int height = SwingUtil.getStringHeight(g2);
                    int width = SwingUtil.getStringWidth(clippedText, g2);
                    if (null != clippedAdditionalText) {
                        width += TEXT_SPACE_BETWEEN + SwingUtil.getStringWidth(clippedAdditionalText, g2);
                    }
                    paintSelectionLine(g2, location.x, location.y, width, height);
                }
            }
        }
        finally { g2.dispose(); }
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(32767, 24));
        setMinimumSize(new java.awt.Dimension(20, 24));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(20, 24));
        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconDraft.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 5);
        add(iconJLabel, gridBagConstraints);

        textJLabel.setFont(Fonts.DefaultFont);
        textJLabel.setText("!East Cell Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(textJLabel, gridBagConstraints);

        additionalTextJLabel.setFont(Fonts.DefaultFont);
        additionalTextJLabel.setForeground(Colors.Browser.Panel.PANEL_ADDITIONAL_TEXT_FG);
        additionalTextJLabel.setText("!East Cell Additional Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(additionalTextJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

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
