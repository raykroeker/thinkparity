/*
 * Created On:  9-Jan-07 1:03:56 PM
 */
package com.thinkparity.desdemona.model;

import java.lang.reflect.Proxy;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.artifact.ArtifactModelImpl;
import com.thinkparity.desdemona.model.backup.BackupModel;
import com.thinkparity.desdemona.model.backup.BackupModelImpl;
import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.model.contact.ContactModelImpl;
import com.thinkparity.desdemona.model.container.ContainerModel;
import com.thinkparity.desdemona.model.container.ContainerModelImpl;
import com.thinkparity.desdemona.model.crypto.CryptoModel;
import com.thinkparity.desdemona.model.crypto.CryptoModelImpl;
import com.thinkparity.desdemona.model.migrator.MigratorModel;
import com.thinkparity.desdemona.model.migrator.MigratorModelImpl;
import com.thinkparity.desdemona.model.profile.ProfileModel;
import com.thinkparity.desdemona.model.profile.ProfileModelImpl;
import com.thinkparity.desdemona.model.queue.QueueModel;
import com.thinkparity.desdemona.model.queue.QueueModelImpl;
import com.thinkparity.desdemona.model.rules.RuleModel;
import com.thinkparity.desdemona.model.rules.RuleModelImpl;
import com.thinkparity.desdemona.model.session.SessionModel;
import com.thinkparity.desdemona.model.session.SessionModelImpl;
import com.thinkparity.desdemona.model.stream.StreamModel;
import com.thinkparity.desdemona.model.stream.StreamModelImpl;
import com.thinkparity.desdemona.model.system.SystemModel;
import com.thinkparity.desdemona.model.system.SystemModelImpl;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.model.user.UserModelImpl;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ModelFactory {

    /**
     * Obtain an instance of <code>ModelFactory</code>.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @return A <code>ModelFactory</code>.
     */
    public static ModelFactory getInstance(final ClassLoader loader) {
        return new ModelFactory(loader);
    }

    /**
     * Obtain an instance of <code>ModelFactory</code>.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @return A <code>ModelFactory</code>.
     */
    public static ModelFactory getInstance(final User user, final ClassLoader loader) {
        return new ModelFactory(user, loader);
    }

    /**
     * Create a proxy for a thinkParity model.
     * 
     * @param session
     *            A user <code>Session</code>.
     * @param classLoader
     *            A <code>ClassLoader</code>.
     * @param modelInterface
     *            A model interface <code>Class</code>.
     * @param modelImplementation
     *            A model implementation <code>Class</code>.
     * @return An instance of the model.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    static Object newModelProxy(final User user, final ClassLoader loader,
            final Class<?> modelInterface, final Class<?> modelImplementation) {
        try {
            final AbstractModelImpl modelInstance =
                (AbstractModelImpl) modelImplementation.newInstance();
            modelInstance.setUser(user);
            modelInstance.initialize();
            return Proxy.newProxyInstance(loader,
                    new Class<?>[] { modelInterface },
                    new ModelInvocationHandler(modelInstance));
        } catch (final Exception x) {
            throw new ThinkParityException("Cannot instantiate model.", x);
        }
    }

    /** The proxy <code>ClassLoader</code>. */
    private final ClassLoader loader;

    /** The model user. */
    private final User user;

    /**
     * Create ModelFactory.
     *
     */
    private ModelFactory(final ClassLoader loader) {
        this(null, loader);
    }

    /**
     * Create ModelFactory.
     *
     */
    private ModelFactory(final User user, final ClassLoader loader) {
        super();
        this.user = user;
        this.loader = loader;
    }

    /**
     * Obtain an artifact model.
     * 
     * @return An instance of <code>ArtifactModel</code>.
     */
    public final ArtifactModel getArtifactModel() {
        return (ArtifactModel) newModelProxy(
                ArtifactModel.class, ArtifactModelImpl.class);
    }

    /**
     * Obtain a backup model.
     * 
     * @return An instance of <code>BackupModel</code>.
     */
    public final BackupModel getBackupModel() {
        return (BackupModel) newModelProxy(
                BackupModel.class, BackupModelImpl.class);
    }

    /**
     * Obtain a contact model.
     * 
     * @return An instance of <code>ContactModel</code>.
     */
    public final ContactModel getContactModel() {
        return (ContactModel) newModelProxy(
                ContactModel.class, ContactModelImpl.class);
    }

    /**
     * Obtain a container model.
     * 
     * @return An instance of <code>ContainerModel</code>.
     */
    public final ContainerModel getContainerModel() {
        return (ContainerModel) newModelProxy(
                ContainerModel.class, ContainerModelImpl.class);
    }

    /**
     * Obtain a crypto model.
     * 
     * @return An instance of <code>CryptoModel</code>.
     */
    public final CryptoModel getCryptoModel() {
        return (CryptoModel) newModelProxy(
                CryptoModel.class, CryptoModelImpl.class);
    }

    /**
     * Obtain a migrator model.
     * 
     * @return An instance of <code>MigratorModel</code>.
     */
    public final MigratorModel getMigratorModel() {
        return (MigratorModel) newModelProxy(
                MigratorModel.class, MigratorModelImpl.class);
    }

    /**
     * Obtain a profile model.
     * 
     * @return An instance of <code>ProfileModel</code>.
     */
    public final ProfileModel getProfileModel() {
        return (ProfileModel) newModelProxy(
                ProfileModel.class, ProfileModelImpl.class);
    }

    /**
     * Obtain a queue model.
     * 
     * @return An instance of <code>QueueModel</code>.
     */
    public final QueueModel getQueueModel() {
        return (QueueModel) newModelProxy(QueueModel.class, QueueModelImpl.class);
    }

    /**
     * Obtain a rule model.
     * 
     * @return An instance of <code>RuleModel</code>.
     */
    public final RuleModel getRuleModel() {
        return (RuleModel) newModelProxy(
                RuleModel.class, RuleModelImpl.class);
    }

    /**
     * Obtain a session model.
     * 
     * @return An instance of <code>SessionModel</code>.
     */
    public final SessionModel getSessionModel() {
        return (SessionModel) newModelProxy(
                SessionModel.class, SessionModelImpl.class);
    }

    /**
     * Obtain an stream model.
     * 
     * @return An instance of <code>StreamModel</code>.
     */
    public final StreamModel getStreamModel() {
        return (StreamModel) newModelProxy(StreamModel.class, StreamModelImpl.class);
    }

    /**
     * Obtain a system model.
     * 
     * @return An instance of <code>SystemModel</code>.
     */
    public final SystemModel getSystemModel() {
        return (SystemModel) newModelProxy(
                SystemModel.class, SystemModelImpl.class);
    }

    /**
     * Obtain an user model.
     * 
     * @return An instance of <code>UserModel</code>.
     */
    public final UserModel getUserModel() {
        return (UserModel) newModelProxy(
                UserModel.class, UserModelImpl.class);
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
    private Object newModelProxy(final Class<?> modelInterface,
            final Class<?> modelImplementation) {
        return newModelProxy(user, loader, modelInterface, modelImplementation);
    }
}
