/*
 * SignupUpdateConfigurationAvatar.java
 *
 * Created on December 31, 2007, 2:11 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.constraint.IllegalValueException;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.ophelia.model.session.OfflineException;
import com.thinkparity.ophelia.model.workspace.configuration.Proxy;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyConfiguration;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyConstraints;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyCredentials;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyType;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.CheckBoxFactory;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;

import com.thinkparity.common.StringUtil.Separator;


/**
 *
 * @author robert@thinkparity.com
 */
public class SignupUpdateConfigurationAvatar extends DefaultSignupPage {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JRadioButton automaticProxyJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JCheckBox httpAuthenticateJCheckBox = CheckBoxFactory.create(Fonts.DialogFont);
    private final javax.swing.JLabel httpPasswordJLabel = LabelFactory.create(Fonts.DialogFont);
    private final javax.swing.JPasswordField httpPasswordJPasswordField = TextFactory.createPassword(Fonts.DialogTextEntryFont);
    private final javax.swing.JTextField httpPortJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JTextField httpProxyJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel httpUsernameJLabel = LabelFactory.create(Fonts.DialogFont);
    private final javax.swing.JTextField httpUsernameJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JPanel manualJPanel = new javax.swing.JPanel();
    private final javax.swing.JRadioButton manualProxyJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.ButtonGroup proxyButtonGroup = new javax.swing.ButtonGroup();
    private final javax.swing.JTextField socksPortJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JTextField socksProxyJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    // End of variables declaration//GEN-END:variables

    /** An instance of <code>ProxyConstraints</code>. */
    private final ProxyConstraints constraints;

    /** Creates new form SignupUpdateConfigurationAvatar */
    public SignupUpdateConfigurationAvatar() {
        super("SignupAvatar.UpdateConfiguration", BrowserConstants.DIALOGUE_BACKGROUND);
        this.constraints = ProxyConstraints.getInstance();
        initComponents();
        addValidationListeners();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_UPDATE_CONFIGURATION;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     */
    public String getNextPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_INTRO);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_INTRO);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        if (!isInputValid()) {
            return Boolean.FALSE;
        }
        saveConfiguration();
        return !containsInputErrors();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadProxyRadioButtons();
        reloadHttpHost();
        reloadHttpPort();
        reloadSocksHost();
        reloadSocksPort();
        reloadAuthenticationCheckBox();
        reloadUsername();
        reloadPassword();
        validateInput();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     */
    public void saveData() {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public final void validateInput() {
        super.validateInput();
        final boolean manual = manualProxyJRadioButton.isSelected();
        if (manual) {
            if (null == extractHttpHost() ||
                    null == extractHttpPort() ||
                    null == extractSocksHost() ||
                    null == extractSocksPort()) {
                addInputError(Separator.Space.toString());
            } else {
                try {
                    constraints.getHttpProxyHost().validate(extractHttpHost().toLowerCase());
                } catch (final IllegalValueException ivx) {
                    if (ivx.getReason() == IllegalValueException.Reason.TOO_SHORT) {
                        final int minimumHttpHostLength = constraints.getHttpProxyHost().getMinLength();
                        addInputError(getString("ErrorHttpHostTooShort", new Object[] {minimumHttpHostLength}));
                    } else {
                        addInputError(getString("ErrorHttpHostInvalid"));
                    }
                }
                try {
                    constraints.getHttpProxyPort().validate(extractHttpPort());
                } catch (final IllegalValueException ivx) {
                    addInputError(Separator.Space.toString());
                }
    
                try {
                    constraints.getSocksProxyHost().validate(extractSocksHost().toLowerCase());
                } catch (final IllegalValueException ivx) {
                    if (ivx.getReason() == IllegalValueException.Reason.TOO_SHORT) {
                        final int minimumSocksHostLength = constraints.getSocksProxyHost().getMinLength();
                        addInputError(getString("ErrorSocksHostTooShort", new Object[] {minimumSocksHostLength}));
                    } else {
                        addInputError(getString("ErrorSocksHostInvalid"));
                    }
                }
                try {
                    constraints.getSocksProxyPort().validate(extractSocksPort());
                } catch (final IllegalValueException ivx) {
                    addInputError(Separator.Space.toString());
                }
            }
        }

        if (!containsInputErrors()) {
            final boolean authenticate = httpAuthenticateJCheckBox.isSelected();
            if (manual && authenticate) {
                try {
                    constraints.getHttpProxyPassword().validate(extractHttpPassword());
                } catch (final IllegalValueException ivx) {
                    addInputError(Separator.Space.toString());
                }
                try {
                    constraints.getHttpProxyUsername().validate(extractHttpUsername());
                } catch (final IllegalValueException ivx) {
                    addInputError(Separator.Space.toString());
                }
            }
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }

        if (isSignupDelegateInitialized()) {
            signupDelegate.enableNextButton(!containsInputErrors());
        }
        setControlAccess();
    }

    /**
     * Add validation listeners.
     */
    private void addValidationListeners() {
        addValidationListener(httpProxyJTextField);
        addValidationListener(httpPortJTextField);
        addValidationListener(httpUsernameJTextField);
        addValidationListener(httpPasswordJPasswordField);
        addValidationListener(socksProxyJTextField);
        addValidationListener(socksPortJTextField);
    }

    private void automaticProxyJRadioButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automaticProxyJRadioButtonActionPerformed
        validateInput();
    }//GEN-LAST:event_automaticProxyJRadioButtonActionPerformed

    /**
     * Delete the proxy configuration.
     */
    private void deleteProxyConfiguration() {
        ((SignupProvider) contentProvider).deleteProxyConfiguration();
    }

    /**
     * Extract the configuration.
     * 
     * @return A <code>ProxyConfiguration</code>.
     */
    private ProxyConfiguration extractConfiguration() {
        final boolean manual = manualProxyJRadioButton.isSelected();
        if (manual) {
            final ProxyConfiguration configuration = new ProxyConfiguration();

            final Proxy http = new Proxy();
            http.setHost(extractHttpHost().toLowerCase());
            http.setPort(extractHttpPort());
            http.setType(ProxyType.HTTP);
            configuration.setHttp(http);

            final boolean credentialsEnabled = httpAuthenticateJCheckBox.isSelected();
            final ProxyCredentials httpCredentials;
            if (credentialsEnabled) {
                httpCredentials = new ProxyCredentials();
                httpCredentials.setUsername(extractHttpUsername());
                httpCredentials.setPassword(extractHttpPassword());
            } else {
                httpCredentials = null;
            }
            configuration.setHttpCredentials(httpCredentials);

            final Proxy socks = new Proxy();
            socks.setHost(extractSocksHost().toLowerCase());
            socks.setPort(extractSocksPort());
            socks.setType(ProxyType.SOCKS);
            configuration.setSocks(socks);

            return configuration;
        } else {
            return null;
        }
    }

    /**
     * Extract the host from the control.
     *
     * @return The host <code>String</code>.
     */
    private String extractHttpHost() {
        return SwingUtil.extract(httpProxyJTextField, Boolean.TRUE);
    }

    /**
     * Extract the password from the control.
     * 
     * @return The password <code>String</code>.
     */
    private String extractHttpPassword() {
        return SwingUtil.extract(httpPasswordJPasswordField, Boolean.TRUE);
    }

    /**
     * Extract the port from the control.
     * 
     * @return The port <code>Integer</code>.
     */
    private Integer extractHttpPort() {
        final String portString = SwingUtil.extract(httpPortJTextField, Boolean.TRUE);
        if (null == portString) {
            return null;
        } else {
            try {
                return Integer.parseInt(portString);
            } catch (final NumberFormatException nfe) {
                return null;
            }
        }
    }

    /**
     * Extract the username from the control.
     * 
     * @return The username <code>String</code>.
     */
    private String extractHttpUsername() {
        return SwingUtil.extract(httpUsernameJTextField, Boolean.TRUE);
    }

    /**
     * Extract the host from the control.
     *
     * @return The host <code>String</code>.
     */
    private String extractSocksHost() {
        return SwingUtil.extract(socksProxyJTextField, Boolean.TRUE);
    }

    /**
     * Extract the port from the control.
     * 
     * @return The port <code>Integer</code>.
     */
    private Integer extractSocksPort() {
        final String portString = SwingUtil.extract(socksPortJTextField, Boolean.TRUE);
        if (null == portString) {
            return null;
        } else {
            try {
                return Integer.parseInt(portString);
            } catch (final NumberFormatException nfe) {
                return null;
            }
        }
    }

    private void httpAuthenticateJCheckBoxActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_httpAuthenticateJCheckBoxActionPerformed
        validateInput();
    }//GEN-LAST:event_httpAuthenticateJCheckBoxActionPerformed

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel httpProxyJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel httpPortJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel socksProxyJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel socksPortJLabel = LabelFactory.create(Fonts.DialogFont);

        setOpaque(false);
        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.UpdateConfiguration.Explanation"));
        explanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        proxyButtonGroup.add(automaticProxyJRadioButton);
        automaticProxyJRadioButton.setFont(Fonts.DialogFont);
        automaticProxyJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.UpdateConfiguration.AutomaticProxy"));
        automaticProxyJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        automaticProxyJRadioButton.setOpaque(false);
        automaticProxyJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                automaticProxyJRadioButtonActionPerformed(evt);
            }
        });

        proxyButtonGroup.add(manualProxyJRadioButton);
        manualProxyJRadioButton.setFont(Fonts.DialogFont);
        manualProxyJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.UpdateConfiguration.ManualProxy"));
        manualProxyJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        manualProxyJRadioButton.setOpaque(false);
        manualProxyJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualProxyJRadioButtonActionPerformed(evt);
            }
        });

        manualJPanel.setOpaque(false);
        httpProxyJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.UpdateConfiguration.HttpProxy"));

        httpProxyJTextField.setMaximumSize(new java.awt.Dimension(110, 2147483647));
        httpProxyJTextField.setMinimumSize(new java.awt.Dimension(110, 20));
        httpProxyJTextField.setPreferredSize(new java.awt.Dimension(110, 20));
        ((AbstractDocument) httpProxyJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getHttpProxyHost()));

        httpPortJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.UpdateConfiguration.HttpPort"));

        httpPortJTextField.setFont(Fonts.DialogTextEntryFont);
        httpPortJTextField.setMaximumSize(new java.awt.Dimension(60, 2147483647));
        httpPortJTextField.setMinimumSize(new java.awt.Dimension(60, 20));
        httpPortJTextField.setPreferredSize(new java.awt.Dimension(60, 20));

        httpAuthenticateJCheckBox.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.UpdateConfiguration.RequiresAuthentication"));
        httpAuthenticateJCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        httpAuthenticateJCheckBox.setOpaque(false);
        httpAuthenticateJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                httpAuthenticateJCheckBoxActionPerformed(evt);
            }
        });

        httpUsernameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.UpdateConfiguration.Username"));

        httpUsernameJTextField.setFont(Fonts.DialogTextEntryFont);
        httpUsernameJTextField.setMaximumSize(new java.awt.Dimension(224, 2147483647));
        httpUsernameJTextField.setMinimumSize(new java.awt.Dimension(224, 20));
        httpUsernameJTextField.setPreferredSize(new java.awt.Dimension(224, 20));
        ((AbstractDocument) httpUsernameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getHttpProxyUsername()));

        httpPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.UpdateConfiguration.Password"));

        httpPasswordJPasswordField.setFont(Fonts.DialogTextEntryFont);
        httpPasswordJPasswordField.setMaximumSize(new java.awt.Dimension(224, 2147483647));
        httpPasswordJPasswordField.setMinimumSize(new java.awt.Dimension(224, 20));
        httpPasswordJPasswordField.setPreferredSize(new java.awt.Dimension(224, 20));
        ((AbstractDocument) httpPasswordJPasswordField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getHttpProxyPassword()));

        socksProxyJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.UpdateConfiguration.SocksProxy"));

        socksProxyJTextField.setPreferredSize(new java.awt.Dimension(110, 20));
        ((AbstractDocument) socksProxyJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getSocksProxyHost()));

        socksPortJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.UpdateConfiguration.SocksPort"));

        socksPortJTextField.setPreferredSize(new java.awt.Dimension(60, 20));

        javax.swing.GroupLayout manualJPanelLayout = new javax.swing.GroupLayout(manualJPanel);
        manualJPanel.setLayout(manualJPanelLayout);
        manualJPanelLayout.setHorizontalGroup(
            manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manualJPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(socksProxyJLabel)
                    .addGroup(manualJPanelLayout.createSequentialGroup()
                        .addComponent(httpProxyJLabel)
                        .addGap(28, 28, 28)
                        .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(httpAuthenticateJCheckBox)
                            .addGroup(manualJPanelLayout.createSequentialGroup()
                                .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(manualJPanelLayout.createSequentialGroup()
                                        .addComponent(httpProxyJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                        .addComponent(httpPortJLabel)
                                        .addGap(8, 8, 8))
                                    .addGroup(manualJPanelLayout.createSequentialGroup()
                                        .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(httpUsernameJLabel)
                                            .addComponent(httpPasswordJLabel))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                        .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(httpPasswordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(httpUsernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(manualJPanelLayout.createSequentialGroup()
                                        .addComponent(socksProxyJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                        .addComponent(socksPortJLabel)
                                        .addGap(8, 8, 8)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(socksPortJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(httpPortJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        manualJPanelLayout.setVerticalGroup(
            manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manualJPanelLayout.createSequentialGroup()
                .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(httpProxyJLabel)
                    .addComponent(httpPortJLabel)
                    .addComponent(httpProxyJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(httpPortJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(httpAuthenticateJCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(httpUsernameJLabel)
                    .addComponent(httpUsernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(httpPasswordJLabel)
                    .addComponent(httpPasswordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(socksProxyJLabel)
                    .addComponent(socksPortJLabel)
                    .addComponent(socksProxyJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(socksPortJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(explanationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(manualJPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(manualProxyJRadioButton)
                            .addComponent(automaticProxyJRadioButton))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(explanationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(automaticProxyJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manualProxyJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manualJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorMessageJLabel)
                .addContainerGap(22, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Determine if the proxy configuration is set.
     * 
     * @return true if the proxy configuration is set.
     */
    private Boolean isSetProxyConfiguration() {
        return ((SignupProvider) contentProvider).isSetProxyConfiguration();
    }

    private void manualProxyJRadioButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualProxyJRadioButtonActionPerformed
        validateInput();
    }//GEN-LAST:event_manualProxyJRadioButtonActionPerformed

    /**
     * Reload the authentication checkbox.
     */
    private void reloadAuthenticationCheckBox() {
        httpAuthenticateJCheckBox.setSelected(false);
    }

    /**
     * Reload the http host.
     */
    private void reloadHttpHost() {
        httpProxyJTextField.setText(null);
    }

    /**
     * Reload the http port.
     */
    private void reloadHttpPort() {
        httpPortJTextField.setText(null);
    }

    /**
     * Reload the password.
     */
    private void reloadPassword() {
        httpPasswordJPasswordField.setText(null);
    }

    /**
     * Reload the proxy radio buttons.
     */
    private void reloadProxyRadioButtons() {
        automaticProxyJRadioButton.setSelected(true);
    }

    /**
     * Reload the socks host.
     */
    private void reloadSocksHost() {
        socksProxyJTextField.setText(null);
    }

    /**
     * Reload the socks port.
     */
    private void reloadSocksPort() {
        socksPortJTextField.setText(null);
    }

    /**
     * Reload the username.
     */
    private void reloadUsername() {
        httpUsernameJTextField.setText(null);
    }

    /**
     * Save the configuration.
     */
    private void saveConfiguration() {
        final ProxyConfiguration configuration = extractConfiguration();
        signupDelegate.enableNextButton(Boolean.FALSE);
        SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
        try {
            errorMessageJLabel.setText(getString("SavingConfiguration"));
            errorMessageJLabel.paintImmediately(0, 0, errorMessageJLabel
                    .getWidth(), errorMessageJLabel.getHeight());
            if (null == configuration) {
                if (isSetProxyConfiguration()) {
                    deleteProxyConfiguration();
                }
            } else {
                updateProxyConfiguration(configuration);
            }
        } catch (final OfflineException ox) {
            logger.logError(ox, "An offline error has occurred.");
            addInputError(getSharedString("ErrorOffline"));
        } catch (final Throwable t) {
            logger.logError(t, "An unexpected error has occurred.");
            addInputError(getSharedString("ErrorUnexpected"));
        }
        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        SwingUtil.setCursor(this, null);
        signupDelegate.enableNextButton(Boolean.TRUE);
    }

    /**
     * Enable/disable the proxy controls based upon the auto/manual checkbox
     * selection, and the authentication controls based upon the authenticate
     * checkbox selection.
     */
    private void setControlAccess() {
        final boolean manual = manualProxyJRadioButton.isSelected();
        for (final Component component : manualJPanel.getComponents()) {
            component.setEnabled(manual);
            if (component instanceof JTextField) {
                setEditable((JTextField) component, manual);
            }
        }
        final boolean authenticate = manual && httpAuthenticateJCheckBox.isSelected();
        httpUsernameJLabel.setEnabled(authenticate);
        httpPasswordJLabel.setEnabled(authenticate);
        setEditable(httpUsernameJTextField, authenticate);
        setEditable(httpPasswordJPasswordField, authenticate);
    }

    /**
     * Update the proxy configuration.
     * 
     * @param configuration
     *            A <code>ProxyConfiguration</code>.
     */
    private void updateProxyConfiguration(final ProxyConfiguration configuration) {
        ((SignupProvider) contentProvider).updateProxyConfiguration(configuration);
    }
}
