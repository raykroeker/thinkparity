/*
 * Created On:  25-Jul-07 1:30:12 PM
 */
package com.thinkparity.ophelia.model.crypto;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Ophelia Model Internal Encryption Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalCryptoModel extends CryptoModel {

    /**
     * Read a secret for a document version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>Secret</code>.
     */
    Secret readSecret(DocumentVersion version);

    /**
     * Determine if a secret exists for a version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return True if a secret exists.
     */
    Boolean doesExistSecret(DocumentVersion version);

    /**
     * Create a secret for a document version.
     * 
     * @return A <code>DocumentVersion</code>.
     */
    Secret createSecret(DocumentVersion version);
}
