/*
 * Created On:  30-May-07 9:51:56 AM
 */
package com.thinkparity.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;
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
    void addEMail(AuthToken authToken, EMail email);

    @WebMethod
    void create(AuthToken authToken, UsernameReservation usernameReservation,
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
    List<ProfileEMail> readEMails(AuthToken authToken);

    @WebMethod
    List<Feature> readFeatures(AuthToken authToken);

    @WebMethod
    Token readToken(AuthToken authToken);

    @WebMethod
    void removeEMail(AuthToken authToken, EMail email);

    @WebMethod
    void update(AuthToken authToken, ProfileVCard vcard);

    @WebMethod
    void updatePassword(AuthToken authToken, Credentials credentials,
            String password);

    @WebMethod
    void verifyEMail(AuthToken authToken, EMail email, String token);
}
