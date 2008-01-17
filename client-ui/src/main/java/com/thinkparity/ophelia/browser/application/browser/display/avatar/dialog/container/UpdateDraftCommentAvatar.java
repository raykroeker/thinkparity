/*
 * UpdateDraftCommentAvatar.java
 *
 * Created on May 16, 2007, 3:47 AM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.AbstractDocument;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextComponentLengthFilter;

import com.thinkparity.codebase.model.container.ContainerConstraints;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.UpdateDraftCommentProvider;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author robert@thinkparity.com
 */
public class UpdateDraftCommentAvatar extends Avatar {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JTextArea commentJTextArea = new javax.swing.JTextArea();
    private final javax.swing.JButton okJButton = ButtonFactory.create();
    // End of variables declaration//GEN-END:variables

    /** An instance of <code>ContainerConstraints</code>. */
    private final ContainerConstraints containerConstraints;

    /** The initial comment <code>String</code>. */
    private String initialComment;

    /** Creates new form UpdateDraftCommentAvatar */
    public UpdateDraftCommentAvatar() {
        super("UpdateDraftCommentAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.containerConstraints = ContainerConstraints.getInstance();
        initComponents();
        addValidationListener(commentJTextArea);
        bindEscapeKey();
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_UPDATE_DRAFT_COMMENT;
    }

    public State getState() {
        return null;
    }

    public void reload() {
        if (input != null) {
            reloadComment();
            commentJTextArea.setCaretPosition(0); //scroll to top
            validateInput();
        }
    }

    public void setState(final State state) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     */
    @Override
    protected void validateInput() {
        super.validateInput();
        final String comment = extractComment();
        if (null == comment && null == initialComment) {
            addInputError(Separator.Space.toString());
        } else if (null != comment
                && null != initialComment
                && comment.equals(initialComment)) {
            addInputError(Separator.Space.toString());
        }
        okJButton.setEnabled(!containsInputErrors());
    }

    /**
     * Make the escape key behave like cancel.
     */
    private void bindEscapeKey() {
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Extract the comment.
     */
    private String extractComment() {
        final String comment = SwingUtil.extract(commentJTextArea, Boolean.FALSE);
        return StringUtil.trimTail(comment);
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
        final javax.swing.JScrollPane commentJScrollPane = new javax.swing.JScrollPane();
        final javax.swing.JButton cancelJButton = ButtonFactory.create();

        commentJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        commentJTextArea.setFont(Fonts.DialogTextEntryFont);
        commentJTextArea.setLineWrap(true);
        commentJTextArea.setWrapStyleWord(true);
        ((AbstractDocument) commentJTextArea.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(containerConstraints.getDraftComment()));
        commentJScrollPane.setViewportView(commentJTextArea);

        okJButton.setFont(Fonts.DialogButtonFont);
        okJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateDraftCommentAvatar.OK"));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateDraftCommentAvatar.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(commentJScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
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
                .addGap(24, 24, 24)
                .addComponent(commentJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(okJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void okJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            disposeWindow();
            updateComment();
        }
    }//GEN-LAST:event_okJButtonActionPerformed

    /**
     * Reload the comment.
     */
    private void reloadComment() {
        final Long containerId = getInputContainerId();
        initialComment = ((UpdateDraftCommentProvider) contentProvider).readComment(containerId);
        commentJTextArea.setText(initialComment);
    }

    /**
     * Update the version comment.
     */
    private void updateComment() {
        final Long containerId = getInputContainerId();
        final String comment = extractComment();
        getController().runUpdateDraftComment(containerId, comment);
    }

    public enum DataKey { CONTAINER_ID }
}
