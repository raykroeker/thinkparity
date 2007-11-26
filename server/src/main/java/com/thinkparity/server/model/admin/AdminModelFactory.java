/*
 * Created On:  9-Oct-07 12:29:18 PM
 */
package com.thinkparity.desdemona.model.admin;

import java.lang.reflect.Proxy;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.admin.derby.DerbyModel;
import com.thinkparity.desdemona.model.admin.derby.DerbyModelImpl;
import com.thinkparity.desdemona.model.admin.message.MessageModel;
import com.thinkparity.desdemona.model.admin.message.MessageModelImpl;
import com.thinkparity.desdemona.model.admin.report.ReportModel;
import com.thinkparity.desdemona.model.admin.report.ReportModelImpl;
import com.thinkparity.desdemona.model.admin.user.AdminUserModel;
import com.thinkparity.desdemona.model.admin.user.AdminUserModelImpl;

/**
 * <b>Title:</b>thinkParity Desdemona Admin Model Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AdminModelFactory {

    /**
     * Obtain an instance of an admin model factory.
     * 
     * @param user
     *            A <code>User</code>.
     * @param loader
     *            A <code>ClassLoader</code>.
     * @return An <code>AdminModelFactory</code>.
     */
    public static AdminModelFactory getInstance(final User user,
            final ClassLoader loader) {
        return new AdminModelFactory(user, loader);
    }

    /**
     * Instantiate a proxy to the interface via the implementation.
     * 
     * @param <T>
     *            An <code>Object</code>.
     * @param user
     *            A <code>User</code>.
     * @param loader
     *            A <code>ClassLoader</code>.
     * @param proxyInterface
     *            A <code>Class<T></code>.
     * @param proxyImplementation
     *            A <code>Class<? extends T</code>.
     * @return An <code>Object</code>.
     */
    static <T extends Object> Object newProxy(final User user,
            final ClassLoader loader, final Class<T> proxyInterface,
            final Class<? extends T> proxyImplementation) {
        try {
            final AdminModel model = (AdminModel) proxyImplementation.newInstance();
            model.setUser(user);
            model.initialize();
            return Proxy.newProxyInstance(loader,
                    new Class<?>[] { proxyInterface },
                    new AdminInvocationHandler(model));
        } catch (final InstantiationException ix) {
            throw new ThinkParityException(ix);
        } catch (final IllegalAccessException iax) {
            throw new ThinkParityException(iax);
        }
    }

    /** A class loader. */
    private final ClassLoader loader;

    /** A user. */
    private final User user;

    /**
     * Create AdminModelFactory.
     *
     */
    private AdminModelFactory(final User user, final ClassLoader loader) {
        super();
        this.user = user;
        this.loader = loader;
    }

    /**
     * Instantiate a derby model.
     * 
     * @return A <code>DerbyModel</code>.
     */
    public final DerbyModel newDerbyModel() {
        return (DerbyModel) newProxy(DerbyModel.class, DerbyModelImpl.class);
    }

    /**
     * Instantiate a message model.
     * 
     * @return A <code>MessageModel</code>.
     */
    public final MessageModel newMessageModel() {
        return (MessageModel) newProxy(MessageModel.class,
                MessageModelImpl.class);
    }

    /**
     * Instantiate a report model.
     * 
     * @return A <code>ReportModel</code>.
     */
    public final ReportModel newReportModel() {
        return (ReportModel) newProxy(ReportModel.class, ReportModelImpl.class);
    }

    /**
     * Instantiate a user model.
     * 
     * @return An <code>AdminUserModel</code>.
     */
    public AdminUserModel newUserModel() {
        return (AdminUserModel) newProxy(AdminUserModel.class,
                AdminUserModelImpl.class);
    }

    /**
     * Instantiate a proxy to the interface via the implementation.
     * 
     * @param <T>
     *            An <code>Object</code>.
     * @param proxyInterface
     *            A <code>Class<T></code>.
     * @param proxyImplementation
     *            A <code>Class<? extends T</code>.
     * @return An <code>Object</code>.
     */
    private <T extends Object> Object newProxy(final Class<T> proxyInterface,
            final Class<? extends T> proxyImplementation) {
        return newProxy(user, loader, proxyInterface, proxyImplementation);
    }
}
