/*
 * Created on July 14, 2006, 2:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.CreateContainerProvider;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity Create Container Avatar<br>
 * <b>Description:</b>This avatar is used to create containers within a
 * dialogue.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateContainerAvatar extends Avatar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** List of containers. */
    private List<Container> containers = null;
       
    /** Creates new form NewContainerDialogue */
    public CreateContainerAvatar() {
        super("NewContainerDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        initDocumentHandler();
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }
    
    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_CREATE;
    }

    public void setState(final State state) {
    }

    public State getState() {
        return null;
    }
    
    public void reload() {
        // If input is null then this call to reload() is too early,
        // the input isn't set up yet.
        if (input!=null) {
            readContainers();
            reloadExplanation();
            reloadErrorMessage();
            nameJTextField.setText("");
            okJButton.setEnabled(Boolean.FALSE);
            nameJTextField.requestFocusInWindow();
        }
    }
    
    /**
     * Reload the error message.
     */
    private void reloadErrorMessage() {
        if (isInputValid() && !isInputNameUnique()) {
            errorMessageJLabel.setText(getString("ErrorNotUnique"));
        } else {
            errorMessageJLabel.setText("");
        }       
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
     * Determine whether the user input is valid.
     * This method should return false whenever we want the
     * OK button to be disabled.
     * 
     * @return True if the input is valid; false otherwise.
     */
    public Boolean isInputValid() {
        final String name = extractName();
        if (null != name && (0 < name.length())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    /**
     * Check if the name is unique.
     */
    private Boolean isInputNameUnique() {
        if (isInputValid()) {
            Boolean unique = Boolean.TRUE;
            final String newName = extractName();
            for (final Container container : containers) {
                if (container.getName().equalsIgnoreCase(newName)) {
                    unique = Boolean.FALSE;
                    break;
                }
            }
            return unique;
        }
        
        return Boolean.TRUE;
    }
    
    /**
     * Read containers.
     */
    private void readContainers() {
        containers = ((CreateContainerProvider) contentProvider).readContainers();
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

        explanationJLabel.setFont(Fonts.DialogFont);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        explanationJLabel.setText(bundle.getString("NewContainerDialog.Explanation")); // NOI18N
        explanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        nameJLabel.setFont(Fonts.DialogFont);
        nameJLabel.setText(bundle.getString("NewContainerDialog.Name")); // NOI18N

        nameJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                nameJTextFieldActionPerformed(e);
            }
        });

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setText(bundle.getString("NewContainerDialog.ErrorNotUnique")); // NOI18N
        errorMessageJLabel.setMinimumSize(new java.awt.Dimension(290, 15));

        okJButton.setText(bundle.getString("NewContainerDialog.Ok")); // NOI18N
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                okJButtonActionPerformed(e);
            }
        });

        cancelJButton.setText(bundle.getString("NewContainerDialog.Cancel")); // NOI18N
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(errorMessageJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .add(explanationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(nameJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(nameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelJButton)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancelJButton, okJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(explanationJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameJLabel)
                    .add(nameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(errorMessageJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelJButton)
                    .add(okJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nameJTextFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_nameJTextFieldActionPerformed
        okJButtonActionPerformed(evt);
    }// GEN-LAST:event_nameJTextFieldActionPerformed

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            createContainer();
            disposeWindow();
        }
    }// GEN-LAST:event_okJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }                                             
        
    private void initDocumentHandler() {
        Document document = nameJTextField.getDocument();
        document.addDocumentListener( new DocumentHandler() );        
    }
    
    // Enable or disable the OK control. Some notes:
    //    - ActionPerformed on a JTextField will happen when press enter.
    //    - TextChanged message is received after user presses letter but before extractName() will see it.
    //    - The correct way to enable and disable the OK control is with the document interface.    
    class DocumentHandler implements DocumentListener {
        public void insertUpdate( DocumentEvent event ) {
            if ( isInputValid() ) {
                okJButton.setEnabled(Boolean.TRUE);
            }
            else {
                okJButton.setEnabled(Boolean.FALSE);
            }
            reloadErrorMessage();
        }
        
        public void removeUpdate( DocumentEvent event ) {
            // Do the same check as insertUpdate()
            insertUpdate( event );
        }
        
        public void changedUpdate( DocumentEvent event ) {
            // Nothing to do here
        }
    }
    
    /**
     * Reload explanation text.
     */
    private void reloadExplanation() {
        // Adjust the embedded assistance if a list of documents is provided.
        explanationJLabel.setText(getString("Explanation"));          
        if(null != input) {
            final Integer numFiles = (Integer) ((Data) input).get(DataKey.NUM_FILES);
            if (numFiles > 0) {
                final List<File> files = getDataFiles((Data) input, DataKey.FILES);
                if (numFiles == 1) {
                    final String name = (String) files.get(0).getName();
                    explanationJLabel.setText(
                            getString("ExplanationForOneFile",
                            new Object[] { name }));
                } else if (numFiles == 2) {
                    final String name1 = (String) files.get(0).getName();
                    final String name2 = (String) files.get(1).getName();
                    explanationJLabel.setText(
                            getString("ExplanationForTwoFiles",
                            new Object[] { name1, name2 }));
                } else if (numFiles > 2) {
                    final String name1 = (String) files.get(0).getName();
                    final String name2 = (String) files.get(1).getName();
                    explanationJLabel.setText(
                            getString("ExplanationForManyFiles",
                            new Object[] { name1, name2, numFiles }));
                }
            }
        }        
    }
    
    /**
     * If the user presses "OK" or Enter, and input is valid, create the container
     */
    private void createContainer() {
        final String containerName = nameJTextField.getText();
        final Integer numFiles = (Integer) ((Data) input)
                .get(DataKey.NUM_FILES);
        if (numFiles > 0) {
            final List<File> files = getDataFiles((Data) input, DataKey.FILES);
            getController().runCreateContainer(containerName, files);
        } else {
            getController().runCreateContainer(containerName);
        }     
    }
    
    /**
     * Convert the data element found at the given key to a list of files.
     * 
     * @param data
     *            The action data.
     * @param key
     *            The data element key.
     * @return A list of files.
     */
    private List<File> getDataFiles(final Data data, final Enum<?> key) {
        final List<?> list = (List<?>) data.get(key);
        if(null == list) { return null; }
        final List<File> files = new ArrayList<File>();
        for(final Object o : list) { files.add((File) o); }
        return files;
    }    
      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel errorMessageJLabel;
    private javax.swing.JLabel explanationJLabel;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JButton okJButton;
    // End of variables declaration//GEN-END:variables

    public enum DataKey { NUM_FILES, FILES }
}
