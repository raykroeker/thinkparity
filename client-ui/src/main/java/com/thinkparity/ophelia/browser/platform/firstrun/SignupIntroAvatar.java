/*
 * SignupIntroAvatar.java
 *
 * Created on April 19, 2007, 10:22 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Graphics;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity OpheliaUI Sign-Up Intro Avatar<br>
 * 
 * @author robert@thinkparity.com
 */
public class SignupIntroAvatar extends DefaultSignupPage {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.ButtonGroup accountButtonGroup = new javax.swing.ButtonGroup();
    private final javax.swing.JRadioButton doNotHaveAccountJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JRadioButton haveAccountJRadioButton = new javax.swing.JRadioButton();
    // End of variables declaration//GEN-END:variables

    /** Creates new form SignupIntroAvatar */
    public SignupIntroAvatar() {
        super("SignupAvatar.Intro", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_INTRO;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     */
    public String getNextPageName() {
        if (haveAccountJRadioButton.isSelected()) {
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_CREDENTIALS);
        } else {
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_AGREEMENT);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isFirstPage()
     */
    public Boolean isFirstPage() {
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        reloadAccountRadioButtons();
        validateInput();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#saveData()
     */
    public void saveData() {
        ((Data) input).set(SignupData.DataKey.EXISTING_ACCOUNT, haveAccountJRadioButton.isSelected());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public final void validateInput() {
        super.validateInput();
        if (isSignupDelegateInitialized()) {
            signupDelegate.enableNextButton(Boolean.TRUE);
        }
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics g2 = g.create();
        try {
            // Draw the logo.
            g2.drawImage(Images.BrowserTitle.LOGO_LARGE,
                    (getWidth() - Images.BrowserTitle.LOGO_LARGE.getWidth()) / 2, 35,
                    Images.BrowserTitle.LOGO_LARGE.getWidth(),
                    Images.BrowserTitle.LOGO_LARGE.getHeight(), SignupIntroAvatar.this);
        }
        finally { g2.dispose(); }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel welcomeJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel explanationJLabel = new javax.swing.JLabel();

        setOpaque(false);
        welcomeJLabel.setFont(Fonts.DialogFontBold);
        welcomeJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Intro.Welcome"));

        explanationJLabel.setFont(Fonts.DialogFont);
        explanationJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Intro.Explanation"));

        accountButtonGroup.add(doNotHaveAccountJRadioButton);
        doNotHaveAccountJRadioButton.setFont(Fonts.DialogFont);
        doNotHaveAccountJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Intro.DoNotHaveAccount"));
        doNotHaveAccountJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        doNotHaveAccountJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        doNotHaveAccountJRadioButton.setOpaque(false);

        accountButtonGroup.add(haveAccountJRadioButton);
        haveAccountJRadioButton.setFont(Fonts.DialogFont);
        haveAccountJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Intro.HaveAccount"));
        haveAccountJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        haveAccountJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        haveAccountJRadioButton.setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(explanationJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(welcomeJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(haveAccountJRadioButton)
                            .addComponent(doNotHaveAccountJRadioButton))
                        .addGap(121, 121, 121))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(welcomeJLabel)
                .addGap(42, 42, 42)
                .addComponent(explanationJLabel)
                .addGap(22, 22, 22)
                .addComponent(doNotHaveAccountJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(haveAccountJRadioButton)
                .addContainerGap(47, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reload the account radio buttons.
     */
    private void reloadAccountRadioButtons() {
        doNotHaveAccountJRadioButton.setSelected(true);
    }
}
