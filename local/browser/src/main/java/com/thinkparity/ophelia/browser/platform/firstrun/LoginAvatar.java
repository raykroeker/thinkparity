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

import com.thinkparity.codebase.StringUtil.Separator;
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
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity OpheliaUI Login Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class LoginAvatar extends Avatar implements LoginSwingDisplay {

    /** The <code>Platform</code>. */
    private final Platform platform;

    /** The valid credentials flag. */
    private Boolean validCredentials;

    /** The username. */
    private String username;

    /** The password. */
    private String password;

    /** Signup flag. */
    private Boolean signup;

    /**
     * Create LoginAvatar.
     *
     */
    public LoginAvatar() {
        super("LoginAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.validCredentials = Boolean.TRUE;
        this.password = this.username = null;
        this.platform = BrowserPlatform.getInstance();
        initComponents();
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

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#dispose()
     */
    public void dispose() {
        disposeWindow();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    protected void validateInput() {
        super.validateInput();
        if (Boolean.FALSE == platform.isOnline())
            addInputError(getString("ErrorOffline"));
        final String username = extractUsername();
        final String password = extractPassword();
        if (null == username)
            addInputError(Separator.Space.toString());
        if (null == password)
            addInputError(Separator.Space.toString());

        errorMessageJLabel.setText(" ");
        if (containsInputErrors())
            errorMessageJLabel.setText(getInputErrors().get(0));
        nextJButton.setEnabled(!containsInputErrors());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_LOGIN;
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState() */
    public State getState() {
        throw Assert.createNotYetImplemented("LoginAvatar#getState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#installProgressBar()
     *
     */
    public void installProgressBar() {
        loginJProgressBar.setIndeterminate(true);
        progressBarJPanel.setVisible(true);
        buttonBarJPanel.setVisible(false);
        validate();
    }

    /**
     * Determine if signup was selected.
     * 
     * @return true if signup was selected, false otherwise.
     */
    public Boolean isSignup() {
        return signup;
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
        reloadUsername(username);
        reloadPassword(password);
        reloadError(validCredentials);
        reloadProgressBar();
        usernameJTextField.requestFocusInWindow();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#resetProgressBar()
     */
    public void resetProgressBar() {
        reload();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#setDetermination(java.lang.Integer)
     */
    public void setDetermination(final Integer steps) {
        loginJProgressBar.setMinimum(0);
        loginJProgressBar.setMaximum(steps);
        loginJProgressBar.setIndeterminate(false);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("LoginAvatar#setState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#setValidCredentials(java.lang.Boolean)
     * 
     */
    public void setValidCredentials(final Boolean validCredentials) {
        this.validCredentials = validCredentials;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.LoginSwingDisplay#updateProgress(java.lang.Integer,
     *      java.lang.String)
     * 
     */
    public void updateProgress(final Integer step, final String note) {
        loginJProgressBar.setValue(step);
        if (null != note && 0 < note.trim().length()) {
            stepJLabel.setText(note);
        } else {
            stepJLabel.setText(" ");
        }
    }

    protected String getPassword() { return password; }

    protected String getUsername() { return username; }

    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        password = username = null;
        signup = Boolean.FALSE;
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private ThinkParitySwingMonitor createMonitor() {
        return new LoginSwingMonitor(this);
    }

    private String extractPassword() {
        return SwingUtil.extract(passwordJPasswordField, Boolean.TRUE);
    }

    private String extractUsername() {
        return SwingUtil.extract(usernameJTextField, Boolean.TRUE);
    }

    private void forgotPasswordJLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forgotPasswordJLabelMousePressed
        platform.runResetPassword();
    }//GEN-LAST:event_forgotPasswordJLabelMousePressed

    /**
     * Obtain the text for the step label.
     * 
     * @return A text <code>String<code>.
     */
    private String getStepJLabelText() {
        return getString("LoginAvatar.progressBarJPanel.stepJLabel");
    }

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

        buttonBarJPanel.setOpaque(false);
        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.ErrorBadCredentials"));
        errorMessageJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        signUpJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.SignUp"));
        signUpJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                signUpJLabelMousePressed(evt);
            }
        });

        forgotPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.ForgotPassword"));
        forgotPasswordJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                forgotPasswordJLabelMousePressed(evt);
            }
        });

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

        org.jdesktop.layout.GroupLayout buttonBarJPanelLayout = new org.jdesktop.layout.GroupLayout(buttonBarJPanel);
        buttonBarJPanel.setLayout(buttonBarJPanelLayout);
        buttonBarJPanelLayout.setHorizontalGroup(
            buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, buttonBarJPanelLayout.createSequentialGroup()
                .add(buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, errorMessageJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                    .add(buttonBarJPanelLayout.createSequentialGroup()
                        .add(buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(forgotPasswordJLabel)
                            .add(signUpJLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 28, Short.MAX_VALUE)
                        .add(nextJButton)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cancelJButton))
        );

        buttonBarJPanelLayout.linkSize(new java.awt.Component[] {cancelJButton, nextJButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        buttonBarJPanelLayout.setVerticalGroup(
            buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(buttonBarJPanelLayout.createSequentialGroup()
                .add(errorMessageJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(buttonBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(cancelJButton)
                        .add(nextJButton))
                    .add(buttonBarJPanelLayout.createSequentialGroup()
                        .add(signUpJLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(forgotPasswordJLabel)))
                .addContainerGap())
        );

        progressBarJPanel.setOpaque(false);
        stepJLabel.setFont(Fonts.DialogFont);
        stepJLabel.setText(getStepJLabelText());

        loginJProgressBar.setBorder(null);
        loginJProgressBar.setBorderPainted(false);
        loginJProgressBar.setMaximumSize(new java.awt.Dimension(32767, 23));
        loginJProgressBar.setMinimumSize(new java.awt.Dimension(10, 23));
        loginJProgressBar.setOpaque(false);
        loginJProgressBar.setPreferredSize(new java.awt.Dimension(146, 23));

        org.jdesktop.layout.GroupLayout progressBarJPanelLayout = new org.jdesktop.layout.GroupLayout(progressBarJPanel);
        progressBarJPanel.setLayout(progressBarJPanelLayout);
        progressBarJPanelLayout.setHorizontalGroup(
            progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, stepJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
            .add(loginJProgressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
        );
        progressBarJPanelLayout.setVerticalGroup(
            progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(progressBarJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(stepJLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(loginJProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(progressBarJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(buttonBarJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(usernameJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(usernameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(passwordJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(passwordJPasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {passwordJPasswordField, usernameJTextField}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.linkSize(new java.awt.Component[] {passwordJLabel, usernameJLabel}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(usernameJLabel)
                    .add(usernameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(passwordJLabel)
                    .add(passwordJPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(21, 21, 21)
                .add(buttonBarJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(progressBarJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     *  Initialize the document handler for the username and password fields.
     */
    private void initDocumentHandlers() {
        final DocumentListener documentListener = new DocumentListener() {
            public void changedUpdate(final DocumentEvent e) {
                validateInput();
            }
            public void insertUpdate(final DocumentEvent e) {
                validateInput();
            }
            public void removeUpdate(final DocumentEvent e) {
                validateInput();
            }
        };
        usernameJTextField.getDocument().addDocumentListener(documentListener);
        passwordJPasswordField.getDocument().addDocumentListener(documentListener);
    }

    /**
     * Perform the login.
     */
    private void login() {
        platform.runLogin(extractUsername(), extractPassword(), createMonitor());
    }

    private void nextJButtonActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_nextJButtonActionPerformed
        if (isInputValid()) {
            // Username and password are saved in case they turn out to be wrong.
            // In that event reload() will be called so the user can have another go.
            username = extractUsername();
            password = extractPassword();
            signup = Boolean.FALSE;
            nextJButton.setEnabled(false);
            login();
        }
    }//GEN-LAST:event_nextJButtonActionPerformed

    private void reloadError(final Boolean validCredentials) {
        if (!validCredentials) {
            errorMessageJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("LoginAvatar.ErrorBadCredentials"));
            nextJButton.setEnabled(Boolean.FALSE);
        } else {
            validateInput();
        }
    }

    private void reloadPassword(final String password) {
        passwordJPasswordField.setText(password);
    }

    private void reloadProgressBar() {
        buttonBarJPanel.setVisible(true);
        progressBarJPanel.setVisible(false);
        /* The space is deliberate (as opposed to an empty string) in
         * order to maintain vertical spacing. */
        stepJLabel.setText(" ");
        validate();
    }

    private void reloadUsername(final String username) {
        usernameJTextField.setText(username);
    }

    private void signUpJLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signUpJLabelMousePressed
        password = username = null;
        signup = Boolean.TRUE;
        disposeWindow();
    }//GEN-LAST:event_signUpJLabelMousePressed
    // Variables declaration - do not modify//GEN-BEGIN:variables

    private final javax.swing.JPanel buttonBarJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel forgotPasswordJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JProgressBar loginJProgressBar = new javax.swing.JProgressBar();
    private final javax.swing.JButton nextJButton = ButtonFactory.create();
    private final javax.swing.JPasswordField passwordJPasswordField = TextFactory.createPassword();
    private final javax.swing.JPanel progressBarJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel signUpJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel stepJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField usernameJTextField = TextFactory.create();
    // End of variables declaration//GEN-END:variables
}
