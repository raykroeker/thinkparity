/*
 * Generated On: Oct 04 06 09:40:46 AM
 */
package com.thinkparity.ophelia.model.backup;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.InvalidLocationException;

import com.thinkparity.ophelia.model.Delegate;
import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.backup.delegate.Restore;
import com.thinkparity.ophelia.model.events.BackupListener;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.service.BackupService;
import com.thinkparity.service.ServiceFactory;
import com.thinkparity.service.SessionService;

/**
 * <b>Title:</b>thinkParity Backup Model Implementation</br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
public final class BackupModelImpl extends Model<BackupListener> implements
        BackupModel, InternalBackupModel {

    /** The backup web-service. */
    private BackupService backupService;

    /** A default artifact comparator. */
    private final Comparator<Artifact> defaultComparator;

    /** A default artifact filter. */
    private final Filter<? super Artifact> defaultFilter;

    /** The default artifact receipt comparator. */
    private final Comparator<ArtifactReceipt> defaultReceiptComparator;

    /** The default artifact receipt filter. */
    private final Filter<? super ArtifactReceipt> defaultReceiptFilter;

    /** A default artifact version comparator. */
    private final Comparator<ArtifactVersion> defaultVersionComparator;

    /** A default artifact version filter. */
    private final Filter<ArtifactVersion> defaultVersionFilter;

    /** The session web-service. */
    private SessionService sessionService;

    /**
     * Create BackupModelImpl.
     *
     */
    public BackupModelImpl() {
        super();
        this.defaultComparator = new ComparatorBuilder().createByName();
        this.defaultFilter = FilterManager.createDefault();
        this.defaultReceiptComparator = new ComparatorBuilder().createArtifactReceiptByReceivedOnAscending();
        this.defaultReceiptFilter = FilterManager.createDefault();
        this.defaultVersionComparator = new ComparatorBuilder().createVersionById(Boolean.TRUE);
        this.defaultVersionFilter = FilterManager.createDefault();
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#addListener(com.thinkparity.codebase.event.EventListener)
     *
     */
    @Override
    public void addListener(final BackupListener l) {
        super.addListener(l);
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readContainer(java.util.UUID)
     * 
     */
    public Container readContainer(final UUID uniqueId) {
        try {
            return getSessionModel().readBackupContainer(uniqueId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read the containers from the backup.
     * 
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers() {
        try {
            return readContainers(defaultComparator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read the containers from the backup.
     * 
     * @param comparator
     *            A <code>Comparator&lt;Artifact&gt;</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers(final Comparator<Artifact> comparator) {
        try {
            return readContainers(comparator, defaultFilter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read the containers from the backup.
     * 
     * @param comparator
     *            A <code>Comparator&lt;Artifact&gt;</code>.
     * @param filter
     *            A <code>Filter&lt;? super Artifact&gt;</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers(final Comparator<Artifact> comparator, final Filter<? super Artifact> filter) {
        try {
            final List<Container> containers =
                getSessionModel().readBackupContainers();
            FilterManager.filter(containers, filter);
            ModelSorter.sortContainers(containers, comparator);
            return containers;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId) {
        return readContainerVersions(uniqueId, defaultVersionComparator,
                defaultVersionFilter);
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator) {
        return readContainerVersions(uniqueId, comparator,
                defaultVersionFilter);
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        try {
            final List<ContainerVersion> versions =
                getSessionModel().readBackupContainerVersions(uniqueId);
            FilterManager.filter(versions, filter);
            ModelSorter.sortContainerVersions(versions, comparator);
            return versions;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Filter<? super ArtifactVersion> filter) {
        return readContainerVersions(uniqueId, defaultVersionComparator,
                filter);
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId) {
        return readDocuments(uniqueId, versionId, defaultComparator,
                defaultFilter);
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator) {
        return readDocuments(uniqueId, versionId, comparator, defaultFilter);
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        try {
            final List<Document> documents =
                getSessionModel().readBackupDocuments(uniqueId, versionId);
            FilterManager.filter(documents, filter);
            ModelSorter.sortDocuments(documents, comparator);
            return documents;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    public List<Document> readDocuments(final UUID uniqueId, final Long versionId,
            final UUID documentUniqueId, final Filter<? super Artifact> filter) {
        return readDocuments(uniqueId, versionId, defaultComparator, filter);
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readDocumentVersions(java.util.UUID,
     *      java.lang.Long)
     * 
     */
    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId) {
        return readDocumentVersions(uniqueId, versionId,
                defaultVersionComparator, defaultVersionFilter);
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator) {
        return readDocumentVersions(uniqueId, versionId, comparator,
                defaultVersionFilter);
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        try {
            final List<DocumentVersion> versions =
                getSessionModel().readBackupDocumentVersions(uniqueId,
                        versionId);
            FilterManager.filter(versions, filter);
            ModelSorter.sortDocumentVersions(versions, comparator);
            return versions;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Filter<? super ArtifactVersion> filter) {
        return readDocumentVersions(uniqueId, versionId,
                defaultVersionComparator, filter);
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readPublishedTo(java.util.UUID,
     *      java.lang.Long)
     * 
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId) {
        try {
            return readPublishedTo(uniqueId, versionId, defaultReceiptComparator,
                    defaultReceiptFilter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readPublishedTo(java.util.UUID,
     *      java.lang.Long, java.util.Comparator)
     * 
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactReceipt> comparator) {
        try {
            return readPublishedTo(uniqueId, versionId, comparator,
                    defaultReceiptFilter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readPublishedTo(java.util.UUID,
     *      java.lang.Long, java.util.Comparator,
     *      com.thinkparity.codebase.filter.Filter)
     * 
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactReceipt> comparator,
            final Filter<? super ArtifactReceipt> filter) {
        try {
            final List<ArtifactReceipt> publishedTo =
                getSessionModel().readBackupPublishedTo(uniqueId, versionId);
            FilterManager.filter(publishedTo, filter);
            ModelSorter.sortReceipts(publishedTo, comparator);
            return publishedTo;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Filter<? super ArtifactReceipt> filter) {
        try {
            return readPublishedTo(uniqueId, versionId,
                    defaultReceiptComparator, filter); 
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readPublishedToEMails(java.util.UUID, java.lang.Long)
     *
     */
    public List<PublishedToEMail> readPublishedToEMails(UUID uniqueId, Long versionId) {
        try {
            return getSessionModel().readBackupPublishedToEMails(uniqueId,
                    versionId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readTeamIds(java.util.UUID)
     *
     */
    public List<JabberId> readTeamIds(final UUID uniqueId) {
        try {
            return getSessionModel().readBackupTeamIds(uniqueId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#removeListener(com.thinkparity.codebase.event.EventListener)
     *
     */
    @Override
    public void removeListener(final BackupListener l) {
        super.removeListener(l);
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.BackupModel#restore(com.thinkparity.ophelia.model.util.ProcessMonitor,
     *      com.thinkparity.codebase.model.session.Credentials)
     * 
     */
    @Override
    public void restore(final ProcessMonitor monitor,
            final Credentials credentials) throws InvalidCredentialsException,
            InvalidLocationException {
        try {
            final Restore delegate = createDelegate(Restore.class);
            delegate.setCredentials(credentials);
            delegate.setMonitor(monitor);
            delegate.restore();
        } catch (final InvalidCredentialsException icx) {
            throw icx;
        } catch (final InvalidLocationException ilx) {
            throw ilx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        final ServiceFactory serviceFactory = getServiceFactory();
        this.backupService = serviceFactory.getBackupService();
        this.sessionService = serviceFactory.getSessionService();
    }

    /**
     * Obtain the backup web-serivce.
     * 
     * @return An instance of <code>BackupService</code>.
     */
    BackupService getBackupService() {
        return backupService;
    }

    /**
     * Obtain the session web-serivce.
     * 
     * @return An instance of <code>SessionService</code>.
     */
    SessionService getSessionService() {
        return sessionService;
    }

    /**
     * Create an instance of a delegate.
     * 
     * @param <D>
     *            A delegate type.
     * @param type
     *            The delegate type <code>Class</code>.
     * @return An instance of <code>D</code>.
     */
    private <D extends Delegate<BackupModelImpl>> D createDelegate(
            final Class<D> type) {
        try {
            final D instance = type.newInstance();
            instance.initialize(this);
            return instance;
        } catch (final IllegalAccessException iax) {
            throw new ThinkParityException("Could not create delegate.", iax);
        } catch (final InstantiationException ix) {
            throw new ThinkParityException("Could not create delegate.", ix);
        }
    }
}
