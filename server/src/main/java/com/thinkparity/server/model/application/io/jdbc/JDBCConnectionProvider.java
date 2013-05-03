/*
 * Created On: Sep 4, 2006 4:08:33 PM
 */
package com.thinkparity.desdemona.model.io.jdbc;

import java.sql.Connection;

/**
 * <b>Title:</b>thinkParity Desdemona Model JDBC Connection Provider<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface JDBCConnectionProvider {

    /**
     * Obtain a jdbc sql connection.
     * 
     * @return A <code>Connection</code>.
     */
    public Connection getConnection();
}
