/*
 * Created On:  28-Dec-07 2:07:39 PM
 */
package com.thinkparity.desdemona.model.ticket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.artifact.DraftExistsException;
import com.thinkparity.codebase.model.artifact.IllegalVersionException;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.service.AuthToken;

import com.thinkparity.ophelia.model.util.UUIDGenerator;

import com.thinkparity.desdemona.model.Constants.Versioning;

/**
 * <b>Title:</b>thinkParity Desdemona Model Ticket 1222 Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Ticket1222Test extends TicketTestCase {

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket1222Test.
     *
     * @param name
     */
    public Ticket1222Test() {
        super("Ticket 1222:Restored package always indicates latest version");
    }

    /**
     * Test the ticket use-cases.
     * 
     */
    public void testTicket() {
        final User publishAs = datum.lookupUser(datum.publishAs);
        /* publish to first user */
        final List<User> publishToUserList = new ArrayList<User>();
        publishToUserList.add(datum.lookupUser(datum.publishToFirst));
        final ContainerVersion firstVersion = newVersion(publishAs, Versioning.START);
        final List<DocumentVersion> documentVersionList = new ArrayList<DocumentVersion>();
        final DocumentVersion firstDocumentVersion = newDocumentVersion(publishAs);
        documentVersionList.add(firstDocumentVersion);
        datum.getContainerModel(datum.publishAs).publish(firstVersion,
                documentVersionList, Collections.<EMail>emptyList(),
                publishToUserList);
        final List<JabberId> teamIds = new ArrayList<JabberId>(1);
        teamIds.add(datum.lookupUser(datum.publishToFirst).getId());
        Throwable cause = null;
        try {
            datum.getArtifactModel(datum.publishAs).createDraft(teamIds,
                    firstVersion.getArtifactUniqueId(),
                    firstVersion.getVersionId(), datum.now());
        } catch (final IllegalVersionException ivx) {
            cause = ivx;
        } catch (final DraftExistsException dex) {
            cause = dex;
        }
        assertNull("Could not create draft:  " +
                (null == cause ? "" : cause.getMessage()), cause);
        /* publish to second user */
        publishToUserList.clear();
        publishToUserList.add(datum.lookupUser(datum.publishToSecond));
        final ContainerVersion secondVersion = newVersion(firstVersion);
        documentVersionList.clear();
        documentVersionList.add(newDocumentVersion(firstDocumentVersion));
        datum.getContainerModel(datum.publishAs).publish(secondVersion,
                documentVersionList, Collections.<EMail>emptyList(),
                publishToUserList);
        /* read backup as first user */
        final Container firstContainer = datum.getBackupModel(datum.publishToFirst).readContainer(firstVersion.getArtifactUniqueId());
        final List<Container> firstContainerList = datum.getBackupModel(datum.publishToFirst).readContainers();
        assertNotNull("Backup container is null.", firstContainer);
        assertEquals("Backup container size does not match expectation.", 1, firstContainerList.size());
        assertEquals("Backup container does not match that of the backup list container.", firstContainer, firstContainerList.get(0));
        assertEquals("Backup container flags do not match that of the backup list container.", firstContainer.getFlags(), firstContainerList.get(0).getFlags());
        assertFalse("Backup container flags do not match expectation.", firstContainer.contains(ArtifactFlag.LATEST));
        /* read backup as second user */
        final Container secondContainer = datum.getBackupModel(datum.publishToSecond).readContainer(secondVersion.getArtifactUniqueId());
        final List<Container> secondContainerList = datum.getBackupModel(datum.publishToSecond).readContainers();
        assertNotNull("Backup container is null.", secondContainer);
        assertEquals("Backup container size does not match expectation.", 1, secondContainerList.size());
        assertEquals("Backup container does not match that of the backup list container.", secondContainer, secondContainerList.get(0));
        assertEquals("Backup container flags do not match that of the backup list container.", secondContainer.getFlags(), secondContainerList.get(0).getFlags());
        assertTrue("Backup container flags do not match expectation.", secondContainer.contains(ArtifactFlag.LATEST));
    }

    /**
     * @see com.thinkparity.desdemona.model.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture();
        final String publishAs = datum.newUniqueUsername();
        datum.createProfile(publishAs);
        datum.publishAs = datum.login(publishAs);
        datum.verifyEMail(datum.publishAs);
        final String publishToFirst = datum.newUniqueUsername();
        datum.createProfile(publishToFirst);
        datum.publishToFirst = datum.login(publishToFirst);
        datum.verifyEMail(datum.publishToFirst);
        final String publishToSecond = datum.newUniqueUsername();
        datum.createProfile(publishToSecond);
        datum.publishToSecond = datum.login(publishToSecond);
        datum.verifyEMail(datum.publishToSecond);
    }

    /**
     * @see com.thinkparity.desdemona.model.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        datum.logout(datum.publishAs);
        datum.logout(datum.publishToFirst);
        datum.logout(datum.publishToSecond);
    }

    /**
     * Instantiate a new document version.
     * 
     * @param createdBy
     *            A <code>User</code>.
     * @param versionId
     *            A <code>Long</code>.
     * @return A <code>DocumentVersion</code>.
     */
    private DocumentVersion newDocumentVersion(final DocumentVersion previous) {
        final DocumentVersion version = new DocumentVersion();
        version.setArtifactName(previous.getArtifactName());
        version.setArtifactType(previous.getArtifactType());
        version.setArtifactUniqueId(previous.getArtifactUniqueId());
        final Long versionId = previous.getVersionId() + Versioning.INCREMENT;
        final String content = MessageFormat.format("{0}:{1}",
                version.getArtifactUniqueId().toString(), String.valueOf(versionId));
        final ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(content.getBytes()));
        try {
            version.setChecksum(datum.checksum(channel));
        } finally {
            try {
                channel.close();
            } catch (final IOException iox) {
                fail(createFailMessage(iox, "Could not close document version byte channel."));
            }
        }
        version.setChecksumAlgorithm(previous.getChecksumAlgorithm());
        version.setCreatedBy(previous.getCreatedBy());
        version.setCreatedOn(datum.now());
        version.setSize(Long.valueOf(content.getBytes().length));
        version.setUpdatedBy(version.getCreatedBy());
        version.setUpdatedOn(version.getCreatedOn());
        version.setVersionId(versionId);
        return version;
    }

    /**
     * Instantiate a new document version.
     * 
     * @param createdBy
     *            A <code>User</code>.
     * @param versionId
     *            A <code>Long</code>.
     * @return A <code>DocumentVersion</code>.
     */
    private DocumentVersion newDocumentVersion(final User createdBy) {
        final DocumentVersion version = new DocumentVersion();
        version.setArtifactName(getName() + ":  Document");
        version.setArtifactType(ArtifactType.DOCUMENT);
        version.setArtifactUniqueId(UUIDGenerator.nextUUID());
        final Long versionId = Versioning.START;
        final String content = MessageFormat.format("{0}:{1}",
                version.getArtifactUniqueId().toString(),
                String.valueOf(versionId.longValue()));
        final ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(content.getBytes()));
        try {
            version.setChecksum(datum.checksum(channel));
        } finally {
            try {
                channel.close();
            } catch (final IOException iox) {
                fail(createFailMessage(iox, "Could not close document version byte channel."));
            }
        }
        version.setChecksumAlgorithm(datum.getChecksumAlgorithm());
        version.setCreatedBy(createdBy.getId());
        version.setCreatedOn(datum.now());
        version.setSize(Long.valueOf(content.getBytes().length));
        version.setUpdatedBy(version.getCreatedBy());
        version.setUpdatedOn(version.getCreatedOn());
        version.setVersionId(versionId);
        return version;
    }

    /**
     * Instantiate a new container version.
     * 
     * @param createdBy
     *            A <code>User</code>.
     * @param versionId
     *            A <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    private ContainerVersion newVersion(final ContainerVersion previous) {
        final ContainerVersion version = new ContainerVersion();
        version.setArtifactName(previous.getName());
        version.setArtifactType(previous.getArtifactType());
        version.setArtifactUniqueId(previous.getArtifactUniqueId());
        version.setCreatedBy(previous.getCreatedBy());
        version.setCreatedOn(datum.now());
        version.setUpdatedBy(version.getCreatedBy());
        version.setUpdatedOn(version.getCreatedOn());
        version.setVersionId(previous.getVersionId() + Versioning.INCREMENT);
        return version;
    }

    /**
     * Instantiate a new container version.
     * 
     * @param createdBy
     *            A <code>User</code>.
     * @param versionId
     *            A <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    private ContainerVersion newVersion(final User createdBy,
            final Long versionId) {
        final ContainerVersion version = new ContainerVersion();
        version.setArtifactName(getName() + ":  Container");
        version.setArtifactType(ArtifactType.CONTAINER);
        version.setArtifactUniqueId(UUIDGenerator.nextUUID());
        version.setCreatedBy(createdBy.getId());
        version.setCreatedOn(datum.now());
        version.setUpdatedBy(version.getCreatedBy());
        version.setUpdatedOn(version.getCreatedOn());
        version.setVersionId(versionId);
        return version;
    }

    /** <b>Title:</b>Test Fixture<br> */
    private class Fixture extends TicketTestCase.Fixture {
        private AuthToken publishAs;
        private AuthToken publishToFirst;
        private AuthToken publishToSecond;
    }
}
