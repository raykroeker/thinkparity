/*
 * RenameDocumentAvatar.java
 *
 * Created on November 10, 2006, 11:15 AM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
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
    
    /** List of draft documents for this package. */
    private Document[] draftDocuments;

    /** Creates new form RenameDocumentAvatar */
    public RenameDocumentAvatar() {
        super("RenameDocumentDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        initDocumentHandler();
        bindEscapeKey();
    }

    public void setState(final State state) {
    }

    public State getState() {
        return null;
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_RENAME_DOCUMENT;
    }

    public void reload() {
        // If input is null then this call to reload() is too early,
        // the input isn't set up yet.
        if (input!=null) {
            readDraftDocuments();
            reloadExplanation();
            reloadName();
            reloadErrorMessage();
        }
    }
    
    /**
     *  Initialize the document handler for the name text field.
     */
    private void initDocumentHandler() {
        javax.swing.text.Document document = nameJTextField.getDocument();
        document.addDocumentListener( new DocumentHandler() );   
    }
    
    /**
     * Make the escape key behave like cancel.
     */
    private void bindEscapeKey() {
        bindEscapeKey("Cancel", new AbstractAction() {
            /** @see java.io.Serializable */
            private static final long serialVersionUID = 1;

            /** @see javax.swing.ActionListener#actionPerformed(java.awt.event.ActionEvent) */
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
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
    
    /**
     * Determine whether the user input is valid.
     * This method should return false whenever we want the
     * OK button to be disabled.
     * 
     * @return True if the input is valid; false otherwise.
     */
    public Boolean isInputValid() {
        if (isInputEntered() && isInputExtensionValid() && isInputNameUnique()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    /**
     * Check if the user typed a name.
     */
    private Boolean isInputEntered() {
        final String name = extractName();
        if (null != name && (0 < name.length())) {
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }
    
    /**
     * Check if the extension is valid.
     * The extension is not required if the input name doesn't have one.
     * If there is an extension then it must stay the same.
     */
    private Boolean isInputExtensionValid() {
        if (FileUtil.getName(getInputDocumentName()).equals(getInputDocumentName())) {
            return Boolean.TRUE;
        }
        
        if (isInputEntered()) {        
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
        if (isInputEntered()) { 
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
        
        return Boolean.FALSE;
    }
       
    /**
     * Read draft documents.
     */
    private void readDraftDocuments() {
        final Long containerId = getInputContainerId();
        draftDocuments = (Document[])((RenameDocumentProvider) contentProvider).readDraftDocuments(containerId);
    }  
    
    /**
     * Extract the name from the control.
     *
     * @return The name.
     */
    private String extractName() {
        String name = SwingUtil.extract(nameJTextField);
        if (null!=name) {
            return name.trim();
        } else {
            return name;
        }
    }
    
    /**
     * Reload the explanation control.
     */
    private void reloadExplanation() {
        explanationJLabel.setText(getString("Explanation", new Object[] {getInputDocumentName()} ));
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
    
    /**
     * Reload the error message.
     */
    private void reloadErrorMessage() {
        if (!isInputEntered()) {
            errorMessageJLabel.setText("");
        } else if (!isInputExtensionValid()) {
            errorMessageJLabel.setText(getString("ErrorExtension", new Object[] {FileUtil.getExtension(getInputDocumentName())}));
        } else if (!isInputNameUnique()) {
            errorMessageJLabel.setText(getString("ErrorNotUnique"));
        } else {
            errorMessageJLabel.setText("");
        }
       
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton cancelJButton;
        javax.swing.JLabel nameJLabel;

        explanationJLabel = new javax.swing.JLabel();
        nameJLabel = new javax.swing.JLabel();
        nameJTextField = new javax.swing.JTextField();
        errorMessageJLabel = new javax.swing.JLabel();
        okJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();

        explanationJLabel.setFont(new java.awt.Font("Arial", 0, 12));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        explanationJLabel.setText(bundle.getString("RenameDocumentDialog.Explanation")); // NOI18N
        explanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        nameJLabel.setFont(new java.awt.Font("Arial", 0, 12));
        nameJLabel.setText(bundle.getString("RenameDocumentDialog.Name")); // NOI18N

        nameJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameJTextFieldActionPerformed(evt);
            }
        });

        errorMessageJLabel.setFont(new java.awt.Font("Arial", 0, 12));
        errorMessageJLabel.setText(bundle.getString("RenameDocumentDialog.ErrorNotUnique")); // NOI18N
        errorMessageJLabel.setPreferredSize(new java.awt.Dimension(32, 14));

        okJButton.setText(bundle.getString("RenameDocumentDialog.OK")); // NOI18N
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setText(bundle.getString("RenameDocumentDialog.Cancel")); // NOI18N
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
                    .add(errorMessageJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                    .add(explanationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelJButton))
                    .add(layout.createSequentialGroup()
                        .add(nameJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(nameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancelJButton, okJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(explanationJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameJLabel)
                    .add(nameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(errorMessageJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(okJButton)
                    .add(cancelJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nameJTextFieldActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_nameJTextFieldActionPerformed
        okJButtonActionPerformed(evt);
    }// GEN-LAST:event_nameJTextFieldActionPerformed

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }// GEN-LAST:event_cancelJButtonActionPerformed

    private void okJButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okJButtonActionPerformed
        if(isInputValid()) {
            final Long documentId = getInputDocumentId();
            final String documentName = extractName();
            if (!documentName.equals(getInputDocumentName())) {
                getController().runRenameDocument(documentId, documentName);
            }
            disposeWindow();
        }
    }// GEN-LAST:event_okJButtonActionPerformed
    
    // Enable or disable the OK control.
    class DocumentHandler implements DocumentListener {
        public void changedUpdate(final DocumentEvent e) {
            // Nothing to do here
        }

        public void insertUpdate(final DocumentEvent e) {
            if (isInputValid()) {
                okJButton.setEnabled(Boolean.TRUE);
            }
            else {
                okJButton.setEnabled(Boolean.FALSE);
            }
            reloadErrorMessage();
        }

        public void removeUpdate(final DocumentEvent e) {
            // Do the same check as insertUpdate()
            insertUpdate(e);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel errorMessageJLabel;
    private javax.swing.JLabel explanationJLabel;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JButton okJButton;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { CONTAINER_ID, DOCUMENT_ID, DOCUMENT_NAME }
}
