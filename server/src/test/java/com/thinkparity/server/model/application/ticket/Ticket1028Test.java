/*
 * Created On:  8-Sep-07 4:16:44 PM
 */
package com.thinkparity.desdemona.model.ticket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.artifact.DraftExistsException;
import com.thinkparity.codebase.model.artifact.IllegalVersionException;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.util.UUIDGenerator;

import com.thinkparity.desdemona.model.Constants.Versioning;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Ticket1028Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 1028";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket1028Test.
     *
     */
    public Ticket1028Test() {
        super(NAME);
    }

    /**
     * Test ticket 1028.
     * 
     */
    public void testTicket1028() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test ticket 1028.");
        setUpTicket1028();
        try {
            publish(Versioning.START);
            createDraft();
            publish(Versioning.START + Versioning.INCREMENT);
            verify();
        } catch (final DraftExistsException dex) {
            fail(dex, "Could not create draft.");
        } catch (final IllegalVersionException ivx) {
            fail(ivx, "Could not create draft.");
        } finally {
            tearDownTicket1028();
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture();
    }

    /**
     * @see com.thinkparity.desdemona.model.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }

    /**
     * Create a draft.
     * 
     */
    private void createDraft() throws DraftExistsException,
            IllegalVersionException {
        final List<JabberId> team = Collections.<JabberId>emptyList();
        datum.getArtifactModel(datum.publishAs).createDraft(team,
                datum.firstVersion.getArtifactUniqueId(),
                datum.firstVersion.getVersionId(), datum.now());
    }

    /**
     * Publish to an e-mail address.
     * 
     * @param versionId
     *            A <code>Long</code>.
     */
    private void publish(final Long versionId) {
        final ContainerVersion version = new ContainerVersion();
        version.setArtifactName(getName());
        version.setArtifactType(ArtifactType.CONTAINER);
        version.setArtifactUniqueId(UUIDGenerator.nextUUID());
        version.setCreatedBy(datum.lookupUser(datum.publishAs).getId());
        version.setCreatedOn(datum.now());
        version.setUpdatedBy(version.getCreatedBy());
        version.setUpdatedOn(version.getCreatedOn());
        version.setVersionId(versionId);
        final List<DocumentVersion> documentVersions = new ArrayList<DocumentVersion>();
        final DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setArtifactName(getName() + ":  Document");
        documentVersion.setArtifactType(ArtifactType.DOCUMENT);
        documentVersion.setArtifactUniqueId(UUIDGenerator.nextUUID());
        final ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(new byte[0]));
        try {
            documentVersion.setChecksum(datum.checksum(channel));
        } finally {
            try {
                channel.close();
            } catch (final IOException iox) {
                fail(createFailMessage(iox, "Could not close empty byte channel."));
            }
        }
        documentVersion.setChecksumAlgorithm(datum.getChecksumAlgorithm());
        documentVersion.setCreatedBy(version.getCreatedBy());
        documentVersion.setCreatedOn(version.getCreatedOn());
        documentVersion.setSize(0L);
        documentVersion.setUpdatedBy(documentVersion.getCreatedBy());
        documentVersion.setUpdatedOn(documentVersion.getCreatedOn());
        documentVersion.setVersionId(Versioning.START);
        documentVersions.add(documentVersion);
        final EMail publishAsEMail = datum.lookupEMail(datum.publishAs);
        final String username = publishAsEMail.getUsername();
        final List<EMail> emails = new ArrayList<EMail>();
        if (Versioning.START == versionId) {
            emails.add(EMailBuilder.parse("application+" + username + "_ticket1028_" + System.currentTimeMillis() + "@thinkparity.com"));
            emails.add(EMailBuilder.parse("application+" + username + "_ticket1028_" + System.currentTimeMillis() + "@thinkparity.com"));            
        } else if (Versioning.START + Versioning.INCREMENT == versionId) {
            emails.add(datum.firstVersionPublishedTo.get(0));
        } else {
            fail("Unexpected version id " + versionId + ".");
        }
        
        final List<User> users = Collections.<User>emptyList();
        datum.getContainerModel(datum.publishAs).publish(version,
                    documentVersions, emails, users);
        if (Versioning.START == versionId) {
            datum.firstVersion = version;
            datum.firstVersionPublishedTo = emails;
        } else if (Versioning.START + Versioning.INCREMENT == versionId) {
            datum.secondVersion = version;
            datum.secondVersionPublishedTo = emails;
        } else {
            fail("Unexpected version id " + versionId + ".");
        }
    }

    /**
     * Initialize test ticket 1028.
     * 
     */
    private void setUpTicket1028() {
        final String publishAsUsername = datum.newUniqueUsername();
        datum.createProfile(publishAsUsername);
        datum.publishAs = datum.login(publishAsUsername);
        datum.verifyEMail(datum.publishAs);
    }

    /**
     * Finalize test ticket 1028.
     * 
     */
    private void tearDownTicket1028() {
        if (null == datum.publishAs) {
            TEST_LOGGER.logWarning("Publish as user is not logged in.");
        } else {
            datum.logout(datum.publishAs);
        }
    }

    /**
     * Verify unique e-mails from the restore.
     * 
     */
    private void verify() {
        final List<PublishedToEMail> firstVersionPublishedToEMails =
            datum.getBackupModel(datum.publishAs).readPublishedToEMails(
                    datum.firstVersion.getArtifactUniqueId(),
                    datum.firstVersion.getVersionId());
        assertNotNull("First version published to e-mail list is null.", firstVersionPublishedToEMails);
        assertFalse("First version published to e-mail list is invalid.", firstVersionPublishedToEMails.isEmpty());

        final List<EMail> firstEMails = new ArrayList<EMail>();
        for (final PublishedToEMail publishedToEMail : firstVersionPublishedToEMails) {
            if (firstEMails.contains(publishedToEMail.getEMail())) {
                fail("Published to e-mail list contains duplicate e-mail.");
            } else {
                firstEMails.add(publishedToEMail.getEMail());
            }

            if (datum.firstVersionPublishedTo.contains(publishedToEMail.getEMail())) {
                continue;
            } else {
                fail("First version published to e-mail not as expected.");
            }
        }

        boolean found;
        for (final EMail email : datum.firstVersionPublishedTo) {
            found = false;
            for (final PublishedToEMail publishedToEMail : firstVersionPublishedToEMails) {
                if (publishedToEMail.getEMail().equals(email)) {
                    found = true;
                    break;
                }
            }
            assertTrue("Could not find e-mail " + email + " in first version published to e-mail list.", found);
        }

        final List<PublishedToEMail> secondVersionPublishedToEMails =
            datum.getBackupModel(datum.publishAs).readPublishedToEMails(
                    datum.secondVersion.getArtifactUniqueId(),
                    datum.secondVersion.getVersionId());
        assertNotNull("Second version published to e-mail list is null.", secondVersionPublishedToEMails);
        assertFalse("Second version published to e-mail list is invalid.", secondVersionPublishedToEMails.isEmpty());

        final List<EMail> secondEMails = new ArrayList<EMail>();
        for (final PublishedToEMail publishedToEMail : secondVersionPublishedToEMails) {
            if (secondEMails.contains(publishedToEMail.getEMail())) {
                fail("Second published to e-mail list contains duplicate e-mail.");
            } else {
                secondEMails.add(publishedToEMail.getEMail());
            }

            if (datum.secondVersionPublishedTo.contains(publishedToEMail.getEMail())) {
                continue;
            } else {
                fail("Second version published to e-mail not as expected.");
            }
        }

        for (final EMail email : datum.secondVersionPublishedTo) {
            found = false;
            for (final PublishedToEMail publishedToEMail : secondVersionPublishedToEMails) {
                if (publishedToEMail.getEMail().equals(email)) {
                    found = true;
                    break;
                }
            }
            assertTrue("Could not find e-mail " + email + " in second version published to e-mail list.", found);
        }
    }

    /** <b>Title:</b>Ticket 1028 Fixture<br> */
    private class Fixture extends TicketTestCase.Fixture {
        private ContainerVersion firstVersion;
        private List<EMail> firstVersionPublishedTo;
        private AuthToken publishAs;
        private ContainerVersion secondVersion;
        private List<EMail> secondVersionPublishedTo;
    }
}
