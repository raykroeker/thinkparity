/*
 * Created On: Sep 16, 2006 12:57:18 PM
 */
package com.thinkparity.desdemona.model.archive;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.container.InternalContainerModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerReader extends ArchiveReader<Container, ContainerVersion> {

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
     * @see com.thinkparity.desdemona.model.archive.ArchiveReader#readVersions(java.util.UUID)
     */
    @Override
    public List<ContainerVersion> readVersions(final UUID uniqueId) {
        final Long containerId = readArchivedArtifactId(uniqueId);
        return containerModel.readVersions(containerId);
    }
}
