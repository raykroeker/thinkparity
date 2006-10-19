/*
 * ContainerVersionCommentAvatar.java
 *
 * Created on October 18, 2006, 4:50 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author  Administrator
 */
public class ContainerVersionCommentAvatar extends Avatar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Creates new form ContainerVersionCommentAvatar */
    public ContainerVersionCommentAvatar() {
        super("ContainerVersionCommentDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        containerVersionCommentJPanel.setBackground(BrowserConstants.DIALOGUE_BACKGROUND);
    }

    public void setState(final State state) {
    }

    public State getState() {
        return null;
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_VERSION_COMMENT;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        containerVersionCommentJPanel = new javax.swing.JPanel();
        explanationJLabel = new javax.swing.JLabel();
        commentJScrollPane = new javax.swing.JScrollPane();
        commentJTextArea = new javax.swing.JTextArea();
        okJButton = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        containerVersionCommentJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("ContainerVersionCommentDialog.BorderTitle"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        explanationJLabel.setText(bundle.getString("ContainerVersionCommentDialog.Explanation")); // NOI18N
        explanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        commentJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        commentJTextArea.setColumns(20);
        commentJTextArea.setEditable(false);
        commentJTextArea.setRows(5);
        commentJTextArea.setFocusable(false);
        commentJScrollPane.setViewportView(commentJTextArea);

        org.jdesktop.layout.GroupLayout containerVersionCommentJPanelLayout = new org.jdesktop.layout.GroupLayout(containerVersionCommentJPanel);
        containerVersionCommentJPanel.setLayout(containerVersionCommentJPanelLayout);
        containerVersionCommentJPanelLayout.setHorizontalGroup(
            containerVersionCommentJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(containerVersionCommentJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(containerVersionCommentJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, commentJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, explanationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                .addContainerGap())
        );
        containerVersionCommentJPanelLayout.setVerticalGroup(
            containerVersionCommentJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(containerVersionCommentJPanelLayout.createSequentialGroup()
                .add(explanationJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(commentJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        okJButton.setText(bundle.getString("ContainerVersionCommentDialog.Ok")); // NOI18N
        okJButton.setMaximumSize(new java.awt.Dimension(65, 23));
        okJButton.setMinimumSize(new java.awt.Dimension(65, 23));
        okJButton.setPreferredSize(new java.awt.Dimension(65, 23));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, containerVersionCommentJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, okJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(containerVersionCommentJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(okJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            disposeWindow();
        }
    }// GEN-LAST:event_okJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane commentJScrollPane;
    private javax.swing.JTextArea commentJTextArea;
    private javax.swing.JPanel containerVersionCommentJPanel;
    private javax.swing.JLabel explanationJLabel;
    private javax.swing.JButton okJButton;
    // End of variables declaration//GEN-END:variables
    
    public enum DataKey { CONTAINER_ID, VERSION_ID }

}
