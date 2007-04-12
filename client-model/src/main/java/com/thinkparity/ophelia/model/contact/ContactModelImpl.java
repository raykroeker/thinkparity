/*
 * Generated On: Jun 27 06 04:14:53 PM
 */
package com.thinkparity.ophelia.model.contact;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterChain;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.*;

import com.thinkparity.ophelia.model.Model;
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
     * @see com.thinkparity.ophelia.model.contact.ContactModel#acceptIncomingEMailInvitation(java.lang.Long)
     * 
     */
    public void acceptIncomingEMailInvitation(final Long invitationId) {
        try {
            final InternalIndexModel indexModel = getIndexModel();
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar acceptedOn = sessionModel.readDateTime();

            final IncomingEMailInvitation incomingEMailInvitation =
                contactIO.readIncomingEMailInvitation(invitationId);
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

            // accept remote
            final Contact contact = sessionModel.readContact(extendedBy.getId());
            sessionModel.acceptInvitation(incomingEMailInvitation, acceptedOn);

            // delete outgoing e-mail invitations and indicies
            final List<OutgoingEMailInvitation> outgoingEMailInvitations =
                contactIO.readOutgoingEMailInvitations(contact.getEmails());
            for (final OutgoingEMailInvitation oei : outgoingEMailInvitations) {
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
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#acceptIncomingUserInvitation(java.lang.Long)
     * 
     */
    public void acceptIncomingUserInvitation(final Long invitationId) {
        try {
            final InternalIndexModel indexModel = getIndexModel();
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar acceptedOn = sessionModel.readDateTime();

            final IncomingUserInvitation incomingUserInvitation =
                contactIO.readIncomingUserInvitation(invitationId);
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
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar createdOn = sessionModel.readDateTime();
            final User invitationUser = getUserModel().read(userId);

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
     * @see com.thinkparity.ophelia.model.contact.ContactModel#declineIncomingEMailInvitation(java.lang.Long)
     * 
     */
    public void declineIncomingEMailInvitation(final Long invitationId) {
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar declinedOn = sessionModel.readDateTime();

            // delete incoming e-mail invitation
            final IncomingEMailInvitation incomingEMailInvitation =
                contactIO.readIncomingEMailInvitation(invitationId);
            contactIO.deleteInvitation(incomingEMailInvitation);

            // delete index
            getIndexModel().deleteIncomingEMailInvitation(
                    incomingEMailInvitation.getId());

            // decline remote
            sessionModel.declineInvitation(incomingEMailInvitation, declinedOn);

            // fire event
            notifyIncomingEMailInvitationDeclined(incomingEMailInvitation,
                    localEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#declineIncomingUserInvitation(java.lang.Long)
     * 
     */
    public void declineIncomingUserInvitation(final Long invitationId) {
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar declinedOn = sessionModel.readDateTime();

            // delete incoming user invitation
            final IncomingUserInvitation incomingUserInvitation =
                contactIO.readIncomingUserInvitation(invitationId);
            contactIO.deleteInvitation(incomingUserInvitation);

            // delete index
            getIndexModel().deleteIncomingUserInvitation(
                    incomingUserInvitation.getId());

            // decline remote
            sessionModel.declineInvitation(incomingUserInvitation, declinedOn);

            // fire event
            notifyIncomingUserInvitationDeclined(incomingUserInvitation,
                    localEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#delete(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public void delete(final JabberId contactId) {
        try {
            final Contact contact = read(contactId);

            // delete local and index
            deleteLocal(contact);

            // delete remote
            getSessionModel().delete(contactId);

            // fire event
            notifyContactDeleted(contact, localEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#deleteOutgoingEMailInvitation(java.lang.Long)
     * 
     */
    public void deleteOutgoingEMailInvitation(final Long invitationId) {
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar deletedOn = sessionModel.readDateTime();

            // delete outgoing e-mail invitation
            final OutgoingEMailInvitation outgoingEMailInvitation =
                contactIO.readOutgoingEMailInvitation(invitationId);
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
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactModel#deleteOutgoingUserInvitation(java.lang.Long)
     * 
     */
    public void deleteOutgoingUserInvitation(final Long invitationId) {
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar deletedOn = sessionModel.readDateTime();

            // delete outgoing user invitation
            final OutgoingUserInvitation outgoingUserInvitation =
                contactIO.readOutgoingUserInvitation(invitationId);
            contactIO.deleteInvitation(outgoingUserInvitation);

            // delete index
            getIndexModel().deleteOutgoingUserInvitation(
                    outgoingUserInvitation.getId());

            // delete remote
            sessionModel.deleteInvitation(outgoingUserInvitation, deletedOn);

            // fire event
            notifyOutgoingUserInvitationDeleted(outgoingUserInvitation,
                    localEventGenerator);
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
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#download(com.thinkparity.ophelia.model.util.ProcessMonitor)
     * 
     */
    public void download(final ProcessMonitor monitor) {
        try {
            assertXMPPOnline();
            final InternalSessionModel sessionModel = getSessionModel();
            final List<JabberId> contactIds = sessionModel.readContactIds();
            notifyDetermine(monitor, contactIds.size());
            for (final JabberId contactId : contactIds) {
                notifyStepBegin(monitor, DownloadStep.DOWNLOAD);
                createLocal(sessionModel.readContact(contactId));
                notifyStepEnd(monitor, DownloadStep.DOWNLOAD);
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
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleContactDeleted(com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent)
     * 
     */
    public void handleContactDeleted(final ContactDeletedEvent event) {
        try {
            // delete contact and index
            final Contact contact = read(event.getDeletedBy());
            deleteLocal(contact);

            // fire event
            notifyContactDeleted(contact, remoteEventGenerator);
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
            final InternalSessionModel sessionModel = getSessionModel();

            final Contact local = read(event.getContactId());
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
                getIndexModel().updateContact(event.getContactId());
            }

            // fire event
            notifyContactUpdated(read(event.getContactId()),
                    remoteEventGenerator);
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
            contactIO.deleteInvitation(outgoingEMailInvitation);

            // delete index
            getIndexModel().deleteOutgoingEMailInvitation(
                    outgoingEMailInvitation.getId());

            // fire event
            notifyOutgoingEMailInvitationDeclined(outgoingEMailInvitation,
                    remoteEventGenerator);
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
            contactIO.deleteInvitation(incomingEMailInvitation);

            // delete index
            getIndexModel().deleteIncomingEMailInvitation(
                    incomingEMailInvitation.getId());

            // fire event
            notifyIncomingEMailInvitationDeleted(incomingEMailInvitation,
                    remoteEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleEMailInvitationExtended(com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationExtendedEvent)
     * 
     */
    public void handleEMailInvitationExtended(
            final ContactEMailInvitationExtendedEvent event) {
        try {
            final InternalUserModel userModel = getUserModel();
            final User extendedBy = userModel.readLazyCreate(event.getInvitedBy());

            // create incoming e-mail invitation
            final IncomingEMailInvitation incomingEMailInvitation = new IncomingEMailInvitation();
            incomingEMailInvitation.setCreatedBy(extendedBy);
            incomingEMailInvitation.setCreatedOn(event.getInvitedOn());
            incomingEMailInvitation.setExtendedBy(extendedBy);
            incomingEMailInvitation.setInvitationEMail(event.getInvitedAs());
            contactIO.createInvitation(incomingEMailInvitation);

            // index
            getIndexModel().indexIncomingEMailInvitation(
                    incomingEMailInvitation.getId());

            // fire event
            notifyIncomingEMailInvitationCreated(incomingEMailInvitation,
                    remoteEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleInvitationAccepted(com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent)
     * 
     */
    public void handleInvitationAccepted(
            final ContactInvitationAcceptedEvent event) {
       try {
           final InternalIndexModel indexModel = getIndexModel();
           final InternalSessionModel sessionModel = getSessionModel();
           final User acceptedBy = getUserModel().read(event.getAcceptedBy());
           final Contact contact = sessionModel.readContact(event.getAcceptedBy());

           final List<IncomingEMailInvitation> incomingEMailInvitations;
           final IncomingUserInvitation incomingUserInvitation;
           final OutgoingUserInvitation outgoingUserInvitation;
           /* if the accepted by user is not null, there exists the possibility
            * of incoming e-mail invitations; incoming user invitations and
            * outgoing user invitations; if it is null these invitations cannot
            * exist
            */
           if (null != acceptedBy) {
               incomingEMailInvitations =
                   contactIO.readIncomingEMailInvitations(acceptedBy);
               for (final IncomingEMailInvitation iei : incomingEMailInvitations) {
                   contactIO.deleteInvitation(iei);
                   indexModel.deleteIncomingEMailInvitation(iei.getId());
               }

               // delete incoming user invitation and index
               incomingUserInvitation = contactIO.readIncomingUserInvitation(
                       acceptedBy);
               if (null != incomingUserInvitation) {
                   contactIO.deleteInvitation(incomingUserInvitation);
                   indexModel.deleteIncomingUserInvitation(
                           incomingUserInvitation.getId());
               }

               // delete outgoing user invitation and index
               outgoingUserInvitation = contactIO.readOutgoingUserInvitation(
                       acceptedBy);
               if (null != outgoingUserInvitation) {
                   contactIO.deleteInvitation(outgoingUserInvitation);
                   indexModel.deleteOutgoingUserInvitation(
                           outgoingUserInvitation.getId());
               }
           } else {
               incomingEMailInvitations = Collections.emptyList();
               incomingUserInvitation = null;
               outgoingUserInvitation = null;
           }

           // delete outgoing e-mail invitations and indicies
           final List<OutgoingEMailInvitation> outgoingEMailInvitations =
               contactIO.readOutgoingEMailInvitations(contact.getEmails());
           for (final OutgoingEMailInvitation oei : outgoingEMailInvitations) {
               contactIO.deleteInvitation(oei);
               indexModel.deleteOutgoingEMailInvitation(oei.getId());
           }

           // create contact data
           final Contact localContact = createLocal(contact);

           // fire events
           for (final IncomingEMailInvitation iei : incomingEMailInvitations)
               notifyIncomingEMailInvitationDeleted(iei, remoteEventGenerator);
           if (null != incomingUserInvitation)
               notifyIncomingUserInvitationDeleted(incomingUserInvitation,
                       remoteEventGenerator);
           for (final OutgoingEMailInvitation oei : outgoingEMailInvitations)
               notifyOutgoingEMailInvitationDeleted(oei, remoteEventGenerator);
           if (null != outgoingUserInvitation)
               notifyOutgoingUserInvitationDeleted(outgoingUserInvitation,
                       remoteEventGenerator);
           notifyContactCreated(localContact, remoteEventGenerator);
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
            contactIO.deleteInvitation(outgoingUserInvitation);

            // delete index
            getIndexModel().deleteOutgoingUserInvitation(
                    outgoingUserInvitation.getId());

            // fire event
            notifyOutgoingUserInvitationDeleted(outgoingUserInvitation,
                    remoteEventGenerator);
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
            contactIO.deleteInvitation(incomingUserInvitation);

            // delete index
            getIndexModel().deleteIncomingUserInvitation(
                    incomingUserInvitation.getId());

            // fire event
            notifyIncomingUserInvitationDeleted(incomingUserInvitation,
                    remoteEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.InternalContactModel#handleUserInvitationExtended(com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationExtendedEvent)
     *
     */
    public void handleUserInvitationExtended(
            final ContactUserInvitationExtendedEvent event) {
        try {
            final InternalUserModel userModel = getUserModel();
            final User extendedBy = userModel.readLazyCreate(event.getInvitedBy());

            // create incoming user invitation
            final IncomingUserInvitation incomingUserInvitation = new IncomingUserInvitation();
            incomingUserInvitation.setCreatedBy(extendedBy);
            incomingUserInvitation.setCreatedOn(event.getInvitedOn());
            incomingUserInvitation.setExtendedBy(extendedBy);
            incomingUserInvitation.setInvitationUser(localUser());
            contactIO.createInvitation(incomingUserInvitation);

            // index
            getIndexModel().indexIncomingUserInvitation(incomingUserInvitation.getId());

            // fire event
            notifyIncomingUserInvitationCreated(incomingUserInvitation,
                    remoteEventGenerator);
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
     * Search for contacts.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    public List<JabberId> search(final String expression) {
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
    }

    /**
     * Create the local contact data.
     * 
     * @param contact
     *            A contact.
     * @return The new contact.
     */
    private Contact createLocal(final Contact remote) {
        // create user (if required)
        final InternalUserModel userModel = getUserModel();
        final User user = userModel.readLazyCreate(remote.getId());

        // create contact
        final Contact local = new Contact();
        local.setEMails(remote.getEmails());
        local.setFlags(remote.getFlags());
        local.setId(remote.getId());
        local.setLocalId(user.getLocalId());
        local.setVCard(remote.getVCard());
        contactIO.create(local);

        // index
        getIndexModel().indexContact(local.getId());
        return contactIO.read(local.getId());
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
        getIndexModel().deleteContact(contact.getId());
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
