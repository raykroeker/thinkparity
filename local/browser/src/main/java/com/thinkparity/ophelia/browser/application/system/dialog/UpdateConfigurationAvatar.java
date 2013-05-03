/*
 * Created On: 2007-12-21 10:41 -0700
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.constraint.IllegalValueException;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextFieldLengthFilter;

import com.thinkparity.ophelia.model.workspace.configuration.Proxy;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyConfiguration;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyConstraints;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyCredentials;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyType;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.CheckBoxFactory;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

import com.thinkparity.common.StringUtil.Separator;

/**
 * <b>Title:</b>thinkParity OpheliaUI Update Configuration Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 */
public final class UpdateConfigurationAvatar extends SystemAvatar {

    /** Close label icon. */
    private static final Icon CLOSE_ICON;

    /** Close label rollover icon. */
    private static final Icon CLOSE_ROLLOVER_ICON;

    static {
        CLOSE_ICON = ImageIOUtil.readIcon("Dialog_CloseButton.png");
        CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("Dialog_CloseButtonRollover.png");
    }

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
    private final javax.swing.JButton saveJButton = ButtonFactory.create(Fonts.DialogButtonFont);
    private final javax.swing.JTextField socksPortJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JTextField socksProxyJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    // End of variables declaration//GEN-END:variables

    /** The proxy configuration. */
    private ProxyConfiguration configuration;

    /** An instance of <code>ProxyConstraints</code>. */
    private final ProxyConstraints constraints;

    /**
     * Create UpdateConfigurationAvatar.
     */
    public UpdateConfigurationAvatar() {
        super("SystemApplication.UpdateConfigurationAvatar");
        this.constraints = ProxyConstraints.getInstance();
        initComponents();
        addValidationListener(httpProxyJTextField);
        addValidationListener(httpPortJTextField);
        addValidationListener(httpUsernameJTextField);
        addValidationListener(httpPasswordJPasswordField);
        addValidationListener(socksProxyJTextField);
        addValidationListener(socksPortJTextField);
        bindEscapeKey("Close", new AbstractAction() {
            /**
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                closeAvatar();
            }
        });
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     */
    @Override
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_UPDATE_CONFIGURATION;
    }

    /**
     * Reload the configuration.
     * 
     * @param configuration
     *            A <code>ProxyConfiguration</code>.
     */
    public void reloadConfiguration(final ProxyConfiguration configuration) {
        this.configuration = configuration;
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
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     */
    @Override
    protected void validateInput() {
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

        // check for user changes
        if (!containsInputErrors()) {
            final Boolean inputChanged = isInputChanged();
            if (!inputChanged) {
                addInputError(Separator.Space.toString());
            }
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        saveJButton.setEnabled(!containsInputErrors());
        setControlAccess();
    }

    private void automaticProxyJRadioButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automaticProxyJRadioButtonActionPerformed
        validateInput();
    }//GEN-LAST:event_automaticProxyJRadioButtonActionPerformed

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        closeAvatar();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    /**
     * Close the avatar.
     */
    private void closeAvatar() {
        disposeWindow();
    }

    private void closeJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeJButtonActionPerformed
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ICON);
        closeAvatar();
    }//GEN-LAST:event_closeJButtonActionPerformed

    private void closeJButtonMouseEntered(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeJButtonMouseEntered
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ROLLOVER_ICON);
    }//GEN-LAST:event_closeJButtonMouseEntered

    private void closeJButtonMouseExited(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeJButtonMouseExited
        ((javax.swing.JButton) evt.getSource()).setIcon(CLOSE_ICON);
    }//GEN-LAST:event_closeJButtonMouseExited

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
        final javax.swing.JLabel titleJLabel = LabelFactory.create(Fonts.DialogTitle);
        final javax.swing.JButton closeJButton = ButtonFactory.create();
        final javax.swing.JLabel httpProxyJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel httpPortJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel socksProxyJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JLabel socksPortJLabel = LabelFactory.create(Fonts.DialogFont);
        final javax.swing.JButton cancelJButton = ButtonFactory.create(Fonts.DialogButtonFont);

        setOpaque(false);
        titleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.Title"));
        titleJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        closeJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Dialog_CloseButton.png")));
        closeJButton.setBorderPainted(false);
        closeJButton.setContentAreaFilled(false);
        closeJButton.setFocusPainted(false);
        closeJButton.setFocusable(false);
        closeJButton.setMaximumSize(new java.awt.Dimension(14, 14));
        closeJButton.setMinimumSize(new java.awt.Dimension(14, 14));
        closeJButton.setPreferredSize(new java.awt.Dimension(14, 14));
        closeJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeJButtonMouseExited(evt);
            }
        });
        closeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeJButtonActionPerformed(evt);
            }
        });

        proxyButtonGroup.add(automaticProxyJRadioButton);
        automaticProxyJRadioButton.setFont(Fonts.DialogFont);
        automaticProxyJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.AutomaticProxy"));
        automaticProxyJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        automaticProxyJRadioButton.setOpaque(false);
        automaticProxyJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                automaticProxyJRadioButtonActionPerformed(evt);
            }
        });

        proxyButtonGroup.add(manualProxyJRadioButton);
        manualProxyJRadioButton.setFont(Fonts.DialogFont);
        manualProxyJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.ManualProxy"));
        manualProxyJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        manualProxyJRadioButton.setOpaque(false);
        manualProxyJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualProxyJRadioButtonActionPerformed(evt);
            }
        });

        manualJPanel.setOpaque(false);
        httpProxyJLabel.setLabelFor(httpProxyJTextField);
        httpProxyJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.HttpProxy"));

        httpProxyJTextField.setMaximumSize(new java.awt.Dimension(110, 2147483647));
        httpProxyJTextField.setMinimumSize(new java.awt.Dimension(110, 20));
        httpProxyJTextField.setPreferredSize(new java.awt.Dimension(110, 20));
        ((AbstractDocument) httpProxyJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getHttpProxyHost()));

        httpPortJLabel.setLabelFor(httpPortJTextField);
        httpPortJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.HttpPort"));

        httpPortJTextField.setFont(Fonts.DialogTextEntryFont);
        httpPortJTextField.setMaximumSize(new java.awt.Dimension(60, 2147483647));
        httpPortJTextField.setMinimumSize(new java.awt.Dimension(60, 20));
        httpPortJTextField.setPreferredSize(new java.awt.Dimension(60, 20));

        httpAuthenticateJCheckBox.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.RequiresAuthentication"));
        httpAuthenticateJCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        httpAuthenticateJCheckBox.setOpaque(false);
        httpAuthenticateJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                httpAuthenticateJCheckBoxActionPerformed(evt);
            }
        });

        httpUsernameJLabel.setLabelFor(httpUsernameJTextField);
        httpUsernameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.Username"));

        httpUsernameJTextField.setFont(Fonts.DialogTextEntryFont);
        httpUsernameJTextField.setMaximumSize(new java.awt.Dimension(224, 2147483647));
        httpUsernameJTextField.setMinimumSize(new java.awt.Dimension(224, 20));
        httpUsernameJTextField.setPreferredSize(new java.awt.Dimension(224, 20));
        ((AbstractDocument) httpUsernameJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getHttpProxyUsername()));

        httpPasswordJLabel.setLabelFor(httpPasswordJPasswordField);
        httpPasswordJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.Password"));

        httpPasswordJPasswordField.setFont(Fonts.DialogTextEntryFont);
        httpPasswordJPasswordField.setMaximumSize(new java.awt.Dimension(224, 2147483647));
        httpPasswordJPasswordField.setMinimumSize(new java.awt.Dimension(224, 20));
        httpPasswordJPasswordField.setPreferredSize(new java.awt.Dimension(224, 20));
        ((AbstractDocument) httpPasswordJPasswordField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getHttpProxyPassword()));

        socksProxyJLabel.setLabelFor(socksProxyJTextField);
        socksProxyJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.SocksProxy"));

        socksProxyJTextField.setPreferredSize(new java.awt.Dimension(110, 20));
        ((AbstractDocument) socksProxyJTextField.getDocument()).setDocumentFilter(new JTextFieldLengthFilter(constraints.getSocksProxyHost()));

        socksPortJLabel.setLabelFor(socksProxyJTextField);
        socksPortJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.SocksPort"));

        socksPortJTextField.setPreferredSize(new java.awt.Dimension(60, 20));

        javax.swing.GroupLayout manualJPanelLayout = new javax.swing.GroupLayout(manualJPanel);
        manualJPanel.setLayout(manualJPanelLayout);
        manualJPanelLayout.setHorizontalGroup(
            manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manualJPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(manualJPanelLayout.createSequentialGroup()
                        .addComponent(httpProxyJLabel)
                        .addGap(28, 28, 28)
                        .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(httpAuthenticateJCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, manualJPanelLayout.createSequentialGroup()
                                    .addComponent(httpProxyJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(22, 22, 22)
                                    .addComponent(httpPortJLabel)
                                    .addGap(12, 12, 12)
                                    .addComponent(httpPortJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(manualJPanelLayout.createSequentialGroup()
                                    .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(httpUsernameJLabel)
                                        .addComponent(httpPasswordJLabel))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                    .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(httpPasswordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(httpUsernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, manualJPanelLayout.createSequentialGroup()
                                    .addComponent(socksProxyJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(17, 17, 17)
                                    .addComponent(socksPortJLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(socksPortJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(socksProxyJLabel))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        manualJPanelLayout.setVerticalGroup(
            manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manualJPanelLayout.createSequentialGroup()
                .addGroup(manualJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(httpPortJLabel)
                    .addComponent(httpProxyJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(httpPortJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(httpProxyJLabel))
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
                    .addComponent(socksProxyJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(socksPortJLabel)
                    .addComponent(socksPortJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        saveJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.Save"));
        saveJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SystemApplication.UpdateConfigurationAvatar.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(titleJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeJButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(254, Short.MAX_VALUE)
                        .addComponent(saveJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(manualProxyJRadioButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(automaticProxyJRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addComponent(manualJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(errorMessageJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, saveJButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleJLabel)
                    .addComponent(closeJButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(automaticProxyJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manualProxyJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manualJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorMessageJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(saveJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Determine if the input has changed from the original state.
     * 
     * @return true if the input changed; false otherwise.
     */
    private Boolean isInputChanged() {
        final ProxyConfiguration newConfiguration = extractConfiguration();
        final boolean manual = (null != configuration);
        final boolean newManual = (null != newConfiguration);
        if (manual != newManual) {
            return Boolean.TRUE;
        } else if (manual) {
            final Proxy http = configuration.getHttp();
            final Proxy socks = configuration.getSocks();
            final Proxy newHttp = newConfiguration.getHttp();
            final Proxy newSocks = newConfiguration.getSocks();
            if (!http.getHost().equals(newHttp.getHost()) ||
                    !http.getPort().equals(newHttp.getPort()) ||
                    !socks.getHost().equals(newSocks.getHost()) ||
                    !socks.getPort().equals(newSocks.getPort())) {
                return Boolean.TRUE;
            }
            final boolean credentialsEnabled = (null != configuration.getHttpCredentials());
            final boolean newCredentialsEnabled = (null != newConfiguration.getHttpCredentials());
            if (credentialsEnabled != newCredentialsEnabled) {
                return Boolean.TRUE;
            } else if (credentialsEnabled) {
                final ProxyCredentials credentials = configuration.getHttpCredentials();
                final ProxyCredentials newCredentials = newConfiguration.getHttpCredentials();
                if (!credentials.getUsername().equals(newCredentials.getUsername()) ||
                        !credentials.getPassword().equals(newCredentials.getPassword())) {
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }

    private void manualProxyJRadioButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualProxyJRadioButtonActionPerformed
        validateInput();
    }//GEN-LAST:event_manualProxyJRadioButtonActionPerformed

    /**
     * Reload the authentication checkbox.
     */
    private void reloadAuthenticationCheckBox() {
        if (null == configuration) {
            httpAuthenticateJCheckBox.setSelected(false);
        } else {
            httpAuthenticateJCheckBox.setSelected(configuration.isSetHttpCredentials());    
        }
    }

    /**
     * Reload the http host.
     */
    private void reloadHttpHost() {
        if (null == configuration) {
            httpProxyJTextField.setText(null);
        } else {
            httpProxyJTextField.setText(configuration.getHttp().getHost());
        }
    }

    /**
     * Reload the http port.
     */
    private void reloadHttpPort() {
        if (null == configuration) {
            httpPortJTextField.setText(null);
        } else {
            httpPortJTextField.setText(configuration.getHttp().getPort().toString());
        }
    }

    /**
     * Reload the password.
     */
    private void reloadPassword() {
        if (null == configuration) {
            httpPasswordJPasswordField.setText(null);
        } else {
            if (configuration.isSetHttpCredentials()) {
                httpPasswordJPasswordField.setText(configuration.getHttpCredentials().getPassword());
            } else {
                httpPasswordJPasswordField.setText(null);
            }
        }
    }

    /**
     * Reload the proxy radio buttons.
     */
    private void reloadProxyRadioButtons() {
        if (null == configuration) {
            automaticProxyJRadioButton.setSelected(true);
        } else {
            manualProxyJRadioButton.setSelected(true);
        }
    }

    /**
     * Reload the socks host.
     */
    private void reloadSocksHost() {
        if (null == configuration) {
            socksProxyJTextField.setText(null);
        } else {
            socksProxyJTextField.setText(configuration.getSocks().getHost());
        }
    }

    /**
     * Reload the socks port.
     */
    private void reloadSocksPort() {
        if (null == configuration) {
            socksPortJTextField.setText(null);
        } else {
            socksPortJTextField.setText(configuration.getSocks().getPort().toString());
        }
    }

    /**
     * Reload the username.
     */
    private void reloadUsername() {
        if (null == configuration) {
            httpUsernameJTextField.setText(null);
        } else {
            if (configuration.isSetHttpCredentials()) {
                httpUsernameJTextField.setText(configuration.getHttpCredentials().getUsername());
            } else {
                httpUsernameJTextField.setText(null);
            }
        }
    }

    /**
     * Save the configuration.
     */
    private void saveConfiguration() {
        final ProxyConfiguration configuration = extractConfiguration();
        if (null == configuration) {
            getApplication().runDeleteProxyConfiguration();
        } else {
            getApplication().runUpdateProxyConfiguration(configuration);
        }
    }

    private void saveJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveJButtonActionPerformed
        if (isInputValid()) {
            disposeWindow();
            saveConfiguration();
        }
    }//GEN-LAST:event_saveJButtonActionPerformed

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
}
