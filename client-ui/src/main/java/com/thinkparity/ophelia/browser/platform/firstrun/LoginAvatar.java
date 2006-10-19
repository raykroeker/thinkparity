/*
 * LoginAvatar.java
 *
 * Created on June 10, 2006, 12:05 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author raymond@thinkparity.com
 * @revision $Revision$
 */
public class LoginAvatar extends Avatar {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The username. */
    private String username;

    /** The password. */
    private String password;

    /** Create LoginAvatar. */
    public LoginAvatar() {
        super("LoginAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        bindEscapeKey("Cancel", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });
        bindEnterKey("Next", new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                nextJButtonActionPerformed(e);
            }
        });
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId() */
    public AvatarId getId() { return AvatarId.DIALOG_PLATFORM_LOGIN; }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState() */
    public State getState() {
        throw Assert.createNotYetImplemented("LoginAvatar#getState");
    }

    /** @see com.thinkparity.codebase.swing.AbstractJPanel#isInputValid() */
    public Boolean isInputValid() {
        final String username = extractUsername();
        final String password = extractPassword();
        return null != username && 0 < username.length()
                && null != password && 0 < password.length();
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload() */
    public void reload() { usernameJTextField.requestFocusInWindow(); }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State) */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("LoginAvatar#setState");
    }

    String getPassword() { return password; }

    String getTitle() { return getString("Title"); }

    String getUsername() { return username; }

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        password = username = null;
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private String extractPassword() { return SwingUtil.extract(passwordJPasswordField); }

    private String extractUsername() { return SwingUtil.extract(usernameJTextField); }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JButton cancelJButton;
        javax.swing.JLabel eaJLabel;
        javax.swing.JPanel loginInfoJPanel;
        javax.swing.JButton nextJButton;
        javax.swing.JLabel passwordJLabel;
        javax.swing.JLabel usernameJLabel;

        loginInfoJPanel = new javax.swing.JPanel();
        eaJLabel = LabelFactory.create();
        usernameJLabel = LabelFactory.create();
        passwordJLabel = LabelFactory.create();
        usernameJTextField = TextFactory.create();
        passwordJPasswordField = TextFactory.createPassword();
        nextJButton = ButtonFactory.create();
        cancelJButton = ButtonFactory.create();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/JPanel_Messages"); // NOI18N
        loginInfoJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("LoginAvatar.LoginInfoTitle"))); // NOI18N
        loginInfoJPanel.setOpaque(false);
        eaJLabel.setText(bundle.getString("LoginAvatar.EmbeddedAssistance")); // NOI18N

        usernameJLabel.setText(bundle.getString("LoginAvatar.UsernameLabel")); // NOI18N

        passwordJLabel.setText(bundle.getString("LoginAvatar.PasswordLabel")); // NOI18N

        passwordJPasswordField.setFont(usernameJTextField.getFont());

        nextJButton.setText(bundle.getString("LoginAvatar.LoginButton")); // NOI18N
        nextJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setText(bundle.getString("LoginAvatar.CancelButton")); // NOI18N
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout loginInfoJPanelLayout = new org.jdesktop.layout.GroupLayout(loginInfoJPanel);
        loginInfoJPanel.setLayout(loginInfoJPanelLayout);
        loginInfoJPanelLayout.setHorizontalGroup(
            loginInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, loginInfoJPanelLayout.createSequentialGroup()
                .add(loginInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, loginInfoJPanelLayout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(eaJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, loginInfoJPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(loginInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(usernameJLabel)
                            .add(passwordJLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(loginInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(usernameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .add(passwordJPasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)))
                    .add(loginInfoJPanelLayout.createSequentialGroup()
                        .addContainerGap(159, Short.MAX_VALUE)
                        .add(cancelJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(nextJButton)))
                .addContainerGap())
        );
        loginInfoJPanelLayout.setVerticalGroup(
            loginInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(loginInfoJPanelLayout.createSequentialGroup()
                .add(eaJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(loginInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(usernameJLabel)
                    .add(usernameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(loginInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordJLabel)
                    .add(passwordJPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(loginInfoJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nextJButton)
                    .add(cancelJButton))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(loginInfoJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(loginInfoJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nextJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_nextJButtonActionPerformed
        if(isInputValid()) {
            username = extractUsername();
            password = extractPassword();
            disposeWindow();
        }
    }//GEN-LAST:event_nextJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField passwordJPasswordField;
    private javax.swing.JTextField usernameJTextField;
    // End of variables declaration//GEN-END:variables
}
