/*
 * Created On: Fri May 12 2006 09:11 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Internal Release Model<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalReleaseModel extends ReleaseModel {}
