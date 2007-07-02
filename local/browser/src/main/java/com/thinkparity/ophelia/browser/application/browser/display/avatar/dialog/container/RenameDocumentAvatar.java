/*
 * RenameDocumentAvatar.java
 *
 * Created on November 10, 2006, 11:15 AM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextComponentLengthFilter;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentConstraints;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.RenameDocumentProvider;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author  Administrator
 */
public class RenameDocumentAvatar extends Avatar {

    /** An instance of <code>DocumentConstraints</code>. */
    private final DocumentConstraints documentConstraints;

    /** List of draft documents for this package. */
    private List<Document> draftDocuments;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField nameJTextField = new javax.swing.JTextField();
    private final javax.swing.JButton okJButton = ButtonFactory.create();
    // End of variables declaration//GEN-END:variables

    /** Creates new form RenameDocumentAvatar */
    public RenameDocumentAvatar() {
        super("RenameDocumentAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.documentConstraints = DocumentConstraints.getInstance();
        initComponents();
        addValidationListener(nameJTextField);
        bindEscapeKey();
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_RENAME_DOCUMENT;
    }

    public State getState() {
        return null;
    }

    public void reload() {
        // If input is null then this call to reload() is too early,
        // the input isn't set up yet.
        if (input!=null) {
            readDraftDocuments();
            reloadName();
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
        final String newName = extractName();
        if (null == newName) {
            addInputError(Separator.Space.toString());
        } else if (newName.equals(getInputDocumentName())) {
            addInputError(Separator.Space.toString());
        } else if (!isInputExtensionValid()) {
            addInputError(getString("ErrorExtension", new Object[] {FileUtil.getExtension(getInputDocumentName())}));
        } else if (!isInputNameUnique()) {
            addInputError(getString("ErrorNotUnique"));
        }
        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
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

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }// GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Extract the name from the control.
     *
     * @return The name.
     */
    private String extractName() {
        return SwingUtil.extract(nameJTextField, Boolean.TRUE);
    }

    /**
     * Obtain the input container id.
     *
     * @return An artifact id.
     */
    private Long getInputContainerId() {
        return (Long) ((Data) input).get(DataKey.CONTAINER_ID);
    }

    /**
     * Obtain the input document id.
     *
     * @return An artifact id.
     */
    private Long getInputDocumentId() {
        return (Long) ((Data) input).get(DataKey.DOCUMENT_ID);
    }

    /**
     * Obtain the input name.
     *
     * @return A name.
     */
    private String getInputDocumentName() {
        return (String) ((Data) input).get(DataKey.DOCUMENT_NAME);
    }  

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel nameJLabel = new javax.swing.JLabel();
        final javax.swing.JButton cancelJButton = ButtonFactory.create();

        nameJLabel.setFont(Fonts.DialogFont);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RenameDocumentAvatar.Name"));

        nameJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) nameJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(documentConstraints.getDocumentName()));
        nameJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameJTextFieldActionPerformed(evt);
            }
        });

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RenameDocumentAvatar.ErrorNotUnique"));
        errorMessageJLabel.setPreferredSize(new java.awt.Dimension(32, 14));

        okJButton.setFont(Fonts.DialogButtonFont);
        okJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RenameDocumentAvatar.OK"));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("RenameDocumentAvatar.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelJButton))
                    .add(errorMessageJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(nameJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(nameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 264, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancelJButton, okJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(nameJLabel))
                .add(16, 16, 16)
                .add(errorMessageJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(17, 17, 17)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(okJButton)
                    .add(cancelJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Check if the extension is valid.
     * The extension is not required if the input name doesn't have one.
     * If there is an extension then it must stay the same.
     */
    private Boolean isInputExtensionValid() {
        if (FileUtil.getName(getInputDocumentName()).equals(getInputDocumentName())) {
            return Boolean.TRUE;
        } else {      
            final String originalExtension = FileUtil.getExtension(getInputDocumentName());
            final String newExtension = FileUtil.getExtension(extractName());
            if (originalExtension.equalsIgnoreCase(newExtension)) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * Check if the name is unique.
     */
    private Boolean isInputNameUnique() {
        Boolean unique = Boolean.TRUE;
        final String newName = extractName();
        if (!newName.equalsIgnoreCase(getInputDocumentName())) {
            for (final Document document : draftDocuments) {
                if (document.getName().equalsIgnoreCase(newName)) {
                    unique = Boolean.FALSE;
                    break;
                }
            }
        }
        return unique;
    }

    private void nameJTextFieldActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_nameJTextFieldActionPerformed
        okJButtonActionPerformed(evt);
    }// GEN-LAST:event_nameJTextFieldActionPerformed

    private void okJButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            disposeWindow();
            getController().runRenameDocument(getInputContainerId(), getInputDocumentId(), extractName());
        }
    }// GEN-LAST:event_okJButtonActionPerformed

    /**
     * Read draft documents.
     */
    private void readDraftDocuments() {
        final Long containerId = getInputContainerId();
        draftDocuments = ((RenameDocumentProvider) contentProvider).readDraftDocuments(containerId);
    }

    /**
     *  Reload the name text control.
     */
    private void reloadName() {
        nameJTextField.setText("");
        if (null != input) {
            final String name = getInputDocumentName();
            nameJTextField.setText(name);
            nameJTextField.select(0, FileUtil.getName(name).length());
        }
        nameJTextField.requestFocusInWindow();
    }

    public enum DataKey { CONTAINER_ID, DOCUMENT_ID, DOCUMENT_NAME }
}
