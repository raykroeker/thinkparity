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

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
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
public final class Ticket1008Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 1008";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket1008Test.
     *
     */
    public Ticket1008Test() {
        super(NAME);
    }

    /**
     * Test ticket 1008.
     * 
     */
    public void testTicket1008() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test ticket 1008.");
        setUpTicket1008();
        try {
            publish();
            accept();
            verify();
        } finally {
            tearDownTicket1008();
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
     * Accept the resultant invitation.
     * 
     */
    private void accept() {
        final IncomingEMailInvitation incomingEMail =
            datum.findIncomingEMailInvitation(datum.acceptAs,
                    datum.lookupUser(datum.publishAs),
                    datum.lookupEMail(datum.acceptAs));
        assertNotNull("Incoming e-mail invitation is null.", incomingEMail);
        datum.getContactModel(datum.acceptAs).acceptInvitation(incomingEMail,
                datum.now());
    }

    /**
     * Publish to an e-mail address.
     * 
     */
    private void publish() {
        final ContainerVersion version = new ContainerVersion();
        version.setArtifactName(getName());
        version.setArtifactType(ArtifactType.CONTAINER);
        version.setArtifactUniqueId(UUIDGenerator.nextUUID());
        version.setCreatedBy(datum.lookupUser(datum.publishAs).getId());
        version.setCreatedOn(datum.now());
        version.setUpdatedBy(version.getCreatedBy());
        version.setUpdatedOn(version.getCreatedOn());
        version.setVersionId(Versioning.START);
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
        final List<EMail> emails = new ArrayList<EMail>();
        emails.add(datum.lookupEMail(datum.acceptAs));
        final List<User> users = Collections.<User>emptyList();
        datum.getContainerModel(datum.publishAs).publish(version,
                    documentVersions, emails, users);
    }

    /**
     * Initialize test ticket 1008.
     * 
     */
    private void setUpTicket1008() {
        final String acceptAsUsername = datum.newUniqueUsername();
        datum.createProfile(acceptAsUsername);
        datum.acceptAs = datum.login(acceptAsUsername);
        datum.verifyEMail(datum.acceptAs);

        final String publishAsUsername = datum.newUniqueUsername();
        datum.createProfile(publishAsUsername);
        datum.publishAs = datum.login(publishAsUsername);
        datum.verifyEMail(datum.publishAs);
    }

    /**
     * Finalize test ticket 1008.
     * 
     */
    private void tearDownTicket1008() {
        if (null == datum.acceptAs) {
            TEST_LOGGER.logWarning("Accept as user is not logged in.");
        } else {
            datum.logout(datum.acceptAs);
        }

        if (null == datum.publishAs) {
            TEST_LOGGER.logWarning("Publish as user is not logged in.");
        } else {
            datum.logout(datum.publishAs);
        }
    }

    /**
     * Verify no invitations exist.
     * 
     */
    private void verify() {
        final List<IncomingEMailInvitation> incomingEMail =
            datum.getContactModel(datum.acceptAs).readIncomingEMailInvitations();
        assertNotNull("Incoming e-mail list is null.", incomingEMail);
        assertTrue("Incoming e-mail list is invalid.", incomingEMail.isEmpty());
    }

    /** <b>Title:</b>Ticket 1008 Fixture<br> */
    private class Fixture extends TicketTestCase.Fixture {
        private AuthToken publishAs, acceptAs;
    }
}
