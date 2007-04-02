/*
 * Created On:  2007-02-01 14:11
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;

/**
 * <b>Title:</b>thinkParity Migrator IO Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface MigratorIOHandler {

    /**
     * Create the product.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param releaseResources
     *            A <code>List</code> of <code>Resource</code>s.
     */
    public void createProduct(final Product product,
            final Release release,
            final List<Resource> releaseResources);

    /**
     * Create the release.
     * 
     * @param release
     *            A <code>Release</code>.
     * @param releaseResources
     *            A <code>List</code> of <code>Resource</code>s.
     */
    public void createRelease(final Release release,
            final List<Resource> releaseResources);

    /**
     * Delete a release.
     * 
     * @param release
     *            A <code>Release</code>.
     */
    public void delete(final Release release);

    /**
     * Determine if the product exists.
     * 
     * @param name
     *            The product name <code>String</code>.
     * @return True if the product has been initialized.
     */
    public Boolean doesExistProduct(final String name);

    /**
     * Determine if the release has been initialized.
     * 
     * @param release
     *            A <code>Release</code>.
     * @return True if the release has been initialized.
     */
    public Boolean isReleaseInitialized(final Release release);

    /**
     * Read the installed release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @return A <code>Release</code>.
     */
    public Release readInstalledRelease(final Product product);

    /**
     * Read the installed resources for a product.
     * 
     * @param product
     *            A <code>Product</code>.
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    public List<Resource> readInstalledResources(final Product product);

    /**
     * Read the latest release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @return A <code>Release</code>.
     */
    public Release readLatestRelease(final Product product);

    /**
     * Read the latest resources for a product.
     * 
     * @param product
     *            A <code>Product</code>.
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    public List<Resource> readLatestResources(final Product product);

    /**
     * Read the previous release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @return A <code>Release</code>.
     */
    public Release readPreviousRelease(final Product product);

    /**
     * Read the product.
     * 
     * @param name
     *            A product name <code>String</code>.
     * @return A <code>Product</code>.
     */
    public Product readProduct(final String name);

    /**
     * Read all releases.
     * 
     * @return A <code>List</code> of <code>Release</code>s.
     */
    public List<Release> readReleases();

    /**
     * Update the installed release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     */
    public void updateInstalledRelease(final Product product,
            final Release release);

    /**
     * Update the latest release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     */
    public void updateLatestRelease(final Product product, final Release release);

    /**
     * Update the previous release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     */
    public void updatePreviousRelease(final Product product,
            final Release release);

    /**
     * Update the release initialization.
     * 
     * @param release
     *            A <code>Release</code>.
     */
    public void updateReleaseInitialization(final Release release);
}
