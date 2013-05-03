/*
 * Created On:  24-Sep-07 11:03:39 AM
 */
package com.thinkparity.ophelia.model.workspace;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Transaction {

    void begin();

    void commit();

    Boolean isActive();

    Boolean isRollbackOnly();

    void rollback();

    void setRollbackOnly();
}
