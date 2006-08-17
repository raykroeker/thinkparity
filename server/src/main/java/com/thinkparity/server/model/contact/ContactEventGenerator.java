/*
 * Created On: Aug 1, 2006 6:18:53 PM
 */
package com.thinkparity.server.model.contact;

import java.util.Calendar;

import org.xmpp.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.EventGenerator;
import com.thinkparity.model.xmpp.IQWriter;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.ParityServerConstants.Xml.Event;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ContactEventGenerator extends EventGenerator {

    /** Create ContactEventGenerator. */
    ContactEventGenerator() { super(); }

    IQ generateInvitationAccepted(final JabberId acceptedBy, final Calendar acceptedOn) {
        final IQWriter iqWriter = createEventWriter(Event.Contact.INVITATION_ACCEPTED);
        iqWriter.writeJabberId(Xml.Contact.ACCEPTED_BY, acceptedBy);
        iqWriter.writeCalendar(Xml.Contact.ACCEPTED_ON, acceptedOn);
        return iqWriter.getIQ();
    }

    IQ generateInvitationDeclined(final String invitedAs,
            final JabberId declinedBy, final Calendar declinedOn) {
        final IQWriter iqWriter = createEventWriter(Event.Contact.INVITATION_DECLINED);
        iqWriter.writeString(Xml.Contact.INVITED_AS, invitedAs);
        iqWriter.writeJabberId(Xml.Contact.DECLINED_BY, declinedBy);
        iqWriter.writeCalendar(Xml.Contact.DECLINED_ON, declinedOn);
        return iqWriter.getIQ();
    }

    /**
     * Create an invitation extended notification.
     * 
     * @param invitedAs
     *            The invitation e-mail address.
     * @param invitedBy
     *            The invitaion extender.
     * @param invitedOn
     *            The invitation date.
     * @return The invitation notification.
     */
    IQ generateInvitationExtended(final String invitedAs,
            final JabberId invitedBy, final Calendar invitedOn) {
        final IQWriter iqWriter = createEventWriter(Event.Contact.INVITATION_EXTENDED);
        iqWriter.writeString(Xml.Contact.INVITED_AS, invitedAs);
        iqWriter.writeJabberId(Xml.Contact.INVITED_BY, invitedBy);
        iqWriter.writeCalendar(Xml.Contact.INVITED_ON, invitedOn);
        return iqWriter.getIQ();
    }
}
