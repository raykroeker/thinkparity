/*
 * Created On:  28-Feb-07 8:33:01 PM
 */
package com.thinkparity.desdemona.model.user;

import java.util.List;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity Desdemona Internal User Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalUserModel extends UserModel {

    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return A <code>User</code>.
     */
    public User read(final Long userId);

    /**
     * Read the product features for the model user.
     * 
     * @param productId
     *            A product id <code>Long</code>.
     * @return A <code>List&lt;Feature&gt</code>.
     */
    public List<Feature> readFeatures(final Long productId);

    /**
     * Determine if a user is referencing the product/release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return True if it is still referenced.
     */
    Boolean doesExistUser(Product product, Release release);
}
