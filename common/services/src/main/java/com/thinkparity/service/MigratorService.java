/*
 * Created On:  30-May-07 9:45:59 AM
 */
package com.thinkparity.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.OS;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;

/**
 * <b>Title:</b>thinkParity Migrator Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Migrator")
public interface MigratorService {

    @WebMethod
    void deploy(AuthToken authToken, Product product, Release release,
            List<Resource> resources);

    @WebMethod
    void logError(AuthToken authToken, Product product,
            Release release, Error error);

    @WebMethod
    Release readLatestRelease(AuthToken authToken,
            String productName, OS os);

    @WebMethod
    Product readProduct(AuthToken authToken, String name);

    @WebMethod
    List<Feature> readProductFeatures(AuthToken authToken, String name);

    @WebMethod
    Release readRelease(AuthToken authToken, String name,
            String productName, OS os);

    @WebMethod
    List<Resource> readResources(AuthToken authToken,
            String productName, String releaseName, OS os);
}
