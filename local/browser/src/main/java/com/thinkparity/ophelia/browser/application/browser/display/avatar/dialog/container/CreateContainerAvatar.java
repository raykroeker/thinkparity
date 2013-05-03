/*
 * Created on July 14, 2006, 2:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.text.AbstractDocument;

import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.codebase.model.container.ContainerConstraints;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
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

    /** An instance of <code>ContainerConstraints</code>. */
    private final ContainerConstraints containerConstraints;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JTextField nameJTextField = TextFactory.create();
    private final javax.swing.JButton okJButton = ButtonFactory.create();
    // End of variables declaration//GEN-END:variables

    /** Creates new form CreateContainerAvatar */
    public CreateContainerAvatar() {
        super("CreateContainerAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.containerConstraints = ContainerConstraints.getInstance();
        initComponents();
        addValidationListener(nameJTextField);
        bindEscapeKey();
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_CONTAINER_CREATE;
    }

    public State getState() {
        return null;
    }

    public void reload() {
        // If input is null then this call to reload() is too early,
        // the input isn't set up yet.
        if (input!=null) {
            nameJTextField.setText("");
            nameJTextField.requestFocusInWindow();
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
        if (null == extractName()) {
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

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }

    /**
     * If the user presses "OK" or Enter, and input is valid, create the container
     */
    private void createContainer() {
        final String containerName = extractName();
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
     * Extract the name from the control.
     *
     * @return The name.
     */
    private String extractName() {
        return SwingUtil.extract(nameJTextField, Boolean.TRUE);
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
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateContainerAvatar.Name"));

        nameJTextField.setFont(Fonts.DialogTextEntryFont);
        ((AbstractDocument) nameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(containerConstraints.getContainerName()));
        nameJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameJTextFieldActionPerformed(evt);
            }
        });

        okJButton.setFont(Fonts.DialogButtonFont);
        okJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateContainerAvatar.Ok"));
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("CreateContainerAvatar.Cancel"));
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
                    .add(layout.createSequentialGroup()
                        .add(nameJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(nameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
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
                .add(24, 24, 24)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameJLabel)
                    .add(nameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 47, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelJButton)
                    .add(okJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nameJTextFieldActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_nameJTextFieldActionPerformed
        okJButtonActionPerformed(evt);
    }// GEN-LAST:event_nameJTextFieldActionPerformed    

    private void okJButtonActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okJButtonActionPerformed
        if (isInputValid()) {
            disposeWindow();
            createContainer();
        }
    }// GEN-LAST:event_okJButtonActionPerformed

    public enum DataKey { FILES, NUM_FILES }
}
