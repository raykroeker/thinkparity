/*
 * Created On: Sep 11, 2006 4:06:57 PM
 */
package com.thinkparity.desdemona.model.admin;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.admin.derby.DerbyModelImpl;
import com.thinkparity.desdemona.model.admin.derby.InternalDerbyModel;
import com.thinkparity.desdemona.model.admin.message.InternalMessageModel;
import com.thinkparity.desdemona.model.admin.message.MessageModelImpl;
import com.thinkparity.desdemona.model.admin.report.InternalReportModel;
import com.thinkparity.desdemona.model.admin.report.ReportModelImpl;
import com.thinkparity.desdemona.model.admin.user.AdminUserModelImpl;
import com.thinkparity.desdemona.model.admin.user.InternalAdminUserModel;

/**
 * <b>Title:</b>thinkParity Desdemona Admin Internal Model Factory<br>
 * <b>Description:</b>An internal model factory is used by the model to
 * generate references to interal models outside the scope of the model impl
 * classes; usually to pass off to helper command pattern objects.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class InternalAdminModelFactory {

    /**
     * Obtain an instance of <code>InternalModelFactory</code>.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @return A <code>InternalModelFactory</code>.
     */
    public static InternalAdminModelFactory getInstance(final Context context,
            final User user) {
        return new InternalAdminModelFactory(context, user);
    }

    /** A <code>ClassLoader</code>. */
    private final ClassLoader classLoader;

    /** A <code>User</code>. */
    private final User user;

    /**
     * Create InternalModelFactory.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @param user
     *            A <code>User</code>.
     */
    private InternalAdminModelFactory(final Context context, final User user) {
        super();
        this.user = user;
        this.classLoader = context.getClass().getClassLoader();
    }

    /**
     * Obtain an internal derby model.
     * 
     * @return An instance of <code>InternalDerbyModel</code>.
     */
    public final InternalDerbyModel newDerbyModel() {
        return (InternalDerbyModel) newProxy(InternalDerbyModel.class,
                DerbyModelImpl.class);
    }

    /**
     * Obtain an internal message model.
     * 
     * @return An instance of <code>InternalMessageModel</code>.
     */
    public final InternalMessageModel newMessageModel() {
        return (InternalMessageModel) newProxy(InternalMessageModel.class,
                MessageModelImpl.class);
    }

    /**
     * Obtain an internal report model.
     * 
     * @return An instance of <code>InternalReportModel</code>.
     */
    public final InternalReportModel newReportModel() {
        return (InternalReportModel) newProxy(InternalReportModel.class,
                ReportModelImpl.class);
    }

    /**
     * Obtain an internal user model.
     * 
     * @return An instance of <code>InternalAdminUserModel</code>.
     */
    public final InternalAdminUserModel newUserModel() {
        return (InternalAdminUserModel) newProxy(InternalAdminUserModel.class,
                AdminUserModelImpl.class);
    }

    /**
     * Create a proxy for a thinkParity model.
     * 
     * @param modelInterface
     *            A thinkParity model interface.
     * @param modelImplementation
     *            A thinkParity model implementation.
     * @return A new proxy instance <code>Object</code>.
     */
    private <T extends Object> Object newProxy(final Class<T> proxyInterface,
            final Class<? extends T> proxyImplementation) {
        return AdminModelFactory.newProxy(user, classLoader, proxyInterface,
                proxyImplementation);
    }
}
