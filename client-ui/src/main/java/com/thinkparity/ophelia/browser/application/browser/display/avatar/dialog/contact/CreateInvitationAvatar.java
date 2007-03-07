/*
 * Created On: July 10, 2006, 4:30 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.contact;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.email.EMailFormatException;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity Create Invitation Avatar<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateInvitationAvatar extends Avatar {

    /**
     * Create CreateInvitationAvatar.
     *
     */
    public CreateInvitationAvatar() {
        super("CreateInvitation", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        emailJTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                reloadAddJButton();
            }
            public void insertUpdate(DocumentEvent e) {
                reloadAddJButton();
            }
            public void removeUpdate(DocumentEvent e) {
                reloadAddJButton();
            }
        });
        bindEnterKey("Add", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                addJButtonActionPerformed(e);
            }
        });
        bindEscapeKey("Cancel", new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
    }

    /**
     * Set the avatar state.
     * 
     * 
     * @param state
     *            The avatar's state information.
     */
    public void setState(final State state) { throw Assert.createUnreachable("CreateInvitation#setState()"); }

    /**
     * @see Avatar#getAvatarTitle()
     * 
     */
    @Override
    public String getAvatarTitle() {
        return getString("Title");
    }

    /**
     * Obtain the avatar's state information.
     * 
     * 
     * @return The avatar's state information.
     */
    public State getState() { throw Assert.createUnreachable("CreateInvitation#getState()"); }

    /**
     * Obtain the avatar id.
     * 
     * 
     * @return The avatar id.
     */
    public AvatarId getId() { return AvatarId.DIALOG_CONTACT_CREATE_OUTGOING_INVITATION; }

    /**
     * @see com.thinkparity.codebase.swing.AbstractJPanel#isInputValid()
     */
    @Override
    public Boolean isInputValid() {
        try {
            extractEmail();
            return Boolean.TRUE;
        } catch(final EMailFormatException efx) {
            return Boolean.FALSE;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadAddJButton();
    }

    /**
     * Extract the e-mail from the text field.
     * 
     * @return The e-mail address.
     */
    private EMail extractEmail() {
        return EMailBuilder.parse(SwingUtil.extract(emailJTextField));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JPanel addContactJPanel;
        javax.swing.JButton cancelJButton;
        javax.swing.JLabel emailJLabel;

        addContactJPanel = new javax.swing.JPanel();
        addJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();
        emailJLabel = LabelFactory.create();
        emailJTextField = TextFactory.create();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        addContactJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("AddContact.Title"))); // NOI18N
        addContactJPanel.setOpaque(false);
        addJButton.setText(bundle.getString("AddContact.AddButton")); // NOI18N
        addJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addJButtonActionPerformed(e);
            }
        });

        cancelJButton.setText(bundle.getString("AddContact.CancelButton")); // NOI18N
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });

        emailJLabel.setText(bundle.getString("AddContact.EmailLabel")); // NOI18N

        org.jdesktop.layout.GroupLayout addContactJPanelLayout = new org.jdesktop.layout.GroupLayout(addContactJPanel);
        addContactJPanel.setLayout(addContactJPanelLayout);
        addContactJPanelLayout.setHorizontalGroup(
            addContactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(addContactJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(addContactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, addContactJPanelLayout.createSequentialGroup()
                        .add(cancelJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(addJButton))
                    .add(addContactJPanelLayout.createSequentialGroup()
                        .add(emailJLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(emailJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)))
                .addContainerGap())
        );

        addContactJPanelLayout.linkSize(new java.awt.Component[] {addJButton, cancelJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        addContactJPanelLayout.setVerticalGroup(
            addContactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, addContactJPanelLayout.createSequentialGroup()
                .add(addContactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(emailJLabel)
                    .add(emailJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(addContactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addJButton)
                    .add(cancelJButton))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(addContactJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(addContactJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void addJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_addJButtonActionPerformed
        if(isInputValid()) {
            disposeWindow();
            final EMail email = extractEmail();
            getController().runCreateContactOutgoingEMailInvitation(email);
        }
    }//GEN-LAST:event_addJButtonActionPerformed

    private void reloadAddJButton() { addJButton.setEnabled(isInputValid()); }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJButton;
    private javax.swing.JTextField emailJTextField;
    // End of variables declaration//GEN-END:variables
}
