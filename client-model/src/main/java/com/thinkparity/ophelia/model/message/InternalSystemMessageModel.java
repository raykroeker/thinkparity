/*
 * Feb 24, 2006
 */
package com.thinkparity.ophelia.model.message;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Internal System Message Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalSystemMessageModel extends SystemMessageModel {}
