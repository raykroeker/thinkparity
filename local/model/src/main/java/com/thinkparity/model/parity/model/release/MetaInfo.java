/*
 * Created On: May 21, 2006 9:07:02 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.thinkparity.model.parity.model.release.helper.AbstractHelper;

import com.thinkparity.migrator.MigrationError;
import com.thinkparity.migrator.Release;


/**
 * Using a class path helper can retreive parity specifiy meta information.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class MetaInfo extends AbstractHelper {

    /** The meta info core properties. */
    private Properties coreProperties;

    /** The meta info's library class loader. */
    private ClassLoader libraryClassLoader;

    /** The meta info's release class loader. */
    private ClassLoader releaseClassLoader;

    /** Create MetaInfo. */
    MetaInfo(final ClassLoader libraryClassLoader,
            final ClassLoader releaseClassLoader) throws IOException {
        super();
        this.libraryClassLoader = libraryClassLoader;
        this.releaseClassLoader = releaseClassLoader;
        this.coreProperties = initCoreProperties();
    }

    void clean() {
        coreProperties = null;
        libraryClassLoader = null;
        releaseClassLoader = null;
    }

    /**
     * Obtain the instance of the migrator.
     * 
     * @return A migrator.
     */
    Migrator getMigrator() {
        /*
         * This instance of the migrator is a proxy for invoking the migrator
         * within the other class loader.
         */
        return new MetaInfo.Migrator();
    }

    /**
     * Load META-INF/core.properties from the library.
     * 
     * @return The core properties.
     * @throws MalformedURLException
     * @throws IOException
     */
    private Properties initCoreProperties() throws IOException {
        final Properties props = new Properties();
        URL resource = libraryClassLoader.getResource("META-INF/core.properties");
        if(null != resource) {
//            InputStream is = resource.openStream();
//            is.close();
//            is = null;
            System.out.println(resource.getFile());
        }
        resource = null;
//        InputStream is =
//            libraryClassLoader.getResourceAsStream("META-INF/core.properties");
//        if(null != is) {
//            props.load(is);
//            is.close();
//            is = null;
//        }
        return props;
    }

    /**
     * Invoke a method on an object instance passing parametes.
     * 
     * @param method
     *            A method.
     * @param instance
     *            An object instance.
     * @param parameters
     *            The method parameters.
     * @return The method return value.
     */
    private Object invoke(final Method method, final Object instance,
            final Object[] parameters) throws InvocationTargetException {
        try { return method.invoke(instance, parameters); }
        catch(final IllegalAccessException iax) { throw new RuntimeException(iax); }
    }

    /**
     * Load a class with a given name.
     * 
     * @param name
     *            The core property name.
     * @return A class.
     * @throws ClassNotFoundException
     * @throws MalformedURLException
     */
    private Class loadClass(final String name) {
        try { return releaseClassLoader.loadClass(name); }
        catch(final ClassNotFoundException cnfx) { throw new RuntimeException(cnfx); }
    }

    /**
     * Lookup a core property.
     * 
     * @param name
     *            The property name.
     * @param defaultValue
     *            The property default value.
     * @return The property value.
     */
    private String lookup(final String name, final String defaultValue) {
        return coreProperties.getProperty(name, defaultValue);
    }

    /**
     * Create a new instance of a class.
     * 
     * @param clasz
     *            A class.
     * @return A new instance of a class.
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private Object newInstance(final Class clasz)
            throws IllegalAccessException, InstantiationException {
        return clasz.newInstance();
    }

    /** An inner class used to call the migrator. */
    private class Migrator implements com.thinkparity.migrator.Migrator {

        /** The class name of the migrator. */
        private final String iMigratorClassName;

        /** Create Migrator. */
        private Migrator() {
//            this.iMigratorClassName = lookup("com.thinkparity.parity.migrator", null);
            iMigratorClassName = "com.thinkparity.model.Migrator";
        }

        /** @see com.thinkparity.migrator.Migrator#deprecate() */
        public void deprecate() throws MigrationError {
            if(null == iMigratorClassName) { return ; }

            Class iMigratorClass = loadClass(iMigratorClassName);

            Method iDeprecate;
            try { iDeprecate = iMigratorClass.getMethod("deprecate", new Class[] {}); }
            catch(final NoSuchMethodException nsmx) { throw new RuntimeException(nsmx); }

            Object iMigrator;
            try { iMigrator = newInstance(iMigratorClass); }
            catch(final IllegalAccessException iax) { throw new RuntimeException(iax); }
            catch(final InstantiationException ix) { throw new RuntimeException(ix); }

            try { invoke(iDeprecate, iMigrator, new Object[] {}); }
            catch(final InvocationTargetException itx) {
                throw new MigrationError(itx.getMessage(), itx.getCause());
            }

            iMigrator = null;
            iDeprecate = null;
            iMigratorClass = null;
        }

        /** @see com.thinkparity.migrator.Migrator#upgrade(com.thinkparity.migrator.Release, com.thinkparity.migrator.Release) */
        public void upgrade(Release from, Release to) throws MigrationError {
            if(null == iMigratorClassName) { return ; }

            Class iMigratorClass = loadClass(iMigratorClassName);
            Class iReleaseClass = loadClass(Release.class.getName());

            Method iUpgrade;
            try { iUpgrade = iMigratorClass.getMethod("upgrade", new Class[] {iReleaseClass, iReleaseClass}); }
            catch(final NoSuchMethodException nsmx) { throw new RuntimeException(nsmx); }
            try {
                // grab the bean info and the property descriptors for the releas
                BeanInfo iReleaseInfo = Introspector.getBeanInfo(iReleaseClass);
                PropertyDescriptor[] iReleaseProps = iReleaseInfo.getPropertyDescriptors();

                BeanInfo releaseInfo = Introspector.getBeanInfo(Release.class);
                PropertyDescriptor[] releaseProps = releaseInfo.getPropertyDescriptors();

                // instantiate the releases
                Object iReleaseFrom = newInstance(iReleaseClass);
                Object iReleaseTo = newInstance(iReleaseClass);

                // copy all properties
                Object value;
                Method readMethod, writeMethod;
                for(int i = 0; i < releaseProps.length; i++) {
                    readMethod = releaseProps[i].getReadMethod();
                    if(null != readMethod) {
                        writeMethod = iReleaseProps[i].getWriteMethod();
                        if(null != writeMethod) {
                            try {
                                value = invoke(readMethod, from, new Object[] {});
                                invoke(writeMethod, iReleaseFrom, new Object[] {value});
                            }
                            catch(final InvocationTargetException itx) {
                                throw new RuntimeException(itx);
                            }
                        }
                    }

                    readMethod = releaseProps[i].getReadMethod();
                    if(null != readMethod) {
                        writeMethod = iReleaseProps[i].getWriteMethod();
                        if(null != writeMethod) {
                            try {
                                value = invoke(readMethod, to, new Object[] {});
                                invoke(writeMethod, iReleaseTo, new Object[] {value});
                            }
                            catch(final InvocationTargetException itx) {
                                throw new RuntimeException(itx);
                            }
                        }
                    }
                }
                value = null;
                readMethod = writeMethod = null;
                releaseInfo = null;
                releaseProps = null;
                iReleaseInfo = null;
                iReleaseProps = null;

                Object iMigrator;
                try { iMigrator = newInstance(iMigratorClass); }
                catch(final IllegalAccessException iax) { throw new RuntimeException(iax); }
                catch(final InstantiationException ix) { throw new RuntimeException(ix); }

                // invoke upgrade
                try { invoke(iUpgrade, iMigrator, new Object[] {iReleaseFrom, iReleaseTo}); }
                catch(final InvocationTargetException itx) {
                    throw new MigrationError(itx.getMessage(), itx.getCause());
                }
                finally {
                    iReleaseTo = null;
                    iReleaseFrom = null;
                    iMigrator = null;
                    iUpgrade = null;
                    iReleaseClass = null;
                    iMigratorClass = null;
                }
            }
            catch(final IllegalAccessException iax) { throw new RuntimeException(iax); }
            catch(final InstantiationException ix) { throw new RuntimeException(ix); }
            catch(final IntrospectionException ix) { throw new RuntimeException(ix); }
        }
    }
}
