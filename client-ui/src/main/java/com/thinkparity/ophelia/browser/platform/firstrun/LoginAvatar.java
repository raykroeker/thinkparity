/*
 * LoginAvatar.java
 *
 * Created on June 10, 2006, 12:05 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
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
    public LoginAvatar(final String username, final String password) {
        super("LoginAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
        reload(username, password);
        initDocumentHandlers();
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

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // These images help to make the rounded corners look good.
        // Note that top left and top right are drawn by the window title.
        g.drawImage(Images.BrowserTitle.DIALOG_BOTTOM_LEFT,
                0,
                getSize().height - Images.BrowserTitle.DIALOG_BOTTOM_LEFT.getHeight(),
                Images.BrowserTitle.DIALOG_BOTTOM_LEFT.getWidth(),
                Images.BrowserTitle.DIALOG_BOTTOM_LEFT.getHeight(), this);
        g.drawImage(Images.BrowserTitle.DIALOG_BOTTOM_RIGHT,
                getSize().width - Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getWidth(),
                getSize().height - Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getHeight(),
                Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getWidth(),
                Images.BrowserTitle.DIALOG_BOTTOM_RIGHT.getHeight(), this);
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload() */
    public void reload() {
        usernameJTextField.requestFocusInWindow();
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State) */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("LoginAvatar#setState");
    }

    protected String getPassword() { return password; }

    protected String getUsername() { return username; }

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
        final javax.swing.JLabel usernameJLabel = LabelFactory.create();
        final javax.swing.JLabel passwordJLabel = LabelFactory.create();
        final javax.swing.JButton cancelJButton = ButtonFactory.create();

        usernameJLabel.setFont(Fonts.DialogFont);
        usernameJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.UsernameLabel"));

        usernameJTextField.setFont(Fonts.DialogTextEntryFont);

        passwordJLabel.setFont(Fonts.DialogFont);
        passwordJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.PasswordLabel"));

        passwordJPasswordField.setFont(usernameJTextField.getFont());

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.ErrorBadCredentials"));

        forgotPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.ForgotPassword"));

        nextJButton.setFont(Fonts.DialogButtonFont);
        nextJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.LoginButton"));
        nextJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.CancelButton"));
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
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(usernameJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, passwordJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, passwordJPasswordField)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, usernameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 253, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(errorMessageJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(forgotPasswordJLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 138, Short.MAX_VALUE)
                        .add(nextJButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelJButton)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancelJButton, nextJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.linkSize(new java.awt.Component[] {passwordJPasswordField, usernameJTextField}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.linkSize(new java.awt.Component[] {passwordJLabel, usernameJLabel}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(33, 33, 33)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(usernameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(usernameJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordJPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(passwordJLabel))
                .add(15, 15, 15)
                .add(errorMessageJLabel)
                .add(14, 14, 14)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(nextJButton)
                    .add(cancelJButton)
                    .add(forgotPasswordJLabel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     *  Initialize the document handler for the username and password fields.
     */
    private void initDocumentHandlers() {
        final DocumentHandler documentHandler = new DocumentHandler();
        javax.swing.text.Document usernameDocument = usernameJTextField.getDocument();
        javax.swing.text.Document passwordDocument = passwordJPasswordField.getDocument();
        usernameDocument.addDocumentListener(documentHandler);
        passwordDocument.addDocumentListener(documentHandler);
    }

    private void nextJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_nextJButtonActionPerformed
        if (isInputValid()) {
            username = extractUsername();
            password = extractPassword();
            disposeWindow();
        }
    }//GEN-LAST:event_nextJButtonActionPerformed

    private void reload(final String username, final String password) {
        reloadUsername(username);
        reloadPassword(password);
        reloadError(username, password);
        nextJButton.setEnabled(isInputValid());
    }

    private void reloadError(final String username, final String password) {
        if (null!=username && username.length()>0 && null!=password && password.length()>0) {
            errorMessageJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.ErrorBadCredentials"));
        } else {
            // Note the space to ensure the dialog leaves room.
            errorMessageJLabel.setText(" ");
        }
    }

    private void reloadPassword(final String password) {
        passwordJPasswordField.setText(password);
    }

    private void reloadUsername(final String username) {
        usernameJTextField.setText(username);
    }

    // Enable or disable the OK control.
    class DocumentHandler implements DocumentListener {
        public void changedUpdate(final DocumentEvent e) {
            maybeEnableOKButton();
        }
        public void insertUpdate(final DocumentEvent e) {
            maybeEnableOKButton();
        }
        public void removeUpdate(final DocumentEvent e) {
            maybeEnableOKButton();
        }
        private void maybeEnableOKButton() {
            nextJButton.setEnabled(isInputValid());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel forgotPasswordJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JButton nextJButton = ButtonFactory.create();
    private final javax.swing.JPasswordField passwordJPasswordField = TextFactory.createPassword();
    private final javax.swing.JTextField usernameJTextField = TextFactory.create();
    // End of variables declaration//GEN-END:variables
}
