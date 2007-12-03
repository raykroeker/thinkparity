/*
 * Created On:  October 7, 2006, 1:34 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.awt.Graphics;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;

/**
 * <b>Title:</b>thinkParity Version Content Cell Renderer<br>
 * <b>Description:</b>A cell renderer for the eastern list within the versions
 * panel.<br>
 * 
 * @author robert@thinkparity.com
 * @version 1.1.2.1
 */
public class EastCellRenderer extends DefaultCellRenderer implements PanelCellRenderer {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel additionalTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel iconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel textJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

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
        paintText(g, textJLabel, additionalTextJLabel);
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
}
