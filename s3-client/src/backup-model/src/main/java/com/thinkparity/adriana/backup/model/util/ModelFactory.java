/*
 * Created On:  29-Sep-07 5:02:26 PM
 */
package com.thinkparity.adriana.backup.model.util;

import java.lang.reflect.Proxy;

import com.thinkparity.adriana.backup.model.ModelRuntimeException;
import com.thinkparity.adriana.backup.model.ModelRuntimeException.Code;
import com.thinkparity.adriana.backup.model.backup.BackupModel;
import com.thinkparity.adriana.backup.model.backup.BackupModelImpl;
import com.thinkparity.adriana.backup.model.command.CommandModel;
import com.thinkparity.adriana.backup.model.command.CommandModelImpl;
import com.thinkparity.adriana.backup.model.notify.NotifyModel;
import com.thinkparity.adriana.backup.model.notify.NotifyModelImpl;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ModelFactory {

    /** An instance of the model factory. */
    private static ModelFactory instance;

    /**
     * Obtain an instance of the model factory.
     * 
     * @return A <code>ModelFactory</code>.
     */
    public static ModelFactory getInstance() {
        if (null == instance) {
            instance = new ModelFactory();
        }
        return instance;
    }

    /**
     * Instantiate a model proxy.
     * 
     * @param loader
     *            A <code>ClassLoader</code>.
     * @param model
     *            A <code>Class<?></code>.
     * @param impl
     *            A <code>Class<?></code>.
     * @return An <code>Object</code>.
     */
    private static Object newModel(final ClassLoader loader,
            final Class<?> model, final Class<?> impl) {
        try {
            return Proxy.newProxyInstance(loader, new Class<?>[] { model },
                    new ModelInvocationHandler(impl.newInstance()));
        } catch (final InstantiationException ix) {
            throw new ModelRuntimeException(Code.MODEL_INSTANTIATION, ix);
        } catch (final IllegalAccessException iax) {
            throw new ModelRuntimeException(Code.MODEL_INSTANTIATION, iax);
        }
    }

    /** A proxy class loader. */
    private final ClassLoader proxyLoader;

    /**
     * Create ModelFactory.
     *
     */
    private ModelFactory() {
        super();
        this.proxyLoader = Thread.currentThread().getContextClassLoader();
    }

    /**
     * Obtain an instance of a backup model.
     * 
     * @return A <code>BackupModel</code>.
     */
    public BackupModel getBackupModel() {
        return (BackupModel) newModel(BackupModel.class, BackupModelImpl.class);
    }

    /**
     * Obtain an instance of a command model.
     * 
     * @return A <code>CommandModel</code>.
     */
    public CommandModel getCommandModel() {
        return (CommandModel) newModel(CommandModel.class, CommandModelImpl.class);
    }

    /**
     * Obtain an instance of a notify model.
     * 
     * @return A <code>NotifyModel</code>.
     */
    public NotifyModel getNotifyModel() {
        return (NotifyModel) newModel(NotifyModel.class, NotifyModelImpl.class);
    }

    /**
     * Instantiate a model proxy.
     * 
     * @param model
     *            A <code>Class<?></code>.
     * @param impl
     *            A <code>Class<?></code>.
     * @return An <code>Object</code>.
     */
    private Object newModel(final Class<?> model, final Class<?> impl) {
        return newModel(proxyLoader, model, impl);
    }
}
