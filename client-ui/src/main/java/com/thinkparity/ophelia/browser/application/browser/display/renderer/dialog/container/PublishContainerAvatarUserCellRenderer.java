/*
 * PublishContainerAvatarUserCellRenderer.java
 *
 * Created on December 9, 2006, 4:19 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.container;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.PublishContainerAvatar.PublishContainerAvatarUser;

/**
 *
 * @author  Administrator
 */
public class PublishContainerAvatarUserCellRenderer extends AbstractJPanel implements ListCellRenderer {

    /** Flag indicating if this is a spacer cell */
    Boolean spacer;

    /** Creates new form PublishContainerAvatarUserCellRenderer */
    public PublishContainerAvatarUserCellRenderer() {
        initComponents();
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (spacer) {
            // Draw a line through the middle.
            final Graphics g2 = g.create();
            try {
                g2.setColor(Colors.Browser.Publish.FIRST_CONTACT_BORDER);
                g2.drawLine(20, getSize().height / 2, getSize().width - 1, getSize().height / 2);
            }
            finally { g2.dispose(); }
        }
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
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        final PublishContainerAvatarUser user = (PublishContainerAvatarUser) value;

        if (!user.isUser() && !user.isEMailUser()) {
            spacer = Boolean.TRUE;     
            userJCheckBox.setVisible(false);     
        } else {  
            spacer = Boolean.FALSE;
            userJCheckBox.setVisible(true);
            userJCheckBox.setText(user.getExtendedName());
            userJCheckBox.setSelected(user.isSelected());
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
        java.awt.GridBagConstraints gridBagConstraints;

        userJCheckBox = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(50, 18));
        userJCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        userJCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        userJCheckBox.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(userJCheckBox, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox userJCheckBox;
    // End of variables declaration//GEN-END:variables
}
