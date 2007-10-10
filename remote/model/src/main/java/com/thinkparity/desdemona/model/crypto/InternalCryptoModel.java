/*
 * Created On:  25-Jul-07 9:35:40 AM
 */
package com.thinkparity.desdemona.model.crypto;

import com.thinkparity.codebase.model.crypto.Secret;

import com.thinkparity.desdemona.model.migrator.Archive;

/**
 * <b>Title:</b>thinkParity Desdemona Internal Encryption Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalCryptoModel extends CryptoModel {
    
    /**
     * Create an encryption secret for an archive.
     * 
     * @param archive
     *            An <code>Archive</code>.
     * @return A <code>Secret</code>.
     */
    public Secret createSecret(Archive archive);
}
