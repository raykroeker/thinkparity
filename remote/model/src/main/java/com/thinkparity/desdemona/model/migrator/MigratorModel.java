/*
 * Created On:  23-Jan-07 5:33:57 PM
 */
package com.thinkparity.desdemona.model.migrator;

import java.util.List;

import com.thinkparity.codebase.OS;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;

import com.thinkparity.desdemona.model.annotation.ThinkParityAuthenticate;
import com.thinkparity.desdemona.util.AuthenticationType;

/**
 * <b>Title:</b>thinkParity Migrator Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityAuthenticate(AuthenticationType.USER)
public interface MigratorModel {

    /**
     * Deploy a release for a model user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     */
    @ThinkParityAuthenticate(AuthenticationType.SYSTEM)
    public void deploy(final Product product, final Release release,
            final List<Resource> resources);

    /**
     * Log an error for a model user.
     * 
     * @param logAs
     *            A <code>User</code> to log the error on behalf of.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param occuredOn
     *            The <code>Calendar</code> the error occured.
     */
    public void logError(final Product product, final Release release,
            final Error error);

    /**
     * Read the latest release for a model user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param productName
     *            A product name <code>String</code>.
     * @return A <code>Release</code>.
     */
    public Release readLatestRelease(final String productName, final OS os);

    /**
     * Read a product for a model user.
     * 
     * @param name
     *            A product name <code>String</code>.
     * @return A <code>Product</code>.
     */
    public Product readProduct(final String name);

    /**
     * Read a list of the product features for a model user.
     * 
     * @param name
     *            A product name <code>String</code>.
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    @ThinkParityAuthenticate(AuthenticationType.SYSTEM)
    public List<Feature> readProductFeatures(final String name);

    /**
     * Read a release for a model user.
     * 
     * @param productName
     *            A product name <code>String</code>.
     * @param name
     *            A release name.
     * @return A <code>Release</code>.
     */
    public Release readRelease(final String productName, final String name,
            final OS os);

    /**
     * Read resources for a model user.
     * 
     * @param productName
     *            A product name <code>String</code>.
     * @param releaseName
     *            A release name.
     * @return A <code>Release</code>.
     */
    public List<Resource> readResources(final String productName,
            final String releaseName, final OS os);
}
