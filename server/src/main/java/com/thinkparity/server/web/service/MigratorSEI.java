/*
 * Created On:  3-Jun-07 2:17:24 PM
 */
package com.thinkparity.desdemona.web.service;

import java.util.List;

import javax.jws.WebService;

import com.thinkparity.codebase.OS;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.ModelFactory;
import com.thinkparity.desdemona.model.migrator.MigratorModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.MigratorService;

/**
 * <b>Title:</b>thinkParity Desdemona Migrator Service Endpoint Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.MigratorService")
public class MigratorSEI extends ServiceSEI implements MigratorService {

    /**
     * Create MigratorSEI.
     *
     */
    public MigratorSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.MigratorService#deploy(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release, java.util.List)
     * 
     */
    public void deploy(final AuthToken authToken, final Product product,
            final Release release, final List<Resource> resources) {
        getModel(authToken).deploy(product, release, resources);
    }

    /**
     * @see com.thinkparity.service.MigratorService#logError(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.migrator.Release, com.thinkparity.codebase.model.migrator.Error)
     *
     */
    public void logError(final AuthToken authToken, final Product product,
            final Release release, final Error error) {
        getModel(authToken).logError(product, release, error);
    }

    /**
     * @see com.thinkparity.service.MigratorService#readLatestRelease(com.thinkparity.service.AuthToken, java.lang.String, com.thinkparity.codebase.OS)
     *
     */
    public Release readLatestRelease(final AuthToken authToken,
            final String productName, final OS os) {
        return getModel(authToken).readLatestRelease(productName, os);
    }

    /**
     * @see com.thinkparity.service.MigratorService#readProduct(com.thinkparity.service.AuthToken, java.lang.String)
     *
     */
    public Product readProduct(final AuthToken authToken, final String name) {
        return getModel().readProduct(name);
    }

    /**
     * @see com.thinkparity.service.MigratorService#readProductFeatures(com.thinkparity.service.AuthToken, java.lang.String)
     *
     */
    public List<Feature> readProductFeatures(final AuthToken authToken,
            final String name) {
        if (null == authToken) {
            return getModel().readProductFeatures(name);
        } else {
            return getModel(authToken).readProductFeatures(name);
        }
    }

    /**
     * @see com.thinkparity.service.MigratorService#readRelease(com.thinkparity.service.AuthToken, java.lang.String, java.lang.String, com.thinkparity.codebase.OS)
     *
     */
    public Release readRelease(final AuthToken authToken, final String name,
            final String productName, final OS os) {
        if (null == authToken) {
            return getModel().readRelease(productName, name, os);
        } else {
            return getModel(authToken).readRelease(productName, name, os);
        }
    }

    /**
     * @see com.thinkparity.service.MigratorService#readResources(com.thinkparity.service.AuthToken, java.lang.String, java.lang.String, com.thinkparity.codebase.OS)
     *
     */
    public List<Resource> readResources(final AuthToken authToken,
            final String productName, final String releaseName, final OS os) {
        return getModel(authToken).readResources(productName, releaseName, os);
    }

    /**
     * Obtain a migrator model for an administrative user.
     * 
     * @return An instance of <code>MigratorModel</code>.
     */
    private MigratorModel getModel() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return ModelFactory.getInstance(User.THINKPARITY, loader).getMigratorModel();
    }

    /**
     * Obtain a migrator model for an authenticated session.
     * 
     * @param authToken
     *            A session <code>AuthToken</code>.
     * @return An instance of <code>MigratorModel</code>.
     */
    private MigratorModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getMigratorModel();
    }
}
