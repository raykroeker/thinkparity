/*
 * SignupLicenseAgreementAvatar.java
 *
 * Created on April 2, 2007, 10:01 AM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Point;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;

/**
 *
 * @author  user
 */
public class SignupLicenseAgreementAvatar extends Avatar
        implements SignupPage {

    /** The  <code>SignupDelegate</code>. */
    private SignupDelegate signupDelegate;

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
        return SignupPageId.ACCOUNT_INFORMATION.toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPageName()
     */
    public String getPageName() {
        return getSignupPageId().toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return null;
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState() */
    public State getState() {
        throw Assert.createNotYetImplemented("SignupAvatar.LicenseAgreement#getState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isAvatarBackgroundImage()
     */
    @Override
    public Boolean isAvatarBackgroundImage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isFirstPage()
     */
    public Boolean isFirstPage() {
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isLastPage()
     */
    public Boolean isLastPage() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        return isInputValid();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadRadioButtons();
        reloadLicenseAgreementText();
        licenseAgreementJScrollPane.getViewport().setViewPosition(new Point(0,0));
        // Don't bother validating input if the signup delegate has not been assigned yet
        // since the goal is to enable or disable the 'next' button
        if (null != signupDelegate) {
            validateInput();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     */
    public void saveData() {
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#setSignupDelegate(com.thinkparity.ophelia.browser.platform.firstrun.SignupDelegate)
     */
    public void setSignupDelegate(final SignupDelegate signupDelegate) {
        this.signupDelegate = signupDelegate;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     * 
     */
    public void setState(final State state) {
        throw Assert.createNotYetImplemented("SignupAvatar.LicenseAgreement#setState");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public final void validateInput() {
        super.validateInput();
        signupDelegate.enableNextButton(acceptJRadioButton.isSelected());
    }

    private void acceptJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptJRadioButtonActionPerformed
        acceptJRadioButton.setSelected(true);
        validateInput();
    }//GEN-LAST:event_acceptJRadioButtonActionPerformed

    private void declineJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineJRadioButtonActionPerformed
        declineJRadioButton.setSelected(true);
        validateInput();
    }//GEN-LAST:event_declineJRadioButtonActionPerformed

    /**
     * Get the signup page id.
     * 
     * @return A <code>SignupPageId</code>.
     */
    private SignupPageId getSignupPageId() {
        return SignupPageId.LICENSE_AGREEMENT;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.ButtonGroup acceptOrDeclineButtonGroup = new javax.swing.ButtonGroup();
        final javax.swing.JLabel titleJLabel = new javax.swing.JLabel();

        setOpaque(false);
        titleJLabel.setFont(Fonts.DialogFont);
        titleJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.LicenseAgreement.Title"));

        licenseAgreementJTextArea.setColumns(20);
        licenseAgreementJTextArea.setEditable(false);
        licenseAgreementJTextArea.setFont(Fonts.DialogTextEntryFont);
        licenseAgreementJTextArea.setRows(5);
        licenseAgreementJTextArea.setTabSize(4);
        licenseAgreementJTextArea.setFocusable(false);
        licenseAgreementJScrollPane.setViewportView(licenseAgreementJTextArea);

        acceptOrDeclineButtonGroup.add(acceptJRadioButton);
        acceptJRadioButton.setFont(Fonts.DialogFont);
        acceptJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.LicenseAgreement.AcceptLicenseAgreement"));
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
        declineJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("SignupAvatar.LicenseAgreement.DeclineLicenseAgreement"));
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
                    .addComponent(titleJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                    .addComponent(acceptJRadioButton, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(declineJRadioButton, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleJLabel)
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
