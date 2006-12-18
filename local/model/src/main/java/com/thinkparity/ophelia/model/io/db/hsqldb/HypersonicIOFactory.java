/*
 * Created On: Feb 6, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.db.hsqldb.handler.AbstractIOHandler;
import com.thinkparity.ophelia.model.io.db.hsqldb.util.HypersonicValidator;
import com.thinkparity.ophelia.model.io.handler.*;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicIOFactory extends IOFactory {

	/**
     * Create a HypersonicIOFactory [Concrete Factory]
     * 
     */
    public HypersonicIOFactory(final Workspace workspace) {
        super(workspace);
        new HypersonicValidator(workspace).validate();
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createArtifactHandler()
     * 
     */
    @Override
    public ArtifactIOHandler createArtifactHandler() {
        return (ArtifactIOHandler) createHandler("ArtifactIOHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createAuditHandler()
     * 
     */
    @Override
    public AuditIOHandler createAuditHandler() {
        return (AuditIOHandler) createHandler("AuditIOHandler");
    }

    /** @see com.thinkparity.ophelia.model.io.IOFactory#createConfigurationHandler() */
    @Override
    public ConfigurationIOHandler createConfigurationHandler() {
        return (ConfigurationIOHandler) createHandler("ConfigurationIOHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createContactHandler()
     */
    @Override
    public ContactIOHandler createContactHandler() {
        return (ContactIOHandler) createHandler("ContactIOHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createContainerHandler()
     * 
     */
    @Override
    public ContainerIOHandler createContainerHandler() {
        return (ContainerIOHandler) createHandler("ContainerIOHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createDocumentHandler()
     * 
     */
    @Override
    public DocumentIOHandler createDocumentHandler() {
        return (DocumentIOHandler) createHandler("DocumentIOHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createDocumentHistoryIOHandler()
     * 
     */
    @Override
    public DocumentHistoryIOHandler createDocumentHistoryIOHandler() {
        throw Assert
                .createNotYetImplemented("HypersonicIOFactory#createDocumentHistoryIOHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createLibraryHandler()
     */
    @Override
    public LibraryIOHandler createLibraryHandler() {
        throw Assert
                .createNotYetImplemented("HypersonicIOFactory#createLibraryHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createProfileHandler()
     */
    @Override
    public ProfileIOHandler createProfileHandler() {
        return (ProfileIOHandler) createHandler("ProfileIOHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createReleaseHandler()
     */
    @Override
    public ReleaseIOHandler createReleaseHandler() {
        throw Assert
                .createNotYetImplemented("HypersonicIOFactory#createReleaseHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createSystemMessageHandler()
     * 
     */
    @Override
    public SystemMessageIOHandler createSystemMessageHandler() {
        return (SystemMessageIOHandler) createHandler("SystemMessageIOHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#createUserIOHandler()
     * 
     */
    @Override
    public UserIOHandler createUserIOHandler() {
        return (UserIOHandler) createHandler("UserIOHandler");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.IOFactory#initialize()
     * 
     */
    @Override
    public void initialize() {}

    /**
     * Create the handler.
     * 
     * @param simpleName
     *            The handler class' simple name.
     * @return The <code>AbstractIOHandler</code>.
     */
    private AbstractIOHandler createHandler(final String simpleName) {
        try {
            final String className =
                    new StringBuffer("com.thinkparity.ophelia.model.io.db.hsqldb.handler.")
                    .append(simpleName).toString();
            final Class<?> handlerClass = Class.forName(className);
            final Class[] paramTypes = new Class[] { SessionManager.class };
            final Object[] params = new Object[] { workspace.getSessionManager() };
            final Constructor constructor = handlerClass.getConstructor(paramTypes);
            final AbstractIOHandler handler =
                (AbstractIOHandler) constructor.newInstance(params);
            return handler;
        } catch (final ClassNotFoundException cnfx) {
            throw new HypersonicException(cnfx);            
        } catch (final IllegalAccessException iax) {
            throw new HypersonicException(iax);
        } catch (final InstantiationException ix) {
            throw new HypersonicException(ix);
        } catch (final InvocationTargetException itx) {
            throw new HypersonicException(itx);
        } catch (final NoSuchMethodException nsmx) {
            throw new HypersonicException(nsmx);
        }
    }
}
