/*
 * VersionContentCellRenderer.java
 *
 * Created on October 7, 2006, 1:34 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionsPanel.AbstractContentCell;

/**
 *
 * @author  raymond
 */
public class VersionContentCellRenderer extends AbstractJPanel implements
        ListCellRenderer {
    
    /** Creates new form VersionContentCellRenderer */
    public VersionContentCellRenderer() {
        initComponents();
    }

    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
     *      java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(final JList list,
            final Object value, final int index, final boolean isSelected,
            final boolean cellHasFocus) {
        final AbstractContentCell cell = (AbstractContentCell) value;
        textJLabel.setText(cell.getText());
        final Icon icon = cell.getIcon();
        // Set icon, even if it is null
        iconJLabel.setIcon(icon);
        
        // Set border.
        if (isSelected && cell.isSelectedContainer() && !cell.isFillerCell()) {
            // Note that during a popup, cell.isFocusOnThisList() returns true whereas
            // isFocusOwner() returns false.
            if (cell.isFocusOnThisList()) {
                setBorder(new LineBorder(Colors.Browser.List.INNER_LIST_SELECTION_BORDER));
            } else {
                setBorder(new LineBorder(Colors.Browser.List.INNER_LIST_SELECTION_NOFOCUS_BORDER));
            }
        } else {
            final int adjustedIndex = index + cell.getContainerIndex() + 1;
            if (0 == adjustedIndex % 2) {
                setBorder(new LineBorder(Colors.Browser.List.LIST_EVEN_BG));
            } else {
                setBorder(new LineBorder(Colors.Browser.List.LIST_ODD_BG));
            }
        }
        
        // Set foreground and background colours.
        final int adjustedIndex = index + cell.getContainerIndex() + 1;
        textJLabel.setForeground(Colors.Browser.List.LIST_FG);
        if (0 == adjustedIndex % 2) {
            setBackground(Colors.Browser.List.LIST_EVEN_BG);
        } else {
            setBackground(Colors.Browser.List.LIST_ODD_BG);
        }

        // This code is here temporarily, for further experimentation with Omid...
/*        if (isSelected && cell.isSelectedContainer() && !cell.isFillerCell()) {
            // Note that during a popup, cell.isFocusOnThisList() returns true whereas
            // isFocusOwner() returns false.
            if (cell.isFocusOnThisList()) {
                textJLabel.setForeground(Colors.Browser.List.INNER_LIST_SELECTION_FG);
                setBackground(Colors.Browser.List.INNER_LIST_SELECTION_BG);
            } else {
                textJLabel.setForeground(Colors.Browser.List.INNER_LIST_SELECTION_NOFOCUS_FG);
                setBackground(Colors.Browser.List.INNER_LIST_SELECTION_NOFOCUS_BG);
            }
        } else {
            final int adjustedIndex = index + cell.getContainerIndex() + 1;
            textJLabel.setForeground(Colors.Browser.List.LIST_FG);
            if (0 == adjustedIndex % 2) {
                setBackground(Colors.Browser.List.LIST_EVEN_BG);
            } else {
                setBackground(Colors.Browser.List.LIST_ODD_BG);
            }
        } */

        return this;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JLabel westPaddingJLabel;

        westPaddingJLabel = new javax.swing.JLabel();
        iconJLabel = new javax.swing.JLabel();
        textJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(32767, 18));
        setMinimumSize(new java.awt.Dimension(20, 18));
        setPreferredSize(new java.awt.Dimension(20, 18));
        westPaddingJLabel.setFocusable(false);
        westPaddingJLabel.setMaximumSize(new java.awt.Dimension(3, 16));
        westPaddingJLabel.setMinimumSize(new java.awt.Dimension(3, 16));
        westPaddingJLabel.setPreferredSize(new java.awt.Dimension(3, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(westPaddingJLabel, gridBagConstraints);

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconFileDefault.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        add(iconJLabel, gridBagConstraints);

        textJLabel.setText("!VersionContent!");
        textJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        textJLabel.setMaximumSize(new java.awt.Dimension(20, 16));
        textJLabel.setMinimumSize(new java.awt.Dimension(20, 16));
        textJLabel.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        add(textJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JLabel iconJLabel;
    javax.swing.JLabel textJLabel;
    // End of variables declaration//GEN-END:variables
    
}
