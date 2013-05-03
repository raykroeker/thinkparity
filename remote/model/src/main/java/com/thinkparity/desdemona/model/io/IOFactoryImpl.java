/*
 * Created On:  7-Oct-07 12:54:08 PM
 */
package com.thinkparity.desdemona.model.io;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.sql.DataSource;

import com.thinkparity.desdemona.model.io.sql.*;

import com.thinkparity.desdemona.util.DesdemonaProperties;
import com.thinkparity.desdemona.util.crypto.CryptoException;
import com.thinkparity.desdemona.util.crypto.CryptoProvider;
import com.thinkparity.desdemona.util.crypto.CryptoProviderFactory;

/**
 * <b>Title:</b>thinkParity Desdemona Model IO Factory Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class IOFactoryImpl implements IOFactory {

    /** A crypto provider. */
    private final CryptoProvider cryptoProvider;

    /**
     * Create IOFactoryImpl.
     *
     */
    public IOFactoryImpl() throws CryptoException {
        super();
        final DesdemonaProperties properties = DesdemonaProperties.getInstance();
        this.cryptoProvider = CryptoProviderFactory.getInstance().newProvider(properties);
    }

    /**
     * @see com.thinkparity.desdemona.model.io.IOFactory#newContactIO(javax.sql.DataSource)
     *
     */
    @Override
    public ContactSql newContactIO(final DataSource dataSource) {
        return newIO(ContactSql.class, dataSource);
    }

    /**
     * @see com.thinkparity.desdemona.model.io.IOFactory#newDerbyIO(javax.sql.DataSource)
     *
     */
    @Override
    public DerbySql newDerbyIO(final DataSource dataSource) {
        return newIO(DerbySql.class, dataSource);
    }

    /**
     * @see com.thinkparity.desdemona.model.io.IOFactory#newMessageIO(javax.sql.DataSource)
     *
     */
    @Override
    public MessageSql newMessageIO(final DataSource dataSource) {
        return newIO(MessageSql.class, dataSource);
    }

    /**
     * @see com.thinkparity.desdemona.model.io.IOFactory#newPaymentIO(javax.sql.DataSource)
     *
     */
    @Override
    public PaymentSql newPaymentIO(final DataSource dataSource) {
        return newIO(PaymentSql.class, dataSource);
    }

    /**
     * @see com.thinkparity.desdemona.model.io.IOFactory#newQueueIO(javax.sql.DataSource)
     *
     */
    @Override
    public QueueSql newQueueIO(final DataSource dataSource) {
        return newIO(QueueSql.class, dataSource);
    }

    /**
     * @see com.thinkparity.desdemona.model.io.IOFactory#newReportIO(javax.sql.DataSource)
     *
     */
    @Override
    public ReportSql newReportIO(final DataSource dataSource) {
        return newIO(ReportSql.class, dataSource);
    }

    /**
     * @see com.thinkparity.desdemona.model.io.IOFactory#newSessionIO(javax.sql.DataSource)
     *
     */
    @Override
    public SessionSql newSessionIO(final DataSource dataSource) {
        return newIO(SessionSql.class, dataSource);
    }

    /**
     * @see com.thinkparity.desdemona.model.io.IOFactory#newUserIO(javax.sql.DataSource)
     *
     */
    @Override
    public UserSql newUserIO(final DataSource dataSource) {
        return newIO(UserSql.class, dataSource);
    }

    /**
     * Instantiate an io handler.
     * 
     * @param <T>
     *            An <code>Object</code>.
     * @param ioClass
     *            A <code>Class<T></code>.
     * @param dataSource
     *            A <code>DataSource</code>.
     * @return A <code>T</code>.
     */
    private <T extends Object> T newIO(final Class<T> ioClass,
            final DataSource dataSource) {
        try {
            final Constructor<T> ioConstructor =
                ioClass.getConstructor(new Class<?>[] { DataSource.class });
            final T io = ioConstructor.newInstance(dataSource);
            /* HACK - IOFactoryImpl#newIO(Class, DataSource) - this should be
             * changed to use a dependency injection pattern */
            ((AbstractSql) io).setCryptoProvider(cryptoProvider);
            return io;
        } catch (final NoSuchMethodException nsmx) {
            /* HACK - IOFactoryImpl#newIO(Class, DataSource) - this should be
             * changed to throw a typed error */
            throw new RuntimeException(nsmx);
        } catch (final InvocationTargetException itx) {
            throw new RuntimeException(itx);
        } catch (final IllegalAccessException iax) {
            throw new RuntimeException(iax);
        } catch (final InstantiationException ix) {
            throw new RuntimeException(ix);
        }
    }
}
