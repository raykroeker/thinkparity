/*
 * Created On:  25-Jul-07 9:26:22 AM
 */
package com.thinkparity.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.document.DocumentVersion;

/**
 * <b>Title:</b>thinkParity Crypto Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Crypto")
public interface CryptoService {

    /**
     * Create a secret used to encrypt/decrypt a document version.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>Secret</code>.
     */
    @WebMethod
    Secret createSecret(AuthToken authToken, DocumentVersion version);

    /**
     * Determine if a secret exists for a document version.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return True if a secret exists.
     */
    @WebMethod
    Boolean doesExistSecret(AuthToken authToken, DocumentVersion version);

    /**
     * Read the secret used to encrypt/decrypt a document version.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>Secret</code>.
     */
    @WebMethod
    Secret readSecret(AuthToken authToken, DocumentVersion version);
}
