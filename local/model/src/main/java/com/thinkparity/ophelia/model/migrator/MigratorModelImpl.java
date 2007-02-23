/*
 * Created On:  23-Jan-07 4:43:12 PM
 */
package com.thinkparity.ophelia.model.migrator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.event.EventNotifier;

import com.thinkparity.codebase.model.DownloadMonitor;
import com.thinkparity.codebase.model.UploadMonitor;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.util.xmpp.event.ProductReleaseDeployedEvent;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.events.MigratorEvent;
import com.thinkparity.ophelia.model.events.MigratorListener;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.MigratorIOHandler;
import com.thinkparity.ophelia.model.migrator.monitor.DeployMonitor;
import com.thinkparity.ophelia.model.migrator.monitor.MigrateMonitor;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Migrator Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MigratorModelImpl extends Model<MigratorListener> implements
        MigratorModel, InternalMigratorModel {

    /** A <code>MigratorIOHandler</code>. */
    private MigratorIOHandler migratorIO;

    /** A remote <code>MigratorEventGenerator</code>. */
    private final MigratorEventGenerator remoteEventGenerator;

    /**
     * Create MigratorModelImpl.
     *
     */
    public MigratorModelImpl() {
        super();
        this.remoteEventGenerator = new MigratorEventGenerator(MigratorEvent.Source.REMOTE);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#addListener(com.thinkparity.codebase.event.EventListener)
     *
     */
    @Override
    public void addListener(MigratorListener listener) {
        super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.MigratorModel#deployProduct(com.thinkparity.ophelia.model.migrator.monitor.DeployMonitor, java.lang.String, java.lang.String, java.io.File)
     *
     */
    public void deploy(final DeployMonitor monitor, final Product product,
            final Release release, final List<Resource> resources,
            final File file) {
        try {
            // upload the stream
            final String streamId = upload(file);
            // deploy
            getSessionModel().deployMigrator(product, release, resources, streamId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.InternalMigratorModel#handleProductReleaseDeployed(com.thinkparity.codebase.model.util.xmpp.event.ProductReleaseDeployedEvent)
     * 
     */
    public void handleProductReleaseDeployed(
            final ProductReleaseDeployedEvent event) {
// TODO finish migrator implementation
//        try {
//            migratorIO.deleteLatestRelease();
//            migratorIO.createLatestRelease(event.getRelease(), event.getResources());
//
//            notifyProductReleaseDeployed(event.getProduct(), event.getRelease(),
//                    remoteEventGenerator);
//        } catch (final Throwable t) {
//            throw panic(t);
//        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.InternalMigratorModel#initialize()
     *
     */
    public void initialize() {
// TODO finish migrator implementation
//        assertOnline();
//        try {
//            // create the product
//            final Product product = readRemoteProduct(getProductName());
//            migratorIO.createProduct(product);
//            // create the release
//            final Release release = readRemoteRelease(product, getReleaseName());
//            final List<Resource> resources = readRemoteResources(release);
//            migratorIO.createInstalledRelease(release, resources);
//            // create the latest release
//            final Release latestRelease = readRemoteLatestRelease(product);
//            final List<Resource> latestResources = readRemoteResources(latestRelease);
//            migratorIO.createLatestRelease(latestRelease, latestResources);
//        } catch (final Throwable t) {
//            throw panic(t);
//        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.MigratorModel#isMigrationPossible()
     *
     */
    public Boolean isMigrationPossible() {
        return Boolean.FALSE;
// TODO finish migrator implementation
//        try {
//            final Release release = migratorIO.readInstalledRelease();
//            final Release latestRelease = migratorIO.readLatestRelease();
//            return !release.equals(latestRelease);
//        } catch (final Throwable t) {
//            throw panic(t);
//        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.MigratorModel#migrate()
     *
     */
    public void migrate(final MigrateMonitor monitor) {
// TODO finish migrator implementation
//        try {
//            final Product product = migratorIO.readProduct();
//            final List<Resource> resources = migratorIO.readInstalledResources();
//            // read the latest release
//            final Release latestRelease = migratorIO.readLatestRelease();
//            final List<Resource> latestResources = migratorIO.readLatestResources();
//            // calculate the resources that need to be downloaded
//            final File resourceArchive = download(createDownloadList(resources, latestResources));
//            try {
//                final FileSystem installFileSystem = new FileSystem(workspace.createTempDirectory());
//                try {
//                    // extract the archive
//                    ZipUtil.extractZipFile(resourceArchive, installFileSystem.getRoot());
//
//                } finally {
//                    installFileSystem.deleteTree();
//                }
//            } finally {
//                resourceArchive.delete();
//            }
//        } catch (final Throwable t) {
//            throw panic(t);
//        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.MigratorModel#readProduct(java.lang.String)
     *
     */
    public Product readProduct(final String name) {
        return null;
// TODO finish migrator implementation
//        return getSessionModel().readMigratorProduct(name);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#removeListener(com.thinkparity.codebase.event.EventListener)
     *
     */
    @Override
    public void removeListener(MigratorListener listener) {
        super.removeListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        migratorIO = IOFactory.getDefault(workspace).createMigratorHandler();
    }

    /**
     * Create a list of resources that need to be downloaded.
     * 
     * @param resources
     *            A <code>List</code> of installed <code>Resource</code>s.
     * @param latestResources
     *            The latest <code>List</code> of <code>Resource</code>s.
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    private List<Resource> createDownloadList(final List<Resource> resources,
            final List<Resource> latestResources) {
        final List<Resource> downloadList = new ArrayList<Resource>();
        for (final Resource latestResource : latestResources) {
            if (!resources.contains(latestResource))
                downloadList.add(latestResource);
        }
        return downloadList;
    }

    /**
     * Download a list of resources.
     * 
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     * @return A <code>File</code>.
     */
    private File download(final List<Resource> resources) throws IOException {
        final InternalSessionModel sessionModel = getSessionModel();
        final StreamSession session = sessionModel.createStreamSession();
        final String streamId = sessionModel.createStream(session);
        sessionModel.createMigratorStream(streamId, resources);
        return downloadStream(new DownloadMonitor() {
            public void chunkDownloaded(final int chunkSize) {
                logger.logTrace("Downloading {0}/{1}", chunkSize, -1L);
            }
        }, streamId);
    }

    private String getProductName() {
        return System.getProperty("thinkparity.product-name");
    }

    private String getReleaseName() {
        return System.getProperty("thinkparity.release-name");
    }

    /**
     * Fire a product release deployed event.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param eventGenerator
     *            A <code>MigratorEventGenerator</code>.
     */
    private void notifyProductReleaseDeployed(final Product product,
            final Release release, final MigratorEventGenerator eventGenerator) {
        super.notifyListeners(new EventNotifier<MigratorListener>() {
            public void notifyListener(final MigratorListener listener) {
                listener.productReleaseDeployed(eventGenerator.generate(
                        product, release));
            }
        });
    }

    private Release readRemoteLatestRelease(final Product product) {
        return getSessionModel().readMigratorLatestRelease(
                product.getUniqueId(), OSUtil.getOs());
    }

    private Product readRemoteProduct(final String name) {
        return getSessionModel().readMigratorProduct(name);
    }

    private Release readRemoteRelease(final Product product, final String name) {
        return getSessionModel().readMigratorRelease(product.getUniqueId(),
                name, OSUtil.getOs());
    }

    private List<Resource> readRemoteResources(final Release release) {
        return getSessionModel().readMigratorResources(
                release.getProduct().getUniqueId(), release.getName(),
                OSUtil.getOs());
    }

    /**
     * Upload a release to the streaming server. The release will be zipped up
     * and uploaded.
     * 
     * @param releaseRoot
     *            A release root directory <code>File</code>.
     * @return The stream id <code>String</code>.
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String upload(final File file) throws FileNotFoundException,
            IOException {
        final InternalSessionModel sessionModel = getSessionModel();
        final StreamSession session = sessionModel.createStreamSession();
        final InputStream stream = new BufferedInputStream(
                new FileInputStream(file), getDefaultBufferSize());
        final Long streamSize = file.length();
        try {
            final String streamId = sessionModel.createStream(session);
            upload(new UploadMonitor() {
                public void chunkUploaded(final int chunkSize) {
                    logger.logTraceId();
                    logger.logVariable("chunkSize:{0}", chunkSize);
                }
            }, streamId, session, stream, streamSize);
            return streamId;
        } finally {
            stream.close();
        }
    }
}
