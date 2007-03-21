/*
 * Created On: Jul 6, 2006 11:51:59 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.xmpp.event.MigratorListener;

/**
 * <b>Title:</b>thinkParity XMPP Migrator<br>
 * <b>Description:</b>The migrator remote interface implemenation. Handles all
 * remote method innvocations to the thinkParity server for the migrator model.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @see XMPPCore
 */
final class XMPPMigrator extends AbstractXMPP<MigratorListener> {

    /**
     * Create XMPPMigrator.
     * 
     */
    XMPPMigrator(final XMPPCore xmppCore) {
        super(xmppCore);
    }

    void createProduct(final JabberId userId, final Product product) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("product", product);
        final XMPPMethod createProduct = new XMPPMethod("migrator:createproduct");
        createProduct.setParameter("userId", userId);
        createProduct.setParameter("product", product);
        execute(createProduct);
    }

    void createStream(final JabberId userId, final String streamId,
            final List<Resource> resources) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("streamId", streamId);
        logger.logVariable("resources", resources);
        final XMPPMethod createStream = new XMPPMethod("migrator:createstream");
        createStream.setParameter("userId", userId);
        createStream.setParameter("streamId", streamId);
        createStream.setResourceParameters("resources", resources);
        execute(createStream);
    }

    /**
     * Deploy a release.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param product
     *            A <code>Product</code>.
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
        final XMPPMethod deployRelease = new XMPPMethod("migrator:deploy");
        deployRelease.setParameter("userId", userId);
        deployRelease.setParameter("product", product);
        deployRelease.setParameter("release", release);
        deployRelease.setResourceParameters("resources", resources);
        deployRelease.setParameter("streamId", streamId);
        execute(deployRelease);
    }

    /**
     * Log an error.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param product
     *            A <code>Product</code>.
     * @param error
     *            An <code>Error</code>.
     * @param occuredOn
     *            The error's occured on <code>Calendar</code>.
     */
    void logError(final JabberId userId, final Product product,
            final Error error) {
        final XMPPMethod xmppMethod = new XMPPMethod("migrator:logerror");
        xmppMethod.setParameter("userId", userId);
        xmppMethod.setParameter("product", product);
        xmppMethod.setParameter("error", error);
        execute(xmppMethod);
    }

    /**
     * Read the latest release.
     * 
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    Release readLatestRelease(final JabberId userId,
            final UUID productUniqueId, final OS os) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("productUniqueId", productUniqueId);
        logger.logVariable("os", os);
        final XMPPMethod readRelease = new XMPPMethod("migrator:readlatestrelease");
        readRelease.setParameter("userId", userId);
        readRelease.setParameter("productUniqueId", productUniqueId);
        readRelease.setParameter("os", os);
        return execute(readRelease, Boolean.TRUE).readResultRelease("release");
    }

    /**
     * Read a migrator product.
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
        final XMPPMethod readProduct = new XMPPMethod("migrator:readproduct");
        readProduct.setParameter("userId", userId);
        readProduct.setParameter("name", name);
        return execute(readProduct, Boolean.TRUE).readResultProduct("product");
    }
    
    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readMigratorRelease(com.thinkparity.codebase.jabber.JabberId, java.util.UUID, java.lang.String, com.thinkparity.codebase.OS)
     *
     */
    Release readRelease(final JabberId userId,
            final UUID productUniqueId, final String name, final OS os) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("productUniqueId", productUniqueId);
        logger.logVariable("name", name);
        logger.logVariable("os", os);
        final XMPPMethod readRelease = new XMPPMethod("migrator:readrelease");
        readRelease.setParameter("userId", userId);
        readRelease.setParameter("productUniqueId", productUniqueId);
        readRelease.setParameter("name", name);
        readRelease.setParameter("os", os);
        return execute(readRelease, Boolean.TRUE).readResultRelease("release");
    }

    /**
     * Read migrator release resources.
     * 
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @param name
     *            A release name.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    List<Resource> readResources(final JabberId userId,
            final UUID productUniqueId, final String releaseName, final OS os) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("productUniqueId", productUniqueId);
        logger.logVariable("releaseName", releaseName);
        logger.logVariable("os", os);
        final XMPPMethod readResources = new XMPPMethod("migrator:readresources");
        readResources.setParameter("userId", userId);
        readResources.setParameter("productUniqueId", productUniqueId);
        readResources.setParameter("releaseName", releaseName);
        readResources.setParameter("os", os);
        return execute(readResources, Boolean.TRUE).readResultResources("resources");
    }
}
