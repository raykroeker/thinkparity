/*
 * Created On:  25-Jul-07 1:31:03 PM
 */
package com.thinkparity.ophelia.model.crypto;

import com.thinkparity.codebase.event.EventListener;

import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.CryptoService;
import com.thinkparity.service.ServiceFactory;

/**
 * <b>Title:</b>thinkParity Ophelia Model Encryption Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CryptoModelImpl extends Model<EventListener> implements CryptoModel,
        InternalCryptoModel {

    /** The encryption web-service. */
    private CryptoService cryptoService;

    /**
     * Create CryptoModelImpl.
     *
     */
    public CryptoModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.crypto.InternalCryptoModel#createSecret(com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public Secret createSecret(final DocumentVersion version) {
        try {
            return cryptoService.createSecret(getAuthToken(), version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.crypto.InternalCryptoModel#readSecret(com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public Secret readSecret(final DocumentVersion version) {
        try {
            return cryptoService.readSecret(getAuthToken(), version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.crypto.InternalCryptoModel#doesExistSecret(com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public Boolean doesExistSecret(final DocumentVersion version) {
        try {
            return cryptoService.doesExistSecret(getAuthToken(), version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        final ServiceFactory serviceFactory = workspace.getServiceFactory(environment);
        this.cryptoService = serviceFactory.getCryptoService();
    }

    /**
     * Obtain the authentication token.
     * 
     * @return An <code>AuthToken</code>.
     */
    private AuthToken getAuthToken() {
        return getSessionModel().getAuthToken();
    }
}
