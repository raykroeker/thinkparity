/*
 * Created On:  16-Dec-06 3:19:43 PM
 */
package com.thinkparity.desdemona.util.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.UserVCard;
import com.thinkparity.codebase.model.util.Token;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity Service Reponse Writer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ServiceResponseWriter {

    public void write(final String name, final EMailReservation value);

    /**
     * Write the product to the service response.
     * 
     * @param name
     *            A name <code>String</code>.
     * @param value
     *            A <code>Product</code> value.
     */
    public void write(final String name, final Product value);

    /**
     * Write the release to the service response.
     * 
     * @param name
     *            A name <code>String</code>.
     * @param value
     *            A <code>Release</code> value.
     */
    public void write(final String name, final Release value);

    public void write(final String name, final Statistics value);

    public void write(final String name, final UsernameReservation value);

    public void writeArtifactReceipts(final String name,
            List<ArtifactReceipt> values);

    public void writeBoolean(final String name, final Boolean value);

    /**
     * Write a calendar value.
     *
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    public void writeCalendar(final String name, Calendar value);

    public void writeContainer(final String name, Container value);
    
    public void writeContainers(final String parentName,
            String name, List<Container> values);

    public void writeContainerVersions(final String parentName,
            String name, List<ContainerVersion> values);

    public void writeDocuments(final String parentName,
            String name, List<Document> values);

    public void writeDocumentVersion(final String name,
            final DocumentVersion value);

    public void writeDocumentVersionDeltas(final String name, Map<DocumentVersion, Delta> values);

    public void writeDocumentVersions(final String parentName,
            String name, List<DocumentVersion> values);

    public void writeEMail(final String name, EMail value);

    /**
     * Write email values.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param values
     *            The element values.
     */
    public void writeEMails(final String parentName, String name,
            List<EMail> values);

    public void writeEvents(final String name, String childName,
            List<XMPPEvent> values);

    public void writeFeatures(final String name, final List<Feature> values);

    /**
     * Write contact invitations.
     * 
     * @param <T>
     *            A contact invitation type.
     * @param name
     *            A service response name.
     * @param values
     *            A list of contact invitations.
     */
    public void writeIncomingEMailInvitations(final String name,
            final List<IncomingEMailInvitation> values);

    public void writeIncomingUserInvitations(final String name,
            final List<IncomingUserInvitation> values);

    /**
     * Write a jabber id value.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public void writeJabberId(final String name, JabberId value);

    /**
     * Write a list of jabber id values.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public void writeJabberIds(final String parentName,
            String name, List<JabberId> values);

    /**
     * Write a list of long values.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element values.
     */
    public void writeLongs(final String parentName, String name,
            List<Long> values);

    /**
     * Write contact invitations.
     * 
     * @param <T>
     *            A contact invitation type.
     * @param name
     *            A service response name.
     * @param values
     *            A list of contact invitations.
     */
    public void writeOutgoingEMailInvitations(final String name,
            final List<OutgoingEMailInvitation> values);

    /**
     * Write contact invitations.
     * 
     * @param <T>
     *            A contact invitation type.
     * @param name
     *            A service response name.
     * @param values
     *            A list of contact invitations.
     */
    public void writeOutgoingUserInvitations(final String name,
            final List<OutgoingUserInvitation> values);

    public void writeProfileEMails(final String name,
            final List<ProfileEMail> values);

    /**
     * Write the resources to the query.
     * 
     * @param name
     *            The element name <code>String</code>.
     * @param values
     *            The element <code>Resource</code> values.
     */
    public void writeResources(final String name, final List<Resource> values);

    public void writeStreamSession(final String name, StreamSession value);

    /**
     * Write a string value.
     *
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    public void writeString(final String name, String value);

    public void writeTeam(final String name, String childName, List<TeamMember> values);

    public void writeToken(final String name, Token value);

    /**
     * Write unique id value.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public void writeUniqueId(final String name, UUID value);

    public void writeVCard(final String name, final UserVCard vcard);
}
