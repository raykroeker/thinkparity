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
import java.util.Properties;

import com.thinkparity.migrator.MigrationError;
import com.thinkparity.migrator.Migrator;
import com.thinkparity.migrator.Release;


/**
 * Using a class path helper can retreive parity specifiy meta information.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class MetaInfo {

    /** The meta info core properties. */
    private final Properties coreProperties;

    /** The meta info's library class loader. */
    private final ClassLoader libraryClassLoader;

    /** The meta info's release class loader. */
    private final ClassLoader releaseClassLoader;

    /** Create MetaInfo. */
    MetaInfo(final ClassLoader libraryClassLoader,
            final ClassLoader releaseClassLoader) throws IOException {
        super();
        this.libraryClassLoader = libraryClassLoader;
        this.releaseClassLoader = releaseClassLoader;
        this.coreProperties = initCoreProperties();
    }

    /**
     * Obtain the instance of the migrator.
     * 
     * @return A migrator.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws MalformedURLException
     */
    Migrator getMigrator() throws ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            IntrospectionException, MalformedURLException {
        final String iMigratorClassName = lookup("com.thinkparity.parity.migrator", null);
        if(null == iMigratorClassName) { return null; }

        final Class iMigratorClass = loadClass(iMigratorClassName);
        final Class iReleaseClass = loadClass(Release.class.getName());

        /*
         * This instance of the migrator is a proxy for invoking the migrator
         * within the other class loader.
         */
        return new Migrator() {
            final Method deprecate = getMethod(iMigratorClass, "deprecate", new String[] {});
            final Object instance = newInstance(iMigratorClass);
            final Method upgrade = getMethod(iMigratorClass, "upgrade", new String[] {Release.class.getName(), Release.class.getName()});

            public void deprecate() throws MigrationError {
                invoke(deprecate, instance, new Object[] {});
            }
            public void upgrade(final Release from, final Release to)
                    throws MigrationError {
                try {
                    // grab the bean info and the property descriptors for the releas
                    final BeanInfo iReleaseInfo = Introspector.getBeanInfo(iReleaseClass, iReleaseClass);
                    final PropertyDescriptor[] iReleaseProps = iReleaseInfo.getPropertyDescriptors();
    
                    final BeanInfo releaseInfo = Introspector.getBeanInfo(Release.class, Release.class);
                    final PropertyDescriptor[] releaseProps = releaseInfo.getPropertyDescriptors();
    
                    // instantiate the releases
                    final Object iReleaseFrom = newInstance(iReleaseClass);
                    final Object iReleaseTo = newInstance(iReleaseClass);
    
                    // copy all properties
                    Object value;
                    for(int i = 0; i < releaseProps.length; i++) {
                        value = invoke(releaseProps[i].getReadMethod(), from, new Object[] {});
                        invoke(iReleaseProps[i].getWriteMethod(), iReleaseFrom, new Object[] {value});
    
                        value = invoke(releaseProps[i].getReadMethod(), to, new Object[] {});
                        invoke(iReleaseProps[i].getWriteMethod(), iReleaseTo, new Object[] {value});
                    }
    
                    // invoke upgrade
                    invoke(upgrade, instance, new Object[] {iReleaseFrom, iReleaseTo});
                }
                catch(final IllegalAccessException iax) { throw new RuntimeException(iax); }
                catch(final InstantiationException ix) { throw new RuntimeException(ix); }
                catch(final IntrospectionException ix) { throw new RuntimeException(ix); }
            }
        };
    }

    /**
     * Obtain a method for a class.
     * 
     * @param clasz
     *            A class.
     * @param name
     *            A method name.
     * @param parameterTypeNames
     *            The method parameter type names.
     * @return A method.
     */
    private Method getMethod(final Class clasz, final String name,
            final String[] parameterTypeNames) {
        final Class[] parameterTypes;
        if(0 < parameterTypeNames.length) {
            // lookup the parameter types via the class loader
            parameterTypes = new Class[parameterTypeNames.length];
            for(int i = 0; i < parameterTypeNames.length; i++) {
                parameterTypes[i] =  loadClass(parameterTypeNames[i]);
            }
        }
        else { parameterTypes = new Class[] {}; }

        try { return clasz.getMethod(name, parameterTypes); }
        catch(final NoSuchMethodException nsmx) { throw new RuntimeException(nsmx); }
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
        final InputStream is =
            libraryClassLoader.getResourceAsStream("META-INF/core.properties");
        if(null != is) { props.load(is); }
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
            final Object[] parameters) {
        try { return method.invoke(instance, parameters); }
        catch(final IllegalAccessException iax) { throw new RuntimeException(iax); }
        catch(final InvocationTargetException itx) { throw new RuntimeException(itx); }
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
}
