/*
 * Created On:  23-Jan-07 4:24:24 PM
 */
package com.thinkparity.ophelia.model.migrator;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.ProductReleaseDeployedEvent;

/**
 * <b>Title:</b>thinkParity Internal Migrator Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalMigratorModel extends MigratorModel {

    /**
     * Handle the product release deployed remote event.
     * 
     * @param event
     *            A <code>ProductReleaseDeployedEvent</code>.
     */
    public void handleProductReleaseDeployed(final ProductReleaseDeployedEvent event);

    /**
     * Initialize the migrator model.
     *
     */
    public void initialize();
}
