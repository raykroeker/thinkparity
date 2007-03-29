/*
 * Created On:  23-Jan-07 5:33:57 PM
 */
package com.thinkparity.desdemona.model.migrator;

import java.util.List;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;

/**
 * <b>Title:</b>thinkParity Migrator Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface MigratorModel {

    /**
     * Create a stream of the resources.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     */
    public void createStream(final JabberId userId, final String streamId,
            final Product product, final Release release,
            final List<Resource> resources);

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
    public void deploy(final JabberId userId, final Product product,
            final Release release, final List<Resource> resources,
            final String streamId);

    /**
     * Log an error.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param occuredOn
     *            The <code>Calendar</code> the error occured.
     */
    public void logError(final JabberId userId, final Product product,
            final Release release, final Error error);

    /**
     * Read the latest release.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param productName
     *            A product name <code>String</code>.
     * @return A <code>Release</code>.
     */
    public Release readLatestRelease(final JabberId userId,
            final String productName, final OS os);

    /**
     * Read a product.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param name
     *            A product name <code>String</code>.
     * @return A <code>Product</code>.
     */
    public Product readProduct(final JabberId userId, final String name);

    /**
     * Read a release.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param productName
     *            A product name <code>String</code>.
     * @param name
     *            A release name.
     * @return A <code>Release</code>.
     */
    public Release readRelease(final JabberId userId,
            final String productName, final String name, final OS os);

    /**
     * Read a release.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param productName
     *            A product name <code>String</code>.
     * @param releaseName
     *            A release name.
     * @return A <code>Release</code>.
     */
    public List<Resource> readResources(final JabberId userId,
            final String productName, final String releaseName, final OS os);
}
