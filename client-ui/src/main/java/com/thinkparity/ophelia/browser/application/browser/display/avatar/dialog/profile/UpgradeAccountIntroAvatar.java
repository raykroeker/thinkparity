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
        return getPageName(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT_PAYMENT);
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
        final javax.swing.JLabel benefit1JLabel = new javax.swing.JLabel();
        final javax.swing.JLabel benefit2JLabel = new javax.swing.JLabel();
        final javax.swing.JLabel benefitLearnMoreJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
        final javax.swing.JLabel privacyTitleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel privacyExplanationJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel privacyLearnMoreJLabel = LabelFactory.createLink("",Fonts.DefaultFont);

        setOpaque(false);
        welcomeJLabel.setFont(Fonts.DialogFontBold);
        welcomeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.Welcome"));

        introJLabel.setFont(Fonts.DialogFont);
        introJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.Intro"));
        introJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        benefitsTitleJLabel.setFont(Fonts.DialogFontBold);
        benefitsTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.BenefitsTitle"));

        benefit1JLabel.setFont(Fonts.DialogFont);
        benefit1JLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CheckBoxMenuItemCheckIcon.png")));
        benefit1JLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.Benefit1"));

        benefit2JLabel.setFont(Fonts.DialogFont);
        benefit2JLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CheckBoxMenuItemCheckIcon.png")));
        benefit2JLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.Benefit2"));

        benefitLearnMoreJLabel.setFont(Fonts.DialogFont);
        benefitLearnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.BenefitsLearnMore"));
        benefitLearnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                benefitLearnMoreJLabelMousePressed(evt);
            }
        });

        privacyTitleJLabel.setFont(Fonts.DialogFontBold);
        privacyTitleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.PrivacyTitle"));

        privacyExplanationJLabel.setFont(Fonts.DialogFont);
        privacyExplanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.PrivacyExplanation"));
        privacyExplanationJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        privacyLearnMoreJLabel.setFont(Fonts.DialogFont);
        privacyLearnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("UpgradeAccountAvatar.Intro.PrivacyLearnMore"));
        privacyLearnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                privacyLearnMoreJLabelMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(privacyTitleJLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(introJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(benefitsTitleJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(benefit1JLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(benefit2JLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(benefitLearnMoreJLabel, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(privacyLearnMoreJLabel)
                            .addComponent(privacyExplanationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(welcomeJLabel))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(welcomeJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(introJLabel)
                .addGap(33, 33, 33)
                .addComponent(benefitsTitleJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(benefit2JLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(benefit1JLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(benefitLearnMoreJLabel)
                .addGap(33, 33, 33)
                .addComponent(privacyTitleJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(privacyExplanationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(privacyLearnMoreJLabel)
                .addContainerGap(27, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void privacyLearnMoreJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_privacyLearnMoreJLabelMousePressed
        getController().runLearnMore(LearnMore.Topic.PRIVACY);
    }//GEN-LAST:event_privacyLearnMoreJLabelMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
