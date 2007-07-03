/*
 * Created on January 31, 2007, 3:14 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.AbstractDocument;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.BytesFormat.Unit;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.email.EMailFormatException;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.text.JTextComponentLengthFilter;

import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileConstraints;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.events.BackupEvent;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.component.TextFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.event.UpdateProfileDispatcher;
import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile.UpdateProfileProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile.LocaleRenderer;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 * <b>Title:</b>thinkParity OpheliaUI Update Profile Avatar<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateProfileAvatar extends Avatar {

    /** A size format to use for the backup statistics. */
    private static final BytesFormat BYTES_FORMAT;

    static {
        BYTES_FORMAT = new BytesFormat();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel addressJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField addressJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel backupStatisticsJLabel = new javax.swing.JLabel();
    private final javax.swing.JButton changePasswordJButton = ButtonFactory.create();
    private final javax.swing.JLabel cityJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField cityJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JComboBox countryJComboBox = new javax.swing.JComboBox();
    private final javax.swing.JLabel countryJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel countryJPanel = new javax.swing.JPanel();
    private final javax.swing.JTextField countryJTextField = TextFactory.create();
    private final javax.swing.JLabel emailJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField emailJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField mobilePhoneJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel nameJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField nameJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel organizationJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField organizationJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JTextField phoneJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel postalCodeJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField postalCodeJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JLabel provinceJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField provinceJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    private final javax.swing.JButton saveJButton = ButtonFactory.create();
    private final javax.swing.JLabel titleJLabel = new javax.swing.JLabel();
    private final javax.swing.JTextField titleJTextField = TextFactory.create(Fonts.DialogTextEntryFont);
    // End of variables declaration//GEN-END:variables

    /** The unit to use when displaying the backup information. */
    private final Unit backupUnit;

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel countryModel;

    /** The emails. */
    private List<ProfileEMail> emails;

    /** The profile. */
    private Profile profile;

    /** An instance of <code>ProfileConstraints</code>. */
    private final ProfileConstraints profileConstraints;

    /** Backup statistics */
    private Statistics statistics;

    /** Unavailable email. */
    private String unavailableEmail = null;

    /** Creates new form UpdateProfileAvatar */
    public UpdateProfileAvatar() {
        super("UpdateProfileAvatar", BrowserConstants.DIALOGUE_BACKGROUND);
        this.profileConstraints = ProfileConstraints.getInstance();
        this.countryModel = new DefaultComboBoxModel();
        this.backupUnit = Unit.AUTO;
        initCountryModel();
        initComponents();
        addPropertyChangeListener("eventDispatcher", new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                if (null != evt.getOldValue())
                    ((UpdateProfileDispatcher) evt.getOldValue()).removeListeners(
                            UpdateProfileAvatar.this);
                if (null != evt.getNewValue())
                    ((UpdateProfileDispatcher) evt.getNewValue()).addListeners(
                            UpdateProfileAvatar.this);
            }
        });
        addValidationListeners();
        bindEscapeKey();
    }

    /**
     * Fire a backup event.
     * 
     * This event keeps the backup statistics up to date
     * so it is not necessary to read the information when the
     * dialog is opened by the user (other than the first time).
     * 
     * @param e
     *            A <code>BackupEvent</code>.
     */
    public void fireBackupEvent(final BackupEvent e) {
        this.statistics = e.getStatistics();
        reloadBackupStatistics(statistics);
    }

    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_UPDATE;
    }

    public State getState() {
        return null;
    }

    public void reload() {
        this.profile = readProfile();
        this.emails = readEMails();
        final boolean online = isOnline();
        reload(nameJTextField, profile.getName(), online);
        reload(titleJTextField, profile.getTitle(), online);
        reload(organizationJTextField, profile.getOrganization(), online);
        reload(phoneJTextField, profile.getPhone(), online);
        reload(mobilePhoneJTextField, profile.getMobilePhone(), online);
        reload(addressJTextField, profile.getAddress(), online);
        reload(cityJTextField, profile.getCity(), online);
        reload(provinceJTextField, profile.getProvince(), online);
        reloadCountry(profile, online);
        reload(postalCodeJTextField, profile.getPostalCode(), online);
        reload(emailJTextField, getEMailString(), online);
        reloadProvinceLabel();
        reloadPostalCodeLabel();
        reloadBackupStatistics();
        validateInput();
    }

    public void setState(final State state) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     */
    @Override
    protected void validateInput() {
        super.validateInput();

        // check online
        final Boolean online = isOnline();
        if (!online) {
            addInputError(getString("ErrorOffline"));
        }

        // check for blank required fields
        if (isEmpty(extractInputName())) {
            addInputError(getString("ErrorRequiredField", new Object[] {getLabelText(nameJLabel)}));
        } else if (isEmpty(extractInputTitle())) {
            addInputError(getString("ErrorRequiredField", new Object[] {getLabelText(titleJLabel)}));
        } else if (isEmpty(extractInputOrganization())) {
            addInputError(getString("ErrorRequiredField", new Object[] {getLabelText(organizationJLabel)}));
        } else if (isEmpty(extractInputAddress())) {
            addInputError(getString("ErrorRequiredField", new Object[] {getLabelText(addressJLabel)}));
        } else if (isEmpty(extractInputCity())) {
            addInputError(getString("ErrorRequiredField", new Object[] {getLabelText(cityJLabel)}));
        } else if (isEmpty(extractInputProvince())) {
            addInputError(getString("ErrorRequiredField", new Object[] {getLabelText(provinceJLabel)}));
        } else if (isEmpty(extractInputCountry())) {
            addInputError(getString("ErrorRequiredField", new Object[] {getLabelText(countryJLabel)}));
        } else if (isEmpty(extractInputPostalCode())) {
            addInputError(getString("ErrorRequiredField", new Object[] {getLabelText(postalCodeJLabel)}));
        } else if (isEmpty(extractInputEmail())) {
            addInputError(getString("ErrorRequiredField", new Object[] {getLabelText(emailJLabel)}));
        }

        // check for user changes
        final Boolean inputChanged = isInputChanged();
        if (!inputChanged) {
            addInputError(Separator.Space.toString());
        }

        // check the email
        if (!containsInputErrors()) {
            if (!isInputEmailValid()) {
                addInputError(getString("ErrorEmailInvalid"));
            } else if (!isInputEmailAvailableQuick()) {
                addInputError(getString("ErrorEmailNotAvailable"));
            }
        }

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        saveJButton.setEnabled(!containsInputErrors());
        // enable the change password button if online and there are no input changes
        changePasswordJButton.setEnabled(online && !inputChanged);
    }

    /**
     * Add validation listeners.
     */
    private void addValidationListeners() {
        addValidationListener(addressJTextField);
        addValidationListener(cityJTextField);
        addValidationListener(emailJTextField);
        addValidationListener(mobilePhoneJTextField);
        addValidationListener(nameJTextField);
        addValidationListener(organizationJTextField);
        addValidationListener(phoneJTextField);
        addValidationListener(postalCodeJTextField);
        addValidationListener(provinceJTextField);
        addValidationListener(titleJTextField);

        countryJComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                validateInput();
                reloadProvinceLabel();
                reloadPostalCodeLabel();
            }
        });
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

    private void cancelJButtonActionPerformed(final java.awt.event.ActionEvent e) {//GEN-FIRST:event_cancelJButtonActionPerformed
        disposeWindow();
    }//GEN-LAST:event_cancelJButtonActionPerformed

    private void changePasswordJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePasswordJButtonActionPerformed
        disposeWindow();
        getController().displayUpdatePasswordDialog();
    }//GEN-LAST:event_changePasswordJButtonActionPerformed

    private String extract(final javax.swing.JTextField jTextField) {
        return SwingUtil.extract(jTextField, Boolean.TRUE);
    }

    private String extractInputAddress() {
        return extract(addressJTextField);
    }

    private String extractInputCity() {
        return extract(cityJTextField);
    }

    private String extractInputCountry() {
        if (countryJComboBox.getSelectedIndex() >= 0) {
            return ((Locale) countryJComboBox.getSelectedItem()).getISO3Country();
        } else {
            return null;
        }
    }

    private String extractInputEmail() {
        return extract(emailJTextField);
    }

    private String extractInputMobilePhone() {
        return extract(mobilePhoneJTextField);
    }

    private String extractInputName() {
        return extract(nameJTextField);
    }

    private String extractInputOrganization() {
        return extract(organizationJTextField);
    }

    private String extractInputPhone() {
        return extract(phoneJTextField);
    }

    private String extractInputPostalCode() {
        return extract(postalCodeJTextField);
    }

    private String extractInputProvince() {
        return extract(provinceJTextField);
    }

    private String extractInputTitle() {
        return extract(titleJTextField);
    }

    /**
     * Obtain the profile e-mail address.
     * 
     * @return A <code>ProfileEMail</code>.
     */
    private ProfileEMail getEMail() {
        return 0 < emails.size() ? emails.get(0) : null;
    }

    /**
     * Obtain the profile e-mail address as a string.
     * 
     * @return A profile e-mail address <code>String</code>.
     */
    private String getEMailString() {
        final ProfileEMail email = getEMail();
        if (null == email) {
            return null;
        } else {
            return email.getEmail().toString();
        }
    }

    /**
     * Get the label text minus the ":" at the end.
     * 
     * @param jLabel
     *            A <code>JLabel</code>.
     * @return The label text less any ":".
     */
    private String getLabelText(final javax.swing.JLabel jLabel) {
        return StringUtil.removeAfter(jLabel.getText(), ":");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel profileExplanationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel phoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel mobilePhoneJLabel = new javax.swing.JLabel();
        final javax.swing.JSeparator profileJSeparator = new javax.swing.JSeparator();
        final javax.swing.JLabel backupExplanationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel backupJLabel = new javax.swing.JLabel();
        final javax.swing.JButton cancelJButton = ButtonFactory.create();

        profileExplanationJLabel.setFont(Fonts.DialogFont);
        profileExplanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.ProfileExplanation"));

        nameJLabel.setFont(Fonts.DialogFont);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Name"));

        nameJTextField.setText("John McClean");
        ((AbstractDocument) nameJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getName()));
        nameJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                profileJTextFieldActionPerformed(e);
            }
        });

        titleJLabel.setFont(Fonts.DialogFont);
        titleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.UserTitle"));

        titleJTextField.setText("Over The Hill Cop");
        ((AbstractDocument) titleJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getTitle()));
        titleJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                profileJTextFieldActionPerformed(e);
            }
        });

        organizationJLabel.setFont(Fonts.DialogFont);
        organizationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Organization"));

        organizationJTextField.setText("NYPD");
        ((AbstractDocument) organizationJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getOrganization()));
        organizationJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                profileJTextFieldActionPerformed(e);
            }
        });

        phoneJLabel.setFont(Fonts.DialogFont);
        phoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Phone"));

        phoneJTextField.setText("555-555-1111");
        ((AbstractDocument) phoneJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getPhone()));
        phoneJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                profileJTextFieldActionPerformed(e);
            }
        });

        mobilePhoneJLabel.setFont(Fonts.DialogFont);
        mobilePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.MobilePhone"));

        mobilePhoneJTextField.setText("555-555-1111");
        ((AbstractDocument) mobilePhoneJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getMobilePhone()));
        mobilePhoneJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                profileJTextFieldActionPerformed(e);
            }
        });

        addressJLabel.setFont(Fonts.DialogFont);
        addressJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Address"));

        addressJTextField.setText("1234 5th Street");
        ((AbstractDocument) addressJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getAddress()));
        addressJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                profileJTextFieldActionPerformed(e);
            }
        });

        cityJLabel.setFont(Fonts.DialogFont);
        cityJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.City"));

        cityJTextField.setText("NYC");
        ((AbstractDocument) cityJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getCity()));
        cityJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                profileJTextFieldActionPerformed(e);
            }
        });

        provinceJLabel.setFont(Fonts.DialogFont);
        provinceJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Province"));

        provinceJTextField.setText("NY");
        ((AbstractDocument) provinceJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getProvince()));
        provinceJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                profileJTextFieldActionPerformed(e);
            }
        });

        countryJLabel.setFont(Fonts.DialogFont);
        countryJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Country"));
        countryJLabel.setMaximumSize(new java.awt.Dimension(43, 20));
        countryJLabel.setMinimumSize(new java.awt.Dimension(43, 20));
        countryJLabel.setPreferredSize(new java.awt.Dimension(43, 20));

        countryJPanel.setLayout(new java.awt.CardLayout());

        countryJPanel.setMaximumSize(new java.awt.Dimension(32767, 20));
        countryJPanel.setMinimumSize(new java.awt.Dimension(20, 20));
        countryJPanel.setOpaque(false);
        countryJPanel.setPreferredSize(new java.awt.Dimension(74, 20));
        countryJComboBox.setFont(Fonts.DialogTextEntryFont);
        countryJComboBox.setModel(countryModel);
        countryJComboBox.setRenderer(new LocaleRenderer(getController().getLocale()));
        countryJPanel.add(countryJComboBox, "online");

        countryJTextField.setFont(Fonts.DialogFont);
        countryJTextField.setText("Middle Earth");
        countryJPanel.add(countryJTextField, "offline");

        postalCodeJLabel.setFont(Fonts.DialogFont);
        postalCodeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.PostalCode"));

        postalCodeJTextField.setText("90210");
        ((AbstractDocument) postalCodeJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getPostalCode()));
        postalCodeJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                profileJTextFieldActionPerformed(e);
            }
        });

        emailJLabel.setFont(Fonts.DialogFont);
        emailJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Email"));

        emailJTextField.setText("john@nypd.org");
        ((AbstractDocument) emailJTextField.getDocument()).setDocumentFilter(new JTextComponentLengthFilter(profileConstraints.getEmail()));
        emailJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                profileJTextFieldActionPerformed(e);
            }
        });

        backupExplanationJLabel.setFont(Fonts.DialogFont);
        backupExplanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.BackupExplanation"));

        backupJLabel.setFont(Fonts.DialogFont);
        backupJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Backup"));

        backupStatisticsJLabel.setFont(Fonts.DialogFont);
        backupStatisticsJLabel.setText("1 MB");

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error Message!");

        changePasswordJButton.setFont(Fonts.DialogButtonFont);
        changePasswordJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Password"));
        changePasswordJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                changePasswordJButtonActionPerformed(e);
            }
        });

        saveJButton.setFont(Fonts.DialogButtonFont);
        saveJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.OK"));
        saveJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                saveJButtonActionPerformed(e);
            }
        });

        cancelJButton.setFont(Fonts.DialogButtonFont);
        cancelJButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpdateProfileAvatar.Cancel"));
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelJButtonActionPerformed(e);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(profileJSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(backupJLabel)
                        .addContainerGap(423, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                                .addGap(14, 14, 14))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(changePasswordJButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 198, Short.MAX_VALUE)
                                .addComponent(saveJButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelJButton)))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(titleJLabel)
                                            .addComponent(nameJLabel)
                                            .addComponent(organizationJLabel)
                                            .addComponent(phoneJLabel)
                                            .addComponent(mobilePhoneJLabel)
                                            .addComponent(addressJLabel)
                                            .addComponent(cityJLabel)
                                            .addComponent(provinceJLabel)
                                            .addComponent(countryJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(postalCodeJLabel)
                                            .addComponent(emailJLabel))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(countryJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(emailJTextField)
                                            .addComponent(postalCodeJTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(provinceJTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(cityJTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(addressJTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(mobilePhoneJTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(phoneJTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(organizationJTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(titleJTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(nameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)))
                                    .addComponent(backupExplanationJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                                    .addComponent(backupStatisticsJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16))
                            .addComponent(profileExplanationJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, saveJButton});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {backupStatisticsJLabel, nameJTextField});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(profileExplanationJLabel)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(organizationJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(organizationJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mobilePhoneJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mobilePhoneJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cityJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cityJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(provinceJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(provinceJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(countryJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countryJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postalCodeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(postalCodeJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailJLabel))
                .addGap(18, 18, 18)
                .addComponent(profileJSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backupExplanationJLabel)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backupJLabel)
                    .addComponent(backupStatisticsJLabel))
                .addGap(22, 22, 22)
                .addComponent(errorMessageJLabel)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveJButton)
                    .addComponent(cancelJButton)
                    .addComponent(changePasswordJButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Initialize the list of countries for the country picklist.
     */
    private void initCountryModel() {
        for (final Locale locale : getController().getAvailableLocales()) {
            this.countryModel.addElement(locale);
        }
    }

    /**
     * Determine if the backup feature is enabled.
     * 
     * @return True if the backup feature is enabled.
     */
    private boolean isBackupEnabled() {
        return ((UpdateProfileProvider) contentProvider).isBackupEnabled().booleanValue();
    }

    /**
     * Determine if the string changed.
     * 
     * @param current
     *            A <code>String</code>.
     * @param old
     *            A <code>String</code>.
     * @return true if the string changed; false otherwise.
     */
    private Boolean isChanged(final String current, final String old) {
        if (isEmpty(current) || isEmpty(old)) {
            return (isEmpty(current) && isEmpty(old) ? Boolean.FALSE : Boolean.TRUE);
        } else {
            return (!current.equals(old));
        }
    }

    /**
     * Determine if the string is empty.
     * 
     * @param text
     *            A <code>String</code>.
     * @return true if the string is null or blank; false otherwise.
     */
    private Boolean isEmpty(final String text) {
        return (null==text || 0==text.length() ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Determine if the input has changed from the original state.
     * 
     * @return true if the input changed; false otherwise.
     */
    private Boolean isInputChanged() {
        if (isChanged(extractInputName(), profile.getName()) ||
            isChanged(extractInputTitle(), profile.getTitle()) ||
            isChanged(extractInputOrganization(), profile.getOrganization()) ||
            isChanged(extractInputPhone(), profile.getPhone()) ||
            isChanged(extractInputMobilePhone(), profile.getMobilePhone()) ||
            isChanged(extractInputAddress(), profile.getAddress()) ||
            isChanged(extractInputCity(), profile.getCity()) ||
            isChanged(extractInputProvince(), profile.getProvince()) ||
            isChanged(extractInputCountry(), profile.getCountry()) ||
            isChanged(extractInputPostalCode(), profile.getPostalCode()) ||
            isChanged(extractInputEmail(), getEMailString())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Determine if the input email is available.
     * 
     * @return True if the input email is available; false otherwise.
     */
    private Boolean isInputEmailAvailable() {
        final String inputEmail = extractInputEmail();
        if (isChanged(inputEmail, getEMailString())) {
            final EMail newEmail = EMailBuilder.parse(inputEmail);
            final Boolean available = readIsEmailAvailable(newEmail);
            if (!available) {
                unavailableEmail = inputEmail;
            }
            return available;
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * Determine if the input email is different from the last
     * known unavailable email.
     * 
     * @return True if the input email is available; false otherwise.
     */
    private Boolean isInputEmailAvailableQuick() {
        if (null != unavailableEmail) {
            return (!unavailableEmail.equals(extractInputEmail()));
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * Determine if the input email is valid.
     * 
     * @return True if the input email is valid; false otherwise.
     */
    private Boolean isInputEmailValid() {
        if (isEmpty(extractInputEmail())) {
            return Boolean.FALSE;
        } else {
            try {
                EMailBuilder.parse(extractInputEmail());
                return Boolean.TRUE;
            } catch(final EMailFormatException efx) {
                return Boolean.FALSE;
            }
        }
    }

    /**
     * Determine if the model is online.
     * 
     * @return True if the model is online.
     */
    private boolean isOnline() {
        return ((UpdateProfileProvider) contentProvider).isOnline().booleanValue();
    }

    /**
     * Determines if the United States is the selected country.
     * 
     * @return true if the United States is selected; false otherwise
     */
    private Boolean isUnitedStates() {
        if (countryJComboBox.getSelectedIndex() >= 0) {
            return ((Locale) countryJComboBox.getSelectedItem()).getISO3Country().equals("USA");
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Action performed method for all text fields, behaves like save button.
     * 
     * @param evt
     *            An <code>ActionEvent</code>.
     */
    private void profileJTextFieldActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileJTextFieldActionPerformed
        saveJButtonActionPerformed(evt);
    }//GEN-LAST:event_profileJTextFieldActionPerformed

    /**
     * Read the backup statistics.
     * 
     * @return An instance of <code>Statistics</code>.
     */
    private Statistics readBackupStatistics() {
        return ((UpdateProfileProvider) contentProvider).readBackupStatistics();
    }

    /**
     * Read the profile email addresses from the content provider.
     * 
     * @return A <code>List&lt;ProfileEMail&gt;</code>.
     */
    private List<ProfileEMail> readEMails() {
        return ((UpdateProfileProvider) contentProvider).readEMails();
    }

    /**
     * Determine whether or not an e-mail address is available.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if the address is not in use.
     */
    private Boolean readIsEmailAvailable(final EMail email) {
        return ((UpdateProfileProvider) contentProvider).readIsEmailAvailable(email);
    }

    /**
     * Read the profile from the content provider.
     * 
     * @return A <code>Profile</code>.
     */
    private Profile readProfile() {
        return ((UpdateProfileProvider) contentProvider).readProfile();
    }

    /**
     * Reload a text field with a value.
     * 
     * @param jTextField
     *            A <code>JTextField</code>.
     * @param value
     *            A value <code>String</code>.
     * @param online
     *            A <code>boolean</code>, true if online.
     */
    private void reload(final javax.swing.JTextField jTextField,
            final String value, final boolean online) {
        jTextField.setText(null == value ? "" : value);
        setEditable(jTextField, online);
    }

    /**
     * Reload the backup statistics.  If the backup is enabled and the
     * system is online, the backup statistics are displayed.
     */
    private void reloadBackupStatistics() {
        if (null != statistics) {
            reloadBackupStatistics(statistics);
        } else if (isOnline() && isBackupEnabled()) {
            this.statistics = readBackupStatistics();
            reloadBackupStatistics(statistics);
        } else {
            backupStatisticsJLabel.setText(getString("BackupOffline"));
        }
    }

    /**
     * Reload the backup statistics.  If the backup is enabled and the
     * system is online, the backup statistics are displayed.
     * 
     * @param statistics
     *            A <code>Statistics</code>.
     */
    private void reloadBackupStatistics(final Statistics statistics) {
        if (null == backupUnit) {
            backupStatisticsJLabel.setText(BYTES_FORMAT.format(
                    statistics.getDiskUsage()));
        } else {
            backupStatisticsJLabel.setText(BYTES_FORMAT.format(backupUnit,
                    statistics.getDiskUsage()));
        }
    }

    /**
     * Reload the country picklist (used if online) and text field
     * (used if offline).
     * 
     * @param profile
     *            A <code>Profile</code>.
     * @param online
     *            A <code>boolean</code>, true if online.
     */
    private void reloadCountry(final Profile profile,
            final boolean online) {
        Locale locale;
        for (int i = 0; i < countryModel.getSize(); i++) {
            locale = (Locale) countryModel.getElementAt(i);
            if (locale.getISO3Country().equals(profile.getCountry())) {
                countryModel.setSelectedItem(locale);
                countryJTextField.setText(locale.getDisplayCountry(locale));
            }
        }
        if (online) {
            ((java.awt.CardLayout)countryJPanel.getLayout()).show(countryJPanel, "online");
        } else {
            ((java.awt.CardLayout)countryJPanel.getLayout()).show(countryJPanel, "offline");
            setEditable(countryJTextField, online);
        }
    }

    /**
     * Reload the postal code (ie. postal code or zip code) label.
     */
    private void reloadPostalCodeLabel() {
        if (isUnitedStates()) {
            postalCodeJLabel.setText(getString("ZipCode"));
        } else {
            postalCodeJLabel.setText(getString("PostalCode"));    
        }
    }

    /**
     * Reload the province (ie. province or state) label.
     */
    private void reloadProvinceLabel() {
        if (isUnitedStates()) {
            provinceJLabel.setText(getString("State"));
        } else {
            provinceJLabel.setText(getString("Province"));    
        }
    }

    /**
     * Action when the OK button is pressed. Maybe update the profile.
     * 
     * @param evt
     *            An <code>ActionEvent</code>.
     */
    private void saveJButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveJButtonActionPerformed
        if (isInputValid()) {
            boolean emailAvailable = true;
            if (isChanged(extractInputEmail(), getEMailString())) {
                SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
                errorMessageJLabel.setText(getString("CheckingEmail"));
                errorMessageJLabel.paintImmediately(0, 0, errorMessageJLabel
                        .getWidth(), errorMessageJLabel.getHeight());
                emailAvailable = isInputEmailAvailable();
                SwingUtil.setCursor(this, java.awt.Cursor.DEFAULT_CURSOR);
            }
            if (emailAvailable) {
                disposeWindow();
                updateProfile();
                updateEmail();
            } else {
                validateInput();
            }
        }
    }//GEN-LAST:event_saveJButtonActionPerformed

    /**
     * Update the email address.
     */
    private void updateEmail() {
        if (isChanged(extractInputEmail(), getEMailString())) {
            final EMail newEmail = EMailBuilder.parse(extractInputEmail());
            getController().runAddProfileEmail(newEmail);
            getController().runRemoveProfileEmail(emails.get(0).getEmailId());
        }
    }

    /**
     * Update the profile.
     */
    private void updateProfile() {
        final String name = extractInputName();
        final String title = extractInputTitle();
        final String organization = extractInputOrganization();
        final String phone = extractInputPhone();
        final String mobilePhone = extractInputMobilePhone();
        final String address = extractInputAddress();
        final String city = extractInputCity();
        final String province = extractInputProvince();
        final String country = extractInputCountry();
        final String postalCode = extractInputPostalCode();
        getController().runUpdateProfile(name, address, city, country, mobilePhone,
                organization, phone, postalCode, province, title);
    }
}
