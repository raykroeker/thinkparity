/*
 * Created On: Sep 11, 2006 6:15:13 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.io.db.hsqldb.Table;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TableIOHandler extends AbstractIOHandler {

    /**
     * Create TableIOHandler.
     * 
     * @param sessionManager
     *            A hypersonic <code>SessionManager</code>.
     */
    public TableIOHandler(SessionManager sessionManager) {
        super(sessionManager);
    }

    /**
     * Obtain a list of all of the parity tables in the hypersonic database.
     * 
     * @return A list of tables.
     */
    public List<Table> listTables() {
        final Session session = openSession();
        try {
            session.openMetaData();
            session.getMetaDataTables();
            final List<Table> tables = new ArrayList<Table>();
            while (session.nextResult()) {
                tables.add(extract(session));
            }
            return tables;
        } finally {
            session.close();
        }
    }

    Table extract(final Session session) {
        final Table t = new Table();
        t.setCatalog(session.getString("TABLE_CAT"));
        t.setComments(session.getString("REMARKS"));
        t.setName(session.getString("TABLE_NAME"));
        t.setSchema(session.getString("TABLE_SCHEMA"));
        t.setType(session.getString("TABLE_TYPE"));
        return t;
    }
}
