/*
 * Created On:  16-Dec-06 3:16:12 PM
 */
package com.thinkparity.desdemona.util.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity Service Request Reader<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ServiceRequestReader {

    /**
     * Read a calendar.
     * 
     * @param name
     *            A request parameter name <code>String</code>.
     * @return A request parameter value.
     */
    public Calendar readCalendar(final String name);

    /**
     * Read contacts.
     * 
     * @param name
     *            A parameter name.
     * @return A <code>List</code> of <code>Contact</code>s.
     */
    public List<Contact> readContacts(final String name);

    /**
     * Read a container version.
     * 
     * @param name
     *            A parameter name <code>String</code>.
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion readContainerVersion(final String name);

    /**
     * Read a list of document versions.
     * 
     * @param name
     *            A request parameter name <code>String</code>.
     * @return A <code>List</code> of <code>DocumentVersion</code>s.
     */
    public List<DocumentVersion> readDocumentVersions(final String name);

    /**
     * Read document version and stream ids.
     * 
     * @param name
     *            A parameter name.
     * @return A <code>Map</code> of <code>DocumentVersion</code> and their
     *         stream id <code>String</code>.
     */
    public Map<DocumentVersion, String> readDocumentVersionsStreamIds(final String name);

    /**
     * Read an email address.
     * 
     * @param name
     *            A request parameter name <code>String</code>.
     * @return An <code>EMail</code>.
     */
    public EMail readEMail(final String name);

    /**
     * Read email addresses.
     * 
     * @param name
     *            A parameter name.
     * @return A <code>List</code> of <code>EMail</code>s.
     */
    public List<EMail> readEMails(final String name);

    /**
     * Read an integer parameter from the internet query.
     * 
     * @param name
     *            The parameter name.
     * @return An integer value.
     */
    public Integer readInteger(final String name);

    /**
     * Read jabber id data.
     * 
     * @param name
     *            The element name.
     * @return The data; or null if the data does not exist.
     */
    public JabberId readJabberId(final String name);

    /**
     * Read a list of jabber ids.
     * 
     * @param parentName
     *            The parent parameter name.
     * @param name
     *            The parameter name.
     * @return A list of jabber ids.
     */
    public List<JabberId> readJabberIds(final String parentName,
            final String name);

    /**
     * Read long data.
     *
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public Long readLong(final String name);

    /**
     * Read the variable names within the service request.
     * 
     * @return A list of the variable names.
     */
    public List<String> readNames();

    /**
     * Read the operating system.
     * 
     * @param name
     *            A name <code>String</code>.
     * @return An <code>OS</code>.
     */
    public OS readOs(final String name);

    /**
     * Read a product from the service request.
     * 
     * @param name
     *            The name <code>String</code>.
     * @return A <code>Product</code>.
     */
    public Product readProduct(final String name);

    /**
     * Read a profile vcard.
     * 
     * @param name
     *            A request parameter name <code>String</code>.
     * @return A <code>ProfileVCard</code>.
     */
    public ProfileVCard readProfileVCard(final String name);

    /**
     * Read release datum.
     * 
     * @param name
     *            The datum name <code>String</code>.
     * @return The datum value <code>Release</code>.
     */
    public Release readRelease(final String name);

    /**
     * Read resource datum.
     * 
     * @param name
     *            The datum name <code>String</code>.
     * @return The datum value <code>List</code> of <code>Resource</code>s.
     */
    public List<Resource> readResources(final String name);

    /**
     * Read string data.
     *
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public String readString(final String name);

    /**
     * Read team members.
     * 
     * @param name
     *            A parameter name.
     * @return A <code>List</code> of <code>TeamMember</code>s.
     */
    public List<TeamMember> readTeamMembers(final String name);

    /**
     * Read users.
     * 
     * @param name
     *            A parameter name <code>String</code>.
     * @return A <code>List</code> of <code>User</code>s.
     */
    public List<User> readUsers(final String name);

    /**
     * Read a unique id.
     * 
     * @param name
     *            The element name.
     * @return The element value.
     */
    public UUID readUUID(final String name);
}
