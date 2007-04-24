/*
 * SignupLicenseAgreementAvatar.java
 *
 * Created on April 2, 2007, 10:01 AM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Point;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;

/**
 *
 * @author  user
 */
public class SignupLicenseAgreementAvatar extends DefaultSignupPage {

    /** Creates new form SignupLicenseAgreementAvatar */
    public SignupLicenseAgreementAvatar() {
        super("SignupAvatar.LicenseAgreement", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_AGREEMENT;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     */
    public String getNextPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_PROFILE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_INTRO);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadRadioButtons();
        reloadLicenseAgreementText();
        licenseAgreementJScrollPane.getViewport().setViewPosition(new Point(0,0));
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
        if (isSignupDelegateInitialized()) {
            signupDelegate.enableNextButton(acceptJRadioButton.isSelected());
        }
    }

    private void acceptJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptJRadioButtonActionPerformed
        acceptJRadioButton.setSelected(true);
        validateInput();
    }//GEN-LAST:event_acceptJRadioButtonActionPerformed

    private void declineJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineJRadioButtonActionPerformed
        declineJRadioButton.setSelected(true);
        validateInput();
    }//GEN-LAST:event_declineJRadioButtonActionPerformed

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.ButtonGroup acceptOrDeclineButtonGroup = new javax.swing.ButtonGroup();
        final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();

        setOpaque(false);
        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.LicenseAgreement.Explanation"));

        licenseAgreementJTextArea.setColumns(20);
        licenseAgreementJTextArea.setEditable(false);
        licenseAgreementJTextArea.setFont(Fonts.DialogTextEntryFont);
        licenseAgreementJTextArea.setRows(5);
        licenseAgreementJTextArea.setTabSize(4);
        licenseAgreementJTextArea.setFocusable(false);
        licenseAgreementJScrollPane.setViewportView(licenseAgreementJTextArea);

        acceptOrDeclineButtonGroup.add(acceptJRadioButton);
        acceptJRadioButton.setFont(Fonts.DialogFont);
        acceptJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.LicenseAgreement.AcceptLicenseAgreement"));
        acceptJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        acceptJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        acceptJRadioButton.setOpaque(false);
        acceptJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptJRadioButtonActionPerformed(evt);
            }
        });

        acceptOrDeclineButtonGroup.add(declineJRadioButton);
        declineJRadioButton.setFont(Fonts.DialogFont);
        declineJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.LicenseAgreement.DeclineLicenseAgreement"));
        declineJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        declineJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        declineJRadioButton.setOpaque(false);
        declineJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declineJRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(licenseAgreementJScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                    .addComponent(explanationJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                    .addComponent(acceptJRadioButton, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(declineJRadioButton, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(explanationJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(licenseAgreementJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(acceptJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(declineJRadioButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reload the license agreement text.
     */
    private void reloadLicenseAgreementText() {
        SwingUtil.insert(licenseAgreementJTextArea, openResource("LicenseAgreement"));
    }

    /**
     * Reload the radio buttons.
     */
    private void reloadRadioButtons() {
        acceptJRadioButton.setSelected(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JRadioButton acceptJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JRadioButton declineJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JScrollPane licenseAgreementJScrollPane = new javax.swing.JScrollPane();
    private final javax.swing.JTextArea licenseAgreementJTextArea = new javax.swing.JTextArea();
    // End of variables declaration//GEN-END:variables
}
