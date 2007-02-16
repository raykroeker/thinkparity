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
     * Create an installed release.
     * 
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     */
    public void createInstalledRelease(final Release release,
            final List<Resource> resources);

    /**
     * Create a latest release.
     * 
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     */
    public void createLatestRelease(final Release release,
            final List<Resource> resources);

    /**
     * Create the installed product.
     * 
     * @param product
     *            A <code>Product</code>.
     */
    public void createProduct(final Product product);

    /**
     * Delete the latest release.
     *
     */
    public void deleteLatestRelease();

    /**
     * Read the installed release.
     * 
     * @return A <code>Release</code>.
     */
    public Release readInstalledRelease();

    /**
     * Read the installed resources.
     * 
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    public List<Resource> readInstalledResources();

    /**
     * Read the latest release.
     * 
     * @return A <code>Release</code>.
     */
    public Release readLatestRelease();

    /**
     * Read the latest resources.
     * 
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    public List<Resource> readLatestResources();

    /**
     * Read the product.
     * 
     * @return A <code>Product</code>.
     */
    public Product readProduct();
}
