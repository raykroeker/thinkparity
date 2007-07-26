/*
 * Created On:  25-Jul-07 1:30:03 PM
 */
package com.thinkparity.ophelia.model.crypto;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Ophelia Model Encryption Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface CryptoModel {}
