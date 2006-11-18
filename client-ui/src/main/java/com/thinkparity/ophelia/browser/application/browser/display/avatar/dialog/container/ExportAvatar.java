/*
 * ExportAvatar.java
 *
 * Created on November 14, 2006, 1:47 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.swing.JFileChooserUtil;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Keys;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.ExportProvider;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;
import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;
import com.thinkparity.ophelia.model.Constants;

/**
 *
 * @author  Administrator
 */
public class ExportAvatar extends Avatar {
       
    /** A persistence for saving settings. */
    private final Persistence persistence;   

    /** Creates new form ExportAvatar */
    public ExportAvatar() {
        super("ExportDialog", BrowserConstants.DIALOGUE_BACKGROUND);
        this.persistence = PersistenceFactory.getPersistence(getClass());
        initComponents();
        bindEscapeKey();
    }

    public void setState(final State state) {
    }

    public State getState() {
        return null;
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_EXPORT;
    }
    
    public void reload() {
        // If input is null then this call to reload() is too early,
        // the input isn't set up yet.
        if (input!=null) {
            reloadExplanation();
            reloadDirectory();
            okJButton.setEnabled(isInputValid());
        }
        directoryJButton.requestFocusInWindow();
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
     * Get the avatar title.
     * 
     * @return the avatar title
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getAvatarTitle()
     */
    @Override
    public String getAvatarTitle() {
        if (input==null) {
            return null;
        } else {
            final ExportType exportType = getExportType();
            if (exportType==ExportType.CONTAINER) {
                return getString("TitleExportContainer");
            } else {
                return getString("TitleExportVersion");
            }
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
        String directory = SwingUtil.extract(directoryJTextField);
        if (null != directory && (0 < directory.length())) {
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }
    
    /**
     * Obtain the export type.
     *
     * @return The export type.
     */
    private ExportType getExportType() {
        return (ExportType) ((Data) input).get(DataKey.EXPORT_TYPE);
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
    private Long getInputVersionId() {
        return (Long) ((Data) input).get(DataKey.VERSION_ID);
    }
    
    /**
     * Reload the explanation control.
     */
    private void reloadExplanation() {
        final ExportType exportType = getExportType();
        final Long containerId = getInputContainerId();
        final String name = ((ExportProvider) contentProvider).getContainerName(containerId);  
        if (exportType==ExportType.CONTAINER) {
            explanationJLabel.setText(getString("ExplanationContainer", new Object[] {name}));
        } else {
            final Long versionId = getInputVersionId();
            final Calendar publishDate = ((ExportProvider) contentProvider).getPublishDate(containerId, versionId);
            explanationJLabel.setText(getString("ExplanationVersion", new Object[] {publishDate.getTime(), name}));
        }
    }
    
    /**
     * Reload the directory control.
     */
    private void reloadDirectory() {
        final File startingDirectory = getStartingDirectory();
        if (null != startingDirectory) {
            directoryJTextField.setText(startingDirectory.getAbsolutePath());
        } else {
            directoryJTextField.setText(null);
        }
    }
    
    /**
     * Get the starting directory.
     */
    private File getStartingDirectory() {
        final File persistedDirectory = persistence.get(Keys.Persistence.CONTAINER_EXPORT_SELECTED_DIRECTORY, (File) null);
        if ((null != persistedDirectory) && persistedDirectory.isDirectory()) {
            return persistedDirectory;
        } else {
            final File defaultDirectory = Constants.Directories.USER_DATA; 
            if ((null != defaultDirectory) && defaultDirectory.isDirectory()) {
                return defaultDirectory;
            } else {
                return null;
            }
        }
    }
    
    /**
     * Obtain the file chooser.
     * 
     * @return The file chooser.
     */
    private JFileChooser getJFileChooser() {
        return getJFileChooser(Boolean.FALSE);
    }
    
    /**
     * Obtain the file chooser for folder selection.
     * 
     * @param initialize
     *            Set to true to initialize the JFileChooser.
     * @return The file chooser.
     */
    private JFileChooser getJFileChooser(final Boolean initialize) {
        if (initialize) {
            return JFileChooserUtil.getJFileChooser(JFileChooser.DIRECTORIES_ONLY, Boolean.FALSE,
                    getString("JFileChooserTitle"), getStartingDirectory());
        } else {
            return JFileChooserUtil.getJFileChooser();
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

        explanationJLabel = new javax.swing.JLabel();
        directoryJTextField = new javax.swing.JTextField();
        directoryJButton = new javax.swing.JButton();
        okJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        explanationJLabel.setText(bundle.getString("ExportDialog.ExplanationVersion")); // NOI18N
        explanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        explanationJLabel.setFocusable(false);

        directoryJTextField.setEditable(false);
        directoryJTextField.setText(bundle.getString("ExportDialog.ExampleText")); // NOI18N

        directoryJButton.setText(bundle.getString("ExportDialog.DirectoryButton")); // NOI18N
        directoryJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directoryJButtonActionPerformed(evt);
            }
        });

        okJButton.setText(bundle.getString("ExportDialog.OK")); // NOI18N
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setText(bundle.getString("ExportDialog.Cancel")); // NOI18N
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
                    .add(explanationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(directoryJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(directoryJButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okJButton)
                        .add(7, 7, 7)
                        .add(cancelJButton)))
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
                    .add(directoryJButton)
                    .add(directoryJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 13, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(okJButton)
                    .add(cancelJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void directoryJButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_directoryJButtonActionPerformed
        if(JFileChooser.APPROVE_OPTION == getJFileChooser(Boolean.TRUE).showOpenDialog(this)) {
            // Get the path and see if it is valid.
            // Note that getJFileChooser().getSelectedDirectory() does not do what we want, it gets the parent directory.
            final File directory = getJFileChooser().getSelectedFile();
            if (directory.isDirectory()) {            
                directoryJTextField.setText(directory.getAbsolutePath());
                okJButton.setEnabled(isInputValid());
                persistence.set(
                        Keys.Persistence.CONTAINER_EXPORT_SELECTED_DIRECTORY,
                        getJFileChooser().getSelectedFile());
            }
        }
    }// GEN-LAST:event_directoryJButtonActionPerformed

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed
    
    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        if(isInputValid()) {
            File directory = new File(SwingUtil.extract(directoryJTextField));
            final ExportType exportType = getExportType();
            final Long containerId = getInputContainerId();
            if (exportType==ExportType.CONTAINER) {
                getController().runExport(containerId, directory);                 
            } else {
                final Long versionId = getInputVersionId();
                getController().runExportVersion(containerId, versionId, directory);
            }
            disposeWindow();
        }
    }//GEN-LAST:event_okJButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton directoryJButton;
    private javax.swing.JTextField directoryJTextField;
    private javax.swing.JLabel explanationJLabel;
    private javax.swing.JButton okJButton;
    // End of variables declaration//GEN-END:variables

    public enum ExportType { CONTAINER, VERSION }
    public enum DataKey { EXPORT_TYPE, CONTAINER_ID, VERSION_ID }
}
