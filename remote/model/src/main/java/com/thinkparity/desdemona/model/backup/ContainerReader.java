/*
 * Created On: Sep 16, 2006 12:57:18 PM
 */
package com.thinkparity.desdemona.model.backup;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.InternalContainerModel;

import com.thinkparity.desdemona.model.archive.ClientModelFactory;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerReader extends BackupReader<Container, ContainerVersion> {

    /** An internal thinkParity client container interface. */
    private final InternalContainerModel containerModel;

    /** Create ContainerReader. */
    ContainerReader(final ClientModelFactory modelFactory) {
        super(modelFactory);
        this.containerModel = getContainerModel();
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#read()
     */
    @Override
    public List<Container> read() {
        return containerModel.read(filter);
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#read(java.util.UUID)
     */
    @Override
    public Container read(final UUID uniqueId) {
        final Long containerId = readBackupArtifactId(uniqueId);
        if (null == containerId) {
            return null;
        } else {
            return containerModel.read(containerId);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupReader#readPublishedTo(java.util.UUID,
     *      java.lang.Long)
     * 
     */
    @Override
    public Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId) {
        final Long containerId = this.readBackupArtifactId(uniqueId);
        if (null == containerId) {
            return Collections.emptyMap();
        } else {
            return containerModel.readPublishedTo(containerId, versionId);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#readVersions(java.util.UUID)
     */
    @Override
    public List<ContainerVersion> readVersions(final UUID uniqueId) {
        final Long containerId = readBackupArtifactId(uniqueId);
        if (null == containerId) {
            return Collections.emptyList();
        } else {
            return containerModel.readVersions(containerId);
        }
    }
}
