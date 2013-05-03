/*
 * Created On:  7-Oct-07 1:25:58 PM
 */
package com.thinkparity.desdemona.util.crypto;

import java.util.Properties;

import com.thinkparity.desdemona.util.crypto.CryptoException.Code;

/**
 * <b>Title:</b>thinkParity Desdemona Crypto Provider Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CryptoProviderFactory {

    /** A singleton instance. */
    private static final CryptoProviderFactory SINGLETON;

    static {
        SINGLETON = new CryptoProviderFactory();
    }

    /**
     * Obtain a crypto provider factory.
     * 
     * @return A <code>CryptoProviderFactory</code>.
     */
    public static CryptoProviderFactory getInstance() {
        return SINGLETON;
    }

    /**
     * Create CryptoProviderFactory.
     *
     */
    private CryptoProviderFactory() {
        super();
    }

    /**
     * Instantiate a crypto provider.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     * @return A <code>CryptoProvider</code>.
     * @throws CryptoException
     */
    public CryptoProvider newProvider(final Properties properties)
            throws CryptoException {
        final String className = properties.getProperty("thinkparity.crypto.providerimpl");
        try {
            final Class<?> providerImplClass = Class.forName(className);
            return (CryptoProvider) providerImplClass.newInstance();
        } catch (final ClassNotFoundException cnfx) {
            throw new CryptoException(Code.INSTANTIATION, cnfx);
        } catch (final InstantiationException ix) {
            throw new CryptoException(Code.INSTANTIATION, ix);
        } catch (final IllegalAccessException iax) {
            throw new CryptoException(Code.INSTANTIATION, iax);
        }
    }
}
