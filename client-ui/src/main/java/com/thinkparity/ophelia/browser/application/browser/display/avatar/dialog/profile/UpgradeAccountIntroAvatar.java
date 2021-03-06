/*
 * UpgradeAccountIntroAvatar.java
 *
 * Created on July 29, 2007, 3:20 PM
 */

package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;

/**
 *
 * @author robert@thinkparity.com
 */
public class UpgradeAccountIntroAvatar extends DefaultUpgradeAccountPage {

    /** Creates new form UpgradeAccountIntroAvatar */
    public UpgradeAccountIntroAvatar() {
        super("UpgradeAccountAvatar.Intro", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_INTRO;
    }

    /**)
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#getNextPageName()
     */
    public String getNextPageName() {
        return getPageName(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_AGREEMENT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.UpgradeAccountPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.DefaultUpgradeAccountPage#isFirstPage()
     */
    @Override
    public Boolean isFirstPage() {
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        validateInput();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.DefaultUpgradeAccountPage#validateInput()
     */
    @Override
    public void validateInput() {
        super.validateInput();
        if (isUpgradeAccountDelegateInitialized()) {
            upgradeAccountDelegate.enableNextButton(Boolean.TRUE);
        }
    }

    private void benefitLearnMoreJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_benefitLearnMoreJLabelMousePressed
        getController().runLearnMore(LearnMore.Topic.SIGNUP);
    }//GEN-LAST:event_benefitLearnMoreJLabelMousePressed

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel welcomeJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel introJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel benefitsTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel benefitLearnMoreJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
        final javax.swing.JLabel securityTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel securityLearnMoreJLabel = LabelFactory.createLink("",Fonts.DefaultFont);

        setOpaque(false);
        welcomeJLabel.setFont(Fonts.DialogFontBold);
        welcomeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.Welcome"));

        introJLabel.setFont(Fonts.DialogFont);
        introJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.Intro"));
        introJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        benefitsTitleJLabel.setFont(Fonts.DialogFont);
        benefitsTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.BenefitsTitle"));
        benefitsTitleJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        benefitLearnMoreJLabel.setFont(Fonts.DialogFont);
        benefitLearnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.BenefitsLearnMore"));
        benefitLearnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                benefitLearnMoreJLabelMousePressed(evt);
            }
        });

        securityTitleJLabel.setFont(Fonts.DialogFont);
        securityTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.SecurityTitle"));

        securityLearnMoreJLabel.setFont(Fonts.DialogFont);
        securityLearnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.SecurityLearnMore"));
        securityLearnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                securityLearnMoreJLabelMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(benefitLearnMoreJLabel))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(welcomeJLabel)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(introJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(benefitsTitleJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(securityTitleJLabel)
                            .addComponent(securityLearnMoreJLabel))))
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(welcomeJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(introJLabel)
                .addGap(33, 33, 33)
                .addComponent(benefitsTitleJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(benefitLearnMoreJLabel)
                .addGap(33, 33, 33)
                .addComponent(securityTitleJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(securityLearnMoreJLabel)
                .addContainerGap(26, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void securityLearnMoreJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_securityLearnMoreJLabelMousePressed
        getController().runLearnMore(LearnMore.Topic.SECURITY);
    }//GEN-LAST:event_securityLearnMoreJLabelMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
