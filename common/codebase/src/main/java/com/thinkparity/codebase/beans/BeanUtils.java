/*
 * Created On: Aug 25, 2006 9:29:35 AM
 */
package com.thinkparity.codebase.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class BeanUtils {

    /**
     * Obtain the bean info for a bean class.
     * 
     * @param beanClass
     *            A java bean <code>Class</code>.
     * @return A <code>BeanInfo</code>.
     */
    private static BeanInfo getBeanInfo(final Object bean) {
        try {
            return Introspector.getBeanInfo(bean.getClass());
        } catch (final IntrospectionException ix) {
            throw new BeanException(ix);
        }
    }

    /** The java bean <code>Object</code>. */
    private final Object bean;

    /** The bean's <code>BeanInfo</code>. */
    private final BeanInfo beanInfo;

    /**
     * Create BeanUtils.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     */
    public BeanUtils(final Object bean) {
        super();
        this.bean = bean;
        this.beanInfo = getBeanInfo(bean);
    }

    /**
     * Obtain a field for a property descriptor.
     * 
     * @param descriptor
     *            A <code>PropertyDescriptor</code>.
     * @return A <code>Field</code> or null; if no such field exists.
     */
    public Field getField(final PropertyDescriptor descriptor) {
        try {
            return bean.getClass().getField(descriptor.getName());
        } catch (final NoSuchFieldException nsfx) {
            return null;
        }
    }

    /**
     * Obtain the fields for a bean.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     * @return A <code>Field[]</code>.
     */
    public Field[] getFields() {
        return bean.getClass().getFields();
    }

    /**
     * Obtain the property descriptors for a bean.
     * 
     * @param bean
     *            A java bean <code>Object</code>.
     * @return A <code>PropertyDescriptor[]</code>.
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        return beanInfo.getPropertyDescriptors();
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
    public Object readProperty(final PropertyDescriptor descriptor) {
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
}
