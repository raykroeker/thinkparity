/*
 * ContainerUpdateNoteAvatar.java
 *
 * Created on May 16, 2007, 3:47 AM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextAreaLengthFilter;

import com.thinkparity.codebase.model.container.ContainerConstraints;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author  user
 */
public class UpdateNoteAvatar extends Avatar {

    /** An instance of <code>ContainerConstraints</code>. */
    private final ContainerConstraints containerConstraints;

    /** Creates new form ContainerUpdateNoteAvatar */
    public UpdateNoteAvatar() {
        super("UpdateNoteAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.containerConstraints = ContainerConstraints.getInstance();
        initComponents();
    }

    public void setState(final State state) {
    }

    public State getState() {
        return null;
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_UPDATE_NOTE;
    }

    public void reload() {
        // TODO
    }

    /**
     * Extract the note.
     */
    private String extractNote() {
        return SwingUtil.extract(noteJTextArea, Boolean.TRUE);
    }

    /**
     * Obtain the input container id.
     *
     * @return A container id.
     */
    private Long getInputContainerId() {
        if (input != null) {
            return (Long) ((Data) input).get(DataKey.CONTAINER_ID);
        } else {
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
        final javax.swing.JButton okJButton = ButtonFactory.create();
        final javax.swing.JButton cancelJButton = ButtonFactory.create();
        noteJScrollPane = new javax.swing.JScrollPane();
        noteJTextArea = new javax.swing.JTextArea();

        okJButton.setFont(Fonts.DialogButtonFont);
        okJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateNoteAvatar.OK"));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateNoteAvatar.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        noteJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        noteJTextArea.setColumns(20);
        noteJTextArea.setFont(Fonts.DialogTextEntryFont);
        noteJTextArea.setLineWrap(true);
        noteJTextArea.setRows(5);
        noteJTextArea.setWrapStyleWord(true);
        ((AbstractDocument) noteJTextArea.getDocument()).setDocumentFilter(new JTextAreaLengthFilter(containerConstraints.getDraftComment()));
        noteJScrollPane.setViewportView(noteJTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(noteJScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(okJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, okJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noteJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(okJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            disposeWindow();
            updateNote();
        }
    }//GEN-LAST:event_okJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Update the version note.
     */
    private void updateNote() {
        final Long containerId = getInputContainerId();
        final String note = extractNote();
        getController().runUpdateNote(containerId, note);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane noteJScrollPane;
    private javax.swing.JTextArea noteJTextArea;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { CONTAINER_ID }
}
