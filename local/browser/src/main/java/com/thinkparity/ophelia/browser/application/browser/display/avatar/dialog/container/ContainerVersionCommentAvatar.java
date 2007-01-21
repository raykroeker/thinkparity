/*
 * ContainerVersionCommentAvatar.java
 *
 * Created on October 18, 2006, 4:50 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.ContainerVersionProvider;
import com.thinkparity.ophelia.browser.platform.action.Data;
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
    }

    public void setState(final State state) {
    }

    public State getState() {
        return null;
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_VERSION_COMMENT;
    }
    
    public void reload() {
        final Long containerId = getInputContainerId();
        final Long versionId = getInputVersionId();

        // If containerId is null then this call to reload() is too early,
        // the input isn't set up yet.
        if (null!=containerId) {
            final ContainerVersion containerVersion = ((ContainerVersionProvider) contentProvider).readVersion(containerId, versionId);
            if (containerVersion.isSetComment()) {
                commentJTextArea.setText(containerVersion.getComment());
            } else {
                commentJTextArea.setText(null);
            }
        }
    }
    
    /**
     * Obtain the input container id.
     *
     * @return A container id.
     */
    private Long getInputContainerId() {
        if (input!=null) {
            return (Long) ((Data) input).get(DataKey.CONTAINER_ID);
        }
        else {
            return null;
        }
    }
    
    /**
     * Obtain the input container version id.
     *
     * @return A container id.
     */
    private Long getInputVersionId() {
        if (input!=null) {
            return (Long) ((Data) input).get(DataKey.VERSION_ID);
        }
        else {
            return null;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        okJButton = new javax.swing.JButton();
        commentJScrollPane = new javax.swing.JScrollPane();
        commentJTextArea = new javax.swing.JTextArea();

        okJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("ContainerVersionCommentDialog.Ok"));
        okJButton.setMaximumSize(new java.awt.Dimension(65, 23));
        okJButton.setMinimumSize(new java.awt.Dimension(65, 23));
        okJButton.setPreferredSize(new java.awt.Dimension(65, 23));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        commentJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        commentJTextArea.setEditable(false);
        commentJTextArea.setFont(Fonts.DialogFont);
        commentJTextArea.setLineWrap(true);
        commentJTextArea.setWrapStyleWord(true);
        commentJTextArea.setFocusable(false);
        commentJScrollPane.setViewportView(commentJTextArea);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, commentJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .add(okJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(commentJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
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
    private javax.swing.JButton okJButton;
    // End of variables declaration//GEN-END:variables
    
    public enum DataKey { CONTAINER_ID, VERSION_ID }

}
