/*
 * Created On: 2006/09/28 00:23:14
 */
package com.thinkparity.ophelia.model.install;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Install Model<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
@ThinkParityTransaction(TransactionType.REQUIRES_NEW)
public interface InstallModel {
    public void install(final Release release);
}
