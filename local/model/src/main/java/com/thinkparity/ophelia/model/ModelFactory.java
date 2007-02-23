/*
 * Created On:  9-Jan-07 1:03:56 PM
 */
package com.thinkparity.ophelia.model;

import java.lang.reflect.Proxy;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.artifact.ArtifactModelImpl;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.contact.ContactModelImpl;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.ContainerModelImpl;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.document.DocumentModelImpl;
import com.thinkparity.ophelia.model.index.IndexModel;
import com.thinkparity.ophelia.model.index.IndexModelImpl;
import com.thinkparity.ophelia.model.migrator.MigratorModel;
import com.thinkparity.ophelia.model.migrator.MigratorModelImpl;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.profile.ProfileModelImpl;
import com.thinkparity.ophelia.model.script.ScriptModel;
import com.thinkparity.ophelia.model.script.ScriptModelImpl;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.session.SessionModelImpl;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.user.UserModelImpl;
import com.thinkparity.ophelia.model.workspace.Workspace;

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
    public static ModelFactory getInstance(final Environment environment,
            final Workspace workspace) {
        return new ModelFactory(environment, workspace);
    }

    /**
     * Create a proxy for a thinkParity model.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
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
    static Object newModelProxy(final Environment environment,
            final Workspace workspace, final ClassLoader classLoader,
            final Class<?> modelInterface, final Class<?> modelImplementation) {
        try {
            final Model modelInstance = (Model) modelImplementation.newInstance();
            modelInstance.initialize(environment, workspace);
            modelInstance.initializeModel(environment, workspace);
            return Proxy.newProxyInstance(classLoader,
                    new Class<?>[] { modelInterface },
                    new ModelInvocationHandler(workspace, modelInstance));
        } catch (final Exception x) {
            throw new ThinkParityException("Cannot instantiate model.", x);
        }
    }

    /** The proxy <code>ClassLoader</code>. */
    private final ClassLoader classLoader;

    /** A thinkParity <code>Environment</code>. */
    private final Environment environment;

    /** A thinkParity <code>Workspace</code>. */
    private final Workspace workspace;

    /**
     * Create ModelFactory.
     *
     */
    private ModelFactory(final Environment environment, final Workspace workspace) {
        super();
        this.environment = environment;
        this.workspace = workspace;
        this.classLoader = workspace.getClass().getClassLoader();
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
     * Obtain a contact model.
     * 
     * @return An instance of <code>ContactModel</code>.
     */
    public final ContactModel getContactModel() {
        return (ContactModel) newModelProxy(
                ContactModel.class, ContactModelImpl.class);
    }

    /**
     * Obtain an container model.
     * 
     * @return An instance of <code>ContainerModel</code>.
     */
    public final ContainerModel getContainerModel() {
        return (ContainerModel) newModelProxy(
                ContainerModel.class, ContainerModelImpl.class);
    }

    /**
     * Obtain a document model.
     * 
     * @return An instance of <code>DocumentModel</code>.
     */
    public final DocumentModel getDocumentModel() {
        return (DocumentModel) newModelProxy(
                DocumentModel.class, DocumentModelImpl.class);
    }

    /**
     * Obtain an index model.
     * 
     * @return An instance of <code>IndexModel</code>.
     */
    public final IndexModel getIndexModel() {
        return (IndexModel) newModelProxy(
                IndexModel.class, IndexModelImpl.class);
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
     * Obtain a script model.
     * 
     * @return An instance of <code>ScriptModel</code>.
     */
    public final ScriptModel getScriptModel() {
        return (ScriptModel) newModelProxy(
                ScriptModel.class, ScriptModelImpl.class);
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
     * Obtain a user model.
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
        return newModelProxy(environment, workspace, classLoader,
                modelInterface, modelImplementation);
    }
}
