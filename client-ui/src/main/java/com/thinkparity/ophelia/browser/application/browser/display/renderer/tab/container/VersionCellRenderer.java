/*
 * Created On: October 7, 2006, 1:34 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionsPanel.AbstractVersionCell;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class VersionCellRenderer extends AbstractJPanel implements ListCellRenderer {
    
    /** An image cache. */
    private final MainPanelImageCache imageCache;
    
    /** Creates new form VersionCellRenderer */
    VersionCellRenderer() {
        this.imageCache = new MainPanelImageCache();
        initComponents();
    }

    /**
     * Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell.  If it is necessary to compute the dimensions
     * of a list because the list cells do not have a fixed size, this method
     * is called to generate a component on which <code>getPreferredSize</code>
     * can be invoked.
     * 
     * @param list The JList we're painting.
     * @param value The value returned by list.getModel().getElementAt(index).
     * @param index The cells index.
     * @param isSelected True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     * @see JList
     * @see ListSelectionModel
     * @see ListModel
     */
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final AbstractVersionCell cell = (AbstractVersionCell) value;
        textJLabel.setText(cell.getText());
        final Icon icon = cell.getIcon();
        // Set icon, even if it is null
        iconJLabel.setIcon(icon);
        
        // Set border.
        if (cell.isMouseOver() && (cell.getPopupCellIndex() == -1) && !cell.isFillerCell()) {
            // Mouse-over won't take effect if there is a popup on one of the cells.
            setBorder(new LineBorder(Colors.Browser.List.INNER_LIST_MOUSE_OVER_BORDER));
        } else if (cell.getPopupCellIndex()==index) {
            setBorder(new LineBorder(Colors.Browser.List.INNER_LIST_SELECTION_BORDER));
/*        } else if (isSelected && !cell.isFillerCell()) {
            // Note that during a popup, cell.isFocusOnThisList() returns true whereas
            // isFocusOwner() returns false.
            if (cell.isFocusOnThisList()) {
                setBorder(new LineBorder(Colors.Browser.List.INNER_LIST_SELECTION_BORDER));
            } else {
                setBorder(new LineBorder(Colors.Browser.List.INNER_LIST_SELECTION_NOFOCUS_BORDER));
            }*/
        } else {
            final int adjustedIndex = index + 1;
            if (0 == adjustedIndex % 2) {
                setBorder(new LineBorder(Colors.Browser.List.LIST_EVEN_BG));
            } else {
                setBorder(new LineBorder(Colors.Browser.List.LIST_ODD_BG));
            }
        }
        
        // Set selected icon.
        if (isSelected && !cell.isFillerCell()) {
            selectedIconJLabel.setIcon(imageCache.read(TabPanelIcon.SELECTED));
        } else {
            selectedIconJLabel.setIcon(imageCache.read(TabPanelIcon.INVISIBLE));
        }

        // Set foreground and background colours.
        textJLabel.setForeground(Colors.Browser.List.LIST_FG);
        final int adjustedIndex = index + 1;
        if (0 == adjustedIndex % 2) {
            setBackground(Colors.Browser.List.LIST_EVEN_BG);
        } else {
            setBackground(Colors.Browser.List.LIST_ODD_BG);
        }
        
        // This code is here temporarily, for further experimentation with Omid...
/*        if (isSelected && !cell.isFillerCell()) {
            // Note that during a popup, cell.isFocusOnThisList() returns true whereas
            // isFocusOwner() returns false.
            if (cell.isSelectedContainer() && cell.isFocusOnThisList()) {
                textJLabel.setForeground(Colors.Browser.List.INNER_LIST_SELECTION_FG);
                setBackground(Colors.Browser.List.INNER_LIST_SELECTION_BG);
                setBorder(new LineBorder(Colors.Browser.List.INNER_LIST_SELECTION_BORDER));
            } else {
                textJLabel.setForeground(Colors.Browser.List.INNER_LIST_SELECTION_NOFOCUS_FG);
                setBackground(Colors.Browser.List.INNER_LEFT_LIST_SELECTION_NOFOCUS_BG);
                setBorder(new LineBorder(Colors.Browser.List.INNER_LIST_SELECTION_NOFOCUS_BORDER));
            }
        } else {
            final int adjustedIndex = index + cell.getContainerIndex() + 1;
            textJLabel.setForeground(Colors.Browser.List.LIST_FG);
            if (0 == adjustedIndex % 2) {
                setBackground(Colors.Browser.List.LIST_EVEN_BG);
                setBorder(new LineBorder(Colors.Browser.List.LIST_EVEN_BG));
            } else {
                setBackground(Colors.Browser.List.LIST_ODD_BG);
                setBorder(new LineBorder(Colors.Browser.List.LIST_ODD_BG));
            }
        }*/

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
        selectedIconJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(32767, 18));
        setMinimumSize(new java.awt.Dimension(20, 18));
        setPreferredSize(new java.awt.Dimension(20, 18));
        westPaddingJLabel.setMaximumSize(new java.awt.Dimension(3, 16));
        westPaddingJLabel.setMinimumSize(new java.awt.Dimension(3, 16));
        westPaddingJLabel.setPreferredSize(new java.awt.Dimension(3, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(westPaddingJLabel, gridBagConstraints);

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconDraft.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        add(iconJLabel, gridBagConstraints);

        textJLabel.setText("!Version Cell!");
        textJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        textJLabel.setMaximumSize(new java.awt.Dimension(20, 16));
        textJLabel.setMinimumSize(new java.awt.Dimension(20, 16));
        textJLabel.setPreferredSize(new java.awt.Dimension(20, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        add(textJLabel, gridBagConstraints);

        selectedIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Invisible16x16.png")));
        add(selectedIconJLabel, new java.awt.GridBagConstraints());

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JLabel iconJLabel;
    javax.swing.JLabel selectedIconJLabel;
    javax.swing.JLabel textJLabel;
    // End of variables declaration//GEN-END:variables
    
}
