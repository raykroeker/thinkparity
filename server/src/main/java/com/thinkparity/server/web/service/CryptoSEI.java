/*
 * Created On:  3-Jun-07 1:45:01 PM
 */
package com.thinkparity.desdemona.web.service;

import javax.jws.WebService;

import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.desdemona.model.crypto.CryptoModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.CryptoService;

/**
 * <b>Title:</b>thinkParity Desdemona Container Service Endpoint Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.CryptoService")
public class CryptoSEI extends ServiceSEI implements CryptoService {

    /**
     * Create CryptoSEI.
     *
     */
    public CryptoSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.CryptoService#createSecret(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public Secret createSecret(final AuthToken authToken,
            final DocumentVersion version) {
        return getModel(authToken).createSecret(version);
    }

    /**
     * @see com.thinkparity.service.CryptoService#doesExistSecret(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public Boolean doesExistSecret(final AuthToken authToken,
            final DocumentVersion version) {
        return getModel(authToken).doesExistSecret(version);
    }

    /**
     * @see com.thinkparity.service.CryptoService#readSecret(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public Secret readSecret(final AuthToken authToken, final DocumentVersion version) {
        return getModel(authToken).readSecret(version);
    }

    /**
     * Obtain a crypto model for an authenticated user.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return An instance of <code>CryptoModel</code>.
     */
    private CryptoModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getCryptoModel();
    }
}
