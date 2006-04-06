/*
 * InvitePartner.java
 *
 * Created on March 25, 2006, 12:04 PM
 */

package com.thinkparity.browser.application.browser.display.avatar.contact;

import java.awt.Color;

import javax.swing.event.DocumentListener;

import com.thinkparity.browser.application.browser.component.ButtonFactory;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.email.EMailFormatException;

/**
 *
 * @author  raymond
 */
public class InvitePartner extends Avatar {

    /**
     * @see java.io.Serializable
     *
     */
    private static final long serialVersionUID = 1;

    /**
     * Creates new form InvitePartner
     *
     */
    public InvitePartner() {
	super("InvitePartner", Color.WHITE);
	initComponents();
    }
    
    /**
     * Set the avatar state.
     *
     *
     * @param state
     *            The avatar's state information.
     */
    public void setState(final State state) {}
    
    /**
     * Obtain the avatar's state information.
     *
     *
     * @return The avatar's state information.
     */
    public State getState() { return null; }
    
    /**
     * Obtain the avatar id.
     *
     *
     * @return The avatar id.
     */
    public AvatarId getId() { return AvatarId.SESSION_INVITE_PARTNER; }

    public Boolean isInputValid() {
        try { EMailBuilder.parse(extractEMail()); }
        catch(final EMailFormatException emfx) { return Boolean.FALSE; }

        try { extractInvitation(); }
        catch(final Throwable t) { return Boolean.FALSE; }

        return Boolean.TRUE;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JLabel eaJLabel;
        javax.swing.JLabel emailJLabel;
        javax.swing.JLabel invitationJLabel;
        javax.swing.JScrollPane jScrollPane1;
        javax.swing.JButton previewJButton;

        eaJLabel = LabelFactory.create(getString("EmbeddedAssistance"));
        emailJLabel = LabelFactory.create(getString("EmailLabel"));
        emailJTextField = TextFactory.create();
        sendJButton = ButtonFactory.create(getString("SendButton"));
        jScrollPane1 = new javax.swing.JScrollPane();
        invitationJTextArea = TextFactory.createArea();
        invitationJLabel = LabelFactory.create(getString("InvitationLabel"));
        previewJButton = ButtonFactory.create(getString("PreviewButton"));

        emailJTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(final javax.swing.event.DocumentEvent e) {
                emailJTextFieldInsertUpdate(e);
            }
            public void removeUpdate(final javax.swing.event.DocumentEvent e) {
                emailJTextFieldRemoveUpdate(e);
            }
            public void changedUpdate(final javax.swing.event.DocumentEvent e) {
                emailJTextFieldChangedUpdate(e);
            }
        });

        sendJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                sendJButtonActionPerformed(e);
            }
        });

        invitationJTextArea.setColumns(20);
        invitationJTextArea.setRows(5);
        invitationJTextArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(final javax.swing.event.DocumentEvent e) {
                invitationJTextAreaInsertUpdate(e);
            }
            public void removeUpdate(final javax.swing.event.DocumentEvent e) {
                invitationJTextAreaRemoveUpdate(e);
            }
            public void changedUpdate(final javax.swing.event.DocumentEvent e) {
                invitationJTextAreaChangedUpdate(e);
            }
        });
        jScrollPane1.setViewportView(invitationJTextArea);

        previewJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                previewJButtonActionPerformed(e);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(eaJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(emailJLabel)
                            .add(invitationJLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(emailJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(sendJButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(previewJButton))
                            .add(layout.createSequentialGroup()
                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(eaJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(emailJLabel)
                    .add(emailJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(invitationJLabel)
                        .add(123, 123, 123))
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(previewJButton)
                    .add(sendJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void previewJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_previewJButtonActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_previewJButtonActionPerformed

    private String extractEMail() { return emailJTextField.getText(); }

    private String extractInvitation() { return invitationJTextArea.getText(); }

    private void reloadSendJButton() { sendJButton.setEnabled(isInputValid()); }

    private void emailJTextFieldInsertUpdate(final javax.swing.event.DocumentEvent e) {
        reloadSendJButton();
    }

    private void emailJTextFieldRemoveUpdate(final javax.swing.event.DocumentEvent e) {
        reloadSendJButton();
    }

    private void emailJTextFieldChangedUpdate(final javax.swing.event.DocumentEvent e) {
        reloadSendJButton();
    }

    private void invitationJTextAreaInsertUpdate(final javax.swing.event.DocumentEvent e) {
        reloadSendJButton();
    }

    private void invitationJTextAreaRemoveUpdate(final javax.swing.event.DocumentEvent e) {
        reloadSendJButton();
    }

    private void invitationJTextAreaChangedUpdate(final javax.swing.event.DocumentEvent e) {
        reloadSendJButton();
    }

    private void sendJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_sendJButtonActionPerformed
    	if(isInputValid()) {}
    }//GEN-LAST:event_sendJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField emailJTextField;
    private javax.swing.JTextArea invitationJTextArea;
    private javax.swing.JButton sendJButton;
    // End of variables declaration//GEN-END:variables

}
