/*
 * Created On:  23-Jan-07 4:43:12 PM
 */
package com.thinkparity.ophelia.model.migrator;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.ZipUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventNotifier;

import com.thinkparity.codebase.model.DownloadMonitor;
import com.thinkparity.codebase.model.UploadMonitor;
import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.util.xmpp.event.ProductReleaseDeployedEvent;

import com.thinkparity.ophelia.model.Constants;
import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.events.MigratorEvent;
import com.thinkparity.ophelia.model.events.MigratorListener;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.MigratorIOHandler;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
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

    /** A workspace download directory <code>File</code>. */
    private File downloadDirectory;

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
    public void deploy(final ProcessMonitor monitor, final Product product,
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
        try {
            final Release release = event.getRelease();
            final List<Resource> resources = event.getResources();
            migratorIO.createRelease(release, resources);
            final Product product = migratorIO.readProduct(Constants.Product.NAME);
            migratorIO.updateLatestRelease(product, release);

            notifyProductReleaseDeployed(event.getProduct(), event.getRelease(),
                    remoteEventGenerator);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.InternalMigratorModel#initializeProduct()
     * 
     */
    public void initializeProduct() {
        try {
            // create the product
            final Product product = readRemoteProduct(Constants.Product.NAME);
            final Release release = readRemoteRelease(product, Constants.Release.NAME);
            final List<Resource> resources = readRemoteResources(release);
            migratorIO.createProduct(product, release, resources);
            // set the latest release
            final Release latestRelease = readRemoteLatestRelease(product);
            if (!release.equals(latestRelease)) {
                final List<Resource> latestReleaseResources = readRemoteResources(
                        latestRelease);
                migratorIO.createRelease(latestRelease, latestReleaseResources);
                migratorIO.updateLatestRelease(product, latestRelease);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.MigratorModel#initializeRelease(com.thinkparity.ophelia.model.util.ProcessMonitor)
     * 
     */
    public void initializeRelease(final ProcessMonitor monitor,
            final File directory) {
        try {
            final Product product = readProduct();
            final Release installedRelease = readRelease();
            // clean up old releases
            final List<Release> releases = migratorIO.readReleases();
            FileSystem releaseFileSystem;
            for (final Release release : releases) {
                if (!installedRelease.equals(release)) {
                    releaseFileSystem = new FileSystem(new File(directory, release.getName()));
                    releaseFileSystem.deleteTree();
                    migratorIO.delete(release);
                }
            }

            migratorIO.updateInstalledRelease(product, installedRelease);
            migratorIO.updateReleaseInitialization(installedRelease);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.MigratorModel#installRelease(com.thinkparity.ophelia.model.util.ProcessMonitor,
     *      java.io.File)
     * 
     */
    public void installRelease(final ProcessMonitor monitor,
            final File directory) {
        try {
            /* if the release deployed notification has been delivered and the
             * user has closed the workspace we might be in the scenario that we
             * are offline; however we can attempt to login. */
            if (!isOnline()) {
                getSessionModel().login(new ProcessAdapter() {});
            }
            assertOnline(); 
            
            // ensure the migration can be done
            Assert.assertNotNull(directory, "Directory cannot be null.");
            Assert.assertTrue(directory.isDirectory(),
                    "Directory {0} is not a directory.", directory);
            Assert.assertTrue(directory.canRead(),
                    "Directory {0} cannot be read from.", directory);
            Assert.assertTrue(directory.canWrite(),
                    "Directory {0} cannot be written to.", directory);
            final Product product = migratorIO.readProduct(Constants.Product.NAME);
            final Release installedRelease = migratorIO.readInstalledRelease(product);
            final Release latestRelease = migratorIO.readLatestRelease(product);
            Assert.assertNotTrue(installedRelease.equals(latestRelease),
                    "Installed release {0} equals latest release {1}.",
                    installedRelease, latestRelease);

            final List<Resource> installedResources = migratorIO.readInstalledResources(product);
            final List<Resource> latestResources = migratorIO.readLatestResources(product);

            // calculate the resources that need to be downloaded
            final List<Resource> downloadManifest = createDownloadManifest(
                    installedResources, latestResources);
            final File tmp = download(product, latestRelease, downloadManifest);
            try {
                final File download = new File(downloadDirectory,
                        latestRelease.getName());
                // copy to a download directory
                final ByteBuffer buffer = workspace.getDefaultBuffer();
                synchronized (buffer) {
                    FileUtil.copy(tmp, download, buffer);
                }
                final File install = new File(directory, installedRelease.getName());
                final File latest = new File(directory, latestRelease.getName());
                Assert.assertTrue(latest.mkdir(),
                        "Could not create directory {0}.", latest);
                // extract
                synchronized (buffer) {
                    ZipUtil.extractZipFile(download, latest, buffer);
                }
                // copy resources from installed to latest
                final List<Resource> copyManifest = createCopyManifest(
                        installedResources, latestResources);
                File from, to;
                final FileSystem latestFS = new FileSystem(latest);
                final FileSystem installFS = new FileSystem(install);
                for (final Resource copy : copyManifest) {
                    from = installFS.find(copy.getPath());
                    to = latestFS.createFile(copy.getPath());
                    final InputStream fromStream = new FileInputStream(from);
                    try {
                        final OutputStream toStream = new FileOutputStream(to);
                        try {
                            synchronized (buffer) {
                                StreamUtil.copy(fromStream, toStream, buffer);
                            }
                        } finally {
                            toStream.close();
                        }
                    } finally {
                        fromStream.close();
                    }
                }
                // install release
                migratorIO.updateInstalledRelease(product, latestRelease);
            } finally {
                Assert.assertTrue(tmp.delete(), "Could not delete file {0}.",
                        tmp);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.MigratorModel#isMigrationPossible()
     *
     */
    public Boolean isLatestRelease() {
        try {
            final Product product = readProduct();
            final Release installed = migratorIO.readInstalledRelease(product);
            final Release latest = migratorIO.readLatestRelease(product);
            return installed.equals(latest);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.MigratorModel#isReleaseInitialized()
     *
     */
    public Boolean isReleaseInitialized() {
        return migratorIO.isReleaseInitialized(readRelease());
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.InternalMigratorModel#logError(java.lang.Throwable,
     *      java.lang.reflect.Method, java.lang.Object[])
     * 
     */
    public void logError(final Throwable cause, final Method method,
            final Object[] arguments) {
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            if (sessionModel.isLoggedIn()) {
                final Error error = new Error();
                error.setArguments(arguments);
                error.setCause(cause);
                error.setMethod(method);
                error.setOccuredOn(sessionModel.readDateTime());
                final Product product = migratorIO.readProduct(Constants.Product.NAME);
                sessionModel.logError(product,
                        migratorIO.readInstalledRelease(product), error);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.InternalMigratorModel#readProduct()
     *
     */
    public Product readProduct() {
        try {
            return migratorIO.readProduct(Constants.Product.NAME);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.MigratorModel#readProduct(java.lang.String)
     *
     */
    public Product readProduct(final String name) {
        try {
            return getSessionModel().readMigratorProduct(name);   
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.migrator.MigratorModel#readRelease()
     *
     */
    public Release readRelease() {
        try {
            final Product product = readProduct();
            return migratorIO.readInstalledRelease(product);
        } catch (final Throwable t) {
            throw panic(t);
        }
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
        downloadDirectory = workspace.getDownloadDirectory();
        migratorIO = IOFactory.getDefault(workspace).createMigratorHandler();
    }

    /**
     * Create a list of resources that need to be copied.  Compare the list
     * of resources in the installed list with that of the latest list.  Those
     * in the latest that are in the installed list are copied.
     * 
     * @param installed
     *            A <code>List</code> of installed <code>Resource</code>s.
     * @param latest
     *            A <code>List</code> of latest <code>Resource</code>s.
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    private List<Resource> createCopyManifest(final List<Resource> installed,
            final List<Resource> latest) {
        final List<Resource> copy = new ArrayList<Resource>();
        for (final Resource resource : installed) {
            if (latest.contains(resource))
                copy.add(resource);
        }
        return copy;
    }

    /**
     * Create a list of resources that need to be downloaded.  Compare the list
     * of resources in the installed list with that of the latest list.  Those
     * in the latest that are not installed need to be downloaded.
     * 
     * @param installed
     *            A <code>List</code> of installed <code>Resource</code>s.
     * @param latest
     *            A <code>List</code> of latest <code>Resource</code>s.
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    private List<Resource> createDownloadManifest(final List<Resource> installed,
            final List<Resource> latest) {
        final List<Resource> download = new ArrayList<Resource>();
        for (final Resource resource : latest) {
            if (!installed.contains(resource))
                download.add(resource);
        }
        return download;
    }

    /**
     * Download a list of resources for a release.
     * 
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     * @return A <code>File</code>.
     */
    private File download(final Product product, final Release release,
            final List<Resource> resources) throws IOException {
        final InternalSessionModel sessionModel = getSessionModel();
        final StreamSession session = sessionModel.createStreamSession();
        final String streamId = sessionModel.createStream(session);
        sessionModel.createMigratorStream(streamId, product, release, resources);
        return downloadStream(new DownloadMonitor() {
            public void chunkDownloaded(final int chunkSize) {
                logger.logTrace("Downloading {0}/{1}", chunkSize, -1L);
            }
        }, streamId);
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
                product.getName(), OSUtil.getOs());
    }

    private Product readRemoteProduct(final String name) {
        return getSessionModel().readMigratorProduct(name);
    }

    private Release readRemoteRelease(final Product product, final String name) {
        return getSessionModel().readMigratorRelease(product.getName(),
                name, OSUtil.getOs());
    }

    private List<Resource> readRemoteResources(final Release release) {
        return getSessionModel().readMigratorResources(
                Constants.Product.NAME, release.getName(), OSUtil.getOs());
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
