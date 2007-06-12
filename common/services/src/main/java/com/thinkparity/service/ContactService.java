/*
 * Created On:  30-May-07 9:30:34 AM
 */
package com.thinkparity.service;

import java.util.Calendar;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;

/**
 * <b>Title:</b>thinkParity Contact Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Contact")
public interface ContactService {

    @WebMethod
    void acceptInvitation(AuthToken authToken,
            IncomingEMailInvitation invitation, Calendar acceptedOn);

    @WebMethod
    void acceptInvitation(AuthToken authToken,
            IncomingUserInvitation invitation, Calendar acceptedOn);

    @WebMethod
    void createInvitation(AuthToken authToken,
            OutgoingEMailInvitation invitation);

    @WebMethod
    void createInvitation(AuthToken authToken, OutgoingUserInvitation invitation);

    @WebMethod
    void declineInvitation(AuthToken authToken,
            IncomingEMailInvitation invitation, Calendar declinedOn);

    @WebMethod
    void declineInvitation(AuthToken authToken,
            IncomingUserInvitation invitation, Calendar declinedOn);

    @WebMethod
    void delete(AuthToken authToken, JabberId id);

    @WebMethod
    void deleteInvitation(AuthToken authToken,
            OutgoingEMailInvitation invitation, Calendar deletedOn);

    @WebMethod
    void deleteInvitation(AuthToken authToken,
            OutgoingUserInvitation invitation, Calendar deletedOn);

    @WebMethod
    List<Contact> read(AuthToken authToken);

    @WebMethod
    Contact read(AuthToken authToken, JabberId id);

    @WebMethod
    List<IncomingEMailInvitation> readIncomingEMailInvitations(
            AuthToken authToken);

    @WebMethod
    List<IncomingUserInvitation> readIncomingUserInvitations(
            AuthToken authToken);

    @WebMethod
    List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            AuthToken authToken);

    @WebMethod
    List<OutgoingUserInvitation> readOutgoingUserInvitations(
            AuthToken authToken);
}
