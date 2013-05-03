/*
 * Created On:  3-Jun-07 12:58:26 PM
 */
package com.thinkparity.desdemona.web.service;

import java.util.Calendar;
import java.util.List;

import javax.jws.WebService;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.desdemona.model.contact.ContactModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.ContactService;

/**
 * <b>Title:</b>thinkParity Desdemona Contact Service Endpoint Implementation<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.ContactService")
public class ContactSEI extends ServiceSEI implements ContactService {

    /**
     * Create ContactSEI.
     *
     */
    public ContactSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.ContactService#acceptInvitation(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.contact.IncomingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void acceptInvitation(final AuthToken authToken,
            final IncomingEMailInvitation invitation, final Calendar acceptedOn) {
        getModel(authToken).acceptInvitation(invitation, acceptedOn);
    }

    /**
     * @see com.thinkparity.service.ContactService#acceptInvitation(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.contact.IncomingUserInvitation,
     *      java.util.Calendar)
     * 
     */
    public void acceptInvitation(final AuthToken authToken,
            final IncomingUserInvitation invitation, final Calendar acceptedOn) {
        getModel(authToken).acceptInvitation(invitation, acceptedOn);
    }

    /**
     * @see com.thinkparity.service.ContactService#createInvitation(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.contact.OutgoingEMailInvitation)
     * 
     */
    public void createInvitation(final AuthToken authToken,
            final OutgoingEMailInvitation invitation) {
        getModel(authToken).createInvitation(invitation);
    }

    /**
     * @see com.thinkparity.service.ContactService#createInvitation(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.contact.OutgoingUserInvitation)
     * 
     */
    public void createInvitation(final AuthToken authToken,
            final OutgoingUserInvitation invitation) {
        getModel(authToken).createInvitation(invitation);
    }

    /**
     * @see com.thinkparity.service.ContactService#declineInvitation(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.contact.IncomingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void declineInvitation(final AuthToken authToken,
            final IncomingEMailInvitation invitation, final Calendar declinedOn) {
        getModel(authToken).declineInvitation(invitation, declinedOn);
    }

    /**
     * @see com.thinkparity.service.ContactService#declineInvitation(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.contact.IncomingUserInvitation,
     *      java.util.Calendar)
     * 
     */
    public void declineInvitation(final AuthToken authToken,
            final IncomingUserInvitation invitation, final Calendar declinedOn) {
        getModel(authToken).declineInvitation(invitation, declinedOn);
    }

    /**
     * @see com.thinkparity.service.ContactService#delete(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void delete(final AuthToken authToken, final JabberId id) {
        getModel(authToken).delete(id);
    }

    /**
     * @see com.thinkparity.service.ContactService#deleteInvitation(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.contact.OutgoingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void deleteInvitation(final AuthToken authToken,
            final OutgoingEMailInvitation invitation, final Calendar deletedOn) {
        getModel(authToken).deleteInvitation(invitation, deletedOn);
    }

    /**
     * @see com.thinkparity.service.ContactService#deleteInvitation(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.contact.OutgoingUserInvitation,
     *      java.util.Calendar)
     * 
     */
    public void deleteInvitation(final AuthToken authToken,
            final OutgoingUserInvitation invitation, final Calendar deletedOn) {
        getModel(authToken).deleteInvitation(invitation, deletedOn);
    }

    /**
     * @see com.thinkparity.service.ContactService#read(com.thinkparity.service.AuthToken)
     * 
     */
    public List<Contact> read(final AuthToken authToken) {
        return getModel(authToken).read();
    }

    /**
     * @see com.thinkparity.service.ContactService#read(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public Contact read(final AuthToken authToken, final JabberId id) {
        return getModel(authToken).read(id);
    }

    /**
     * @see com.thinkparity.service.ContactService#readIncomingEMailInvitations(com.thinkparity.service.AuthToken)
     * 
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations(
            final AuthToken authToken) {
        return getModel(authToken).readIncomingEMailInvitations();
    }

    /**
     * @see com.thinkparity.service.ContactService#readIncomingUserInvitations(com.thinkparity.service.AuthToken)
     * 
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations(
            final AuthToken authToken) {
        return getModel(authToken).readIncomingUserInvitations();
    }

    /**
     * @see com.thinkparity.service.ContactService#readOutgoingEMailInvitations(com.thinkparity.service.AuthToken)
     * 
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final AuthToken authToken) {
        return getModel(authToken).readOutgoingEMailInvitations();
    }

    /**
     * @see com.thinkparity.service.ContactService#readOutgoingEMailInvitations(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.container.ContainerVersion)
     *
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final AuthToken authToken, final ContainerVersion version) {
        return getModel(authToken).readOutgoingEMailInvitations(version);
    }

    /**
     * @see com.thinkparity.service.ContactService#readOutgoingUserInvitations(com.thinkparity.service.AuthToken)
     * 
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations(
            final AuthToken authToken) {
        return getModel(authToken).readOutgoingUserInvitations();
    }

    /**
     * Obtain a contact model for an authenticated user.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return An instance of <code>ContactModel</code>.
     */
    private ContactModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getContactModel();
    }
}
