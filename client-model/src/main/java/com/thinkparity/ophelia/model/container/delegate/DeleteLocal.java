/*
 * Created On:  28-Apr-07 11:41:25 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.container.monitor.DeleteLocalData;
import com.thinkparity.ophelia.model.container.monitor.DeleteLocalStep;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.document.DocumentFileLock;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

import com.thinkparity.net.NetworkException;
import com.thinkparity.stream.StreamException;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Delete Local Delegate<br>
 * <b>Description:</b>Delete all local container data.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeleteLocal extends ContainerDelegate {

    /** A <code>ProcessMonitor</code>. */
    private ProcessMonitor monitor;

    /** The monitor data. */
    private final DeleteLocalData monitorData;

    /**
     * Create DeleteLocal.
     *
     */
    public DeleteLocal() {
        super();
        this.monitorData = new DeleteLocalData();
    }

    /**
     * Delete local data.
     * 
     * @throws CannotLockException
     *             if a local document cannot be locked
     * @throws NetworkException
     *             if an unrecoverable network read error occurs
     * @throws IOException
     *             if an io error occurs
     * @throws StreamException
     *             if an unrecoverable stream protocol error occurs
     * @throws NoSuchPaddingException
     *             if a decryption error occurs
     * @throws NoSuchAlgorithmException
     *             if a decryption error occurs
     * @throws InvalidKeyException
     *             if a decryption error occurs
     */
    public void deleteLocal() throws CannotLockException, NetworkException,
            IOException, StreamException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException {
        final List<Container> containers = read();
        monitorData.setDeleteContainers(containers);
        notifyStepBegin(monitor, DeleteLocalStep.DELETE_CONTAINERS, monitorData);
        final Map<Container, List<Document>> allDocuments = readAllDocuments();
        final Map<Document, DocumentFileLock> allDocumentsLocks = new HashMap<Document, DocumentFileLock>();
        final Map<DocumentVersion, DocumentFileLock> allDocumentsVersionsLocks = new HashMap<DocumentVersion, DocumentFileLock>();
        try {
            for (final List<Document> documents : allDocuments.values()) {
                for (final Document document : documents) {
                    allDocumentsLocks.put(document, lockDocument(document));
                    allDocumentsVersionsLocks.putAll(lockDocumentVersions(document));
                }
            }
            for (final Container container : containers) {
                monitorData.setDeleteContainer(container);
                notifyStepBegin(monitor, DeleteLocalStep.DELETE_CONTAINER, monitorData);
                deleteLocal(container.getId(), allDocuments.get(container),
                        allDocumentsLocks, allDocumentsVersionsLocks);
                notifyStepEnd(monitor, DeleteLocalStep.DELETE_CONTAINER);
            }
        } finally {
            try {
                releaseLocks(allDocumentsLocks.values());
            } finally {
                releaseLocks(allDocumentsVersionsLocks.values());
            }
        }
    }

    /**
     * Set the process monitor.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    public void setMonitor(final ProcessMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Read a map of all documents for all containers. This will look in the
     * draft as well as all versions.
     * 
     * @return A <code>Map</code> of <code>Container</code>s to their
     *         <code>Document</code>s.
     */
    private Map<Container, List<Document>> readAllDocuments() {
        final List<Container> containers = read();
        final Map<Container, List<Document>> allDocuments =
            new HashMap<Container, List<Document>>(containers.size(), 1.0F);
        for (final Container container : containers) {
            allDocuments.put(container, readAllDocuments(container.getId()));
        }
        return allDocuments;
    }
}
