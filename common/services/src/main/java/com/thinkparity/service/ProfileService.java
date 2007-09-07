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

    @WebMethod
    void create(AuthToken authToken, Product product, Release release,
            UsernameReservation usernameReservation,
            EMailReservation emailReservation, Credentials credentials,
            Profile profile, EMail email,
            SecurityCredentials securityCredentials);

    @WebMethod
    EMailReservation createEMailReservation(AuthToken authToken, EMail email);

    @WebMethod
    Token createToken(AuthToken authToken);

    @WebMethod
    UsernameReservation createUsernameReservation(AuthToken authToken,
            String username);

    @WebMethod
    Boolean isEMailAvailable(AuthToken authToken, EMail email);

    @WebMethod
    Profile read(AuthToken authToken);

    @WebMethod
    ProfileEMail readEMail(AuthToken authToken);

    @WebMethod
    List<Feature> readFeatures(AuthToken authToken);

    @WebMethod
    Token readToken(AuthToken authToken);

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
