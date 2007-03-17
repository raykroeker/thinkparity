/*
 * Created On:  5-Jan-07 2:12:54 PM
 */
package com.thinkparity.codebase.model.util.jta;

import javax.naming.NamingException;

import com.thinkparity.codebase.model.ThinkParityException;

import org.enhydra.jdbc.pool.StandardXAPoolDataSource;
import org.objectweb.carol.util.configuration.ConfigurationException;
import org.objectweb.carol.util.configuration.ConfigurationRepository;
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

    static {
        // configure carol - the default configuration is fine
        try {
            ConfigurationRepository.init();
        } catch (final ConfigurationException cx) {
            throw new ThinkParityException("Could not configure transaction server.", cx);
        }
    }

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
     * Bind the transaction manager to the pool data source.
     * 
     * @param dataSource
     *            An xapool <code>StandardXAPoolDataSource</code>.
     */
    public void bind(final StandardXAPoolDataSource dataSource) {
        dataSource.setTransactionManager(jotm.getTransactionManager());
    }

    /**
     * Obtain a transaction.
     * 
     * @return A <code>Transaction</code>.
     */
    public Transaction getTransaction() {
        return new Transaction(jotm.getUserTransaction());
    }

    /**
     * Start the transaction manager.
     * 
     * @throws NamingException
     */
    public void start() throws ConfigurationException, NamingException {
        this.jotm = new Jotm(true, false);
    }

    /**
     * Stop the transaction manager.
     *
     */
    public void stop() {
        jotm.stop();
    }
}
