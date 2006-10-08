/*
 * Created On: October 7, 2006, 1:34 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.ophelia.browser.Constants.Colors.Swing;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionsPanel.VersionCell;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class VersionCellRenderer extends AbstractJPanel implements ListCellRenderer {
    
    /** Creates new form VersionCellRenderer */
    VersionCellRenderer() {
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
        final VersionCell cell = (VersionCell) value;
        textJLabel.setText(cell.getText());

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
        textJLabel = new javax.swing.JLabel();

        textJLabel.setText("!Version Cell!");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(textJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(textJLabel)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JLabel textJLabel;
    // End of variables declaration//GEN-END:variables
    
}
