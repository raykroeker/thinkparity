/*
 * Generated On: Jun 27 06 04:14:53 PM
 */
package com.thinkparity.ophelia.model.contact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterChain;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.ContactInvitation;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.*;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.ContactService;
import com.thinkparity.service.ServiceFactory;

import com.thinkparity.ophelia.model.Delegate;
import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.contact.delegate.HandleEMailInvitationExtended;
import com.thinkparity.ophelia.model.contact.delegate.HandleInvitationAccepted;
import com.thinkparity.ophelia.model.contact.delegate.HandleUserInvitationExtended;
import com.thinkparity.ophelia.model.contact.monitor.DownloadData;
import com.thinkparity.ophelia.model.contact.monitor.DownloadStep;
import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ContactIOHandler;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.filter.UserFilterManager;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.util.sort.contact.NameComparator;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Contact Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.10
 */
public final class ContactModelImpl extends Model<ContactListener>
        implements ContactModel, InternalContactModel {

    /** The contact db io. */
    private ContactIOHandler contactIO;

    /** A contact web-service interface. */
    private ContactService contactService;

    /** The default contact comparator. */
    private final Comparator<Contact> defaultComparator;

    /** The default contact filter. */
    private final Filter<? super Contact> defaultFilter;

    /** A local event generator. */
    private final ContactEventGenerator localEventGenerator;

    /** A remote event generator. */
    private final ContactEventGenerator remoteEventGenerator;

    /**
     * Create ContactModelImpl.
     *
     */
    public ContactModelImpl() {
        super();
        this.defaultComparator = new NameComparator(Boolean.TRUE);
        this.defaultFilter = FilterManager.createDefault();
        this.localEventGenerator = new ContactEventGenerator(ContactEvent.Source.LOCAL);
        this.remoteEventGenerator = new ContactEventGenerator(ContactEvent.Source.REMOTE);
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#acceptInvitation(com.thinkparity.codebase.model.contact.IncomingEMailInvitation)
     *
     */
    @Override
    public void acceptInvitation(final IncomingEMailInvitation invitation) {
        try {
            if (doesExist(invitation)) {
                final InternalIndexModel indexModel = getIndexModel();
                final InternalSessionModel sessionModel = getSessionModel();
                final Calendar acceptedOn = sessionModel.readDateTime();
    
                final IncomingEMailInvitation incomingEMailInvitation =
                    contactIO.readIncomingEMailInvitation(invitation.getId());
                final User extendedBy = incomingEMailInvitation.getExtendedBy();
    
                // delete incoming e-mail invitations and indicies
                final List<IncomingEMailInvitation> incomingEMailInvitations = 
                    contactIO.readIncomingEMailInvitations(extendedBy);
                for (final IncomingEMailInvitation iei : incomingEMailInvitations) {
                    contactIO.deleteInvitation(iei);
                    indexModel.deleteIncomingEMailInvitation(iei.getId());
                }
    
                // delete incoming user invitation and index
                final IncomingUserInvitation incomingUserInvitation =
                    contactIO.readIncomingUserInvitation(extendedBy);
                if (null != incomingUserInvitation) {
                    contactIO.deleteInvitation(incomingUserInvitation);
                    indexModel.deleteIncomingUserInvitation(
                            incomingUserInvitation.getId());
                }
    
                // delete outgoing e-mail invitations and indicies
                final Contact contact = sessionModel.readContact(extendedBy.getId());
                final List<OutgoingEMailInvitation> outgoingEMailInvitations =
                    contactIO.readOutgoingEMailInvitations(contact.getEmails());
                for (final OutgoingEMailInvitation oei : outgoingEMailInvitations) {
                    getContainerModel().deletePublishedTo(oei.getInvitationEMail());
                    contactIO.deleteInvitation(oei);
                    indexModel.deleteOutgoingEMailInvitation(oei.getId());
                }
    
                // delete outgoing user invitation and index
                final OutgoingUserInvitation outgoingUserInvitation =
                    contactIO.readOutgoingUserInvitation(extendedBy);
                if (null != outgoingUserInvitation) {
                    contactIO.deleteInvitation(outgoingUserInvitation);
                    indexModel.deleteOutgoingUserInvitation(
                            outgoingUserInvitation.getId());
                }
    
                // create contact and index
                final Contact localContact = createLocal(contact);
    
                // accept remote
                sessionModel.acceptInvitation(incomingEMailInvitation, acceptedOn);
    
                // fire events
                for (final IncomingEMailInvitation iei : incomingEMailInvitations)
                    notifyIncomingEMailInvitationDeleted(iei, localEventGenerator);
                if (null != incomingUserInvitation)
                    notifyIncomingUserInvitationDeleted(incomingUserInvitation,
                            localEventGenerator);
                for (final OutgoingEMailInvitation oei : outgoingEMailInvitations)
                    notifyOutgoingEMailInvitationDeleted(oei, localEventGenerator);
                if (null != outgoingUserInvitation)
                    notifyOutgoingUserInvitationDeleted(outgoingUserInvitation,
                            localEventGenerator);
                notifyContactCreated(localContact, localEventGenerator);
            } else {
                logger.logInfo("Invitation {0} no longer exists.", invitation);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#acceptInvitation(com.thinkparity.codebase.model.contact.IncomingUserInvitation)
     * 
     */
    public void acceptInvitation(final IncomingUserInvitation invitation) {
        try {
            if (doesExist(invitation)) {
                final InternalIndexModel indexModel = getIndexModel();
                final InternalSessionModel sessionModel = getSessionModel();
                final Calendar acceptedOn = sessionModel.readDateTime();
    
                final IncomingUserInvitation incomingUserInvitation =
                    contactIO.readIncomingUserInvitation(invitation.getId());
                final User extendedBy = incomingUserInvitation.getExtendedBy();
    
                // delete incoming e-mail invitations and index
                final List<IncomingEMailInvitation> incomingEMailInvitations = 
                    contactIO.readIncomingEMailInvitations(extendedBy);
                for (final IncomingEMailInvitation iei : incomingEMailInvitations) {
                    contactIO.deleteInvitation(iei);
                    indexModel.deleteIncomingEMailInvitation(iei.getId());
                }
    
                // delete incoming user invitation and index
                contactIO.deleteInvitation(incomingUserInvitation);
                indexModel.deleteIncomingUserInvitation(
                        incomingUserInvitation.getId());
    
                // accept remote
                sessionModel.acceptInvitation(incomingUserInvitation, acceptedOn);
                final Contact contact = sessionModel.readContact(extendedBy.getId());
    
                // delete outgoing e-mail invitations and indicies
                final List<OutgoingEMailInvitation> outgoingEMailInvitations =
                    contactIO.readOutgoingEMailInvitations(contact.getEmails());
                for (final OutgoingEMailInvitation oei : outgoingEMailInvitations) {
                    // delete the published to reference
                    getContainerModel().deletePublishedTo(oei.getInvitationEMail());
                    // delete the invitation and e-mail
                    contactIO.deleteInvitation(oei);
                    indexModel.deleteOutgoingEMailInvitation(oei.getId());
                }
    
                // delete outgoing user invitation and index
                final OutgoingUserInvitation outgoingUserInvitation =
                    contactIO.readOutgoingUserInvitation(extendedBy);
                if (null != outgoingUserInvitation) {
                    contactIO.deleteInvitation(outgoingUserInvitation);
                    indexModel.deleteOutgoingUserInvitation(
                            outgoingUserInvitation.getId());
                }
    
                // create contact data
                final Contact localContact = createLocal(contact);
    
                // fire events
                for (final IncomingEMailInvitation iei : incomingEMailInvitations)
                    notifyIncomingEMailInvitationDeleted(iei, localEventGenerator);
                if (null != incomingUserInvitation)
                    notifyIncomingUserInvitationDeleted(incomingUserInvitation,
                            localEventGenerator);
                for (final OutgoingEMailInvitation oei : outgoingEMailInvitations)
                    notifyOutgoingEMailInvitationDeleted(oei, localEventGenerator);
                if (null != outgoingUserInvitation)
                    notifyOutgoingUserInvitationDeleted(outgoingUserInvitation,
                            localEventGenerator);
                notifyContactCreated(localContact, localEventGenerator);
            } else {
                logger.logInfo("Invitation {0} no longer exists.", invitation);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#addListener(com.thinkparity.ophelia.model.util.EventListener)
     * 
     */
    @Override
    public void addListener(final ContactListener listener) {
        super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#createLocalOutgoingEMailInvitation(com.thinkparity.codebase.email.EMail,
     *      java.util.Calendar)
     * 
     */
    public OutgoingEMailInvitation createLocalOutgoingEMailInvitation(
            final EMail email, final Calendar createdOn) {
        try {
            // create outgoing e-mail invitation
            final OutgoingEMailInvitation outgoingEMailInvitation = new OutgoingEMailInvitation();
            outgoingEMailInvitation.setCreatedBy(localUser());
            outgoingEMailInvitation.setCreatedOn(createdOn);
            outgoingEMailInvitation.setInvitationEMail(email);
            contactIO.createInvitation(outgoingEMailInvitation);

            // index
            getIndexModel().indexOutgoingEMailInvitation(outgoingEMailInvitation.getId());

            // HACK - OutgoingEMailInvitation#createLocalOutgoingEMailInvitation - Do not fire event
            return outgoingEMailInvitation;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#createOutgoingEMailInvitation(com.thinkparity.codebase.email.EMail)
     * 
     */
    public OutgoingEMailInvitation createOutgoingEMailInvitation(final EMail email) {
        try {
            Assert.assertNotTrue(isInviteRestricted(email),
                    "The user cannot invite to the specified e-mails/users.");

            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar createdOn = sessionModel.readDateTime();

            // create outgoing e-mail invitation
            final OutgoingEMailInvitation outgoingEMailInvitation = new OutgoingEMailInvitation();
            outgoingEMailInvitation.setCreatedBy(localUser());
            outgoingEMailInvitation.setCreatedOn(createdOn);
            outgoingEMailInvitation.setInvitationEMail(email);
            contactIO.createInvitation(outgoingEMailInvitation);

            // index
            getIndexModel().indexOutgoingEMailInvitation(outgoingEMailInvitation.getId());

            // invite remote
            sessionModel.createInvitation(outgoingEMailInvitation);

            // fire event
            notifyOutgoingEMailInvitationCreated(outgoingEMailInvitation,
                    localEventGenerator);
            return outgoingEMailInvitation;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#createOutgoingUserInvitation(java.lang.Long)
     * 
     */
    public OutgoingUserInvitation createOutgoingUserInvitation(final Long userId) {
        try {
            final User invitationUser = getUserModel().read(userId);
            Assert.assertTrue(getProfileModel().isInviteAvailable(invitationUser),
                    "User cannot invoke create outgoing user invitation.");

            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar createdOn = sessionModel.readDateTime();

            // create outgoing user invitation
            final OutgoingUserInvitation outgoingUserInvitation = new OutgoingUserInvitation();
            outgoingUserInvitation.setCreatedBy(localUser());
            outgoingUserInvitation.setCreatedOn(createdOn);
            outgoingUserInvitation.setInvitationUser(invitationUser);
            contactIO.createInvitation(outgoingUserInvitation);

            // index
            getIndexModel().indexOutgoingUserInvitation(outgoingUserInvitation.getId());

            // invite remote
            sessionModel.createInvitation(outgoingUserInvitation);

            // fire event
            notifyOutgoingUserInvitationCreated(outgoingUserInvitation,
                    localEventGenerator);
            return outgoingUserInvitation;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#declineInvitation(com.thinkparity.codebase.model.contact.IncomingEMailInvitation)
     *
     */
    @Override
    public void declineInvitation(final IncomingEMailInvitation invitation) {
        try {
            if (doesExist(invitation)) {
                final InternalSessionModel sessionModel = getSessionModel();
                final Calendar declinedOn = sessionModel.readDateTime();
    
                // delete incoming e-mail invitation
                final IncomingEMailInvitation incomingEMailInvitation =
                    contactIO.readIncomingEMailInvitation(invitation.getId());
                contactIO.deleteInvitation(incomingEMailInvitation);
    
                // delete index
                getIndexModel().deleteIncomingEMailInvitation(
                        incomingEMailInvitation.getId());
    
                // decline remote
                sessionModel.declineInvitation(incomingEMailInvitation, declinedOn);
    
                // fire event
                notifyIncomingEMailInvitationDeclined(incomingEMailInvitation,
                        localEventGenerator);
            } else {
                logger.logInfo("Invitation {0} no longer exists.", invitation);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#declineInvitation(com.thinkparity.codebase.model.contact.IncomingUserInvitation)
     *
     */
    @Override
    public void declineInvitation(final IncomingUserInvitation invitation) {
        try {
            if (doesExist(invitation)) {
                final InternalSessionModel sessionModel = getSessionModel();
                final Calendar declinedOn = sessionModel.readDateTime();
    
                // delete incoming user invitation
                final IncomingUserInvitation incomingUserInvitation =
                    contactIO.readIncomingUserInvitation(invitation.getId());
                contactIO.deleteInvitation(incomingUserInvitation);
    
                // delete index
                getIndexModel().deleteIncomingUserInvitation(
                        incomingUserInvitation.getId());
    
                // decline remote
                sessionModel.declineInvitation(incomingUserInvitation, declinedOn);
    
                // fire event
                notifyIncomingUserInvitationDeclined(incomingUserInvitation,
                        localEventGenerator);
            } else {
                logger.logInfo("Invitation {0} no longer exists.", invitation);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#delete(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public void delete(final Contact contact) {
        try {
            if (doesExist(contact)) {
                // delete local and index
                deleteLocal(contact);
                
                // delete remote
                getSessionModel().delete(contact.getId());
                
                // fire event
                notifyContactDeleted(contact, localEventGenerator);
            } else {
                logger.logInfo("Contact {0} no longer exists.", contact);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#deleteOutgoingEMailInvitation(java.lang.Long)
     * 
     */
    public void deleteInvitation(final OutgoingEMailInvitation invitation) {
        try {
            if (contactIO.doesExistOutgoingEMailInvitation(invitation.getInvitationEMail())) {
                final InternalSessionModel sessionModel = getSessionModel();
                final Calendar deletedOn = sessionModel.readDateTime();
    
                // delete outgoing e-mail invitation
                final OutgoingEMailInvitation outgoingEMailInvitation =
                    contactIO.readOutgoingEMailInvitation(invitation.getId());
                // delete the published to reference
                getContainerModel().deletePublishedTo(outgoingEMailInvitation.getInvitationEMail());
                contactIO.deleteInvitation(outgoingEMailInvitation);
    
                // delete index
                getIndexModel().deleteOutgoingEMailInvitation(
                        outgoingEMailInvitation.getId());
    
                // delete remote
                sessionModel.deleteInvitation(outgoingEMailInvitation,
                        deletedOn);
    
                // fire event
                notifyOutgoingEMailInvitationDeleted(outgoingEMailInvitation,
                        localEventGenerator);
            } else {
                logger.logInfo("Invitation {0} no longer exists.", invitation);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#deleteInvitation(com.thinkparity.codebase.model.contact.OutgoingUserInvitation)
     * 
     */
    public void deleteInvitation(final OutgoingUserInvitation invitation) {
        try {
            if (doesExistOutgoingUserInvitationForUser(invitation.getInvitationUser().getLocalId())) {
                final InternalSessionModel sessionModel = getSessionModel();
                final Calendar deletedOn = sessionModel.readDateTime();
    
                // delete outgoing user invitation
                final OutgoingUserInvitation outgoingUserInvitation =
                    contactIO.readOutgoingUserInvitation(invitation.getId());
                contactIO.deleteInvitation(outgoingUserInvitation);
    
                // delete index
                getIndexModel().deleteOutgoingUserInvitation(
                        outgoingUserInvitation.getId());
    
                // delete remote
                sessionModel.deleteInvitation(outgoingUserInvitation, deletedOn);
    
                // fire event
                notifyOutgoingUserInvitationDeleted(outgoingUserInvitation,
                        localEventGenerator);
            } else {
                logger.logInfo("Invitation {0} no longer exists.", invitation);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#deleteLocal(com.thinkparity.ophelia.model.util.ProcessMonitor)
     *
     */
    @Override
    public void deleteLocal(final ProcessMonitor monitor) {
        try {
            final InternalIndexModel indexModel = getIndexModel();

            /* delete contacts */
            final List<Contact> contacts = read();
            for (final Contact contact : contacts) {
                deleteLocal(contact);
            }
            /* delete incoming e-mail invitations */
            final List<IncomingEMailInvitation> incomingEMail = readIncomingEMailInvitations();
            for (final IncomingEMailInvitation invitation : incomingEMail) {
                contactIO.deleteInvitation(invitation);
                indexModel.deleteIncomingEMailInvitation(invitation.getId());
            }
            /* delete incoming user invitations */
            final List<IncomingUserInvitation> incomingUser = readIncomingUserInvitations();
            for (final IncomingUserInvitation invitation : incomingUser) {
                contactIO.deleteInvitation(invitation);
                indexModel.deleteIncomingUserInvitation(invitation.getId());
            }
            /* delete outgoing e-mail invitations */
            final List<OutgoingEMailInvitation> outgoingEMail = readOutgoingEMailInvitations();
            for (final OutgoingEMailInvitation invitation : outgoingEMail) {
                contactIO.deleteInvitation(invitation);
                indexModel.deleteOutgoingEMailInvitation(invitation.getId());
            }
            /* delete outgoing user invitations */
            final List<OutgoingUserInvitation> outgoingUser = readOutgoingUserInvitations();
            for (final OutgoingUserInvitation invitation : outgoingUser) {
                contactIO.deleteInvitation(invitation);
                indexModel.deleteOutgoingUserInvitation(invitation.getId());
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#deleteLocalOutgoingEMailInvitation(com.thinkparity.codebase.email.EMail)
     * 
     */
    public OutgoingEMailInvitation deleteLocalOutgoingEMailInvitation(
            final EMail email) {
        try {
            final OutgoingEMailInvitation invitation =
                contactIO.readOutgoingEMailInvitation(email);
            contactIO.deleteInvitation(invitation);
            getIndexModel().deleteOutgoingEMailInvitation(invitation.getId());
            return invitation;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#doesExist(com.thinkparity.codebase.email.EMail)
     *
     */
    public Boolean doesExist(final EMail email) {
        try {
            return contactIO.doesExist(email);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#doesExist(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public Boolean doesExist(final Long contactId) {
        try {
            return contactIO.doesExist(contactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#doesExistOutgoingEMailInvitation(com.thinkparity.codebase.email.EMail)
     *
     */
    public Boolean doesExistOutgoingEMailInvitation(final EMail email) {
        try {
            return contactIO.doesExistOutgoingEMailInvitation(email);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#doesExistOutgoingUserInvitationForUser(java.lang.Long)
     *
     */
    public Boolean doesExistOutgoingUserInvitationForUser(final Long userId) {
        try {
            return contactIO.doesExistOutgoingUserInvitation(userId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleContactDeleted(com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent)
     * 
     */
    public void handleContactDeleted(final ContactDeletedEvent event) {
        try {
            // delete contact and index
            final Contact contact = read(event.getDeletedBy());
            if (null == contact) {
                logger.logInfo("Contact {0} no longer exists.", event.getDeletedBy());
            } else {
                deleteLocal(contact);
    
                // fire event
                notifyContactDeleted(contact, remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleContactUpdated(com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent)
     * 
     */
    public void handleContactUpdated(final ContactUpdatedEvent event) {
        try {
            final Contact local = read(event.getContactId());
            if (null == local) {
                logger.logInfo("Contact {0} no longer exists.", event.getContactId());
            } else {
                final InternalSessionModel sessionModel = getSessionModel();
    
                final Contact remote = sessionModel.readContact(event.getContactId());
                // local contact can be null if the workspace has been re-created
                if (null == local) {
                    // create and index contact
                    createLocal(remote);
                } else {
                    // update contact
                    local.setEMails(remote.getEmails());
                    local.setId(remote.getId());
                    local.setName(remote.getName());
                    local.setOrganization(remote.getOrganization());
                    local.setTitle(remote.getTitle());
                    local.setVCard(remote.getVCard());
                    contactIO.update(local);
    
                    // index
                    getIndexModel().updateContact(local);
                }
    
                // fire event
                notifyContactUpdated(read(event.getContactId()),
                        remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleEMailInvitationDeclined(com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeclinedEvent)
     * 
     */
    public void handleEMailInvitationDeclined(
            final ContactEMailInvitationDeclinedEvent event) {
        try {
            // delete outgoing e-mail invitation
            final OutgoingEMailInvitation outgoingEMailInvitation =
                contactIO.readOutgoingEMailInvitation(event.getInvitedAs());
            if (null == outgoingEMailInvitation) {
                logger.logInfo("Invitation {0} no longer exists.", event.getInvitedAs());
            } else {
                // delete the published to reference
                getContainerModel().deletePublishedTo(outgoingEMailInvitation.getInvitationEMail());
                contactIO.deleteInvitation(outgoingEMailInvitation);
    
                // delete index
                getIndexModel().deleteOutgoingEMailInvitation(
                        outgoingEMailInvitation.getId());
    
                // fire event
                notifyOutgoingEMailInvitationDeclined(outgoingEMailInvitation,
                        remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleEMailInvitationDeleted(com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeletedEvent)
     * 
     */
    public void handleEMailInvitationDeleted(
            final ContactEMailInvitationDeletedEvent event) {
        try {
            final User deletedBy = getUserModel().read(event.getDeletedBy());

            // delete invitation
            final IncomingEMailInvitation incomingEMailInvitation =
                contactIO.readIncomingEMailInvitation(event.getInvitedAs(),
                        deletedBy);
            if (null == incomingEMailInvitation) {
                logger.logInfo("Invitation {0} no longer exists.", event.getInvitedAs());
            } else {
                contactIO.deleteInvitation(incomingEMailInvitation);
    
                // delete index
                getIndexModel().deleteIncomingEMailInvitation(
                        incomingEMailInvitation.getId());
    
                // fire event
                notifyIncomingEMailInvitationDeleted(incomingEMailInvitation,
                        remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleEvent(com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationExtendedEvent)
     *
     */
    @Override
    public void handleEvent(final ContactEMailInvitationExtendedEvent event) {
        try {
            final HandleEMailInvitationExtended delegate = createDelegate(HandleEMailInvitationExtended.class);
            delegate.setEvent(event);
            delegate.handleEMailInvitationExtended();
            if (delegate.isSetInvitation()) {
                // fire event
                notifyIncomingEMailInvitationCreated(delegate.getInvitation(),
                        remoteEventGenerator);
            } 
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleEvent(com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent)
     */
    public void handleEvent(final ContactInvitationAcceptedEvent event) {
        try {
            final HandleInvitationAccepted delegate = createDelegate(HandleInvitationAccepted.class);
            delegate.setEvent(event);
            delegate.handleInvitationAccepted();

            // fire events
            for (final IncomingEMailInvitation iei : delegate.getIncomingEMailInvitations()) {
                notifyIncomingEMailInvitationDeleted(iei, remoteEventGenerator);
            }
            for (final OutgoingEMailInvitation oei : delegate.getOutgoingEMailInvitations()) {
                notifyOutgoingEMailInvitationDeleted(oei, remoteEventGenerator);
            }
            if (delegate.isSetIncomingUserInvitation()) {
                notifyIncomingUserInvitationDeleted(delegate.getIncomingUserInvitation(),
                        remoteEventGenerator);
            }
            if (delegate.isSetOutgoingUserInvitation()) {
                notifyOutgoingUserInvitationDeleted(delegate.getOutgoingUserInvitation(),
                        remoteEventGenerator);
            }
            if (delegate.isSetContact()) {
                // fire event
                notifyContactCreated(delegate.getContact(), remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleEvent(com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationExtendedEvent)
     * 
     */
    @Override
    public void handleEvent(final ContactUserInvitationExtendedEvent event) {
        try {
            final HandleUserInvitationExtended delegate = createDelegate(HandleUserInvitationExtended.class);
            delegate.setEvent(event);
            delegate.handleUserInvitationExtended();
            if (delegate.isSetInvitation()) {
                // fire event
                notifyIncomingUserInvitationCreated(delegate.getInvitation(),
                        remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleUserInvitationDeclined(com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeclinedEvent)
     *
     */
    public void handleUserInvitationDeclined(
            final ContactUserInvitationDeclinedEvent event) {
        try {
            final User declinedBy = getUserModel().read(event.getDeclinedBy());

            // delete outgoing user invitation
            final OutgoingUserInvitation outgoingUserInvitation =
                contactIO.readOutgoingUserInvitation(declinedBy);
            if (null == outgoingUserInvitation) {
                logger.logInfo("Invitation {0} no longer exists.", event.getDeclinedBy());
            } else {
                contactIO.deleteInvitation(outgoingUserInvitation);
    
                // delete index
                getIndexModel().deleteOutgoingUserInvitation(
                        outgoingUserInvitation.getId());
    
                // fire event
                notifyOutgoingUserInvitationDeleted(outgoingUserInvitation,
                        remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleUserInvitationDeleted(com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeletedEvent)
     * 
     */
    public void handleUserInvitationDeleted(
            final ContactUserInvitationDeletedEvent event) {
        try {
            final User deletedBy = getUserModel().read(event.getDeletedBy());

            // delete incoming user invitation
            final IncomingUserInvitation incomingUserInvitation =
                contactIO.readIncomingUserInvitation(deletedBy);
            if (null == incomingUserInvitation) {
                logger.logInfo("Invitation {0} no longer exists.", event.getDeletedBy());
            } else {
                contactIO.deleteInvitation(incomingUserInvitation);
    
                // delete index
                getIndexModel().deleteIncomingUserInvitation(
                        incomingUserInvitation.getId());
    
                // fire event
                notifyIncomingUserInvitationDeleted(incomingUserInvitation,
                        remoteEventGenerator);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#isInviteRestricted(java.util.List)
     *
     */
    @Override
    public Boolean isInviteRestricted(final List<EMail> emailList) {
        try {
            return getSessionModel().isInviteRestricted(emailList);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read a list of contacts.
     * 
     * @return A list of contacts.
     */
    public List<Contact> read() {
        try {
            return read(defaultComparator, defaultFilter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read a list of contacts.
     * 
     * @param comparator
     *            A user comparator to sort the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Comparator<Contact> comparator) {
        try {
            return read(comparator, defaultFilter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read a list of contacts.
     * 
     * @param comparator
     *            A user comparator to sort the contacts.
     * @param filter
     *            A user filter to scope the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Comparator<Contact> comparator,
            final Filter<? super Contact> filter) {
        try {
            final List<Contact> contacts = contactIO.read();
            FilterManager.filter(contacts, filter);
            ModelSorter.sortContacts(contacts, comparator);
            return contacts;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#read(com.thinkparity.codebase.email.EMail)
     *
     */
    public Contact read(final EMail email) {
        try {
            return contactIO.read(email);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read a list of contacts.
     * 
     * @param filter
     *            A user filter to scope the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Filter<? super Contact> filter) {
        try {
            return read(defaultComparator, filter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    public Contact read(final JabberId contactId) {
        try {
            return contactIO.read(contactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#read(java.lang.Long)
     *
     */
    @Override
    public Contact read(final Long contactId) {
        try {
            return contactIO.read(contactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readPublishTo()
     *
     */
    public List<Contact> readContainerPublishTo() {
        try {
            // filter out the contacts flagged with resitricted publish
            final FilterChain<User> filter = new FilterChain<User>();
            filter.addFilter(UserFilterManager.createContainerPublishTo());
            return read(defaultComparator, filter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readIncomingEMailInvitation(java.lang.Long)
     * 
     */
    public IncomingEMailInvitation readIncomingEMailInvitation(
            final Long invitationId) {
        try {
            return contactIO.readIncomingEMailInvitation(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readIncomingEMailInvitations()
     * 
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations() {
        try {
            return contactIO.readIncomingEMailInvitations();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readIncomingUserInvitation(java.lang.Long)
     * 
     */
    public IncomingUserInvitation readIncomingUserInvitation(
            final Long invitationId) {
        try {
            return contactIO.readIncomingUserInvitation(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readIncomingEMailInvitations()
     * 
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations() {
        try {
            return contactIO.readIncomingUserInvitations();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#readOutgoingEMailInvitation(com.thinkparity.codebase.email.EMail)
     *
     */
    public OutgoingEMailInvitation readOutgoingEMailInvitation(final EMail email) {
        try {
            return contactIO.readOutgoingEMailInvitation(email);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readOutgoingEMailInvitation(java.lang.Long)
     * 
     */
    public OutgoingEMailInvitation readOutgoingEMailInvitation(
            final Long invitationId) {
        try {
            return contactIO.readOutgoingEMailInvitation(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readOutgoingEMailInvitations()
     * 
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations() {
        try {
            return contactIO.readOutgoingEMailInvitations();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#readOutgoingEMailInvitations(com.thinkparity.codebase.model.container.ContainerVersion)
     * 
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final ContainerVersion version) {
        try {
            return contactService.readOutgoingEMailInvitations(getAuthToken(),
                    version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readOutgoingEMailInvitation(java.lang.Long)
     * 
     */
    public OutgoingUserInvitation readOutgoingUserInvitation(
            final Long invitationId) {
        try {
            return contactIO.readOutgoingUserInvitation(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#readOutgoingUserInvitations()
     * 
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations() {
        try {
            return contactIO.readOutgoingUserInvitations();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#removeListener(com.thinkparity.ophelia.model.util.EventListener)
     * 
     */
    @Override
    public void removeListener(final ContactListener listener) {
        super.removeListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#restoreLocal(com.thinkparity.ophelia.model.util.ProcessMonitor)
     * 
     */
    @Override
    public void restoreLocal(final ProcessMonitor monitor) {
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            final List<Contact> contacts = sessionModel.readContacts();
            final DownloadData monitorData = new DownloadData();
            monitorData.setSize(contacts.size());
            notifyStepBegin(monitor, DownloadStep.DOWNLOAD_SIZE, monitorData);
            notifyStepEnd(monitor, DownloadStep.DOWNLOAD_SIZE);
            for (final Contact contact : contacts) {
                monitorData.setContact(contact);
                notifyStepBegin(monitor, DownloadStep.DOWNLOAD_CONTACT, monitorData);
                createLocal(contact);
                notifyStepEnd(monitor, DownloadStep.DOWNLOAD_CONTACT);
            }
            final List<IncomingEMailInvitation> incomingEMail = sessionModel.readIncomingEMailInvitations();
            for (final IncomingEMailInvitation iei : incomingEMail) {
                createLocal(iei);
            }
            final List<IncomingUserInvitation> incomingUser = sessionModel.readIncomingUserInvitations();
            for (final IncomingUserInvitation iui : incomingUser) {
                createLocal(iui);
            }
            final List<OutgoingEMailInvitation> outgoingEMail = sessionModel.readOutgoingEMailInvitations();
            for (final OutgoingEMailInvitation oei : outgoingEMail) {
                createLocal(oei);
            }
            final List<OutgoingUserInvitation> outgoingUser = sessionModel.readOutgoingUserInvitations();
            for (final OutgoingUserInvitation oui : outgoingUser) {
                createLocal(oui);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#search(java.lang.String)
     * 
     */
    public List<Long> search(final String expression) {
        try {
            return getIndexModel().searchContacts(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#searchIncomingEMailInvitations(java.lang.String)
     * 
     */
    public List<Long> searchIncomingEMailInvitations(final String expression) {
        try {
            return getIndexModel().searchIncomingEMailInvitations(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#searchIncomingUserInvitations(java.lang.String)
     * 
     */
    public List<Long> searchIncomingUserInvitations(final String expression) {
        try {
            return getIndexModel().searchIncomingUserInvitations(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#searchOutgoingEMailInvitations(java.lang.String)
     *
     */
    public List<Long> searchOutgoingEMailInvitations(final String expression) {
        try {
            return getIndexModel().searchOutgoingEMailInvitations(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#searchOutgoingUserInvitations(java.lang.String)
     *
     */
    public List<Long> searchOutgoingUserInvitations(String expression) {
        try {
            return getIndexModel().searchOutgoingUserInvitations(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.contactIO = IOFactory.getDefault(workspace).createContactHandler();

        final ServiceFactory serviceFactory = getServiceFactory();
        this.contactService = serviceFactory.getContactService();
    }

    /**
     * Create the local contact data.
     * 
     * @param contact
     *            A contact.
     * @return The new contact.
     */
    Contact createLocal(final Contact remote) {
        // create user (if required)
        final InternalUserModel userModel = getUserModel();
        final User user = userModel.readLazyCreate(remote.getId());

        // create contact
        final Contact local = new Contact();
        local.setEMails(remote.getEmails());
        local.setFlags(remote.getFlags());
        local.setId(remote.getId());
        local.setLocalId(user.getLocalId());
        local.setName(remote.getName());
        local.setOrganization(remote.getOrganization());
        local.setTitle(remote.getTitle());
        local.setVCard(remote.getVCard());
        contactIO.create(local);

        // index
        getIndexModel().indexContact(local);
        return contactIO.read(local.getId());
    }

    /**
     * Obtain a contact persistence interface.
     * 
     * @return A <code>ContactIOHandler</code>.
     */
    ContactIOHandler getContactIO() {
        return contactIO;
    }

    /**
     * Create an instance of a delegate.
     * 
     * @param <D>
     *            A delegate type.
     * @param type
     *            The delegate type <code>Class</code>.
     * @return An instance of <code>D</code>.
     */
    private <D extends Delegate<ContactModelImpl>> D createDelegate(
            final Class<D> type) {
        try {
            final D instance = type.newInstance();
            instance.initialize(this);
            return instance;
        } catch (final IllegalAccessException iax) {
            throw new ThinkParityException("Could not create delegate.", iax);
        } catch (final InstantiationException ix) {
            throw new ThinkParityException("Could not create delegate.", ix);
        }
    }

    /**
     * Create the local invitation data.
     * 
     * @param incoming
     *            An <code>IncomingInvitation</code>.
     */
    private void createLocal(final IncomingEMailInvitation remote) {
        final InternalUserModel userModel = getUserModel();
        final User createdBy = userModel.readLazyCreate(remote.getCreatedBy().getId());
        final User extendedBy = userModel.readLazyCreate(remote.getExtendedBy().getId());

        // create
        final IncomingEMailInvitation local = new IncomingEMailInvitation();
        local.setCreatedBy(createdBy);
        local.setCreatedOn(remote.getCreatedOn());
        local.setExtendedBy(extendedBy);
        local.setInvitationEMail(remote.getInvitationEMail());
        contactIO.createInvitation(local);

        // index
        getIndexModel().indexIncomingEMailInvitation(local.getId());
    }

    /**
     * Create the local invitation data.
     * 
     * @param incoming
     *            An <code>IncomingInvitation</code>.
     */
    private void createLocal(final IncomingUserInvitation remote) {
        final InternalUserModel userModel = getUserModel();
        final User createdBy = userModel.readLazyCreate(remote.getCreatedBy().getId());
        final User extendedBy = userModel.readLazyCreate(remote.getExtendedBy().getId());
        final User invitationUser = userModel.readLazyCreate(remote.getInvitationUser().getId());

        // create
        final IncomingUserInvitation local = new IncomingUserInvitation();
        local.setCreatedBy(createdBy);
        local.setCreatedOn(remote.getCreatedOn());
        local.setExtendedBy(extendedBy);
        local.setInvitationUser(invitationUser);
        contactIO.createInvitation(local);

        // index
        getIndexModel().indexIncomingUserInvitation(local.getId());
    }

    /**
     * Create the local invitation data.
     * 
     * @param outgoing
     *            An <code>OutgoingEMailInvitation</code>.
     */
    private void createLocal(final OutgoingEMailInvitation remote) {
        final User createdBy = getUserModel().readLazyCreate(remote.getCreatedBy().getId());

        // create
        final OutgoingEMailInvitation local = new OutgoingEMailInvitation();
        local.setCreatedBy(createdBy);
        local.setCreatedOn(remote.getCreatedOn());
        local.setInvitationEMail(remote.getInvitationEMail());
        contactIO.createInvitation(local);

        // index
        getIndexModel().indexOutgoingEMailInvitation(local.getId());
    }

    /**
     * Create and index the local invitation data.
     * 
     * @param outgoing
     *            An <code>OutgoingUserInvitation</code>.
     */
    private void createLocal(final OutgoingUserInvitation remote) {
        final InternalUserModel userModel = getUserModel();
        final User createdBy = userModel.readLazyCreate(remote.getCreatedBy().getId());
        final User invitationUser = userModel.readLazyCreate(remote.getInvitationUser().getId());

        // create
        final OutgoingUserInvitation local = new OutgoingUserInvitation();
        local.setCreatedBy(createdBy);
        local.setCreatedOn(remote.getCreatedOn());
        local.setInvitationUser(invitationUser);
        contactIO.createInvitation(local);

        // index
        getIndexModel().indexOutgoingUserInvitation(local.getId());
    }

    /**
     * Delete the contact and index.
     * 
     * @param contactId
     *            A contact id.
     */
    private void deleteLocal(final Contact contact) {
        // delete
        contactIO.delete(contact);

        // delete index
        getIndexModel().deleteContact(contact);
    }

    /**
     * Determine whether or not a contact exists.
     * 
     * @param contact
     *            A <code>Contact</code>.
     * @return True if the contact exists.
     */
    private boolean doesExist(final Contact contact) {
        return null != contact && contactIO.doesExist(contact.getLocalId());
    }

    /**
     * Determine whether or not an incoming e-mail invitation exists.
     * 
     * @param invitation
     *            An <code>ContactInvitation</code>.
     * @return True if it exists.
     */
    private boolean doesExist(final ContactInvitation invitation) {
        return null != invitation && contactIO.doesExistInvitation(invitation);
    }

    /**
     * Obtain the web-service authentication token.
     * 
     * @return An <code>AuthToken</code>.
     */
    private AuthToken getAuthToken() {
        return getSessionModel().getAuthToken();
    }

    /**
     * Determine if the user is restricted from creating an invitation.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if the user is restricted from inviting.
     */
    private boolean isInviteRestricted(final EMail email) {
        final List<EMail> emailList = new ArrayList<EMail>();
        emailList.add(email);
        return getSessionModel().isInviteRestricted(emailList);
    }

    private void notifyContactCreated(final Contact contact,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.contactCreated(eventGenerator.generate(contact));
            }
        });
    }

    /**
     * Fire a notification event that a contact was deleted.
     * 
     * @param contact
     *            A contact.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyContactDeleted(final Contact contact,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.contactDeleted(eventGenerator.generate(contact));
            }
        });
    }

    /**
     * Fire a notification event that a contact was updated.
     * 
     * @param contact
     *            A contact.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyContactUpdated(final Contact contact,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.contactUpdated(eventGenerator.generate(contact));
            }
        });
    }

    /**
     * Notify all listeners that an incoming e-mail invitation has been created.
     * 
     * @param incomingInvitation
     *            An incoming invitation.
     * @param eventGenerator
     *            An event generator.
     */
    private void notifyIncomingEMailInvitationCreated(
            final IncomingEMailInvitation incomingInvitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.incomingEMailInvitationCreated(
                        eventGenerator.generate(incomingInvitation));
            }
        });
    }

    /**
     * Fire a notification event that an incoming invitation has been declined.
     * 
     * @param invitation
     *            An invitation.
     * @param eventGenerator
     *            A contact event generator.
     */
    private void notifyIncomingEMailInvitationDeclined(
            final IncomingEMailInvitation incomingInvitation,
            final ContactEventGenerator eventGenerator) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.incomingEMailInvitationDeclined(
                        eventGenerator.generate(incomingInvitation));
            }
        });
    }

    /**
     * Fire an incoming e-mail invitation deleted event.
     * 
     * @param iei
     *            An <code>IncomingEMailInvitation</code>.
     * @param ceg
     *            A <code>ContactEventGenerator</code>.
     */
    private void notifyIncomingEMailInvitationDeleted(
            final IncomingEMailInvitation iei, final ContactEventGenerator ceg) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.incomingEMailInvitationDeleted(ceg.generate(iei));
            }
        });
    }

    /**
     * Fire an incoming user invitation created event.
     * 
     * @param iui
     *            An <code>IncomingUserInvitation</code>.
     * @param ceg
     *            A <code>ContactEventGenerator</code>.
     */
    private void notifyIncomingUserInvitationCreated(
            final IncomingUserInvitation iui, final ContactEventGenerator ceg) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.incomingUserInvitationCreated(ceg.generate(iui));
            }
        });
    }

    /**
     * Fire an incoming user invitation declined event.
     * 
     * @param iei
     *            An <code>IncomingUserInvitation</code>.
     * @param ceg
     *            A <code>ContactEventGenerator</code>.
     */
    private void notifyIncomingUserInvitationDeclined(
            final IncomingUserInvitation iei, final ContactEventGenerator ceg) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.incomingUserInvitationDeclined(ceg.generate(iei));
            }
        });
    }

    /**
     * Fire an incoming user invitation deleted event.
     * 
     * @param iui
     *            An <code>IncomingUserInvitation</code>.
     * @param ceg
     *            A <code>ContactEventGeneraotor</code>.
     */
    private void notifyIncomingUserInvitationDeleted(
            final IncomingUserInvitation iui, final ContactEventGenerator ceg) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.incomingUserInvitationDeleted(ceg.generate(iui));
            }
        });
    }

    /**
     * Fire an outgoing e-mail invitation created event.
     * 
     * @param oei
     *            An <code>OutgoingEMailInvitation</code>.
     * @param ceg
     *            A <code>ContactEventGenerator</code>.
     */
    private void notifyOutgoingEMailInvitationCreated(
            final OutgoingEMailInvitation oei, final ContactEventGenerator ceg) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingEMailInvitationCreated(ceg.generate(oei));
            }
        });
    }

    /**
     * Fire an outgoing e-mail invitation declined event.
     * 
     * @param oei
     *            An <code>OutgoingEMailInvitation</code>.
     * @param ceg
     *            A <code>ContactEventGenerator</code>.
     */
    private void notifyOutgoingEMailInvitationDeclined(
            final OutgoingEMailInvitation oei, final ContactEventGenerator ceg) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingEMailInvitationDeclined(ceg.generate(oei));
            }
        });
    }

    /**
     * Fire an outgoing e-mail invitation deleted event.
     * 
     * @param oei
     *            An <code>OutgoingEMailInvitation</code>.
     * @param ceg
     *            A <code>ContactEventGenerator</code>.
     */
    private void notifyOutgoingEMailInvitationDeleted(
            final OutgoingEMailInvitation oei, final ContactEventGenerator ceg) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingEMailInvitationDeleted(ceg.generate(oei));
            }
        });
    }

    /**
     * Fire an outgoing user invitation created event.
     * 
     * @param oui
     *            An <code>OutgoingUserInvitation</code>
     * @param ceg
     *            A <code>ContactEventGenerator</code>.
     */
    private void notifyOutgoingUserInvitationCreated(
            final OutgoingUserInvitation oui, final ContactEventGenerator ceg) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingUserInvitationCreated(ceg.generate(oui));
            }
        });
    }

    /**
     * Fire an outgoing user invitation deleted event.
     * 
     * @param oui
     *            An <code>OutgoingEMailInvitation</code>.
     * @param ceg
     *            A <code>ContactEventGenerator</code>.
     */
    private void notifyOutgoingUserInvitationDeleted(
            final OutgoingUserInvitation oui, final ContactEventGenerator ceg) {
        notifyListeners(new EventNotifier<ContactListener>() {
            public void notifyListener(final ContactListener listener) {
                listener.outgoingUserInvitationDeleted(ceg.generate(oui));
            }
        });
    }
}
