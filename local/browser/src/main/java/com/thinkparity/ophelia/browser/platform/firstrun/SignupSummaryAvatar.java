/*
 * SignupSummaryAvatar.java
 *
 * Created on April 20, 2007, 10:01 AM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.Constants.Product;
import com.thinkparity.ophelia.model.profile.ReservationExpiredException;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.firstrun.SignupData.DataKey;

/**
 *
 * @author  user
 */
public class SignupSummaryAvatar extends DefaultSignupPage {

    /** Creates new form SignupSummaryAvatar */
    public SignupSummaryAvatar() {
        super("SignupAvatar.Summary", BrowserConstants.DIALOGUE_BACKGROUND);
        initComponents();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getId()
     * 
     */
    public AvatarId getId() {
        return AvatarId.DIALOG_PLATFORM_SIGNUP_SUMMARY;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getNextPageName()
     */
    public String getNextPageName() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#getPreviousPageName()
     */
    public String getPreviousPageName() {
        final FeatureSet featureSet = (FeatureSet) ((Data) input).get(DataKey.FEATURE_SET);
        if (featureSet == FeatureSet.FREE) {
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT);
        } else {
            // TODO When ready, hook in payment tab.
            //return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_ACCOUNT);
            return getPageName(AvatarId.DIALOG_PLATFORM_SIGNUP_PAYMENT);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isLastPage()
     */
    public Boolean isLastPage() {
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.firstrun.SignupPage#isNextOk()
     */
    public Boolean isNextOk() {
        if (!isInputValid()) {
            return Boolean.FALSE;
        }
        signUp();
        return (!containsInputErrors());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        validateInput();
        // TODO
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

        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }

        if (isSignupDelegateInitialized()) {
            signupDelegate.enableNextButton(Boolean.TRUE);
        }
    }

    /**
     * Create a new profile.
     *
     */
    private void createProfile() {
        final UsernameReservation usernameReservation = (UsernameReservation) ((Data) input).get(SignupData.DataKey.USERNAME_RESERVATION);
        final EMailReservation emailReservation = (EMailReservation) ((Data) input).get(SignupData.DataKey.EMAIL_RESERVATION);
        final Credentials credentials = (Credentials) ((Data) input).get(SignupData.DataKey.CREDENTIALS);
        final Profile profile = (Profile) ((Data) input).get(SignupData.DataKey.PROFILE);
        final EMail email = (EMail) ((Data) input).get(SignupData.DataKey.EMAIL);
        final String securityQuestion = (String) ((Data) input).get(SignupData.DataKey.SECURITY_QUESTION);
        final String securityAnswer = (String) ((Data) input).get(SignupData.DataKey.SECURITY_ANSWER);
        try {
            profile.setFeatures(extractFeatures());
            createProfile(usernameReservation, emailReservation, credentials,
                    profile, email, securityQuestion, securityAnswer);
        } catch (final ReservationExpiredException rex) {
            addInputError(getString("ErrorReservationExpired"));
        } catch (final Throwable t) {
            addInputError(getString("ErrorCreateAccount"));
        }
    }

    /**
     * Create a new profile.
     * 
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
     * @param emailReservation
     *            An <code>EMailReservation</code>.
     * @param credentials
     *            A set of user <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityQuestion
     *            A security question <code>String</code>.
     * @param securityAnswer
     *            A security answer <code>String</code>.
     * @throws ReservationExpiredException
     */
    private void createProfile(final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final String securityQuestion,
            final String securityAnswer) throws ReservationExpiredException {
        ((SignupProvider) contentProvider).createProfile(usernameReservation,
                emailReservation, credentials, profile, email,
                securityQuestion, securityAnswer);
    }

    /**
     * Extract the features.
     * 
     * @return A list of <code>Feature</code>.
     */
    private List<Feature> extractFeatures() {
        final FeatureSet featureSet = (FeatureSet) ((Data) input).get(DataKey.FEATURE_SET);
        final List<Feature> allFeatures = ((SignupProvider) contentProvider).readFeatures();
        final List<Feature> features = new ArrayList<Feature>();
        switch (featureSet) {
        case FREE:
            break;
        case STANDARD:
            for (final Feature feature : allFeatures)
                if (feature.getName().equals(Product.Features.CORE))
                    features.add(feature);
            break;
        case PREMIUM:
            for (final Feature feature : allFeatures)
                features.add(feature);
            break;
        }
        return features;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.JLabel titleJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel explanation1JLabel = new javax.swing.JLabel();
        final javax.swing.JLabel explanation2JLabel = new javax.swing.JLabel();
        final javax.swing.JLabel thankYouJLabel = new javax.swing.JLabel();

        setOpaque(false);
        titleJLabel.setFont(Fonts.DialogFont);
        titleJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Summary.Title"));

        explanation1JLabel.setFont(Fonts.DialogFont);
        explanation1JLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Summary.Explanation1"));
        explanation1JLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        explanation2JLabel.setFont(Fonts.DialogFont);
        explanation2JLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Summary.Explanation2"));
        explanation2JLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        thankYouJLabel.setFont(Fonts.DialogFont);
        thankYouJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("SignupAvatar.Summary.ThankYou"));

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
                    .addComponent(explanation2JLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(explanation1JLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(titleJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(thankYouJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(errorMessageJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleJLabel)
                .addGap(18, 18, 18)
                .addComponent(explanation1JLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(explanation2JLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(thankYouJLabel)
                .addGap(77, 77, 77)
                .addComponent(errorMessageJLabel)
                .addContainerGap(35, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Sign up.
     */
    private void signUp() {
        SwingUtil.setCursor(this, java.awt.Cursor.WAIT_CURSOR);
        errorMessageJLabel.setText(getString("SigningUp"));
        errorMessageJLabel.paintImmediately(0, 0, errorMessageJLabel.getWidth(), errorMessageJLabel.getHeight());
        createProfile();
        errorMessageJLabel.setText(" ");
        if (containsInputErrors()) {
            errorMessageJLabel.setText(getInputErrors().get(0));
        }
        SwingUtil.setCursor(this, java.awt.Cursor.DEFAULT_CURSOR);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel errorMessageJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables
}
