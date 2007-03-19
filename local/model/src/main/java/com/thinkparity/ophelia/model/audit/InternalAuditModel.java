/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Internal Audit Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalAuditModel extends AuditModel {}
