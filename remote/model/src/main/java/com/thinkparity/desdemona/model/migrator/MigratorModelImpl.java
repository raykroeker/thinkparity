/*
 * Created On:  23-Jan-07 5:35:19 PM
 */
package com.thinkparity.desdemona.model.migrator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.ZipUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.DownloadMonitor;
import com.thinkparity.codebase.model.UploadMonitor;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.codec.MD5Util;
import com.thinkparity.codebase.model.util.xmpp.event.ProductReleaseDeployedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.ArtifactSql;
import com.thinkparity.desdemona.model.io.sql.MigratorSql;
import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Migrator Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class MigratorModelImpl extends AbstractModelImpl {

    /** An <code>ArtifactSql</code> persistence. */
    private final ArtifactSql artifactSql;

    /** A <code>MigratorSql</code> persistence. */
    private final MigratorSql migratorSql;

    /**
     * Create MigratorModelImpl.
     *
     * @param session
     */
    MigratorModelImpl(final Session session) {
        super(session);
        this.artifactSql = new ArtifactSql();
        this.migratorSql = new MigratorSql();
    }
   
    /**
     * Create a stream of the resources.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     */
    void createStream(final JabberId userId, final String streamId,
            final List<Resource> resources) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("resources", resources);
        logger.logVariable("streamId", streamId);
        try {
            assertIsAuthenticatedUser(userId);

            final FileSystem streamFileSystem = new FileSystem(session.createTempDirectory());
            try {
                final File streamFile = session.createTempFile();
                try {
                    // copy the resources into the file system
                    OutputStream fileStream;
                    InputStream resourceStream;
                    for (final Resource resource : resources) {
                        fileStream = new BufferedOutputStream(
                                new FileOutputStream(
                                        streamFileSystem.createFile(
                                                resource.getPath())),
                                                getDefaultBufferSize());
                        try {
                            resourceStream = migratorSql.openResource(
                                    resource.getName(), resource.getVersion(),
                                    resource.getChecksum());
                            try {
                                StreamUtil.copy(resourceStream, fileStream,
                                        getDefaultBufferSize());
                            } finally {
                                resourceStream.close();
                            }
                        } finally {
                            fileStream.close();
                        }
                    }
                    // archive the resources
                    ZipUtil.createZipFile(streamFile, streamFileSystem.getRoot(),
                            getDefaultBufferSize());
                    // upload the stream
                    final Long streamSize = streamFile.length();
                    final InputStream stream = new BufferedInputStream(
                            new FileInputStream(streamFile),
                            getDefaultBufferSize());
                    final StreamSession session = getStreamModel().createSession(userId);
                    upload(new UploadMonitor() {
                        public void chunkUploaded(int chunkSize) {
                            logger.logTrace("Uploading {0}/{1}", chunkSize, streamSize);
                        }
                    }, streamId, session, stream, streamSize);                        
                } finally {
                    streamFile.delete();
                }
            } finally {
                streamFileSystem.deleteTree();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Deploy a release.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void deploy(final JabberId userId, final Product product,
            final Release release, final List<Resource> resources,
            final String streamId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("product", product);
        logger.logVariable("release", release);
        logger.logVariable("resources", resources);
        logger.logVariable("streamId", streamId);
        try {
            assertIsAuthenticatedUser(userId);
            assertIsSystemUser(userId);

            // find/create the product
            final Product deployProduct;
            if (!doesExistProduct(product.getName())) {
                createProduct(userId, product);
            }
            deployProduct = readProduct(product.getName());

            Assert.assertNotTrue(doesExistRelease(deployProduct.getId(),
                    release.getName(), release.getOs()),
                    "Release {0} for product {1} on {0} already exists.",
                    release.getName(), product.getName(), release.getOs());
            // find the release
            // download the release
            final File releaseFile = downloadStream(new DownloadMonitor() {
                private long totalDownloadSize = 0;
                public void chunkDownloaded(final int chunkSize) {
                    logger.logTraceId();
                    logger.logVariable("chunkSize", chunkSize);
                    logger.logVariable("totalDownloadSize", (totalDownloadSize += chunkSize));
                }
            }, userId, streamId);
            try {
                final FileSystem tempFileSystem = new FileSystem(session.createTempDirectory());
                try {
                    // extract the release
                    ZipUtil.extractZipFile(releaseFile, tempFileSystem.getRoot(),
                            getDefaultBufferSize());
                    // validate
                    validateRelease(release, resources, releaseFile, tempFileSystem);
                    // create
                    createRelease(deployProduct, release, resources, tempFileSystem);
                    // notify
                    notifyProductReleaseDeployed(deployProduct, release, resources);
                } finally {
                    tempFileSystem.deleteTree();
                }
            } finally {
                releaseFile.delete();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the latest release.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @return A <code>Release</code>.
     */
    Release readLatestRelease(final JabberId userId,
            final UUID productUniqueId, final OS os) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("productUniqueId", productUniqueId);
        logger.logVariable("os", os);
        try {
            assertIsAuthenticatedUser(userId);

            final String latestReleaseName = migratorSql.readLatestReleaseName(
                    productUniqueId, os);
            return migratorSql.readRelease(productUniqueId, latestReleaseName, os);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read a product.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param name
     *            A product name <code>String</code>.
     * @return A <code>Product</code>.
     */
    Product readProduct(final JabberId userId, final String name) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("name", name);
        try {
            assertIsAuthenticatedUser(userId);

            return migratorSql.readProduct(name);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }


    /**
     * Read a release.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @param name
     *            A release name.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    Release readRelease(final JabberId userId, final UUID productUniqueId,
            final String name, final OS os) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("productUniqueId", productUniqueId);
        logger.logVariable("name", name);
        logger.logVariable("os", os);
        try {
            assertIsAuthenticatedUser(userId);

            return migratorSql.readRelease(productUniqueId, name, os);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read a release's resources.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @param releaseName
     *            A release name.
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    List<Resource> readResources(final JabberId userId,
            final UUID productUniqueId, final String releaseName, final OS os) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("productUniqueId", productUniqueId);
        logger.logVariable("releaseName", releaseName);
        logger.logVariable("os", os);
        try {
            assertIsAuthenticatedUser(userId);

            return migratorSql.readResources(productUniqueId, releaseName, os);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create a product.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param product
     *            A <code>Product</code>.
     * @return A <code>Product</code>.
     */
    private void createProduct(final JabberId userId, final Product product) {
        product.setId(new Long(artifactSql.create(product.getUniqueId(), userId,
                    product.getCreatedBy(), product.getCreatedOn())));
        migratorSql.createProduct(product);
    }

    /**
     * Create a release. This will create the release then examine the resources
     * by name version and checksum to see if they already exist. If no such
     * resource exists it will be created otherwise simply a reference between
     * the release and the resource is created.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     * @param releaseFileSystem
     *            A <code>FileSystem</code>.
     * @throws IOException
     */
    private void createRelease(final Product product, final Release release,
            final List<Resource> resources, final FileSystem releaseFileSystem)
            throws IOException {
        migratorSql.createRelease(product, release);
        InputStream stream;
        for (final Resource resource : resources) {
            logger.logVariable("resource.getName()", resource.getName());
            logger.logVariable("resource.getOs()", resource.getOs());
            logger.logVariable("resource.getPath()", resource.getPath());
            logger.logVariable("resource.getVersion()", resource.getVersion());
            if (migratorSql.doesExistResource(resource.getName(),
                    resource.getVersion(), resource.getChecksum())) {
                resource.setId(migratorSql.readResourceId(resource.getName(),
                        resource.getVersion(), resource.getChecksum()));
                if (migratorSql.doesExistResource(resource.getId(), resource.getOs())) {
                    logger.logInfo("Resource {0} for {1} already exists.",
                            resource.getId(), resource.getOs());
                } else {
                    logger.logInfo("Resource {0} already exists.  Adding {1}.",
                            resource.getId(), resource.getOs());
                    migratorSql.addResource(resource, resource.getOs());
                }
                migratorSql.addResource(release, resource);
            } else {
                stream = new FileInputStream(releaseFileSystem.findFile(resource.getPath()));
                try {
                    migratorSql.addResource(release, resource, stream);
                } finally {
                    stream.close();
                }
            }
        }
    }

    /**
     * Determine if a product exists.
     * 
     * @param name
     *            A name <code>String</code>.
     * @return True if the product exists.
     */
    private boolean doesExistProduct(final String name) {
        return migratorSql.doesExistProduct(name).booleanValue();
    }

    /**
     * Determine if a release exists.
     * 
     * @param productId
     *            A product id <code>Long</code>.
     * @param name
     *            A release name <code>String</code>.
     * @return True if the release exists.
     */
    private boolean doesExistRelease(final Long productId, final String name,
            final OS os) {
        return migratorSql.doesExistRelease(productId, name, os); 
    }

    /**
     * Notify all users of a product that a new release is available.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     */
    private void notifyProductReleaseDeployed(final Product product,
            final Release release, final List<Resource> resources) {
        // TODO limit the event to users of the product
        final ProductReleaseDeployedEvent event = new ProductReleaseDeployedEvent();
        event.setProduct(product);
        event.setRelease(release);
        event.setResources(resources);

        final List<User> users = getUserModel().read();
        final List<JabberId> userIds = new ArrayList<JabberId>(users.size());
        for (final User user : users)
            userIds.add(user.getId());
        enqueuePriorityEvent(session.getJabberId(), userIds, event);
    }

    /**
     * Read a product.
     * 
     * @param name
     *            A name <code>String</code>.
     * @return A <code>Product</code>.
     */
    private Product readProduct(final String name) {
        return migratorSql.readProduct(name);
    }

    /**
     * Validate the release and the release file. A checksum will be calculated
     * for the file and verified against the release.
     * 
     * @param release
     *            A <code>Release</code>.
     * @param releaseFile
     *            A release <code>File</code>.
     * @throws IOException
     */
    private void validateRelease(final Release release,
            final List<Resource> resources, final File releaseFile,
            final FileSystem releaseFileSystem) throws IOException {
        final InputStream stream = new FileInputStream(releaseFile);
        final String checksum;
        try {
            checksum = MD5Util.md5Hex(stream, getDefaultBufferSize());
        } finally {
            stream.close();
        }
        Assert.assertTrue(release.getChecksum().equals(checksum),
                "Checksum for release {0} does not match calculation.  {1} <> {2}",
                release.getName());
    }
}
