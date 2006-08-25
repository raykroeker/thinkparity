/*
 * Created On: Aug 25, 2006 9:29:35 AM
 */
package com.thinkparity.codebase.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class BeanUtils {

    /** A singleton instance. */
    private static final BeanUtils SINGLETON;

    static { SINGLETON = new BeanUtils(); }

    /**
     * Copy all properties from a source java bean to a destination java bean.
     * 
     * @param sourceBean
     *            A source java bean <code>Object</code>.
     * @param destinationBean
     *            A destination java bean <code>Object</code>.
     */
    public static void copyProperties(final Object sourceBean,
            final Object destinationBean) {
        SINGLETON.doCopyProperties(sourceBean, destinationBean);
    }

    /**
     * Obtain all properties for a java bean.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     * @return A <code>Map&lt;String, Object&gt;</code>.
     */
    public static Map<String, Object> getProperties(final Object bean) {
        return SINGLETON.doGetProperties(bean);
    }

    /**
     * Obtain the named string property for a java bean.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     * @param name
     *            The property name <code>String</code>.
     * @return The property value <code>String</code>.
     */
    public static String getStringProperty(final Object bean, final String name) {
        return SINGLETON.doGetStringProperty(bean, name);
    }

    /** Create BeanUtils. */
    private BeanUtils() {
        super();
    }

    /**
     * Copy all properties from a source java bean to a destination java bean.
     * 
     * @param sourceBean
     *            A source java bean <code>Object</code>.
     * @param destinationBean
     *            A destination java bean <code>Object</code>.
     */
    private void doCopyProperties(final Object sourceBean,
            final Object destinationBean) {
        final PropertyDescriptor[] sourceDescriptors = doGetDescriptors(sourceBean);
        PropertyDescriptor destinationDescriptor;
        for (final PropertyDescriptor sourceDescriptor : sourceDescriptors) {
            destinationDescriptor =
                    doFindDescriptor(destinationBean, sourceDescriptor.getName());
            doCopyProperty(sourceBean, sourceDescriptor, destinationBean, destinationDescriptor);
        }
    }

    /**
     * Copy a property from a source to a destination bean.
     * 
     * @param sourceBean
     *            A source java bean <code>Object</code>.
     * @param sourceDescriptor
     *            A source <code>PropertyDescriptor</code>.
     * @param destinationBean
     *            A destination java bean <code>Object</code>
     * @param destinationDescriptor
     *            A destination <code>PropertyDescriptor</code>.
     */
    private void doCopyProperty(final Object sourceBean,
            final PropertyDescriptor sourceDescriptor,
            final Object destinationBean,
            final PropertyDescriptor destinationDescriptor) {
        final Method writeMethod = destinationDescriptor.getWriteMethod();
        if (null != writeMethod) {
            try {
                writeMethod.invoke(destinationBean,
                        new Object[] { doGetProperty(sourceBean, sourceDescriptor) });
            } catch (final IllegalAccessException iax) {
                throw new BeanException(iax);
            } catch (final InvocationTargetException itx) {
                throw new BeanException(itx);
            }
        } else {
            throw new BeanException("NO SUCH WRITE METHOD");
        }
    }

    /**
     * Find a bean property descriptor.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     * @param name
     *            A property name <code>String</code>
     * @return A <code>PropertyDescriptor</code>.
     */
    private PropertyDescriptor doFindDescriptor(final Object bean,
            final String name) {
        final PropertyDescriptor[] descriptors = doGetDescriptors(bean);
        for (final PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getName().equals(name)) {
                return descriptor;
            }
        }
        throw new BeanException("NO SUCH DESCRIPTOR");
    }

    /**
     * Obtain the property descriptors for a bean.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     * @return A <code>PropertyDescriptor[]</code>.
     */
    private PropertyDescriptor[] doGetDescriptors(final Object bean) {
        return getBeanInfo(bean.getClass()).getPropertyDescriptors();
    }

    /**
     * Obtain all properties for a java bean.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     * @return A <code>Map&lt;String, Object&gt;</code>.
     */
    private Map<String, Object> doGetProperties(final Object bean) {
        final PropertyDescriptor[] descriptors = doGetDescriptors(bean);
        final Map<String, Object> properties =
                new HashMap<String, Object>(descriptors.length, 1.0F);
        for (final PropertyDescriptor descriptor : descriptors) {
            properties.put(descriptor.getName(), doGetProperty(bean, descriptor));
        }
        return properties;
    }

    /**
     * Obtain a property from a bean via the descriptor.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     * @param descriptor
     *            A bean <code>PropertyDescriptor</code>.
     * @return A property <code>Object</code>.
     */
    private Object doGetProperty(final Object bean,
            final PropertyDescriptor descriptor) {
        final Method readMethod = descriptor.getReadMethod();
        if (null != readMethod) {
            try {
                return readMethod.invoke(bean, new Object[] {});
            } catch (final IllegalAccessException iax) {
                throw new BeanException(iax);
            } catch (final InvocationTargetException itx) {
                throw new BeanException(itx);
            }
        } else {
            throw new BeanException("NO SUCH METHOD");
        }
    }

    /**
     * Obtain the named property for a java bean.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     * @param name
     *            A property name <code>String</code>.
     * @return The property value <code>Object</code>.
     */
    private Object doGetProperty(final Object bean, final String name)
            throws IllegalAccessException, InvocationTargetException {
        final PropertyDescriptor descriptor = doFindDescriptor(bean, name);
        if (null != descriptor) {
            return doGetProperty(bean, descriptor);
        } else {
            throw new BeanException("NO SUCH PROPERTY");
        }
    }

    /**
     * Obtain the named string property for a java bean.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     * @param name
     *            The property name <code>String</code>.
     * @return The property value <code>String</code>.
     */
    private String doGetStringProperty(final Object bean, final String name) {
        try {
            return (String) doGetProperty(bean, name);
        } catch (final IllegalAccessException iax) {
            throw new BeanException(iax);
        } catch (final InvocationTargetException iax) {
            throw new BeanException(iax);
        } catch (final ClassCastException ccx) {
            throw new BeanException("WRONG PROPERTY TYPE", ccx);
        }
    }

    /**
     * Obtain the bean info for a bean class.
     * 
     * @param beanClass
     *            A java bean <code>Class</code>.
     * @return A <code>BeanInfo</code>.
     */
    private BeanInfo getBeanInfo(final Class beanClass) {
        try {
            return Introspector.getBeanInfo(beanClass);
        } catch (final IntrospectionException ix) {
            throw new BeanException(ix);
        }
    }
}
