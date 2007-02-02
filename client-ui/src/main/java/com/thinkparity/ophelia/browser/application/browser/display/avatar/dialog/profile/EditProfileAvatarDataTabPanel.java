/*
 * EditProfileAvatarDataTabPanel.java
 *
 * Created on December 6, 2006, 1:36 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.util.List;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.dialog.profile.LocaleRenderer;


/**
 *
 * @author robert@thinkparity.com
 */
public class EditProfileAvatarDataTabPanel extends EditProfileAvatarAbstractTabPanel {

    /** The country <code>DefaultComboBoxModel</code>. */
    private final DefaultComboBoxModel countryModel;

    /**
     * Create EditProfileAvatarDataTabPanel.
     *
     * @param browser
     * @param editProfileAvatar
     * @param tabName
     */
    public EditProfileAvatarDataTabPanel(final Browser browser,
            final EditProfileAvatar editProfileAvatar, final String tabName) {
        super(browser, editProfileAvatar, tabName);
        this.countryModel = new DefaultComboBoxModel();
        for (final Locale locale : browser.getAvailableLocales())
            this.countryModel.addElement(locale);
        initComponents();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.EditProfileAvatarAbstractTabPanel#reload(com.thinkparity.codebase.model.profile.Profile)
     */
    @Override
    protected void reload(final Profile profile, final List<ProfileEMail> emails) {
        reload(cityJTextField, profile.getCity());
        Locale locale;
        for (int i = 0; i < countryModel.getSize(); i++) {
            locale = (Locale) countryModel.getElementAt(i);
            if (locale.getCountry().equals(profile.getCountry())) {
                countryModel.setSelectedItem(locale);
            }
        }
        reload(mobilePhoneJTextField, profile.getMobilePhone());
        reload(organizationJTextField, profile.getOrganization());
        reload(officePhoneJTextField, profile.getPhone());
        reload(titleJTextField, profile.getTitle());
        reload(nameJTextField, profile.getName());
        reload(emailJTextField, emails.get(0).getEmail().toString());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.EditProfileAvatarAbstractTabPanel#save()
     */
    @Override
    protected void save() {
        if (isInputValid()) {
            final String name = extractInputName();
            final String city = SwingUtil.extract(cityJTextField);
            final Locale locale = (Locale) countryJComboBox.getSelectedItem();
            final String country = locale.getDisplayCountry();
            final String organization = extractInputOrganization();
            final String title = extractInputTitle();
            final String phone = extractInputOfficePhone();
            final String mobilePhone = extractInputMobilePhone();
            getController().runUpdateProfile(name, null, city, country, mobilePhone,
                    organization, phone, null, null, title);
        }
    }

    /**
     * Determine whether the user input is valid.
     * This method should return false whenever we want the
     * OK button to be disabled.
     * 
     * @return True if the input is valid; false otherwise.
     */
    public Boolean isInputValid() {
        final String name = extractInputName();
        final String organization = extractInputOrganization();
        final String title = extractInputTitle();
        if ((null != name) && (null != organization) && (null != title)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    private String extractInputName() {
        return SwingUtil.extract(nameJTextField);
    }

    private String extractInputOrganization() {
        return SwingUtil.extract(organizationJTextField);
    }
    
    private String extractInputTitle() {
        return SwingUtil.extract(titleJTextField);
    }
    
    private String extractInputOfficePhone() {
        return SwingUtil.extract(officePhoneJTextField);
    }
    
    private String extractInputMobilePhone() {
        return SwingUtil.extract(mobilePhoneJTextField);
    }
    
    /**
     * Reload a text field with a value.
     * 
     * @param jTextField
     *            A <code>JTextField</code>.
     * @param value
     *            A value <code>String</code>.
     */
    private void reload(final javax.swing.JTextField jTextField,
            final String value) {
        jTextField.setText(null == value ? "" : value);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        nameJLabel = new javax.swing.JLabel();
        nameJTextField = new javax.swing.JTextField();
        titleJLabel = new javax.swing.JLabel();
        titleJTextField = new javax.swing.JTextField();
        organizationJLabel = new javax.swing.JLabel();
        organizationJTextField = new javax.swing.JTextField();
        officePhoneJLabel = new javax.swing.JLabel();
        officePhoneJTextField = new javax.swing.JTextField();
        mobilePhoneJLabel = new javax.swing.JLabel();
        mobilePhoneJTextField = new javax.swing.JTextField();
        countryJLabel = new javax.swing.JLabel();
        countryJComboBox = new javax.swing.JComboBox();
        cityJTextField = new javax.swing.JTextField();
        cityJLabel = new javax.swing.JLabel();
        emailJLabel = new javax.swing.JLabel();
        emailJTextField = new javax.swing.JTextField();
        emailJButton = new javax.swing.JButton();

        setOpaque(false);
        nameJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        nameJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("EditProfileDialogDataTabPanel.Name"));

        titleJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        titleJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("EditProfileDialogDataTabPanel.Title"));

        organizationJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        organizationJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("EditProfileDialogDataTabPanel.Organization"));

        officePhoneJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        officePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("EditProfileDialogDataTabPanel.OfficePhone"));

        mobilePhoneJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        mobilePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("EditProfileDialogDataTabPanel.MobilePhone"));

        countryJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        countryJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("EditProfileDialogDataTabPanel.Country"));

        countryJComboBox.setModel(countryModel);
        countryJComboBox.setRenderer(new LocaleRenderer(browser.getLocale()));

        cityJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cityJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("EditProfileDialogDataTabPanel.City"));

        emailJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        emailJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("EditProfileDialogDataTabPanel.Email"));

        emailJButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("EditProfileDialogDataTabPanel.EmailButtonChange"));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, titleJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, nameJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, organizationJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, officePhoneJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, mobilePhoneJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, countryJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(nameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                            .add(titleJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                            .add(organizationJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                            .add(officePhoneJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                            .add(countryJComboBox, 0, 263, Short.MAX_VALUE)
                            .add(mobilePhoneJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(emailJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(cityJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(emailJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(emailJButton))
                            .add(cityJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(nameJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(titleJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(titleJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(organizationJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(organizationJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(officePhoneJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(officePhoneJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(mobilePhoneJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(mobilePhoneJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(countryJComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(countryJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cityJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cityJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(emailJLabel)
                    .add(emailJButton)
                    .add(emailJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cityJLabel;
    private javax.swing.JTextField cityJTextField;
    private javax.swing.JComboBox countryJComboBox;
    private javax.swing.JLabel countryJLabel;
    private javax.swing.JButton emailJButton;
    private javax.swing.JLabel emailJLabel;
    private javax.swing.JTextField emailJTextField;
    private javax.swing.JLabel mobilePhoneJLabel;
    private javax.swing.JTextField mobilePhoneJTextField;
    private javax.swing.JLabel nameJLabel;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JLabel officePhoneJLabel;
    private javax.swing.JTextField officePhoneJTextField;
    private javax.swing.JLabel organizationJLabel;
    private javax.swing.JTextField organizationJTextField;
    private javax.swing.JLabel titleJLabel;
    private javax.swing.JTextField titleJTextField;
    // End of variables declaration//GEN-END:variables
}
