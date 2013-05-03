/*
 * Created On:  13-Nov-07 1:26:07 PM
 */
package com.thinkparity.desdemona.model.admin.user;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.session.Configuration;
import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity Desdemona Admin User Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface AdminUserModel {

    /**
     * Update a user's product configuration.
     * 
     * @param user
     *            A <code>User</code>.
     * @param product
     *            A <code>Product</code>.
     * @param configuration
     *            A <code>Configuration</code>.
     */
    void updateProductConfiguration(User user, Product product,
            Configuration configuration);
}
