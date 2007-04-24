/*
 * Created On: Sep 4, 2006 4:08:33 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb;

import java.sql.Connection;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface HypersonicConnectionProvider {
    public Connection getConnection();
}
