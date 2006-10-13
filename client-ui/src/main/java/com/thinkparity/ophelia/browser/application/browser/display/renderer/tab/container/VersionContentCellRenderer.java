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

import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.Constants.Colors.Swing;
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
        if (null!=icon) {
            iconJLabel.setIcon(icon);
        }

        if (isSelected) {
            textJLabel.setForeground(Swing.LIST_SELECTION_FG);
            setBackground(Swing.LIST_SELECTION_BG);
        } else {
            textJLabel.setForeground(Swing.LIST_FG);
            if (0 == index % 2) {
                setBackground(Swing.LIST_EVEN_BG);
            } else {
                setBackground(Swing.LIST_ODD_BG);
            }
        }

        return this;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JLabel eastPaddingJLabel;
        javax.swing.JLabel westPaddingJLabel;

        westPaddingJLabel = new javax.swing.JLabel();
        iconJLabel = new javax.swing.JLabel();
        textJLabel = new javax.swing.JLabel();
        eastPaddingJLabel = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(32767, 18));
        setMinimumSize(new java.awt.Dimension(20, 18));
        setPreferredSize(new java.awt.Dimension(20, 18));
        westPaddingJLabel.setFocusable(false);
        westPaddingJLabel.setMaximumSize(new java.awt.Dimension(3, 16));
        westPaddingJLabel.setMinimumSize(new java.awt.Dimension(3, 16));
        westPaddingJLabel.setPreferredSize(new java.awt.Dimension(3, 16));

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconNotepad.png")));

        textJLabel.setText("!VersionContent!");
        textJLabel.setMaximumSize(new java.awt.Dimension(20, 16));
        textJLabel.setMinimumSize(new java.awt.Dimension(20, 16));
        textJLabel.setPreferredSize(new java.awt.Dimension(20, 16));

        eastPaddingJLabel.setFocusable(false);
        eastPaddingJLabel.setMaximumSize(new java.awt.Dimension(3, 16));
        eastPaddingJLabel.setMinimumSize(new java.awt.Dimension(3, 16));
        eastPaddingJLabel.setPreferredSize(new java.awt.Dimension(3, 16));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(westPaddingJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(iconJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(textJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 100, Short.MAX_VALUE)
                .add(eastPaddingJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(textJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(iconJLabel)
                    .add(westPaddingJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(eastPaddingJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JLabel iconJLabel;
    javax.swing.JLabel textJLabel;
    // End of variables declaration//GEN-END:variables
    
}
