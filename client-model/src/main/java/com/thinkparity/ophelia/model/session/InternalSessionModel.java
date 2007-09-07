/*
 * Feb 13, 2006
 */
package com.thinkparity.ophelia.model.session;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityConcurrency;
import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.DraftExistsException;
import com.thinkparity.codebase.model.artifact.IllegalVersionException;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.concurrent.Lock;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Internal Session Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.28
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalSessionModel extends SessionModel {

    /**
     * Accept the contact invitation.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation<code>.
     * @param acceptedOn
     *            The accepted on <code>Calendar</code>.
     */
    public void acceptInvitation(final IncomingEMailInvitation invitation,
            final Calendar acceptedOn);

    /**
     * Accept the contact invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation<code>.
     * @param acceptedOn
     *            The accepted on <code>Calendar</code>.
     */
    public void acceptInvitation(final IncomingUserInvitation invitation,
            final Calendar acceptedOn);

    // TODO-javadoc InternalSessionModel#createDraft()
    public void createDraft(final List<JabberId> team, final UUID uniqueId,
            final Long latestVersionId, final Calendar createdOn)
            throws DraftExistsException, IllegalVersionException;

    /**
     * Create an outgoing e-mail invitation.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    public void createInvitation(final OutgoingEMailInvitation invitation);

    /**
     * Create an outgoing user invitation.
     * 
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    public void createInvitation(final OutgoingUserInvitation invitation);

    /**
     * Decline an incoming e-mail invitation.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     * @param declinedOn
     *            A declined on <code>Calendar</code>.
     */
    public void declineInvitation(final IncomingEMailInvitation invitation,
            final Calendar declinedOn);

    /**
     * Decline an incoming user invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation</code>.
     * @param declinedOn
     *            A declined on <code>Calendar</code>.
     */
    public void declineInvitation(final IncomingUserInvitation invitation,
            final Calendar declinedOn);

    /**
     * Delete a contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void delete(final JabberId contactId);

    // TODO-javadoc InternalSessionModel#deleteDraft()
    public void deleteDraft(final UUID uniqueId, final Calendar deletedOn);

    /**
     * Delete a contact invitation.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    public void deleteInvitation(final OutgoingEMailInvitation invitation,
            final Calendar deletedOn);

    /**
     * Delete a contact invitation.
     * 
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     * @param deletedOn
     *            The deletion <code>Calendar</code>.
     */
    public void deleteInvitation(final OutgoingUserInvitation invitation,
            final Calendar deletedOn);

    /**
     * Obtain the authentication token.
     * 
     * @return An <code>AuthToken</code>.
     */
    @ThinkParityTransaction(TransactionType.SUPPORTED)
    public AuthToken getAuthToken();

    /**
     * Initialize the user's session token data.
     *
     */
    public void initializeToken();

    /**
     * Determine whether or not this is the first login.
     * 
     * @return True if this is the first login.
     */
    public Boolean isFirstLogin();

	/**
     * Determine if invite is restricted to the user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if invite is restircted.
     */
    public Boolean isInviteRestricted(final User user);

    /**
     * Determine if publish is restricted to the publish to user.
     * 
     * @param publishTo
     *            A publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    public Boolean isPublishRestricted(final JabberId publishTo);

    /**
     * Determine if publish is restricted to the emails/users.
     * 
     * @param emails
     *            A <code>List<EMail></code>.
     * @param users
     *            A <code>List<User></code>.
     * @return True if publish to the user is restricted.
     */
    public Boolean isPublishRestricted(final List<EMail> emails,
            final List<User> users);

    /**
     * Log an error.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param error
     *            An <code>Error</code>.
     * @param occuredOn
     *            The date/time the error occured.
     */
    public void logError(final Product product, final Release release,
            final Error error);

	/**
     * Login for the first time in this workspace.
     * 
     * @param credentials
     *            A set of <code>Credentials</code>.
     * @throws InvalidCredentialsException
     *             if either the username or password do not match
     */
    public void login(final Credentials credentials)
            throws InvalidCredentialsException;

    /**
     * Fire the client maintenance event.
     *
     */
    public void notifyClientMaintenance();

    /**
     * Notify the session was terminated.
     *
     */
    @ThinkParityTransaction(TransactionType.SUPPORTED)
    @ThinkParityConcurrency(Lock.NONE)
    public void notifySessionTerminated();

    // TODO-javadoc InternalSessionModel#publish
    public void publish(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<EMail> publishToEMails, final List<User> publishToUsers);

    // TODO-javadoc InternalSessionModel#publishVersion
    public void publishVersion(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<ArtifactReceipt> receivedBy, final Calendar publishedOn,
            final List<EMail> publishToEMails, final List<User> publishToUsers);

    /**
     * Set an offline code.
     * 
     * @param offlineCode
     *            An <code>OfflineCode</code>.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    @ThinkParityConcurrency(Lock.NONE)
    public void pushOfflineCode(final OfflineCode offlineCode);

    public Container readBackupContainer(final UUID uniqueId);

    /**
     * Read the backup containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readBackupContainers();

    /**
     * Read the backup containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readBackupContainerVersions(
            final UUID uniqueId);

    /**
     * Read the backup containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;Document&gt;</code>.
     */
    public List<Document> readBackupDocuments(final UUID uniqueId,
            final Long versionId);

    /**
     * Read the backup document versions.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param documentUniqueId
     *            A document unique id <code>UUID</code>.
     * @return A <code>List&lt;DocumentVersion&gt;</code>.
     */
    public List<DocumentVersion> readBackupDocumentVersions(
            final UUID uniqueId, final Long versionId);

    public List<ArtifactReceipt> readBackupPublishedTo(final UUID uniqueId,
            final Long versionId);

    public List<PublishedToEMail> readBackupPublishedToEMails(
            final UUID uniqueId, final Long versionId);

    /**
     * Read the backup team.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A list of jabber ids.
     */
    public List<JabberId> readBackupTeamIds(final UUID uniqueId);

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     * @return A <code>Contact</code>.
     */
    public Contact readContact(final JabberId contactId);

    /**
     * Read the contacts.
     * 
     * @return A <code>List<Contact></code>.
     */
    public List<Contact> readContacts();

    /**
     * Return the remote date and time. The transaction type is defined as
     * supported meaning that if called from within an existing transaction
     * context this method will participate in the transaction; however a new
     * transaction context will not be created if none exists.
     * 
     * @return A <code>Calendar</code>.
     */
    @ThinkParityTransaction(TransactionType.SUPPORTED)
    public Calendar readDateTime();

    /**
     * Read all incoming e-mail invitations.
     * 
     * @return A <code>List</code> of <code>IncomingEMailInvitation</code>s.
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations();

    /**
     * Read all incoming user invitations.
     * 
     * @return A <code>List</code> of <code>IncomingUserInvitation</code>s.
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations();

    /**
     * Read the artifact key holder.
     * 
     * @param artifactUniqueId
     *            The artifact unique id.
     * @return The artifact key holder.
     * @throws ParityException
     */
    public JabberId readKeyHolder(final UUID uniqueId);

    /**
     * Read the latest release.
     * 
     * @param productName
     *            A product name <code>String</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public Release readMigratorLatestRelease(final String productName,
            final OS os);

    /**
     * Read a migrator product.
     * 
     * @param name
     *            A product name.
     * @return A <code>Product</code>.
     */
    public Product readMigratorProduct(final String name);

    /**
     * Read a migrator product's features.
     * 
     * @param name
     *            A product name.
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> readMigratorProductFeatures(final String name);

    /**
     * Read a migrator release.
     * 
     * @param productName
     *            A product name <code>String</code>.
     * @param name
     *            A release name.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public Release readMigratorRelease(final String productName,
            final String name, final OS os);

    /**
     * Read migrator release resources.
     * 
     * @param productName
     *            A product name <code>String</code>.
     * @param name
     *            A release name <code>String</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public List<Resource> readMigratorResources(final String productName,
            final String releaseName, final OS os);

    /**
     * Read all outgoing e-mail invitations.
     * 
     * @return A <code>List</code> of <code>OutgoingEMailInvitation</code>s.
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations();

    /**
     * Read all outgoing user invitations.
     * 
     * @return A <code>List</code> of <code>OutgoingUserInvitation</code>s.
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations();

    /**
     * Read a user's profile features.
     * 
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> readProfileFeatures();

    /**
     * Read a thinkParity user from the server.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>
     */
    public User readUser(final JabberId userId);

    /**
     * Update the a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param profile
     *            The user's profile.
     */
    public void updateProfile(final JabberId userId, final Profile profile);

    /**
     * Update the profile's password.
     * 
     * @param credentials
     *            A user's <code>Credentials</code>.
     * @param newPassword
     *            The new profile password<code>String</code>.
     * @throws InvalidCredentialsException
     *             if the credentials are invalid.
     */
    public void updateProfilePassword(final Credentials credentials,
            final String newPassword) throws InvalidCredentialsException;

    /**
     * Verify an email in a user's profile.
     * 
     * @param email
     *            A <code>ProfileEMail</code>.
     * @param key
     *            A verification key <code>String</code>.
     */
    public void verifyProfileEmail(final ProfileEMail email, final String key);
}
