/*
 * Created On:  7-Oct-07 12:38:59 PM
 */
package com.thinkparity.desdemona.model.io;

import java.util.Properties;

import com.thinkparity.desdemona.model.io.IOServiceException.Code;

/**
 * <b>Title:</b>thinkParity Desdemona Model IO Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IOService {

    /** A singleton instance. */
    private static final IOService SINGLETON;

    static {
        SINGLETON = new IOService();
    }

    /**
     * Obtain an instance of the io service.
     * 
     * @return An <code>IOService</code>.
     */
    public static IOService getInstance() {
        return SINGLETON;
    }

    /** The io factory. */
    private IOFactory factory;

    /**
     * Create IOService.
     *
     */
    private IOService() {
        super();
    }

    /**
     * Obtain an io factory.
     * 
     * @return An <code>IOFactory</code>.
     */
    public IOFactory getFactory() {
        return factory;
    }

    /**
     * Start the io service.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     * @return An <code>IOService</code>.
     * @throws IOServiceException
     */
    public void start(final Properties properties) throws IOServiceException {
        final String className = properties.getProperty("thinkparity.io.factoryimpl");
        try {
            final Class<?> factoryImplClass = Class.forName(className);
            factory = (IOFactory) factoryImplClass.newInstance();
        } catch (final ClassNotFoundException cnfx) {
            throw new IOServiceException(Code.INSTANTIATION, cnfx);
        } catch (final InstantiationException ix) {
            throw new IOServiceException(Code.INSTANTIATION, ix);
        } catch (final IllegalAccessException iax) {
            throw new IOServiceException(Code.INSTANTIATION, iax);
        }
    }

    /**
     * Stop the io service.
     * 
     */
    public void stop() {
        factory = null;
    }
}
