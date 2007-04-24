/*
 * Created On: Sep 11, 2006 4:06:57 PM
 */
package com.thinkparity.ophelia.model;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.artifact.ArtifactModelImpl;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerModelImpl;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModelImpl;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.index.IndexModelImpl;
import com.thinkparity.ophelia.model.index.InternalIndexModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.UserModelImpl;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Internal Model Factory<br>
 * <b>Description:</b>An internal model factory is used by the model to
 * generate references to interal models outside the scope of the model impl
 * classes; usually to pass off to helper command pattern objects.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class InternalModelFactory {

    /**
     * Obtain an instance of <code>InternalModelFactory</code>.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @return A <code>InternalModelFactory</code>.
     */
    public static InternalModelFactory getInstance(final Context context,
            final Environment environment, final Workspace workspace) {
        return new InternalModelFactory(context, environment, workspace);
    }

    /** A <code>ClassLoader</code>. */
    private final ClassLoader classLoader;

    /** A thinkParity <code>Environment</code>. */
    private final Environment environment;

    /** A thinkParity <code>Workspace</code>. */
    private final Workspace workspace;

    /**
     * Create InternalModelFactory.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
    private InternalModelFactory(final Context context,
            final Environment environment, final Workspace workspace) {
        super();
        this.classLoader = workspace.getClass().getClassLoader();
        this.environment = environment;
        this.workspace = workspace;
    }

    /**
     * Obtain an internal artifact model.
     * 
     * @return An instance of <code>InternalArtifactModel</code>.
     */
    public final InternalArtifactModel getArtifactModel() {
        return (InternalArtifactModel) newModelProxy(
                InternalArtifactModel.class, ArtifactModelImpl.class);
    }

    /**
     * Obtain an internal container model.
     * 
     * @return An instance of <code>InternalContainerModel</code>.
     */
    public final InternalContainerModel getContainerModel() {
        return (InternalContainerModel) newModelProxy(
                InternalContainerModel.class, ContainerModelImpl.class);
    }

    /**
     * Obtain an internal document model.
     * 
     * @return An instance of <code>InternalDocumentModel</code>.
     */
    public final InternalDocumentModel getDocumentModel() {
        return (InternalDocumentModel) newModelProxy(
                InternalDocumentModel.class, DocumentModelImpl.class);
    }

    /**
     * Obtain an internal index model.
     * 
     * @return An instance of <code>InternalIndexModel</code>.
     */
    public final InternalIndexModel getIndexModel() {
        return (InternalIndexModel) newModelProxy(
                InternalIndexModel.class, IndexModelImpl.class);
    }

    /**
     * Obtain an internal user model.
     * 
     * @return An instance of <code>InternalUserModel</code>.
     */
    public final InternalUserModel getUserModel() {
        return (InternalUserModel) newModelProxy(
                InternalUserModel.class, UserModelImpl.class);
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
        return ModelFactory.newModelProxy(environment, workspace, classLoader,
                modelInterface, modelImplementation);
    }
}
