/*
 * Created On:  2007-02-01 14:11
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

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
     * @param name
     *            A product name <code>String</code>.
     * @param releaseName
     *            A release name <code>String</code>.
     */
    public void createProduct(final String name, final String releaseName);

    /**
     * Delete the current downloaded release.
     * 
     * @param name
     *            A product name <code>String</code>.
     */
    public void deleteDownloadedProductRelease(final String name);

    /**
     * Execute sql.
     * 
     * @param sql
     *            A <code>List</code> of sql statement <code>String</code>s
     *            to execute.
     */
    public void execute(final List<String> sql);

    /**
     * Read the downloaded product release.
     * 
     * @param name
     *            A product name <code>String</code>.
     * @return A release name <code>String</code>.
     */
    public String readDownloadedProductRelease(final String name);

    /**
     * Read the product release.
     * 
     * @param name
     *            A product name <code>String</code>.
     * @return A release name <code>String</code>.
     */
    public String readProductRelease(final String name);

    /**
     * Set the current downloaded release.
     * 
     * @param name
     *            A product name <code>String</code>.
     * @param releaseName
     *            A release name <code>String</code>.
     */
    public void updateDownloadedProductRelease(final String name,
            final String releaseName);

    /**
     * Set the current product release.
     * 
     * @param name
     *            A product name <code>String</code>.
     * @param releaseName
     *            A release name <code>String</code>.
     */
    public void updateProductRelease(final String name, final String releaseName);
}
