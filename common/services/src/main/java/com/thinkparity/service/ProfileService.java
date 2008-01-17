/*
 * Created On:  30-May-07 9:51:56 AM
 */
package com.thinkparity.service;

import java.util.List;

import javax.jws.WebMethod;
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
import com.thinkparity.codebase.model.util.Token;

/**
 * <b>Title:</b>thinkParity Profile Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Profile")
public interface ProfileService {

    /**
     * Create a profile creating a new payment plan.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
     * @param emailReservation
     *            An <code>EMailReservation</code>.
     * @param credentials
     *            A set of <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityCredentials
     *            A set of <code>SecurityCredentials</code>.
     */
    @WebMethod
    void create(AuthToken authToken, Product product, Release release,
            UsernameReservation usernameReservation,
            EMailReservation emailReservation, Credentials credentials,
            Profile profile, EMail email,
            SecurityCredentials securityCredentials);

    /**
     * Create a profile creating a new payment plan.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
     * @param emailReservation
     *            An <code>EMailReservation</code>.
     * @param credentials
     *            A set of <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityCredentials
     *            A set of <code>SecurityCredentials</code>.
     * @param paymentInfo
     *            A <code>PaymentInfo</code>.
     */
    @WebMethod
    void create(AuthToken authToken, Product product, Release release,
            UsernameReservation usernameReservation,
            EMailReservation emailReservation, Credentials credentials,
            Profile profile, EMail email,
            SecurityCredentials securityCredentials, PaymentInfo paymentInfo);

    /**
     * Create a profile selecting an existing payment plan.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
     * @param emailReservation
     *            An <code>EMailReservation</code>.
     * @param credentials
     *            A set of <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityCredentials
     *            A set of <code>SecurityCredentials</code>.
     * @param paymentPlanCredentials
     *            A set of <code>PaymentPlanCredentials</code>.
     * @throws InvalidCredentialsException
     *             if the payment plan credentials are invalid
     */
    @WebMethod
    void create(AuthToken authToken, Product product, Release release,
            UsernameReservation usernameReservation,
            EMailReservation emailReservation, Credentials credentials,
            Profile profile, EMail email,
            SecurityCredentials securityCredentials,
            PaymentPlanCredentials paymentPlanCredentials)
            throws InvalidCredentialsException;

    @WebMethod
    EMailReservation createEMailReservation(AuthToken authToken, EMail email);

    @WebMethod
    Token createToken(AuthToken authToken);

    /**
     * Create a username reservation.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @return A <code>UsernameReservation</code>.
     */
    @WebMethod
    UsernameReservation createUsernameReservation(AuthToken authToken,
            Profile profile);

    /**
     * Create a username reservation.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param username
     *            A <code>String</code>.
     * @return A <code>UsernameReservation</code>.
     * @deprecated 20071231
     */
    @WebMethod
    @Deprecated
    UsernameReservation createUsernameReservation(AuthToken authToken,
            String username);

    /**
     * Determine if the payment info can be accessed.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return True if the payment info can be accessed.
     */
    @WebMethod
    Boolean isAccessiblePaymentInfo(AuthToken authToken);

    @WebMethod
    Boolean isEMailAvailable(AuthToken authToken, EMail email);

    @WebMethod
    Boolean isRequiredPaymentInfo(AuthToken authToken);

    /**
     * Determine whether or not the payment has been set.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return True if the payment info has been set.
     */
    @WebMethod
    Boolean isSetPaymentInfo(AuthToken authToken);

    @WebMethod
    Profile read(AuthToken authToken);

    @WebMethod
    ProfileEMail readEMail(AuthToken authToken);

    @WebMethod
    List<Feature> readFeatures(AuthToken authToken);

    @WebMethod
    Token readToken(AuthToken authToken);

    /**
     * Update the profile.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param paymentInfo
     *            A <code>PaymentInfo</code>.
     */
    @WebMethod
    void update(AuthToken authToken, Profile profile, PaymentInfo paymentInfo);

    @WebMethod
    void update(AuthToken authToken, ProfileVCard vcard);

    /**
     * Update the profile e-mail address.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param email
     *            An <code>EMail</code>.
     * @throws EMailIntegrityException
     *             if the e-mail cannot be updated
     */
    @WebMethod
    void updateEMail(AuthToken authToken, EMail email)
            throws EMailIntegrityException;

    @WebMethod
    void updatePassword(AuthToken authToken, Credentials credentials,
            String password) throws InvalidCredentialsException;

    /**
     * Update the payment info for the profile.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param paymentInfo
     *            A <code>PaymentInfo</code>.
     */
    @WebMethod
    void updatePaymentInfo(AuthToken authToken, PaymentInfo paymentInfo);

    /**
     * Update the product release for the profile.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     */
    @WebMethod
    void updateProductRelease(AuthToken authToken, Product product,
            Release release);

    @WebMethod
    void verifyEMail(AuthToken authToken, EMail email, String token);
}
