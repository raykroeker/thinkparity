/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import javax.sql.DataSource;

/**
 * <b>Title:</b>thinkParity OphliaModel Audit IO Handler Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.9
 */
public class AuditIOHandler extends AbstractIOHandler implements
		com.thinkparity.ophelia.model.io.handler.AuditIOHandler {

    /**
     * Create AuditIOHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
	public AuditIOHandler(final DataSource dataSource) {
        super(dataSource);
	}
}
