/*
 * Created On: July 10, 2006, 4:30 PM
 */
package com.thinkparity.browser.application.browser.display.avatar.contact;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

/**
 *
 * @author raymond@thinkparity.com
 */
public class AddContact extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Create CreateInvitation */
    public AddContact() {
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
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                addJButtonActionPerformed(e);
            }
        });
        bindEscapeKey("Cancel", new AbstractAction() {
            private static final long serialVersionUID = 1;
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
    public AvatarId getId() { return AvatarId.CONTACT_ADD; }

    /**
     * @see com.thinkparity.browser.javax.swing.AbstractJPanel#isInputValid()
     */
    @Override
    public Boolean isInputValid() {
        final String emailAddress = extractEmail();
        if(null == emailAddress) { return Boolean.FALSE; }
        final int indexOfAt = emailAddress.indexOf('@');
        if(-1 == indexOfAt) { return Boolean.FALSE; }
        final int indexOfDot = emailAddress.indexOf('.', indexOfAt + 1);
        if(-1 == indexOfDot) { return Boolean.FALSE; }
        if(emailAddress.length() <= indexOfDot + 1) { return Boolean.FALSE; }
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
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
    private String extractEmail() {
        return SwingUtil.extract(emailJTextField);
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

        addContactJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("AddContact.Title")));
        addContactJPanel.setOpaque(false);
        addJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("AddContact.AddButton"));
        addJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("AddContact.CancelButton"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        emailJLabel.setText(java.util.ResourceBundle.getBundle("com/thinkparity/browser/platform/util/l10n/JPanel_Messages").getString("AddContact.EmailLabel"));

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
                        .add(emailJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)))
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

    private void disposeWindow() {
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void addJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_addJButtonActionPerformed
        if(isInputValid()) {
            disposeWindow();
            final String emailAddress = extractEmail();
            getController().runAddContact(emailAddress);
        }
    }//GEN-LAST:event_addJButtonActionPerformed

    private void reloadAddJButton() { addJButton.setEnabled(isInputValid()); }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJButton;
    private javax.swing.JTextField emailJTextField;
    // End of variables declaration//GEN-END:variables
}
