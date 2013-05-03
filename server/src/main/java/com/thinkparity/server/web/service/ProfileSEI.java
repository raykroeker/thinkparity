/*
 * Created On:  3-Jun-07 3:09:47 PM
 */
package com.thinkparity.desdemona.web.service;

import java.util.List;

import javax.jws.WebService;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.profile.*;
import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.Token;

import com.thinkparity.desdemona.model.ModelFactory;
import com.thinkparity.desdemona.model.profile.ProfileModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.ProfileService;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.ProfileService")
public class ProfileSEI extends ServiceSEI implements ProfileService {

    /**
     * Create ProfileSEI.
     *
     */
    public ProfileSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.ProfileService#create(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.migrator.Release, com.thinkparity.codebase.model.profile.UsernameReservation, com.thinkparity.codebase.model.profile.EMailReservation, com.thinkparity.codebase.model.session.Credentials, com.thinkparity.codebase.model.profile.Profile, com.thinkparity.codebase.email.EMail, com.thinkparity.codebase.model.profile.SecurityCredentials)
     *
     */
    @Override
    public void create(final AuthToken authToken, final Product product,
            final Release release,
            final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials) {
        getModel().create(product, release, usernameReservation,
                emailReservation, credentials, profile, email,
                securityCredentials);
    }

    /**
     * @see com.thinkparity.service.ProfileService#create(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release,
     *      com.thinkparity.codebase.model.profile.UsernameReservation,
     *      com.thinkparity.codebase.model.profile.EMailReservation,
     *      com.thinkparity.codebase.model.session.Credentials,
     *      com.thinkparity.codebase.model.profile.Profile,
     *      com.thinkparity.codebase.email.EMail,
     *      com.thinkparity.codebase.model.profile.SecurityCredentials,
     *      com.thinkparity.codebase.model.profile.payment.PaymentInfo)
     * 
     */
    public void create(final AuthToken authToken, final Product product,
            final Release release,
            final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials,
            final PaymentInfo paymentInfo) {
        getModel().create(product, release, usernameReservation,
                emailReservation, credentials, profile, email,
                securityCredentials, paymentInfo);
    }

    /**
     * @see com.thinkparity.service.ProfileService#create(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release,
     *      com.thinkparity.codebase.model.profile.UsernameReservation,
     *      com.thinkparity.codebase.model.profile.EMailReservation,
     *      com.thinkparity.codebase.model.session.Credentials,
     *      com.thinkparity.codebase.model.profile.Profile,
     *      com.thinkparity.codebase.email.EMail,
     *      com.thinkparity.codebase.model.profile.SecurityCredentials,
     *      com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials)
     * 
     */
    public void create(final AuthToken authToken, final Product product,
            final Release release,
            final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials,
            final PaymentPlanCredentials paymentPlanCredentials)
            throws InvalidCredentialsException {
        getModel().create(product, release, usernameReservation,
                emailReservation, credentials, profile, email,
                securityCredentials, paymentPlanCredentials);
    }

    /**
     * @see com.thinkparity.service.ProfileService#createEMailReservation(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.email.EMail)
     * 
     */
    public EMailReservation createEMailReservation(final AuthToken authToken,
            final EMail email) {
        return getModel().createEMailReservation(email);
    }

    /**
     * @see com.thinkparity.service.ProfileService#createToken(com.thinkparity.service.AuthToken)
     *
     */
    public Token createToken(final AuthToken authToken) {
        return getModel(authToken).createToken();
    }

    /**
     * @see com.thinkparity.service.ProfileService#createUsernameReservation(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.profile.Profile)
     *
     */
    @Override
    public UsernameReservation createUsernameReservation(
            final AuthToken authToken, final Profile profile) {
        return getModel().createUsernameReservation(profile);
    }

    /**
     * @see com.thinkparity.service.SessionService#createUsernameReservation(java.lang.String, java.util.Calendar)
     *
     */
    public UsernameReservation createUsernameReservation(
            final AuthToken authToken, final String username) {
        return getModel().createUsernameReservation(username);
    }

    /**
     * @see com.thinkparity.service.ProfileService#isAccessiblePaymentInfo(com.thinkparity.service.AuthToken)
     *
     */
    @Override
    public Boolean isAccessiblePaymentInfo(final AuthToken authToken) {
        return getModel(authToken).isAccessiblePaymentInfo();
    }

    /**
     * @see com.thinkparity.service.ProfileService#isEMailAvailable(com.thinkparity.service.AuthToken, com.thinkparity.codebase.email.EMail)
     *
     */
    public Boolean isEMailAvailable(final AuthToken authToken, final EMail email) {
        return getModel(authToken).isEmailAvailable(email);
    }

    /**
     * @see com.thinkparity.service.ProfileService#isRequiredPaymentInfo(com.thinkparity.service.AuthToken)
     *
     */
    @Override
    public Boolean isRequiredPaymentInfo(final AuthToken authToken) {
        return getModel(authToken).isRequiredPaymentInfo();
    }

    /**
     * @see com.thinkparity.service.ProfileService#isSetPaymentInfo(com.thinkparity.service.AuthToken)
     *
     */
    @Override
    public Boolean isSetPaymentInfo(final AuthToken authToken) {
        return getModel().isSetPaymentInfo();
    }

    /**
     * @see com.thinkparity.service.ProfileService#read(com.thinkparity.service.AuthToken)
     *
     */
    public Profile read(final AuthToken authToken) {
        return getModel(authToken).read();
    }

    /**
     * @see com.thinkparity.service.ProfileService#readEMail(com.thinkparity.service.AuthToken)
     *
     */
    @Override
    public ProfileEMail readEMail(final AuthToken authToken) {
        return getModel(authToken).readEMail();
    }

    /**
     * @see com.thinkparity.service.ProfileService#readFeatures(com.thinkparity.service.AuthToken)
     *
     */
    public List<Feature> readFeatures(final AuthToken authToken) {
        return getModel(authToken).readFeatures();
    }

    /**
     * @see com.thinkparity.service.ProfileService#readToken(com.thinkparity.service.AuthToken)
     *
     */
    public Token readToken(final AuthToken authToken) {
        return getModel(authToken).readToken();
    }

    /**
     * @see com.thinkparity.service.ProfileService#update(AuthToken, Profile, PaymentInfo)
     * 
     */
    @Override
    public void update(final AuthToken authToken, final Profile profile,
            final PaymentInfo paymentInfo) {
        getModel(authToken).update(profile, paymentInfo);
    }

    /**
     * @see com.thinkparity.service.ProfileService#update(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.profile.ProfileVCard)
     *
     */
    public void update(final AuthToken authToken, final ProfileVCard vcard) {
        getModel(authToken).update(vcard);
    }

    /**
     * @see com.thinkparity.service.ProfileService#updateEMail(com.thinkparity.service.AuthToken, com.thinkparity.codebase.email.EMail)
     *
     */
    @Override
    public void updateEMail(final AuthToken authToken, final EMail email)
            throws EMailIntegrityException {
        getModel(authToken).updateEMail(email);
    }

    /**
     * @see com.thinkparity.service.ProfileService#updatePassword(com.thinkparity.service.AuthToken, java.lang.String)
     *
     */
    public void updatePassword(final AuthToken authToken,
            final Credentials credentials, final String password)
            throws InvalidCredentialsException {
        getModel(authToken).updatePassword(credentials, password);
    }

    /**
     * @see com.thinkparity.service.ProfileService#updatePaymentInfo(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.profile.payment.PaymentInfo)
     *
     */
    @Override
    public void updatePaymentInfo(final AuthToken authToken,
            final PaymentInfo paymentInfo) {
        getModel(authToken).updatePaymentInfo(paymentInfo);
    }

    /**
     * @see com.thinkparity.service.ProfileService#updateProductRelease(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public void updateProductRelease(final AuthToken authToken,
            final Product product, final Release release) {
        getModel(authToken).updateProductRelease(product, release);
    }

    /**
     * @see com.thinkparity.service.ProfileService#verifyEMail(com.thinkparity.service.AuthToken, com.thinkparity.codebase.email.EMail, java.lang.String)
     *
     */
    public void verifyEMail(final AuthToken authToken, final EMail email,
            final String token) {
        getModel(authToken).verifyEMail(email, token);
    }

    /**
     * Obtain a profile model for an administrative user.
     * 
     * @return An instance of <code>ProfileModel</code>.
     */
    private ProfileModel getModel() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return ModelFactory.getInstance(User.THINKPARITY, loader).getProfileModel();
    }

    /**
     * Obtain a profile model for an authenticated session.
     * 
     * @param authToken
     *            A session <code>AuthToken</code>.
     * @return An instance of <code>ProfileModel</code>.
     */
    private ProfileModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getProfileModel();
    }
}
