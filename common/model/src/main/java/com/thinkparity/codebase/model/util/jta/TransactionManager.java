/*
 * Created On:  5-Jan-07 2:12:54 PM
 */
package com.thinkparity.codebase.model.util.jta;

import javax.naming.NamingException;

import com.thinkparity.codebase.JNDIUtil;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.objectweb.jotm.Jotm;

/**
 * <b>Title:</b>thinkParity Model Transaction Manager<br>
 * <b>Description:</b>A thinkParity model transaction manager. Used to
 * centralize lookup and creation of transactions for the thinkParity model.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TransactionManager {

    /**
     * Obtain an instance of a transaction manager.
     * 
     * @param application
     *            A thinkParity <code>Application</code>.
     * @return A <code>TransactionManager</code>.
     */
    public static TransactionManager getInstance() {
        return new TransactionManager();
    }

    /** An objectweb <code>Jotm</code>. */
    private Jotm jotm;

    /**
     * Create TransactionManager.
     *
     */
    private TransactionManager() {
        super();
    }

    /**
     * Bind the transaction manager to the data source.
     * 
     * @param dataSource
     *            An xapool <code>StandardXADataSource</code>.
     */
    public void bind(final StandardXADataSource dataSource) {
        dataSource.setTransactionManager(jotm.getTransactionManager());
    }

    /**
     * Lookup a transaction using a lookup key.
     * 
     * @param lookupKey
     *            A lookup key <code>String</code>.
     */
    public Transaction lookup() throws NamingException {
        return (Transaction) JNDIUtil.lookup(getName());
    }

    /**
     * Start the transaction manager.
     * 
     * @throws NamingException
     */
    public void start() throws NamingException {
        this.jotm = new Jotm(true, false);
        initialize();
    }

    /**
     * Stop the transaction manager.
     *
     */
    public void stop() {
        jotm.stop();
    }

    /**
     * Obtain the JNDI name for a transaction.
     * 
     * @return A JNDI name <code>String</code>.
     */
    private String getName() {
        return "java:comp/thinkParity/tx/UserTransaction";
    }

    /**
     * Initialize the transaction manager. This will create an instance of the
     * underlying transaction manager.
     * 
     * @throws NamingException
     */
    private void initialize() throws NamingException {
        final Transaction transaction = new Transaction(jotm.getUserTransaction());
        JNDIUtil.rebind(getName(), transaction);
    }
}
