/*
 * Created On:  13-Nov-07 1:26:29 PM
 */
package com.thinkparity.desdemona.model.admin.user;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.session.Configuration;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.admin.AdminModel;

/**
 * <b>Title:</b>thinkParity Desdemona Admin User Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AdminUserModelImpl extends AdminModel implements
        AdminUserModel, InternalAdminUserModel {

    /**
     * Create AdminUserModelImpl.
     *
     */
    public AdminUserModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.user.AdminUserModel#updateProductConfiguration(com.thinkparity.codebase.model.user.User, com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.session.Configuration)
     *
     */
    @Override
    public void updateProductConfiguration(User user, Product product,
            Configuration configuration) {
        try {
            getMigratorModel(user).updateProductConfiguration(product,
                    configuration);
        } catch (final Exception x) {
            throw panic(x);
        }
    }
}
