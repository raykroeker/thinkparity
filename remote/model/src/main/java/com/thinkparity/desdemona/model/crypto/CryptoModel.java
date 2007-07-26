/*
 * Created On:  25-Jul-07 9:32:24 AM
 */
package com.thinkparity.desdemona.model.crypto;

import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.document.DocumentVersion;

/**
 * <b>Title:</b>thinkParity Desdemona Encryption Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface CryptoModel {

    /**
     * Create an encryption secret for a document version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>Secret</code>.
     */
    public Secret createSecret(DocumentVersion version);

    /**
     * Determine whether or not a secret exists for a document version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return True if the secret exists.
     */
    public Boolean doesExistSecret(DocumentVersion version);

    /**
     * Read a secret for a document version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>Secret</code>.
     */
    public Secret readSecret(DocumentVersion version);
}
