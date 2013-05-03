/*
 * SignupIntroAvatar.java
 *
 * Created on April 19, 2007, 10:22 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.migrator.Feature;

import com.thinkparity.ophelia.model.session.OfflineException;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;

/**
 * <b>Title:</b>thinkParity OpheliaUI Sign-Up Intro Avatar<br>
 * 
 * @author robert@thinkparity.com
 */
public class SignupIntroAvatar extends DefaultSignupPage {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.ButtonGroup accountButtonGroup = new javax.swing.ButtonGroup();
    private final javax.swing.JRadioButton createGuestAccountJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JRadioButton createStandardAccountJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    private final javax.swing.JRadioButton haveAccountJRadioButton = new javax.swing.JRadioButton();
    private final javax.swing.JLabel learnMoreJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    // End of variables declaration//GEN-END:variables

    /** A list of available features */
    private final List<Feature> featuresAvailable;

    /** Offline error <code>Boolean</code>. */
    Boolean offlineError;

    /** Creates new form SignupIntroAvatar */
    public SignupIntroAvatar() {
        super("SignupAvatar.Intro", BrowserConstants.DIALOGUE_BACKGROUND);
        this.featuresAvailable = new ArrayList<Feature>();
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
        if (offlineError) {
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_UPDATE_CONFIGURATION);
        } else if (haveAccountJRadioButton.isSelected()) {
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
     * @see com.thinkparity.ophelia.browser.platform.firstrun.DefaultSignupPage#isNextOk()
     */
    @Override
    public Boolean isNextOk() {
        if (!isInputValid()) {
            return Boolean.FALSE;
        }
        // read features, and at the same time, determine if it is possible
        // to connect to the server.
        readFeatures();
        return !containsInputErrors();
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
        final Boolean haveAccount = haveAccountJRadioButton.isSelected();
        ((Data) input).set(SignupData.DataKey.EXISTING_ACCOUNT, haveAccount);
        if (!haveAccount) {
            ((Data) input).set(SignupData.DataKey.FEATURE_SET, extractFeatureSet());
        }
        ((Data) input).set(SignupData.DataKey.FEATURES_AVAILABLE, featuresAvailable);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#validateInput()
     *
     */
    @Override
    public final void validateInput() {
        super.validateInput();
        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
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

    /**
     * Extract the feature set based upon the account type selection.
     * 
     * @return A <code>FeatureSet</code>.
     */
    private FeatureSet extractFeatureSet() {
        if (createGuestAccountJRadioButton.isSelected()) {
            return FeatureSet.FREE;
        } else if (createStandardAccountJRadioButton.isSelected()) {
            return FeatureSet.STANDARD;
        } else {
            throw Assert.createUnreachable("Unknown account type selected.");
        }
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

        accountButtonGroup.add(createStandardAccountJRadioButton);
        createStandardAccountJRadioButton.setFont(Fonts.DialogFont);
        createStandardAccountJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Intro.CreateStandardAccount"));
        createStandardAccountJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        createStandardAccountJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        createStandardAccountJRadioButton.setOpaque(false);

        accountButtonGroup.add(createGuestAccountJRadioButton);
        createGuestAccountJRadioButton.setFont(Fonts.DialogFont);
        createGuestAccountJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Intro.CreateGuestAccount"));
        createGuestAccountJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        createGuestAccountJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        createGuestAccountJRadioButton.setOpaque(false);

        accountButtonGroup.add(haveAccountJRadioButton);
        haveAccountJRadioButton.setFont(Fonts.DialogFont);
        haveAccountJRadioButton.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Intro.HaveAccount"));
        haveAccountJRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        haveAccountJRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        haveAccountJRadioButton.setOpaque(false);

        learnMoreJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Intro.LearnMore"));
        learnMoreJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                learnMoreJLabelMousePressed(evt);
            }
        });

        errorMessageJLabel.setFont(Fonts.DialogFont);
        errorMessageJLabel.setForeground(Colours.DIALOG_ERROR_TEXT_FG);
        errorMessageJLabel.setText("!Error message!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(createStandardAccountJRadioButton)
                        .addGap(47, 47, 47)
                        .addComponent(learnMoreJLabel))
                    .addComponent(createGuestAccountJRadioButton)
                    .addComponent(haveAccountJRadioButton)))
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(welcomeJLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(explanationJLabel)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(welcomeJLabel)
                .addGap(29, 29, 29)
                .addComponent(explanationJLabel)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createStandardAccountJRadioButton)
                    .addComponent(learnMoreJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createGuestAccountJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(haveAccountJRadioButton)
                .addGap(30, 30, 30)
                .addComponent(errorMessageJLabel)
                .addContainerGap(47, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void learnMoreJLabelMousePressed(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_learnMoreJLabelMousePressed
        platform.runLearnMore(LearnMore.Topic.SIGNUP);
    }//GEN-LAST:event_learnMoreJLabelMousePressed

    /**
     * Read the available features.
     * This also acts as a test of being online.
     * 
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    private void readFeatures() {
        featuresAvailable.clear();
        offlineError = Boolean.FALSE;
        signupDelegate.enableNextButton(Boolean.FALSE);
        SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
        try {
            errorMessageJLabel.setText(getString("ReadingFeatures"));
            errorMessageJLabel.paintImmediately(0, 0, errorMessageJLabel
                    .getWidth(), errorMessageJLabel.getHeight());
            featuresAvailable.addAll(((SignupProvider) contentProvider).readFeatures());
        } catch (final OfflineException ox) {
            logger.logError(ox, "An offline error has occurred.");
            offlineError = Boolean.TRUE;
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
     * Reload the account radio buttons.
     */
    private void reloadAccountRadioButtons() {
        createStandardAccountJRadioButton.setSelected(true);
    }
}
